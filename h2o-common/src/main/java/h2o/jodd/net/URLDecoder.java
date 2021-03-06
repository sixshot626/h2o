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

package h2o.jodd.net;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * URL decoder.
 */
public class URLDecoder {

	/**
	 * Decodes URL elements.
	 */
	public static String decode(final String url) {
		return decode(url, StandardCharsets.UTF_8, false);
	}

	/**
	 * Decodes URL elements. This method may be used for all
	 * parts of URL, except for the query parts, since it does
	 * not decode the '+' character.
	 * @see #decodeQuery(String, Charset)
	 */
	public static String decode(final String source, final Charset encoding) {
		return decode(source, encoding, false);
	}

	/**
	 * Decodes query name or value.
	 */
	public static String decodeQuery(final String source) {
		return decode(source, StandardCharsets.UTF_8, true);
	}

	/**
	 * Decodes query name or value.
	 */
	public static String decodeQuery(final String source, final Charset encoding) {
		return decode(source, encoding, true);
	}

	private static String decode(final String source, final Charset encoding, final boolean decodePlus) {
		final int length = source.length();
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(length);

		boolean changed = false;

		for (int i = 0; i < length; i++) {
			int ch = source.charAt(i);
			switch (ch) {
				case '%':
					if ((i + 2) < length) {
						final char hex1 = source.charAt(i + 1);
						final char hex2 = source.charAt(i + 2);
						final int u = Character.digit(hex1, 16);
						final int l = Character.digit(hex2, 16);
						if (u == -1 || l == -1) {
							throw new IllegalArgumentException("Invalid sequence: " + source.substring(i));
						}
						bos.write((char) ((u << 4) + l));
						i += 2;
						changed = true;
					} else {
						throw new IllegalArgumentException("Invalid sequence: " + source.substring(i));
					}
					break;

				case '+':
					if (decodePlus) {
						ch = ' ';
						changed = true;
					}

				default:
					bos.write(ch);
			}
		}
		return changed ? new String(bos.toByteArray(), encoding) : source;
	}

}
