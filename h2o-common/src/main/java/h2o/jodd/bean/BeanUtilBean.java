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

package h2o.jodd.bean;

import h2o.jodd.bean.exception.InvalidPropertyBeanException;
import h2o.jodd.bean.exception.InvokePropertyBeanException;
import h2o.jodd.bean.exception.NullPropertyBeanException;
import h2o.jodd.bean.exception.PropertyNotFoundBeanException;
import h2o.jodd.bean.exception.ForcedBeanException;
import h2o.jodd.introspector.Getter;
import h2o.jodd.introspector.Setter;
import h2o.jodd.util.ClassUtil;
import h2o.jodd.util.StringUtil;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Instantiable version of {@link BeanUtil}.
 */
public class BeanUtilBean extends BeanUtilUtil implements BeanUtil {

	/**
	 * Sets the declared flag.
	 */
	public BeanUtilBean declared(final boolean declared) {
		this.isDeclared = declared;
		return this;
	}

	/**
	 * Sets the forced flag.
	 */
	public BeanUtilBean forced(final boolean forced) {
		this.isForced = forced;
		return this;
	}

	/**
	 * Sets the silent flag.
	 */
	public BeanUtilBean silent(final boolean silent) {
		this.isSilent = silent;
		return this;
	}

	// ---------------------------------------------------------------- internal resolver

	/**
	 * Resolves nested property name to the very last indexed property.
	 * If forced, <code>null</code> or non-existing properties will be created.
	 */
	protected void resolveNestedProperties(final BeanProperty bp) {
		String name = bp.name;
		int dotNdx;
		while ((dotNdx = indexOfDot(name)) != -1) {
			bp.last = false;
			bp.setName(name.substring(0, dotNdx));
			bp.updateBean(getIndexProperty(bp));
			name = name.substring(dotNdx + 1);
		}
		bp.last = true;
		bp.setName(name);
	}

	// used only for hasProperty & getPropertyType (!)
	// it continues to work even there is no bean instance!
	protected boolean resolveExistingNestedProperties(final BeanProperty bp) {
		String name = bp.name;
		int dotNdx;
		while ((dotNdx = indexOfDot(name)) != -1) {
			bp.last = false;
			bp.setName(name.substring(0, dotNdx));
			final String temp = bp.name;
			if (!hasIndexProperty(bp)) {
				return false;
			}
			bp.setName(temp);

			final Object indexProperty = bp.bean != null ? getIndexProperty(bp) : null;
			if (indexProperty != null) {
				// regular case, when there is an instance
				bp.updateBean(indexProperty);
			}
			else {
				// when bean is null, continue with the type
				bp.updateBeanClassFromProperty();
			}

			name = name.substring(dotNdx + 1);
		}
		bp.last = true;
		bp.setName(name);
		return true;
	}


	// ---------------------------------------------------------------- simple property

	@Override
	public boolean hasSimpleProperty(final Object bean, final String property) {
		return hasSimpleProperty(new BeanProperty(this, bean, property, false));
	}

	protected boolean hasSimpleProperty(final BeanProperty bp) {
//		if (bp.bean == null) {
//			return false;
//		}

		// try: getter
		final Getter getter = bp.getGetter(isDeclared);
		if (getter != null) {
			return true;
		}

		// try: (Map) get("property")
		if (bp.isMap()) {
			final Map map = (Map) bp.bean;
			if (map.containsKey(bp.name)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public <T> T getSimpleProperty(final Object bean, final String property) {
		final BeanProperty bp = new BeanProperty(this, bean, property, false);
		return (T) getSimpleProperty(bp);
	}

	protected Object getSimpleProperty(final BeanProperty bp) {
		if (bp.name.isEmpty()) {
			if (bp.indexString != null) {
				// index string exist, but property name is missing
				return bp.bean;
			}
			throw new InvalidPropertyBeanException("Empty property name.", bp);
		}

		final Getter getter = bp.getGetter(isDeclared);

		if (getter != null) {
			Object result;
			try {
				result = getter.invokeGetter(bp.bean);
			} catch (final Exception ex) {
				if (isSilent) {
					return null;
				}
				throw new InvokePropertyBeanException("Invoking getter method failed.", bp, ex);
			}

			if ((result == null) && (bp.isForced)) {
				result = createBeanProperty(bp);
			}
			return result;
		}

		// try: (Map) get("property")
		if (bp.isMap()) {
			final Map map = (Map) bp.bean;
			final Object key = convertIndexToMapKey(getter, bp.name);

			if (!map.containsKey(key)) {
				if (!bp.isForced) {
					if (isSilent) {
						return null;
					}
					throw new PropertyNotFoundBeanException("Map key '" +  bp.name + "' not found.", bp);
				}
				final Map value = new HashMap();
				//noinspection unchecked
				map.put(key, value);
				return value;
			}
			return map.get(key);
		}

		// failed
		if (isSilent) {
			return null;
		}

		if (bp.isExistingParentNull() && bp.currentPropertyExistOnParent(isDeclared)) {
			throw new NullPropertyBeanException("Simple property '" + bp.lastName + "' is null.", bp);
		}
		throw new PropertyNotFoundBeanException("Simple property '" + bp.name + "' not found.", bp);
	}

	@Override
	public void setSimpleProperty(final Object bean, final String property, final Object value) {
		setSimpleProperty(new BeanProperty(this, bean, property, true), value);
	}

	/**
	 * Sets a value of simple property.
	 */
	@SuppressWarnings({"unchecked"})
	protected void setSimpleProperty(final BeanProperty bp, final Object value) {
		final Setter setter = bp.getSetter(isDeclared);

		// try: setter
		if (setter != null) {
			invokeSetter(setter, bp, value);
			return;
		}

		// try: put("property", value)
		if (bp.isMap()) {
			((Map) bp.bean).put(bp.name, value);
			return;
		}
		if (isSilent) {
			return;
		}
		if (bp.isExistingParentNull() && bp.currentPropertyExistOnParent(isDeclared)) {
			throw new NullPropertyBeanException("Simple property '" + bp.lastName + "' is null.", bp);
		}
		throw new PropertyNotFoundBeanException("Simple property '" + bp.name + "' not found.", bp);
	}

	// ---------------------------------------------------------------- indexed property

	protected boolean hasIndexProperty(final BeanProperty bp) {
//		if (bp.bean == null) {
//			return false;
//		}
		final String indexString = extractIndex(bp);

		if (indexString == null) {
			return hasSimpleProperty(bp);
		}

		if (bp.bean == null) {
			return false;
		}

		final Object resultBean = getSimpleProperty(bp);

		if (resultBean == null) {
			return false;
		}

		// try: property[index]
		if (resultBean.getClass().isArray()) {
			final int index = parseInt(indexString, bp);
			return (index >= 0) && (index < Array.getLength(resultBean));
		}

		// try: list.get(index)
		if (resultBean instanceof List) {
			final int index = parseInt(indexString, bp);
			return (index >= 0) && (index < ((List)resultBean).size());
		}
		if (resultBean instanceof Map) {
			return ((Map)resultBean).containsKey(indexString);
		}

		// failed
		return false;
	}

	@Override
	public <T> T getIndexProperty(final Object bean, final String property, final int index) {
		final BeanProperty bp = new BeanProperty(this, bean, property, false);

		bp.indexString = bp.index = String.valueOf(index);

		final Object value = _getIndexProperty(bp);

		bp.indexString = null;

		return (T) value;
	}

	/**
	 * Get non-nested property value: either simple or indexed property.
	 * If forced, missing bean will be created if possible.
	 */
	protected Object getIndexProperty(final BeanProperty bp) {
		bp.indexString = extractIndex(bp);

		final Object value = _getIndexProperty(bp);

		bp.indexString = null;

		return value;
	}

	private Object _getIndexProperty(final BeanProperty bp) {
		final Object resultBean = getSimpleProperty(bp);
		final Getter getter = bp.getGetter(isDeclared);
		if (bp.indexString == null) {
			return resultBean;
		}
		if (resultBean == null) {
			if (isSilent) {
				return null;
			}
			throw new NullPropertyBeanException("Index property '" + bp.name + "' is null.", bp);
		}

		// try: property[index]
		if (resultBean.getClass().isArray()) {
			final int index = parseInt(bp.indexString, bp);
			if (bp.isForced) {
				return arrayForcedGet(bp, resultBean, index);
			} else {
				return Array.get(resultBean, index);
			}
		}

		// try: list.get(index)
		if (resultBean instanceof List) {
			final int index = parseInt(bp.indexString, bp);
			final List list = (List) resultBean;
			if (!bp.isForced) {
				return list.get(index);
			}
			if (!bp.last) {
				ensureListSize(list, index);
			}
			Object value = list.get(index);
			if (value == null) {
				Class listComponentType = extractGenericComponentType(getter);
				if (listComponentType == Object.class) {
					// not an error: when component type is unknown, use Map as generic bean
					listComponentType = Map.class;
				}
				try {
					value = ClassUtil.newInstance(listComponentType);
				} catch (final Exception ex) {
					if (isSilent) {
						return null;
					}
					throw new ForcedBeanException("Invalid list element: " + bp.name + '[' + index + "].", bp, ex);
				}
				//noinspection unchecked
				list.set(index, value);
			}
			return value;
		}

		// try: map.get('index')
		if (resultBean instanceof Map) {
			final Map map = (Map) resultBean;
			final Object key = convertIndexToMapKey(getter, bp.indexString);

			if (!bp.isForced) {
				return map.get(key);
			}
			Object value = map.get(key);
			if (!bp.last) {
				if (value == null) {
					Class mapComponentType = extractGenericComponentType(getter);
					if (mapComponentType == Object.class) {
						mapComponentType = Map.class;
					}
					try {
						value = ClassUtil.newInstance(mapComponentType);
					} catch (final Exception ex) {
						if (isSilent) {
							return null;
						}
						throw new ForcedBeanException("Invalid map element: " + bp.name + '[' + bp.indexString + "].", bp, ex);
					}

					//noinspection unchecked
					map.put(key, value);
				}
			}
			return value;
		}

		// failed
		if (isSilent) {
			return null;
		}
		throw new InvalidPropertyBeanException("Index property '" + bp.name + "' is not an array, list or map.", bp);
	}

	@Override
	public void setIndexProperty(final Object bean, final String property, final int index, final Object value) {
		final BeanProperty bp = new BeanProperty(this, bean, property, true);

		bp.indexString = bp.index = String.valueOf(index);

		_setIndexProperty(bp, value);

		bp.indexString = null;
	}

	/**
	 * Sets indexed or regular properties (no nested!).
	 */
	protected void setIndexProperty(final BeanProperty bp, final Object value) {
		bp.indexString = extractIndex(bp);

		_setIndexProperty(bp, value);

		bp.indexString = null;
	}

	@SuppressWarnings({"unchecked"})
	private void _setIndexProperty(final BeanProperty bp, Object value) {
		if (bp.indexString == null) {
			setSimpleProperty(bp, value);
			return;
		}

		// try: getInner()
		final Object nextBean = getSimpleProperty(bp);
		final Getter getter = bp.getGetter(isDeclared);

		if (nextBean == null) {
			if (isSilent) {
				return;
			}
			throw new NullPropertyBeanException("Index property '" + bp.name + " is null.", bp);
		}

		// inner bean found
		if (nextBean.getClass().isArray()) {
			final int index = parseInt(bp.indexString, bp);
			if (bp.isForced) {
				arrayForcedSet(bp, nextBean, index, value);
			} else {
				Array.set(nextBean, index, value);
			}
			return;
		}

		if (nextBean instanceof List) {
			final int index = parseInt(bp.indexString, bp);
			final Class listComponentType = extractGenericComponentType(getter);
			if (listComponentType != Object.class) {
				value = convertType(value, listComponentType);
			}
			final List list = (List) nextBean;
			if (bp.isForced) {
				ensureListSize(list, index);
			}
			list.set(index, value);
			return;
		}
		if (nextBean instanceof Map) {
			final Map map = (Map) nextBean;
			final Object key = convertIndexToMapKey(getter, bp.indexString);

			final Class mapComponentType = extractGenericComponentType(getter);
			if (mapComponentType != Object.class) {
				value = convertType(value, mapComponentType);
			}
			map.put(key, value);
			return;
		}

		// failed
		if (isSilent) {
			return;
		}
		throw new InvalidPropertyBeanException("Index property '" + bp.name + "' is not an array, list or map.", bp);
	}


	// ---------------------------------------------------------------- SET

	@Override
	public void setProperty(final Object bean, final String name, final Object value) {
		final BeanProperty beanProperty = new BeanProperty(this, bean, name, true);

		if (!isSilent) {
			resolveNestedProperties(beanProperty);
			setIndexProperty(beanProperty, value);
		}
		else {
			try {
				resolveNestedProperties(beanProperty);
				setIndexProperty(beanProperty, value);
			}
			catch (final Exception ignore) {}
		}
	}

	// ---------------------------------------------------------------- GET

	/**
	 * Returns value of bean's property.
	 */
	@Override
	public <T> T getProperty(final Object bean, final String name) {
		final BeanProperty beanProperty = new BeanProperty(this, bean, name, false);
		if (!isSilent) {
			resolveNestedProperties(beanProperty);
			return (T) getIndexProperty(beanProperty);
		}
		else {
			try {
				resolveNestedProperties(beanProperty);
				return (T) getIndexProperty(beanProperty);
			}
			catch (final Exception ignore) {
				return null;
			}
		}
	}

	// ---------------------------------------------------------------- HAS

	@Override
	public boolean hasProperty(final Object bean, final String name) {
		final BeanProperty beanProperty = new BeanProperty(this, bean, name, false);
		if (!resolveExistingNestedProperties(beanProperty)) {
			return false;
		}
		return hasIndexProperty(beanProperty);
	}

	@Override
	public boolean hasRootProperty(final Object bean, String name) {
		final int dotNdx = indexOfDot(name);
		if (dotNdx != -1) {
			name = name.substring(0, dotNdx);
		}
		final BeanProperty beanProperty = new BeanProperty(this, bean, name, false);
		extractIndex(beanProperty);
		return hasSimpleProperty(beanProperty);
	}

	// ---------------------------------------------------------------- type

	@Override
	public Class<?> getPropertyType(final Object bean, final String name) {
		final BeanProperty beanProperty = new BeanProperty(this, bean, name, false);
		if (!resolveExistingNestedProperties(beanProperty)) {
			return null;
		}
		hasIndexProperty(beanProperty);
		return extractType(beanProperty);
	}

	// ---------------------------------------------------------------- utilities

	private static final char[] INDEX_CHARS = new char[] {'.', '['};

	/**
	 * Extract the first name of this reference.
	 */
	@Override
	public String extractThisReference(final String propertyName) {
		final int ndx = StringUtil.indexOfChars(propertyName, INDEX_CHARS);
		if (ndx == -1) {
			return propertyName;
		}
		return propertyName.substring(0, ndx);
	}

}
