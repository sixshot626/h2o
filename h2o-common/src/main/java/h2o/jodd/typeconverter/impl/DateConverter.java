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
import h2o.common.lang.SDate;
import h2o.common.lang.SDateTime;
import h2o.common.lang.SNumber;
import h2o.jodd.time.JulianDate;
import h2o.jodd.time.TimeUtil;
import h2o.jodd.typeconverter.TypeConversionException;
import h2o.jodd.typeconverter.TypeConverter;
import h2o.jodd.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class DateConverter implements TypeConverter<Date> {

	@Override
	public Date convert(final Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof Date) {
			return (Date) value;
		}
		if (value instanceof Calendar) {
			return new Date(((Calendar)value).getTimeInMillis());
		}
		if (value instanceof JulianDate) {
			return new Date(((JulianDate) value).toMilliseconds());
		}
		if (value instanceof LocalDateTime) {
			return TimeUtil.toDate((LocalDateTime)value);
		}
		if (value instanceof LocalDate) {
			return TimeUtil.toDate((LocalDate)value);
		}

		if ( value instanceof SDate) {
			if ( ((SDate) value).isPresent() ) {
				return ((SDate) value).toDate();
			} else {
				return null;
			}
		}

		if ( value instanceof SDateTime) {
			if ( ((SDateTime) value).isPresent() ) {
				return ((SDateTime) value).toDate();
			} else {
				return null;
			}
		}

		if ( value instanceof LTime) {
			if ( ((LTime) value).isPresent() ) {
				return new Date(((LTime) value).getValue());
			} else {
				return null;
			}
		}

		if ( value instanceof SNumber) {
			if ( ((SNumber) value).isPresent() ) {
				return new Date(((Number) value).longValue());
			} else {
				return null;
			}
		}


		if (value instanceof Number) {
			return new Date(((Number) value).longValue());
		}

		final String stringValue = value.toString().trim();

		if (!StringUtil.containsOnlyDigits(stringValue)) {
			// try to parse default string format
			return TimeUtil.toDate(LocalDateTime.parse(stringValue));
		}

		try {
			long milliseconds = Long.parseLong(stringValue);
			return new Date(milliseconds);
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
