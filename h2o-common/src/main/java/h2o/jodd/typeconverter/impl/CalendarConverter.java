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

import h2o.common.lang.LTimestamp;
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
 * Converts given object to <code>Byte</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Date</code> object is converted</li>
 * <li><code>JulianDate</code> object is converted</li>
 * <li><code>LocalDateTime</code> object is converted</li>
 * <li><code>LocalDate</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds</li>
 * </ul>
 */
public class CalendarConverter implements TypeConverter<Calendar> {

	@Override
	public Calendar convert(final Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Calendar) {
			return (Calendar) value;
		}
		if (value instanceof Date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) value);
			return calendar;
		}
		if (value instanceof JulianDate) {
			return TimeUtil.toCalendar(((JulianDate)value).toLocalDateTime());
		}
		if (value instanceof LocalDateTime) {
			return TimeUtil.toCalendar((LocalDateTime)value);
		}
		if (value instanceof LocalDate) {
			return TimeUtil.toCalendar((LocalDate)value);
		}

		if ( value instanceof SDate ) {
			if ( ((SDate) value).isPresent() ) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(((SDate) value).toDate());
				return calendar;
			} else {
				return null;
			}
		}

		if ( value instanceof SDateTime) {
			if ( ((SDateTime) value).isPresent() ) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(((SDateTime) value).toDate());
				return calendar;
			} else {
				return null;
			}
		}

		if ( value instanceof LTimestamp) {
			if ( ((LTimestamp) value).isPresent() ) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(((LTimestamp) value).getValue());
				return calendar;
			} else {
				return null;
			}
		}

		if ( value instanceof SNumber) {
			if ( ((SNumber) value).isPresent() ) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(((Number) value).longValue());
				return calendar;
			} else {
				return null;
			}
		}

		if (value instanceof Number) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(((Number) value).longValue());
			return calendar;
		}

		String stringValue = value.toString().trim();

		if (!StringUtil.containsOnlyDigits(stringValue)) {
			// try to parse default string format
			return TimeUtil.toCalendar(LocalDateTime.parse(stringValue));
		}

		try {
			long milliseconds = Long.parseLong(stringValue);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(milliseconds);
			return calendar;
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
