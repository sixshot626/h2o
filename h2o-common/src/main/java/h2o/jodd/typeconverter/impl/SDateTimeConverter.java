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

import h2o.common.lang.*;
import h2o.jodd.time.JulianDate;
import h2o.jodd.time.TimeUtil;
import h2o.jodd.typeconverter.TypeConversionException;
import h2o.jodd.typeconverter.TypeConverter;
import h2o.jodd.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Converts given object to <code>java.util.Date</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Calendar</code> object is converted</li>
 * <li><code>JulianDate</code> object is converted</li>
 * <li><code>LocalDateTime</code> object is converted</li>
 * <li><code>LocalDate</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds</li>
 * </ul>
 */
public class SDateTimeConverter implements TypeConverter<SDateTime> {

	@Override
	public SDateTime convert(final Object value) {

		if (value == null) {
			return SDateTime.NULL;
		}

		if (value instanceof SDateTime) {
			return (SDateTime) value;
		}
		if (value instanceof SDate) {
			if ( ((SDate) value).isPresent() ) {
				return new SDateTime( ((SDate) value).toDate() );
			} else {
				return SDateTime.NULL;
			}
		}
		if (value instanceof LTime) {
			return ((LTime) value).toSDateTime();
		}
		if (value instanceof Date) {
			return new SDateTime((Date) value);
		}
		if (value instanceof Calendar) {
			return new SDateTime(new Date(((Calendar)value).getTimeInMillis()));
		}
		if (value instanceof JulianDate) {
			return new SDateTime(new Date(((JulianDate) value).toMilliseconds()));
		}
		if (value instanceof LocalDateTime) {
			return new SDateTime(TimeUtil.toDate((LocalDateTime)value));
		}
		if (value instanceof LocalDate) {
			return new SDateTime(TimeUtil.toDate((LocalDate)value));
		}
		if (value instanceof LocalTime || value instanceof STime ) {
			throw new TypeConversionException("Can't convert to SDateTime: " + value);
		}
		if (value instanceof SNumber) {
			if (((SNumber) value).isPresent() ) {
				return new SDateTime(new Date(((SNumber) value).longValue()));
			} else {
				return SDateTime.NULL;
			}
		}
		if (value instanceof Number) {
			return new SDateTime(new Date(((Number) value).longValue()));
		}

		final String stringValue = value.toString().trim();

		if (!StringUtil.containsOnlyDigits(stringValue)) {
			if ( stringValue.toLowerCase().equals("null") || stringValue.toLowerCase().equals("<null>") ) {
				return SDateTime.NULL;
			}
			try {
				return new SDateTime( stringValue );
			} catch (NumberFormatException nfex) {
				throw new TypeConversionException(value, nfex);
			}

		}

		try {
			return SDateTime.from(stringValue,"yyyyMMddHHmmss");
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
