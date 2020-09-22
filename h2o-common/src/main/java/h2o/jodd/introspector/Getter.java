// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package h2o.jodd.introspector;

import java.lang.reflect.InvocationTargetException;

/**
 * Unified getter property interface for both methods and fields.
 */
public interface Getter {

	static Getter of(final MethodDescriptor methodDescriptor) {

		return new Getter() {

			@Override
			public Object invokeGetter(final Object target) throws InvocationTargetException, IllegalAccessException {
				return methodDescriptor.method.invoke(target);
			}

			@Override
			public Class getGetterRawType() {
				return methodDescriptor.getRawReturnType();
			}

			@Override
			public Class getGetterRawComponentType() {
				return methodDescriptor.getRawReturnComponentType();
			}

			@Override
			public Class getGetterRawKeyComponentType() {
				return methodDescriptor.getRawReturnKeyComponentType();
			}
		};
	}

	static Getter of(final FieldDescriptor fieldDescriptor) {
		return new Getter() {

			@Override
			public Object invokeGetter(final Object target) throws IllegalAccessException {
				return fieldDescriptor.field.get(target);
			}

			@Override
			public Class getGetterRawType() {
				return fieldDescriptor.getRawType();
			}

			@Override
			public Class getGetterRawComponentType() {
				return fieldDescriptor.getRawComponentType();
			}

			@Override
			public Class getGetterRawKeyComponentType() {
				return fieldDescriptor.getRawKeyComponentType();
			}
		};
	}

	Object invokeGetter(Object target) throws InvocationTargetException, IllegalAccessException;

	Class getGetterRawType();

	Class getGetterRawComponentType();

	Class getGetterRawKeyComponentType();

}
