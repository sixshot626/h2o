/*
    Copyright 2008 Jenkov Development

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/



package com.jenkov.db.scope;

/**
 * Represents the action(s) that is to be carried out inside the given scope.
 * An IScopeAction can be used inside both a connection scope and a transaction
 * scope.
 *
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public interface IScopeAction {


    /**
     * This method is called by the scope boundary object when
     * it is time to execute the actions inside the scope.
     *
     * <br/><br/>
     * As you may have noticed the connection that is scoped
     * is not passed to the IScopeAction in this method.
     * Your implementation of the IScopeAction interface
     * must have a reference to the ScopingDataSource, from
     * which it obtains it's connections. The ScopingDataSource
     * can be obtained from the IScopeFactory you are using,
     * or you can instantiate one yourself. Just remember that
     * the ScopingDataSource referenced by your IScopeAction
     * implementation has to be the same instance as referenced
     * by the IScopeBoundary instance, calling the inScope() method
     * of your IScopeAction implementation.
     *
     * @return An object if your IScopeAction chooses to.
     * @throws Throwable If something goes wrong inside the IScopeAction.
     *                   The surrounding IScopeBoundary object will take
     *                   the correct measures to close the connection and
     *                   rollback any ongoing transaction.
     */
    public Object inScope() throws Throwable;
}
