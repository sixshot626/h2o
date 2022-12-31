/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package h2o.dao.jdbc.sqlpara.namedparam;

import h2o.common.collection.KeyMap;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;



class MapSqlParameterSource  {

    private final Map<String, Object> values;

    private final KeyMap<Object> valuesMap;



    /**
     * Create a new MapSqlParameterSource based on a Map.
     *
     * @param values a Map holding existing parameter values (can be {@code null})
     */
    public MapSqlParameterSource(Map<String, ?> values) {
        this.values = new LinkedHashMap<>();
        if (values != null) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                this.values.put(entry.getKey(), entry.getValue());
            }
        }
        this.valuesMap = new KeyMap<>( this.values );
    }




    /**
     * Expose the current parameter values as read-only Map.
     */
    public Map<String, Object> getValues() {
        return Collections.unmodifiableMap(this.values);
    }


    public boolean hasValue(String paramName) {
        return this.valuesMap.containsKey(paramName);
    }

    public Object getValue(String paramName) {
        if (!hasValue(paramName)) {
            throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
        }
        return this.valuesMap.get(paramName);
    }




}
