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

package h2o.jodd.typeconverter.impl;

import h2o.common.lang.LTime;
import h2o.common.lang.NBool;
import h2o.common.lang.SNumber;
import h2o.jodd.typeconverter.TypeConversionException;
import h2o.jodd.typeconverter.TypeConverter;

/**
 * Converts given object to <code>BigDecimal</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>object is converted to string, trimmed, and then converted if possible</li>
 * </ul>
 */
public class SNumberConverter implements TypeConverter<SNumber> {

	@Override
	public SNumber convert(final Object value) {
		if (value == null) {
			return new SNumber();
		}
		if (value instanceof SNumber) {
			return (SNumber) value;
		}

		if (value.getClass() == Boolean.class) {
			return ((Boolean)value) ? SNumber.ONE : SNumber.ZERO;
		}

		if (value.getClass() == NBool.class) {
			NBool bool = (NBool) value;
			if ( bool == NBool.NULL ) {
				return new SNumber();
			} else {
				return bool == NBool.TRUE ? SNumber.ONE : SNumber.ZERO;
			}
		}

		if (value instanceof LTime) {
			return new SNumber(((LTime)value).getValue());
		}
		if ( value instanceof Number ) {
			return new SNumber((Number) value);
		}
		try {
			return new SNumber(value.toString().trim());
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
