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
import h2o.common.lang.SNumber;
import h2o.common.lang.STime;
import h2o.common.lang.STimestamp;
import h2o.jodd.time.JulianDate;
import h2o.jodd.time.TimeUtil;
import h2o.jodd.typeconverter.TypeConversionException;
import h2o.jodd.typeconverter.TypeConverter;
import h2o.jodd.util.StringUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Converts given object to a <code>Long</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>object is converted to string, trimmed, and then converted if possible.</li>
 * </ul>
 * Number string may start with plus and minus sign.
 */
public class LTimeConverter implements TypeConverter<LTime> {

	@Override
	public LTime convert(final Object value) {

		if (value == null) {
			return LTime.NULL;
		}

		if (value instanceof LTime) {
			return (LTime)value;
		}

		if (value instanceof STimestamp) {
			return ((STimestamp) value).toLTime();
		}

		if (value instanceof Date) {
			return new LTime(((Date)value).getTime());
		}
		if (value instanceof Calendar) {
			return new LTime(((Calendar)value).getTimeInMillis());
		}
		if (value instanceof JulianDate) {
			return new LTime(((JulianDate) value).toMilliseconds());
		}
		if (value instanceof LocalDateTime) {
			return new LTime(TimeUtil.toMilliseconds((LocalDateTime)value));
		}
		if (value instanceof LocalTime || value instanceof STime) {
			throw new TypeConversionException("Can't convert to LTime: " + value);
		}

		if (value.getClass() == Long.class) {
			return new LTime((Long) value);
		}
		if ( value instanceof SNumber && !((SNumber) value).isPresent()) {
			return new LTime();
		}
		if (value instanceof Number) {
			return new LTime(Long.valueOf(((Number)value).longValue()));
		}

		try {
			String stringValue = value.toString().trim();
			if (StringUtil.startsWithChar(stringValue, '+')) {
				stringValue = stringValue.substring(1);
			}
			return new LTime( Long.valueOf(stringValue) );
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
