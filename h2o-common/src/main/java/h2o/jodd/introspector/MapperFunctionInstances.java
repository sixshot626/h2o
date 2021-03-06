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

import h2o.jodd.util.ClassUtil;
import h2o.jodd.util.TypeCache;

/**
 * Simple cache of {@link MapperFunction} instances.
 */
public class MapperFunctionInstances {

	private static final MapperFunctionInstances MAPPER_FUNCTION_INSTANCES = new MapperFunctionInstances();

	/**
	 * Returns the instance.
	 */
	public static MapperFunctionInstances get() {
		return MAPPER_FUNCTION_INSTANCES;
	}

	protected TypeCache<MapperFunction> typeCache = TypeCache.createDefault();

	public MapperFunction lookup(final Class<? extends MapperFunction> mapperFunctionClass) {
		return typeCache.get(mapperFunctionClass, (c) -> {
			try {
				return ClassUtil.newInstance(mapperFunctionClass);
			} catch (final Exception ex) {
				throw new IllegalArgumentException("Invalid mapper class " + c, ex);
			}
		});
	}

}
