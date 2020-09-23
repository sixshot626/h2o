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

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * Converts given object to <code>java.sql.Time</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li><code>Calendar</code> object is converted</li>
 * <li><code>Date</code> object is converted</li>
 * <li><code>JulianDate</code> object is converted</li>
 * <li><code>LocalDateTime</code> object is converted</li>
 * <li><code>LocalDate</code> object is converted</li>
 * <li><code>Number</code> is used as number of milliseconds</li>
 * <li>finally, if string value contains only numbers it is parsed as milliseconds</li>
 * </ul>
 */
public class SqlTimeConverter implements TypeConverter<Time> {

	@Override
	public Time convert(final Object value) {

		if (value == null) {
			return null;
		}
		
		if (value instanceof Time) {
			return (Time) value;
		}
		if (value instanceof Calendar) {
			return new Time(((Calendar) value).getTimeInMillis());
		}
		if (value instanceof Date) {
			return new Time(((Date)value).getTime());
		}
		if (value instanceof JulianDate) {
			return new Time(((JulianDate) value).toMilliseconds());
		}
		if (value instanceof LocalDateTime) {
			return new Time(TimeUtil.toMilliseconds((LocalDateTime) value));
		}
		if (value instanceof LocalDate) {
			return new Time(TimeUtil.toMilliseconds((LocalDate) value));
		}

		if ( value instanceof SDate) {
			if ( ((SDate) value).isPresent() ) {
				return new Time(((SDate) value).toDate().getTime());
			} else {
				return null;
			}
		}

		if ( value instanceof SDateTime) {
			if ( ((SDateTime) value).isPresent() ) {
				return new Time( ((SDateTime) value).toDate().getTime() );
			} else {
				return null;
			}
		}

		if ( value instanceof LTimestamp) {
			if ( ((LTimestamp) value).isPresent() ) {
				return new Time( ((LTimestamp) value).getValue() );
			} else {
				return null;
			}
		}

		if ( value instanceof SNumber) {
			if ( ((SNumber) value).isPresent() ) {
				return new Time(((Number) value).longValue());
			} else {
				return null;
			}
		}



		if (value instanceof Number) {
			return new Time(((Number) value).longValue());
		}


		final String stringValue = value.toString().trim();

		// try yyyy-mm-dd for valueOf
		if (!StringUtil.containsOnlyDigits(stringValue)) {
			try {
				return Time.valueOf(stringValue);
			} catch (IllegalArgumentException iaex) {
				throw new TypeConversionException(value, iaex);
			}
		}

		// assume string to be a number
		try {
			long milliseconds = Long.parseLong(stringValue);
			return new Time(milliseconds);
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
