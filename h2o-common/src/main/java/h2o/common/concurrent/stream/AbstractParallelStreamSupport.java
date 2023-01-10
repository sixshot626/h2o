/*
 * Copyright (c) 2016 Stefan Ferstl <st.ferstl@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package h2o.common.concurrent.stream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.BaseStream;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.ForkJoinTask.adapt;

/**
 * Abstract base class for all parallel streams in this package. It implements all Methods of {@link BaseStream} and
 * holds the {@link ForkJoinPool} and the stream to which subsequent stream operations are delegated. The two methods
 * {@link #execute(Callable)} and {@link #execute(Runnable)} are used to execute terminal operations. In case this
 * stream's {@link #isParallel()} method returns {@code true}, a terminal operation will be executed as
 * {@link ForkJoinTask} in the {@link #workerPool}. Otherwise the terminal operation will be executed in the calling
 * thread.
 *
 * @param <T> The type of the stream elements.
 * @param <S> Type of stream.
 */
abstract class AbstractParallelStreamSupport<T, S extends BaseStream<T, S>> implements BaseStream<T, S> {

  S delegate;
  final ForkJoinPool workerPool;

  AbstractParallelStreamSupport(S delegate, ForkJoinPool workerPool) {
    requireNonNull(delegate, "Stream must not be null");
    requireNonNull(workerPool, "Worker pool must not be null");

    this.delegate = delegate;
    this.workerPool = workerPool;
  }

  @Override
  public boolean isParallel() {
    return this.delegate.isParallel();
  }

  @Override
  public Iterator<T> iterator() {
    return this.delegate.iterator();
  }

  @Override
  public Spliterator<T> spliterator() {
    return this.delegate.spliterator();
  }

  @Override
  @SuppressWarnings("unchecked")
  public S sequential() {
    this.delegate = this.delegate.sequential();
    return (S) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S parallel() {
    this.delegate = this.delegate.parallel();
    return (S) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S unordered() {
    this.delegate = this.delegate.unordered();
    return (S) this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public S onClose(Runnable closeHandler) {
    this.delegate = this.delegate.onClose(closeHandler);
    return (S) this;
  }

  @Override
  public void close() {
    this.delegate.close();
  }

  protected void execute(Runnable terminalOperation) {
    if (isParallel()) {
      ForkJoinTask<?> task = adapt(terminalOperation);
      this.workerPool.invoke(task);
    } else {
      terminalOperation.run();
    }
  }

  protected <R> R execute(Callable<R> terminalOperation) {
    if (isParallel()) {
      ForkJoinTask<R> task = adapt(terminalOperation);
      return this.workerPool.invoke(task);
    }

    try {
      return terminalOperation.call();
    } catch (RuntimeException | Error e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
