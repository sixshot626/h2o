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

package h2o.jodd.mutable;

/**
 * Creates {@link ValueHolder} instances.
 */
public class ValueHolderWrapper {

	/**
	 * Creates new empty {@link ValueHolder}.
	 */
	public static <T> ValueHolder<T> create() {
		return new ValueHolderImpl<T>(null);
	}

	/**
	 * Wraps existing instance to {@link ValueHolder}.
	 */
	public static <T> ValueHolder<T> wrap(final T value) {
		return new ValueHolderImpl<T>(value);
	}

	static class ValueHolderImpl<T> implements ValueHolder<T> {

		T value;

		ValueHolderImpl(T v) {
			this.value = v;
		}

		public T value() {
			return value;
		}

		public void value(T value) {
			this.value = value;
		}

		/**
		 * Simple to-string representation.
		 */
		@Override
		public String toString() {
			if (value == null) {
				return "{null}";
			}
			return '{' + value.toString() + '}';
		}

	}
}