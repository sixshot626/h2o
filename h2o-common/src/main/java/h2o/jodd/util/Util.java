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

package h2o.jodd.util;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Some general utilities.
 * All methods are safe as possible and operates with as many types as possible.
 */
public class Util {

	/**
	 * Returns string representation of an object, while checking for <code>null</code>.
	 */
	public static String toString(final Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}

		// ---------------------------------------------------------------- misc

	/**
	 * Returns length of the object. Returns <code>0</code> for <code>null</code>.
	 * Returns <code>-1</code> for objects without a length.
	 */
	public static int length(final Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length();
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).size();
		}
		if (obj instanceof Map) {
			return ((Map) obj).size();
		}

		int count;
		if (obj instanceof Iterator) {
			final Iterator iter = (Iterator) obj;
			count = 0;
			while (iter.hasNext()) {
				count++;
				iter.next();
			}
			return count;
		}
		if (obj instanceof Enumeration) {
			final Enumeration enumeration = (Enumeration) obj;
			count = 0;
			while (enumeration.hasMoreElements()) {
				count++;
				enumeration.nextElement();
			}
			return count;
		}
		if (obj.getClass().isArray()) {
			return Array.getLength(obj);
		}
		return -1;
	}

	/**
	 * Returns <code>true</code> if first argument contains provided element.
	 * It works for strings, collections, maps and arrays.
s	 */
	public static boolean containsElement(final Object obj, final Object element) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (element == null) {
				return false;
			}
			return ((String) obj).contains(element.toString());
		}
		if (obj instanceof Collection) {
			return ((Collection) obj).contains(element);
		}
		if (obj instanceof Map) {
			return ((Map) obj).values().contains(element);
		}

		if (obj instanceof Iterator) {
			final Iterator iter = (Iterator) obj;
			while (iter.hasNext()) {
				final Object o = iter.next();
				if (Objects.equals(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj instanceof Enumeration) {
			final Enumeration enumeration = (Enumeration) obj;
			while (enumeration.hasMoreElements()) {
				final Object o = enumeration.nextElement();
				if (Objects.equals(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj.getClass().isArray()) {
			final int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				final Object o = Array.get(obj, i);
				if (Objects.equals(o, element)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Converts object into pretty string. All arrays are iterated.
	 */
	public static String toPrettyString(final Object value) {
		if (value == null) {
			return StringPool.NULL;
		}

		final Class<?> type = value.getClass();

		if (type.isArray()) {
			final Class componentType = type.getComponentType();

			if (componentType.isPrimitive()) {
				final StringBuilder sb = new StringBuilder();
				sb.append('[');

				if (componentType == int.class) {
					sb.append(ArraysUtil.toString((int[]) value));
				}
				else if (componentType == long.class) {
					sb.append(ArraysUtil.toString((long[]) value));
				}
				else if (componentType == double.class) {
					sb.append(ArraysUtil.toString((double[]) value));
				}
				else if (componentType == float.class) {
					sb.append(ArraysUtil.toString((float[]) value));
				}
				else if (componentType == boolean.class) {
					sb.append(ArraysUtil.toString((boolean[]) value));
				}
				else if (componentType == short.class) {
					sb.append(ArraysUtil.toString((short[]) value));
				}
				else if (componentType == byte.class) {
					sb.append(ArraysUtil.toString((byte[]) value));
				} else {
					throw new IllegalArgumentException();
				}
				sb.append(']');
				return sb.toString();
			} else {
				final StringBuilder sb = new StringBuilder();
				sb.append('[');

				final Object[] array = (Object[]) value;
				for (int i = 0; i < array.length; i++) {
					if (i > 0) {
						sb.append(',');
					}
					sb.append(toPrettyString(array[i]));
				}
				sb.append(']');
				return sb.toString();
			}
		} else if (value instanceof Iterable) {
			final Iterable iterable = (Iterable) value;
			final StringBuilder sb = new StringBuilder();
			sb.append('{');
			int i = 0;
			for (final Object o : iterable) {
				if (i > 0) {
					sb.append(',');
				}
				sb.append(toPrettyString(o));
				i++;
			}
			sb.append('}');
			return sb.toString();
		}

		return value.toString();
	}
}
