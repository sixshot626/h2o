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

package h2o.jodd.io;

import h2o.jodd.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Network utilities.
 */
public class NetUtil {

	public static final String LOCAL_HOST = "localhost";
	public static final String LOCAL_IP = "127.0.0.1";
	public static final String DEFAULT_MASK = "255.255.255.0";
	public static final int INT_VALUE_127_0_0_1 = 0x7f000001;

	/**
	 * Resolves IP address from a hostname.
	 */
	public static String resolveIpAddress(final String hostname) {
		try {
			final InetAddress netAddress;

			if (hostname == null || hostname.equalsIgnoreCase(LOCAL_HOST)) {
				netAddress = InetAddress.getLocalHost();
			} else {
				netAddress = Inet4Address.getByName(hostname);
			}
			return netAddress.getHostAddress();
		} catch (final UnknownHostException ignore) {
			return null;
		}
	}

	/**
	 * Returns IP address as integer.
	 */
	public static int getIpAsInt(final String ipAddress) {
		int ipIntValue = 0;
		final String[] tokens = StringUtil.splitc(ipAddress, '.');
		for (final String token : tokens) {
			if (ipIntValue > 0) {
				ipIntValue <<= 8;
			}
			ipIntValue += Integer.parseInt(token);
		}
		return ipIntValue;
	}

	public static int getMaskAsInt(String mask) {
		if (!validateIPv4(mask)) {
			mask = DEFAULT_MASK;
		}
		return getIpAsInt(mask);
	}

	public static boolean isSocketAccessAllowed(final int localIp, final int socketIp, final int mask) {
		boolean _retVal = false;

		if (socketIp == INT_VALUE_127_0_0_1 || (localIp & mask) == (socketIp & mask)) {
			_retVal = true;
		}
		return _retVal;
	}

	private static final Pattern ip4RegExp = Pattern.compile("^((?:1?[1-9]?\\d|2(?:[0-4]\\d|5[0-5]))\\.){4}$");

	/**
	 * Checks given string against IP address v4 format.
	 *
	 * @param input an ip address - may be null
	 * @return <tt>true</tt> if param has a valid ip v4 format <tt>false</tt> otherwise
	 * @see <a href="https://en.wikipedia.org/wiki/IP_address#IPv4_addresses">ip address v4</a>
	 */
	public static boolean validateIPv4(final String input) {
		final Matcher m = ip4RegExp.matcher(input + '.');
		return m.matches();
	}

	/**
	 * Resolves host name from IP address bytes.
	 */
	public static String resolveHostName(final byte[] ip) {
		try {
			final InetAddress address = InetAddress.getByAddress(ip);
			return address.getHostName();
		} catch (final UnknownHostException ignore) {
			return null;
		}
	}

	// ---------------------------------------------------------------- download

	/**
	 * Downloads resource as byte array.
	 */
	public static byte[] downloadBytes(final String url) throws IOException {
		try (final InputStream inputStream = new URL(url).openStream()) {
			return IOUtil.readBytes(inputStream);
		}
	}

	/**
	 * Downloads resource as String.
	 */
	public static String downloadString(final String url, final Charset encoding) throws IOException {
		try (final InputStream inputStream = new URL(url).openStream()) {
			return new String(IOUtil.readChars(inputStream, encoding));
		}
	}

	/**
	 * Downloads resource as String.
	 */
	public static String downloadString(final String url) throws IOException {
		try (final InputStream inputStream = new URL(url).openStream()) {
			return new String(IOUtil.readChars(inputStream));
		}
	}

	/**
	 * Downloads resource to a file, potentially very efficiently.
	 */
	public static void downloadFile(final String url, final File file) throws IOException {
		try (
				final InputStream inputStream = new URL(url).openStream();
				final ReadableByteChannel rbc = Channels.newChannel(inputStream);
				final FileChannel fileChannel = FileChannel.open(
				file.toPath(),
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE)
		) {
			fileChannel.transferFrom(rbc, 0, Long.MAX_VALUE);
		}
	}

	/**
	 * Get remote file size. Returns -1 if the content length is unknown
	 *
	 * @param url remote file url
	 * @return file size
	 * @throws IOException JDK-IOException
	 */
	public static long getRemoteFileSize(final String url) throws IOException {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			return connection.getContentLengthLong();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
