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

import h2o.jodd.util.StringPool;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static h2o.jodd.util.CharUtil.*;

/**
 * Encodes URLs correctly, significantly faster and more convenient.
 * <p>
 * Here is an example of full URL:
 * {@literal https://jodd:ddoj@www.jodd.org:8080/file;p=1?q=2#third}.
 * It consist of:
 * <ul>
 *     <li>scheme (https)</li>
 *     <li>user (jodd)</li>
 *     <li>password (ddoj)</li>
 *     <li>host (www.jodd.org)</li>
 *     <li>port (8080)</li>
 *     <li>path (file)</li>
 *     <li>path parameter (p=1)</li>
 *     <li>query parameter (q=2)</li>
 *     <li>fragment (third)</li>
 * </ul>
 * Each URL part has its own encoding rules. The <b>only</b> correct way of
 * encoding URLs is to encode each part separately, and then to concatenate
 * results. For easier query building you can use {@link #build(String) builder}.
 * It provides fluent interface for defining query parameters.
 */
public class URLCoder {

	private static final String SCHEME_PATTERN = "([^:/?#]+):";

	private static final String HTTP_PATTERN = "(http|https):";

	private static final String USERINFO_PATTERN = "([^@/]*)";

	private static final String HOST_PATTERN = "([^/?#:]*)";

	private static final String PORT_PATTERN = "(\\d*)";

	private static final String PATH_PATTERN = "([^?#]*)";

	private static final String QUERY_PATTERN = "([^#]*)";

	private static final String LAST_PATTERN = "(.*)";

	// Regex patterns that matches URIs. See RFC 3986, appendix B

	private static final Pattern URI_PATTERN = Pattern.compile(
			"^(" + SCHEME_PATTERN + ")?" + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN +
					")?" + ")?" + PATH_PATTERN + "(\\?" + QUERY_PATTERN + ")?" + "(#" + LAST_PATTERN + ")?");

	private static final Pattern HTTP_URL_PATTERN = Pattern.compile(
			'^' + HTTP_PATTERN + "(//(" + USERINFO_PATTERN + "@)?" + HOST_PATTERN + "(:" + PORT_PATTERN + ")?" + ")?" +
					PATH_PATTERN + "(\\?" + LAST_PATTERN + ")?");

	/**
	 * Enumeration to identify the parts of a URI.
	 * <p>
	 * Contains methods to indicate whether a given character is valid in a specific URI component.
	 *
	 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
	 */
	enum URIPart {

		UNRESERVED {
			@Override
			public boolean isValid(final char c) {
				return isUnreserved(c);
			}
		},

		SCHEME {
			@Override
			public boolean isValid(final char c) {
				return isAlpha(c) || isDigit(c) || c == '+' || c == '-' || c == '.';
			}
		},
//		AUTHORITY {
//			@Override
//			public boolean isValid(char c) {
//				return isUnreserved(c) || isSubDelimiter(c) || c == ':' || c == '@';
//			}
//		},
		USER_INFO {
			@Override
			public boolean isValid(final char c) {
				return isUnreserved(c) || isSubDelimiter(c) || c == ':';
			}
		},
		HOST {
			@Override
			public boolean isValid(final char c) {
				return isUnreserved(c) || isSubDelimiter(c);
			}
		},
		PORT {
			@Override
			public boolean isValid(final char c) {
				return isDigit(c);
			}
		},
		PATH {
			@Override
			public boolean isValid(final char c) {
				return isPchar(c) || c == '/';
			}
		},
		PATH_SEGMENT {
			@Override
			public boolean isValid(final char c) {
				return isPchar(c);
			}
		},
		QUERY {
			@Override
			public boolean isValid(final char c) {
				return isPchar(c) || c == '/' || c == '?';
			}
		},
		QUERY_PARAM {
			@Override
			public boolean isValid(final char c) {
				if (c == '=' || c == '+' || c == '&' || c == ';') {
					return false;
				}
				return isPchar(c) || c == '/' || c == '?';
			}
		},
		FRAGMENT {
			@Override
			public boolean isValid(final char c) {
				return isPchar(c) || c == '/' || c == '?';
			}
		};

		/**
		 * Indicates whether the given character is allowed in this URI component.
		 *
		 * @return <code>true</code> if the character is allowed; {@code false} otherwise
		 */
		public abstract boolean isValid(char c);

	}


	// ---------------------------------------------------------------- util methods

	/**
	 * Encodes single URI component.
	 */
	private static String encodeUriComponent(final String source, final Charset encoding, final URIPart uriPart) {
		if (source == null) {
			return null;
		}

		final byte[] bytes = encodeBytes(source.getBytes(encoding), uriPart);

		final char[] chars = new char[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			chars[i] = (char) bytes[i];
		}
		return new String(chars);
	}

	/**
	 * Encodes byte array using allowed characters from {@link URIPart}.
	 */
	private static byte[] encodeBytes(final byte[] source, final URIPart uriPart) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream(source.length);
		for (byte b : source) {
			if (b < 0) {
				b += 256;
			}
			if (uriPart.isValid((char) b)) {
				bos.write(b);
			} else {
				bos.write('%');
				final char hex1 = Character.toUpperCase(Character.forDigit((b >> 4) & 0xF, 16));
				final char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
				bos.write(hex1);
				bos.write(hex2);
			}
		}
		return bos.toByteArray();
	}

	// ---------------------------------------------------------------- main methods

	/**
	 * Encodes string using default RFCP rules.
	 */
	public static String encode(final String string, final Charset encoding) {
		return encodeUriComponent(string, encoding, URIPart.UNRESERVED);
	}
	public static String encode(final String string) {
		return encodeUriComponent(string, StandardCharsets.UTF_8, URIPart.UNRESERVED);
	}

	/**
	 * Encodes the given URI scheme with the given encoding.
	 */
	public static String encodeScheme(final String scheme, final Charset encoding) {
		return encodeUriComponent(scheme, encoding, URIPart.SCHEME);
	}
	public static String encodeScheme(final String scheme) {
		return encodeUriComponent(scheme, StandardCharsets.UTF_8, URIPart.SCHEME);
	}

/*	/**
	 * Encodes the given URI authority with the given encoding.
	 *

	public static String encodeAuthority(String authority, String encoding) {
		return encodeUriComponent(authority, encoding, URIPart.AUTHORITY);
	}
	public static String encodeAuthority(String authority) {
		return encodeUriComponent(authority, JoddCore.encoding, URIPart.AUTHORITY);
	}
*/

	/**
	 * Encodes the given URI user info with the given encoding.
	 */
	public static String encodeUserInfo(final String userInfo, final Charset encoding) {
		return encodeUriComponent(userInfo, encoding, URIPart.USER_INFO);
	}
	public static String encodeUserInfo(final String userInfo) {
		return encodeUriComponent(userInfo, StandardCharsets.UTF_8, URIPart.USER_INFO);
	}

	/**
	 * Encodes the given URI host with the given encoding.
	 */
	public static String encodeHost(final String host, final Charset encoding) {
		return encodeUriComponent(host, encoding, URIPart.HOST);
	}
	public static String encodeHost(final String host) {
		return encodeUriComponent(host, StandardCharsets.UTF_8, URIPart.HOST);
	}

	/**
	 * Encodes the given URI port with the given encoding.
	 */
	public static String encodePort(final String port, final Charset encoding) {
		return encodeUriComponent(port, encoding, URIPart.PORT);
	}
	public static String encodePort(final String port) {
		return encodeUriComponent(port, StandardCharsets.UTF_8, URIPart.PORT);
	}

	/**
	 * Encodes the given URI path with the given encoding.
	 */
	public static String encodePath(final String path, final Charset encoding) {
		return encodeUriComponent(path, encoding, URIPart.PATH);
	}
	public static String encodePath(final String path) {
		return encodeUriComponent(path, StandardCharsets.UTF_8, URIPart.PATH);
	}

	/**
	 * Encodes the given URI path segment with the given encoding.
	 */
	public static String encodePathSegment(final String segment, final Charset encoding) {
		return encodeUriComponent(segment, encoding, URIPart.PATH_SEGMENT);
	}
	public static String encodePathSegment(final String segment) {
		return encodeUriComponent(segment, StandardCharsets.UTF_8, URIPart.PATH_SEGMENT);
	}

	/**
	 * Encodes the given URI query with the given encoding.
	 */
	public static String encodeQuery(final String query, final Charset encoding) {
		return encodeUriComponent(query, encoding, URIPart.QUERY);
	}
	public static String encodeQuery(final String query) {
		return encodeUriComponent(query, StandardCharsets.UTF_8, URIPart.QUERY);
	}

	/**
	 * Encodes the given URI query parameter with the given encoding.
	 */
	public static String encodeQueryParam(final String queryParam, final Charset encoding) {
		return encodeUriComponent(queryParam, encoding, URIPart.QUERY_PARAM);
	}
	public static String encodeQueryParam(final String queryParam) {
		return encodeUriComponent(queryParam, StandardCharsets.UTF_8, URIPart.QUERY_PARAM);
	}

	/**
	 * Encodes the given URI fragment with the given encoding.
	 */
	public static String encodeFragment(final String fragment, final Charset encoding) {
		return encodeUriComponent(fragment, encoding, URIPart.FRAGMENT);
	}
	public static String encodeFragment(final String fragment) {
		return encodeUriComponent(fragment, StandardCharsets.UTF_8, URIPart.FRAGMENT);
	}


	// ---------------------------------------------------------------- url

	/**
	 * @see #encodeUri(String, Charset)
	 */
	public static String encodeUri(final String uri) {
		return encodeUri(uri, StandardCharsets.UTF_8);
	}
	/**
	 * Encodes the given source URI into an encoded String. All various URI components are
	 * encoded according to their respective valid character sets.
	 * <p>This method does <b>not</b> attempt to encode "=" and "{@literal &}"
	 * characters in query parameter names and query parameter values because they cannot
	 * be parsed in a reliable way.
	 */
	public static String encodeUri(final String uri, final Charset encoding) {
		final Matcher m = URI_PATTERN.matcher(uri);
		if (m.matches()) {
			final String scheme = m.group(2);
			final String authority = m.group(3);
			final String userinfo = m.group(5);
			final String host = m.group(6);
			final String port = m.group(8);
			final String path = m.group(9);
			final String query = m.group(11);
			final String fragment = m.group(13);

			return encodeUriComponents(scheme, authority, userinfo, host, port, path, query, fragment, encoding);
		}
		throw new IllegalArgumentException("Invalid URI: " + uri);
	}

	/**
	 * @see #encodeHttpUrl(String, Charset)
	 */
	public static String encodeHttpUrl(final String httpUrl) {
		return encodeHttpUrl(httpUrl, StandardCharsets.UTF_8);
	}
	/**
	 * Encodes the given HTTP URI into an encoded String. All various URI components are
	 * encoded according to their respective valid character sets.
	 * <p>This method does <b>not</b> support fragments ({@code #}),
	 * as these are not supposed to be sent to the server, but retained by the client.
	 * <p>This method does <b>not</b> attempt to encode "=" and "{@literal &}"
	 * characters in query parameter names and query parameter values because they cannot
	 * be parsed in a reliable way.
	 */
	public static String encodeHttpUrl(final String httpUrl, final Charset encoding) {
		final Matcher m = HTTP_URL_PATTERN.matcher(httpUrl);
		if (m.matches()) {
			final String scheme = m.group(1);
			final String authority = m.group(2);
			final String userinfo = m.group(4);
			final String host = m.group(5);
			final String portString = m.group(7);
			final String path = m.group(8);
			final String query = m.group(10);

			return encodeUriComponents(scheme, authority, userinfo, host, portString, path, query, null, encoding);
		}
		throw new IllegalArgumentException("Invalid HTTP URL: " + httpUrl);
	}

	private static String encodeUriComponents(
		final String scheme, final String authority, final String userInfo,
		final String host, final String port, final String path, final String query,
		final String fragment, final Charset encoding) {

		final StringBuilder sb = new StringBuilder();

		if (scheme != null) {
			sb.append(encodeScheme(scheme, encoding));
			sb.append(':');
		}

		if (authority != null) {
			sb.append("//");
			if (userInfo != null) {
				sb.append(encodeUserInfo(userInfo, encoding));
				sb.append('@');
			}
			if (host != null) {
				sb.append(encodeHost(host, encoding));
			}
			if (port != null) {
				sb.append(':');
				sb.append(encodePort(port, encoding));
			}
		}

		sb.append(encodePath(path, encoding));

		if (query != null) {
			sb.append('?');
			sb.append(encodeQuery(query, encoding));
		}

		if (fragment != null) {
			sb.append('#');
			sb.append(encodeFragment(fragment, encoding));
		}

		return sb.toString();
	}

	// ---------------------------------------------------------------- builder

	/**
	 * Creates URL builder for user-friendly way of building URLs.
	 * Provided path is parsed and {@link #encodeUri(String) encoded}.
	 * @see #build(String, boolean)
	 */
	public static Builder build(final String path) {
		return build(path, true);
	}

	/**
	 * Creates URL builder with given path that can be optionally encoded.
	 * Since most of the time path is valid and does not require to be encoded,
	 * use this method to gain some performance. When encoding flag is turned off,
	 * provided path is used without processing.
	 * <p>
	 * The purpose of builder is to help with query parameters. All other URI parts
	 * should be set previously or after the URL is built.
	 */
	public static Builder build(final String path, final boolean encodePath) {
		return new Builder(path, encodePath, StandardCharsets.UTF_8);
	}

	public static class Builder {
		protected final StringBuilder url;
		protected final Charset encoding;
		protected boolean hasParams;

		public Builder(final String path, final boolean encodePath, final Charset encoding) {
			this.encoding = encoding;
			url = new StringBuilder();
			if (encodePath) {
				url.append(encodeUri(path, encoding));
			} else {
				url.append(path);
			}
			this.hasParams = url.indexOf(StringPool.QUESTION_MARK) != -1;
		}

		public Builder queryParam(final String name, final Object value) {
			return queryParam(name, value.toString());
		}

		/**
		 * Appends new query parameter to the url.
		 */
		public Builder queryParam(final String name, final String value) {
			url.append(hasParams ? '&' : '?');
			hasParams = true;

			url.append(encodeQueryParam(name, encoding));

			if ((value != null) && (value.length() > 0)) {
				url.append('=');
				url.append(encodeQueryParam(value, encoding));
			}
			return this;
		}

		/**
		 * Returns full URL.
		 */
		public String get() {
			return url.toString();
		}
	}

}
