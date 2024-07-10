package h2o.common.concurrent;


import h2o.common.concurrent.executor.Executors;
import h2o.common.exception.ExceptionUtil;
import h2o.common.exception.UncheckedTimeoutException;

import java.io.Closeable;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.NANOSECONDS;


public class TimeLimiter implements Closeable {

    private final ExecutorService executorService;

    private final SimpleTimeLimiter simpleTimeLimiter;

    private final Supplier<RuntimeException> exceptionSupplier;


    public TimeLimiter( int threadSize , String poolName ) {
        this(Executors.newFixedThreadPool(threadSize, poolName), UncheckedTimeoutException::new);
    }

    public TimeLimiter( ExecutorService executorService) {
        this(executorService, UncheckedTimeoutException::new );
    }

    public TimeLimiter( int threadSize , String poolName , Supplier<RuntimeException> exceptionSupplier ) {
        this(Executors.newFixedThreadPool(threadSize, poolName), exceptionSupplier);
    }

    public TimeLimiter( ExecutorService executorService , Supplier<RuntimeException> exceptionSupplier ) {
        this.executorService = executorService;
        this.simpleTimeLimiter = SimpleTimeLimiter.create( executorService );
        this.exceptionSupplier = exceptionSupplier;
    }

    public <T> T newProxy(T target, Class<T> interfaceType, long timeout) {
        return simpleTimeLimiter.newProxy(target, interfaceType, timeout, TimeUnit.MILLISECONDS);
    }


    public <T> T callWithTimeout( long timeout , Callable<T> callable) {

        try {
            return simpleTimeLimiter.callWithTimeout(callable,timeout , TimeUnit.MILLISECONDS);
        } catch (InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw exceptionSupplier.get();
        } catch (TimeoutException | ExecutionException e) {
            throw exceptionSupplier.get();
        }
    }


    public void runWithTimeout( long timeout , Runnable runnable )  {
        try {
            simpleTimeLimiter.runWithTimeout(runnable, timeout , TimeUnit.MILLISECONDS );
        } catch (InterruptedException e ) {
            Thread.currentThread().interrupt();
        } catch (TimeoutException e) {
            throw exceptionSupplier.get();
        }
    }


    public <T> T callUninterruptiblyWithTimeout( long timeout , Callable<T> callable ) {
        try {
            return simpleTimeLimiter.callUninterruptiblyWithTimeout(callable,timeout , TimeUnit.MILLISECONDS);
        } catch (TimeoutException | ExecutionException e) {
            throw exceptionSupplier.get();
        }
    }

    public void runUninterruptiblyWithTimeout( long timeout , Runnable runnable ) {
        try {
            simpleTimeLimiter.runUninterruptiblyWithTimeout(runnable, timeout , TimeUnit.MILLISECONDS );
        } catch (TimeoutException e) {
            throw exceptionSupplier.get();
        }
    }

    public void close() {
        this.executorService.shutdown();
    }


   private static final class SimpleTimeLimiter {

        private final ExecutorService executor;

        private SimpleTimeLimiter(ExecutorService executor) {
            this.executor = checkNotNull(executor);
        }

        /**
         * Creates a TimeLimiter instance using the given executor service to execute method calls.
         *
         * <p><b>Warning:</b> using a bounded executor may be counterproductive! If the thread pool fills
         * up, any time callers spend waiting for a thread may count toward their time limit, and in this
         * case the call may even time out before the target method is ever invoked.
         *
         * @param executor the ExecutorService that will execute the method calls on the target objects;
         *     for example, a {@link java.util.concurrent.Executors#newCachedThreadPool()}.
         * @since 22.0
         */
        public static SimpleTimeLimiter create(ExecutorService executor) {
            return new SimpleTimeLimiter(executor);
        }


        public <T> T newProxy(
                T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit) {
            checkNotNull(target);
            checkNotNull(interfaceType);
            checkNotNull(timeoutUnit);
            checkPositiveTimeout(timeoutDuration);
            if(!interfaceType.isInterface()) {
                throw new IllegalArgumentException( "interfaceType must be an interface type" );
            }

            Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);

            InvocationHandler handler =
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object obj, Method method,  Object[] args)
                                throws Throwable {
                            Callable<Object> callable =
                                    () -> {
                                        try {
                                            return method.invoke(target, args);
                                        } catch (InvocationTargetException e) {
                                            throw throwCause(e, false /* combineStackTraces */);
                                        }
                                    };
                            return callWithTimeout(
                                    callable, timeoutDuration, timeoutUnit, interruptibleMethods.contains(method));
                        }
                    };
            return newProxy(interfaceType, handler);
        }

        private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
            Object object =
                    Proxy.newProxyInstance(
                            interfaceType.getClassLoader(), new Class<?>[] {interfaceType}, handler);
            return interfaceType.cast(object);
        }

        private <T extends  Object> T callWithTimeout(
                Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
                throws Exception {
            checkNotNull(callable);
            checkNotNull(timeoutUnit);
            checkPositiveTimeout(timeoutDuration);

            Future<T> future = executor.submit(callable);

            try {
                return amInterruptible
                        ? future.get(timeoutDuration, timeoutUnit)
                        : getUninterruptibly(future, timeoutDuration, timeoutUnit);
            } catch (InterruptedException e) {
                future.cancel(true);
                throw e;
            } catch (ExecutionException e) {
                throw throwCause(e, true /* combineStackTraces */);
            } catch (TimeoutException e) {
                future.cancel(true);
                throw new UncheckedTimeoutException(e);
            }
        }



       private static <V extends Object> V getUninterruptibly(
               Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
           boolean interrupted = false;
           try {
               long remainingNanos = unit.toNanos(timeout);
               long end = System.nanoTime() + remainingNanos;

               while (true) {
                   try {
                       // Future treats negative timeouts just like zero.
                       return future.get(remainingNanos, NANOSECONDS);
                   } catch (InterruptedException e) {
                       interrupted = true;
                       remainingNanos = end - System.nanoTime();
                   }
               }
           } finally {
               if (interrupted) {
                   Thread.currentThread().interrupt();
               }
           }
       }


        public <T extends Object> T callWithTimeout(
                Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
                throws TimeoutException, InterruptedException, ExecutionException {
            checkNotNull(callable);
            checkNotNull(timeoutUnit);
            checkPositiveTimeout(timeoutDuration);

            Future<T> future = executor.submit(callable);

            try {
                return future.get(timeoutDuration, timeoutUnit);
            } catch (InterruptedException | TimeoutException e) {
                future.cancel(true /* mayInterruptIfRunning */);
                throw e;
            } catch (ExecutionException e) {
                wrapAndThrowExecutionExceptionOrError(e.getCause());
                throw new AssertionError();
            }
        }

        public <T extends Object> T callUninterruptiblyWithTimeout(
                Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
                throws TimeoutException, ExecutionException {
            checkNotNull(callable);
            checkNotNull(timeoutUnit);
            checkPositiveTimeout(timeoutDuration);

            Future<T> future = executor.submit(callable);

            try {
                return getUninterruptibly(future, timeoutDuration, timeoutUnit);
            } catch (TimeoutException e) {
                future.cancel(true /* mayInterruptIfRunning */);
                throw e;
            } catch (ExecutionException e) {
                wrapAndThrowExecutionExceptionOrError(e.getCause());
                throw new AssertionError();
            }
        }

        public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
                throws TimeoutException, InterruptedException {
            checkNotNull(runnable);
            checkNotNull(timeoutUnit);
            checkPositiveTimeout(timeoutDuration);

            Future<?> future = executor.submit(runnable);

            try {
                future.get(timeoutDuration, timeoutUnit);
            } catch (InterruptedException | TimeoutException e) {
                future.cancel(true /* mayInterruptIfRunning */);
                throw e;
            } catch (ExecutionException e) {
                wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
                throw new AssertionError();
            }
        }

        public void runUninterruptiblyWithTimeout(
                Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException {
            checkNotNull(runnable);
            checkNotNull(timeoutUnit);
            checkPositiveTimeout(timeoutDuration);

            Future<?> future = executor.submit(runnable);

            try {
                getUninterruptibly(future, timeoutDuration, timeoutUnit);
            } catch (TimeoutException e) {
                future.cancel(true /* mayInterruptIfRunning */);
                throw e;
            } catch (ExecutionException e) {
                wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
                throw new AssertionError();
            }
        }



        private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
            Throwable cause = e.getCause();
            if (cause == null) {
                throw e;
            }
            if (combineStackTraces) {
                StackTraceElement[] combined = concat(cause.getStackTrace(), e.getStackTrace(), StackTraceElement.class);
                cause.setStackTrace(combined);
            }
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            // The cause is a weird kind of Throwable, so throw the outer exception.
            throw e;
        }


        private static <T extends Object> T[] concat(
                T[] first, T[] second, Class<T> type) {
            T[] result = newArray(type, first.length + second.length);
            System.arraycopy(first, 0, result, 0, first.length);
            System.arraycopy(second, 0, result, first.length, second.length);
            return result;
        }

        @SuppressWarnings("unchecked")
        private static <T extends Object> T[] newArray(Class<T> type, int length) {
            return (T[]) Array.newInstance(type, length);
        }

        private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
            Set<Method> set = new HashSet<>();
            for (Method m : interfaceType.getMethods()) {
                if (declaresInterruptedEx(m)) {
                    set.add(m);
                }
            }
            return set;
        }

        private static boolean declaresInterruptedEx(Method method) {
            for (Class<?> exType : method.getExceptionTypes()) {
                // debate: == or isAssignableFrom?
                if (exType == InterruptedException.class) {
                    return true;
                }
            }
            return false;
        }

        private void wrapAndThrowExecutionExceptionOrError(Throwable cause) throws ExecutionException {
            if (cause instanceof Error) {
                throw (Error) cause;
            } else if (cause instanceof RuntimeException) {
                throw (RuntimeException)cause;
            } else {
                throw new ExecutionException(cause);
            }
        }

        private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable cause) {
            if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                throw ExceptionUtil.toRuntimeException(cause);
            }
        }

        private static void checkPositiveTimeout(long timeoutDuration) {
            if(timeoutDuration <= 0) {
                throw new IllegalArgumentException("timeout must be positive: " + timeoutDuration);
            }
        }


        private static <T> T checkNotNull( T reference) {
            if (reference == null) {
                throw new NullPointerException();
            }
            return reference;
        }
    }



}
