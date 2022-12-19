/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2020 Equinix, Inc
 * Copyright 2014-2020 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package h2o.common.concurrent.executor;

import org.slf4j.Logger;

import java.util.concurrent.Callable;

class WrappedCallable<T> implements Callable<T> {

    private final Logger log;
    private final Callable<T> callable;

    private WrappedCallable(final Logger log, final Callable<T> callable) {
        this.log = log;
        this.callable = callable;
    }

    public static <T> Callable<T> wrap(final Logger log, final Callable<T> callable) {
        return callable instanceof WrappedCallable ? callable : new WrappedCallable<T>(log, callable);
    }

    @Override
    public T call() throws Exception {
        final Thread currentThread = Thread.currentThread();

        try {
            return callable.call();
        } catch (final Exception e) {
            // since callables are expected to sometimes throw exceptions, log this at DEBUG instead of ERROR
            log.debug(currentThread + " ended with an exception", e);

            throw e;
        } catch (final Error e) {
            log.error(currentThread + " ended with an exception", e);

            throw e;
        } finally {
            log.debug("{} finished executing", currentThread);
        }
    }
}
