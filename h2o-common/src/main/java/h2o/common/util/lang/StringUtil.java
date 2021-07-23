package h2o.common.util.lang;

import h2o.common.util.format.FormattingTuple;
import h2o.common.util.format.MessageFormatter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.INDEX_NOT_FOUND;
import static org.apache.commons.lang.StringUtils.isEmpty;

public abstract class StringUtil {

	private StringUtil() {
	}

	public static final String EMPTY = "";

	public static String referReplace(String str, String t, String searchString, String replacement) {
		return referReplace(str, t, searchString, replacement, -1);
	}
	
	
	public static String replaceIgnoreCase(String str, String searchString, String replacement) {
		return referReplaceIgnoreCase(str, str, searchString, replacement);
	}

	public static String referReplaceIgnoreCase(String str, String t, String searchString, String replacement) {
		if (isEmpty(t) || isEmpty(searchString) || replacement == null) {
			return str;
		}
		return referReplace(str, t.toUpperCase(), searchString.toUpperCase(), replacement, -1);
	}

	public static String referReplaceOnce(String str, String t, String searchString, String replacement) {
		return referReplace(str, t, searchString, replacement, 1);
	}
	
	
	public static String replaceOnceIgnoreCase(String str, String searchString, String replacement) {
		return referReplaceOnceIgnoreCase(str, str, searchString, replacement);
	}

	public static String referReplaceOnceIgnoreCase(String str, String t, String searchString, String replacement) {
		if (isEmpty(str) || isEmpty(t) || isEmpty(searchString) || replacement == null) {
			return str;
		}
		return referReplace(str, t.toUpperCase(), searchString.toUpperCase(), replacement, 1);
	}

	public static String referReplace(String str, String t, String searchString, String replacement, int max) {

		if (isEmpty(str) || isEmpty(t) || isEmpty(searchString) || replacement == null || max == 0) {
			return str;
		}

		int start = 0;
		int end = t.indexOf(searchString, start);
		if (end == INDEX_NOT_FOUND) {
			return t;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = (increase < 0 ? 0 : increase);
		increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
		StrBuilder buf = new StrBuilder(t.length() + increase);
		while (end != INDEX_NOT_FOUND) {
			buf.append(str.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = t.indexOf(searchString, start);
		}
		buf.append(str.substring(start));
		return buf.toString();

	}

	public static String referSubstringBefore(String str, String t, String separator) {

		if (isEmpty(t) || separator == null) {
			return str;
		}
		if (separator.length() == 0) {
			return EMPTY;
		}
		int pos = t.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String substringBeforeIgnoreCase(String str, String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		return referSubstringBefore(str, str.toUpperCase(), separator.toUpperCase());
	}

	public static String referSubstringBeforeLast(String str, String t, String separator) {
		if (isEmpty(t) || isEmpty(separator)) {
			return str;
		}
		int pos = t.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	public static String substringBeforeLastIgnoreCase(String str, String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		return referSubstringBeforeLast(str, str.toUpperCase(), separator.toUpperCase());
	}

	public static String referSubstringAfter(String str, String t, String separator) {

		if (isEmpty(t)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		int pos = t.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static String substringAfterIgnoreCase(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		return referSubstringAfter(str, str.toUpperCase(), separator.toUpperCase());
	}

	public static String referSubstringAfterLast(String str, String t, String separator) {
		if (isEmpty(t)) {
			return str;
		}
		if (isEmpty(separator)) {
			return EMPTY;
		}
		int pos = t.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND || pos == (t.length() - separator.length())) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	public static String substringAfterLastIgnoreCase(String str, String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return EMPTY;
		}
		return referSubstringAfterLast(str, str.toUpperCase(), separator.toUpperCase());
	}

	public static String referSubstringBetween(String str, String t, String open, String close) {
		if (t == null || open == null || close == null) {
			return null;
		}
		int start = t.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			int end = t.indexOf(close, start + open.length());
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	public static String substringBetweenIgnoreCase(String str, String open, String close) {
		if (str == null || open == null || close == null) {
			return null;
		}
		return referSubstringBetween(str, str.toUpperCase(), open.toUpperCase(), close.toUpperCase());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] referSubstringsBetween(String str, String t, String open, String close) {

		if (t == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		int strLen = t.length();
		if (strLen == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		int closeLen = close.length();
		int openLen = open.length();
		List list = new ArrayList();
		int pos = 0;
		while (pos < (strLen - closeLen)) {
			int start = t.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			int end = t.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String[] substringsBetweenIgnoreCase(String str, String open, String close) {
		if (str == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		return referSubstringsBetween(str, str.toUpperCase(), open.toUpperCase(), close.toUpperCase());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] splitByWholeSeparatorWorker(String str, String t, String separator, int max, boolean preserveAllTokens) {
		if (t == null) {
			return null;
		}

		int len = t.length();

		if (len == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}

		if ((separator == null) || (EMPTY.equals(separator))) {
			// Split on whitespace.
			return referSplitWorker(str, t, null, max, preserveAllTokens);
		}

		int separatorLength = separator.length();

		ArrayList substrings = new ArrayList();
		int numberOfSubstrings = 0;
		int beg = 0;
		int end = 0;
		while (end < len) {
			end = t.indexOf(separator, beg);

			if (end > -1) {
				if (end > beg) {
					numberOfSubstrings += 1;

					if (numberOfSubstrings == max) {
						end = len;
						substrings.add(str.substring(beg));
					} else {
						// The following is OK, because String.substring( beg,
						// end ) excludes
						// the character at the position 'end'.
						substrings.add(str.substring(beg, end));

						// Set the starting point for the next search.
						// The following is equivalent to beg = end +
						// (separatorLength - 1) + 1,
						// which is the right calculation:
						beg = end + separatorLength;
					}
				} else {
					// We found a consecutive occurrence of the separator, so
					// skip it.
					if (preserveAllTokens) {
						numberOfSubstrings += 1;
						if (numberOfSubstrings == max) {
							end = len;
							substrings.add(str.substring(beg));
						} else {
							substrings.add(EMPTY);
						}
					}
					beg = end + separatorLength;
				}
			} else {
				// String.substring( beg ) goes from 'beg' to the end of the
				// String.
				substrings.add(str.substring(beg));
				end = len;
			}
		}

		return (String[]) substrings.toArray(new String[substrings.size()]);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] referSplitWorker(String str, String t, String separatorChars, int max, boolean preserveAllTokens) {

		if (t == null) {
			return null;
		}
		int len = t.length();
		if (len == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		List list = new ArrayList();
		int sizePlus1 = 1;
		int i = 0, start = 0;
		boolean match = false;
		boolean lastMatch = false;
		if (separatorChars == null) {
			// Null separator means use whitespace
			while (i < len) {
				if (Character.isWhitespace(t.charAt(i))) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else if (separatorChars.length() == 1) {
			char sep = separatorChars.charAt(0);
			while (i < len) {
				if (t.charAt(i) == sep) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		} else {
			// standard case
			while (i < len) {
				if (separatorChars.indexOf(t.charAt(i)) >= 0) {
					if (match || preserveAllTokens) {
						lastMatch = true;
						if (sizePlus1++ == max) {
							i = len;
							lastMatch = false;
						}
						list.add(str.substring(start, i));
						match = false;
					}
					start = ++i;
					continue;
				}
				lastMatch = false;
				match = true;
				i++;
			}
		}
		if (match || (preserveAllTokens && lastMatch)) {
			list.add(str.substring(start, i));
		}
		return (String[]) list.toArray(new String[list.size()]);
	}
	
	
	
	public static String build( Object... strs ) {
		return append( new StringBuilder() , strs  ).toString();
	}
	
	
	public static String fmtBuild( String fmt , Object... args ) {
		FormattingTuple tp = MessageFormatter.arrayFormat( fmt , args);
		return tp.getMessage();
	}
	
	
	public static StringBuilder append( StringBuilder sb ,  Object... strs ) {
		if( strs == null ) {
			return sb.append("null");
		} else if( strs.length == 0 ){
			return sb;
		} else {			
			for( Object str : strs ) {
				 if( str instanceof CharSequence ) {
					 sb.append((CharSequence)str);
				 } else {
					 sb.append(str);
				 }				
			}
			return sb;
		}
	}
	
	
	public static StringBuffer append( StringBuffer sb ,  Object... strs ) {
		if( strs == null ) {
			return sb.append("null");
		} else if( strs.length == 0 ){
			return sb;
		} else {			
			for( Object str : strs ) {
				 if( str instanceof CharSequence ) {
					 sb.append((CharSequence)str);
				 } else {
					 sb.append(str);
				 }				
			}
			return sb;
		}
	}
	
	
	public static String toString( Object o , String defVal ) {
		return o == null ? defVal : (o instanceof String ? (String)o : o.toString());
	}
	
	public static String toNotEmptyString( Object o , String defVal ) {
		if( o == null ) {
			return defVal;
		} else {
			String s = o instanceof String ? (String)o : o.toString();
			return StringUtils.isEmpty(s) ? defVal : s;			
		}
	}
	
	public static String toNotBlankString( Object o , String defVal ) {
		if( o == null ) {
			return defVal;
		} else {
			String s = o instanceof String ? (String)o : o.toString();
			return StringUtils.isBlank(s) ? defVal : s;			
		}
	}
	
	
	
	
	
	
	


}
