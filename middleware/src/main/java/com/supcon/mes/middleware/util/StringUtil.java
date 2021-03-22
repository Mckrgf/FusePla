package com.supcon.mes.middleware.util;

import android.webkit.MimeTypeMap;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String字符串处理加强版
 *
 * @author Leon Hu create at 2015年6月16日 上午9:44:30
 */
public class StringUtil {
	public static String EMPTY_STRING = "";

	/**
	 * Represents a failed index search.
	 *
	 * @since 2.1
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * <p>Checks if a String is whitespace, empty ("") or null.</p>
	 * <p>
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	// Empty checks
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Checks if a String is empty ("") or null.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.isEmpty(null)      = true
	 * StringUtils.isEmpty("")        = true
	 * StringUtils.isEmpty(" ")       = false
	 * StringUtils.isEmpty("bob")     = false
	 * StringUtils.isEmpty("  bob  ") = false
	 * </pre>
	 * <p>
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the
	 * String. That functionality is available in isBlank().
	 * </p>
	 *
	 * @param str the String to check, may be null
	 * @return <code>true</code> if the String is empty or null
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal
	 * ignoring the case.
	 * </p>
	 * <p>
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered equal. Comparison is case insensitive.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.equalsIgnoreCase(null, null)   = true
	 * StringUtils.equalsIgnoreCase(null, "abc")  = false
	 * StringUtils.equalsIgnoreCase("abc", null)  = false
	 * StringUtils.equalsIgnoreCase("abc", "abc") = true
	 * StringUtils.equalsIgnoreCase("abc", "ABC") = true
	 * </pre>
	 *
	 * @param str1 the first String, may be null
	 * @param str2 the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case insensitive, or
	 * both <code>null</code>
	 * @see String#equalsIgnoreCase(String)
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equalsIgnoreCase(str2);
	}

	/**
	 * <p>
	 * Compares two Strings, returning <code>true</code> if they are equal.
	 * </p>
	 * <p>
	 * <p>
	 * <code>null</code>s are handled without exceptions. Two <code>null</code>
	 * references are considered to be equal. The comparison is case sensitive.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.equals(null, null)   = true
	 * StringUtils.equals(null, "abc")  = false
	 * StringUtils.equals("abc", null)  = false
	 * StringUtils.equals("abc", "abc") = true
	 * StringUtils.equals("abc", "ABC") = false
	 * </pre>
	 *
	 * @param str1 the first String, may be null
	 * @param str2 the second String, may be null
	 * @return <code>true</code> if the Strings are equal, case sensitive, or
	 * both <code>null</code>
	 * @see String#equals(Object)
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	/**
	 * 去掉空格为空返回真
	 *
	 * @param str 字符串
	 * @return 如果字符串为空或去掉空格长度为0, 返回真，其他返回假
	 */
	public static boolean isTrimBlank(String str) {
		return str == null || str.length() < 1;
	}

	/**
	 * 首字母大写
	 *
	 * @param str 要转换的字符串
	 * @return 首字母大写的字符串
	 */
	public static String capFirstUpperCase(String str) {
		if (isBlank(str)) {
			return str;
		}
		return str.substring(0, 1).toUpperCase(Locale.getDefault()) + str.substring(1);

	}

	/**
	 * 去掉字符串中的空 例如 空格 回车 等
	 *
	 * @param str
	 * @return
	 */
	public static String filter(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 将中文转为URL可使用的字符串
	 *
	 * @param s
	 * @return
	 */

	public static String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= 255) {
				sb.append(c);
			} else {
				byte[] b;
				try {
					b = String.valueOf(c).getBytes("utf-8");
				} catch (Exception ex) {
					System.err.println(ex);
					b = new byte[0];
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					sb.append("%" + Integer.toHexString(k).toUpperCase(Locale.getDefault()));
				}
			}
		}
		return sb.toString();
	}


	/**
	 * 根据Unicode编码判断中文汉字
	 */
	public static boolean isChinese(char c) {
		UnicodeBlock ub = UnicodeBlock.of(c);
		if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

	/**
	 * 计算字符串长度,中文算2个字符长度
	 *
	 * @param str
	 * @return
	 */
	public static int getLength(String str) {
		int length = 0;
		char[] ch = str.toCharArray();
		for (char c : ch) {
			if (isChinese(c)) {
				length += 2;
			} else {
				length++;
			}
		}
		return length;
	}

	public static String md5(String string) throws NoSuchAlgorithmException {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	// Contains
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Checks if String contains a search character, handling <code>null</code>.
	 * This method uses {@link String#indexOf(int)}.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>false</code>.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.contains(null, *)    = false
	 * StringUtils.contains("", *)      = false
	 * StringUtils.contains("abc", 'a') = true
	 * StringUtils.contains("abc", 'z') = false
	 * </pre>
	 *
	 * @param str        the String to check, may be null
	 * @param searchChar the character to find
	 * @return true if the String contains the search character, false if not or
	 * <code>null</code> string input
	 * @since 2.0
	 */

	public static boolean contains(String str, char searchChar) {
		if (isEmpty(str))
			return false;
		else
			return str.indexOf(searchChar) >= 0;
	}

	/**
	 * <p>
	 * Checks if String contains a search String, handling <code>null</code>.
	 * This method uses {@link String#indexOf(String)}.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> String will return <code>false</code>.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.contains(null, *)     = false
	 * StringUtils.contains(*, null)     = false
	 * StringUtils.contains("", "")      = true
	 * StringUtils.contains("abc", "")   = true
	 * StringUtils.contains("abc", "a")  = true
	 * StringUtils.contains("abc", "z")  = false
	 * </pre>
	 *
	 * @param str       the String to check, may be null
	 * @param searchStr the String to find, may be null
	 * @return true if the String contains the search String, false if not or
	 * <code>null</code> string input
	 * @since 2.0
	 */
	public static boolean contains(String str, String searchStr) {
		if (str == null || searchStr == null)
			return false;
		else
			return str.indexOf(searchStr) >= 0;
	}

	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * <p>
	 * <p>
	 * A negative start position can be used to start <code>n</code> characters
	 * from the end of the String.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> String will return <code>null</code>. An empty ("")
	 * String will return "".
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.substring(null, *)   = null
	 * StringUtils.substring("", *)     = ""
	 * StringUtils.substring("abc", 0)  = "abc"
	 * StringUtils.substring("abc", 2)  = "c"
	 * StringUtils.substring("abc", 4)  = ""
	 * StringUtils.substring("abc", -2) = "bc"
	 * StringUtils.substring("abc", -4) = "abc"
	 * </pre>
	 *
	 * @param str   the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the
	 *              end of the String by this many characters
	 * @return substring from start position, <code>null</code> if null String
	 * input
	 */
	public static String substring(String str, int start) {
		if (str == null)
			return null;
		if (start < 0)
			start = str.length() + start;
		if (start < 0)
			start = 0;
		if (start > str.length())
			return "";
		else
			return str.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 * <p>
	 * <p>
	 * A negative start position can be used to start/end <code>n</code>
	 * characters from the end of the String.
	 * </p>
	 * <p>
	 * <p>
	 * The returned substring starts with the character in the
	 * <code>start</code> position and ends before the <code>end</code>
	 * position. All position counting is zero-based -- i.e., to start at the
	 * beginning of the string use <code>start = 0</code>. Negative start and
	 * end positions can be used to specify offsets relative to the end of the
	 * String.
	 * </p>
	 * <p>
	 * <p>
	 * If <code>start</code> is not strictly to the left of <code>end</code>, ""
	 * is returned.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.substring(null, *, *)    = null
	 * StringUtils.substring("", * ,  *)    = "";
	 * StringUtils.substring("abc", 0, 2)   = "ab"
	 * StringUtils.substring("abc", 2, 0)   = ""
	 * StringUtils.substring("abc", 2, 4)   = "c"
	 * StringUtils.substring("abc", 4, 6)   = ""
	 * StringUtils.substring("abc", 2, 2)   = ""
	 * StringUtils.substring("abc", -2, -1) = "b"
	 * StringUtils.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 *
	 * @param str   the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the
	 *              end of the String by this many characters
	 * @param end   the position to end at (exclusive), negative means count back
	 *              from the end of the String by this many characters
	 * @return substring from start position to end positon, <code>null</code>
	 * if null String input
	 */
	public static String substring(String str, int start, int end) {
		if (str == null)
			return null;
		if (end < 0)
			end = str.length() + end;
		if (start < 0)
			start = str.length() + start;
		if (end > str.length())
			end = str.length();
		if (start > end)
			return "";
		if (start < 0)
			start = 0;
		if (end < 0)
			end = 0;
		return str.substring(start, end);
	}

	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(int)}.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)         = -1
	 * StringUtils.lastIndexOf("", *)           = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'a') = 7
	 * StringUtils.lastIndexOf("aabaabaa", 'b') = 5
	 * </pre>
	 *
	 * @param str        the String to check, may be null
	 * @param searchChar the character to find
	 * @return the last index of the search character, -1 if no match or
	 * <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar) {
		if (isEmpty(str))
			return -1;
		else
			return str.lastIndexOf(searchChar);
	}

	/**
	 * <p>
	 * Finds the last index within a String from a start position, handling
	 * <code>null</code>. This method uses {@link String#lastIndexOf(int, int)}.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> or empty ("") String will return <code>-1</code>. A
	 * negative start position returns <code>-1</code>. A start position greater
	 * than the string length searches the whole string.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *, *)          = -1
	 * StringUtils.lastIndexOf("", *,  *)           = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 8)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 4)  = 2
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 0)  = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'b', 9)  = 5
	 * StringUtils.lastIndexOf("aabaabaa", 'b', -1) = -1
	 * StringUtils.lastIndexOf("aabaabaa", 'a', 0)  = 0
	 * </pre>
	 *
	 * @param str        the String to check, may be null
	 * @param searchChar the character to find
	 * @param startPos   the start position
	 * @return the last index of the search character, -1 if no match or
	 * <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, char searchChar, int startPos) {
		if (isEmpty(str))
			return -1;
		else
			return str.lastIndexOf(searchChar, startPos);
	}

	/**
	 * <p>
	 * Finds the last index within a String, handling <code>null</code>. This
	 * method uses {@link String#lastIndexOf(String)}.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> String will return <code>-1</code>.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.lastIndexOf(null, *)          = -1
	 * StringUtils.lastIndexOf(*, null)          = -1
	 * StringUtils.lastIndexOf("", "")           = 0
	 * StringUtils.lastIndexOf("aabaabaa", "a")  = 7
	 * StringUtils.lastIndexOf("aabaabaa", "b")  = 5
	 * StringUtils.lastIndexOf("aabaabaa", "ab") = 4
	 * StringUtils.lastIndexOf("aabaabaa", "")   = 8
	 * </pre>
	 *
	 * @param str       the String to check, may be null
	 * @param searchStr the String to find, may be null
	 * @return the last index of the search String, -1 if no match or
	 * <code>null</code> string input
	 * @since 2.0
	 */
	public static int lastIndexOf(String str, String searchStr) {
		if (str == null || searchStr == null)
			return -1;
		else
			return str.lastIndexOf(searchStr);
	}

	// Replacing
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, once.
	 * </p>
	 * <p>
	 * <p>
	 * A <code>null</code> reference passed to this method is a no-op.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.replaceOnce(null, *, *)        = null
	 * StringUtils.replaceOnce("", *, *)          = ""
	 * StringUtils.replaceOnce("any", null, *)    = "any"
	 * StringUtils.replaceOnce("any", *, null)    = "any"
	 * StringUtils.replaceOnce("any", "", *)      = "any"
	 * StringUtils.replaceOnce("aba", "a", null)  = "aba"
	 * StringUtils.replaceOnce("aba", "a", "")    = "ba"
	 * StringUtils.replaceOnce("aba", "a", "z")   = "zba"
	 * </pre>
	 *
	 * @param text         text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement  the String to replace with, may be null
	 * @return the text with any replacements processed, <code>null</code> if
	 * null String input
	 * @see #replace(String text, String searchString, String replacement, int
	 * max)
	 */
	public static String replaceOnce(String text, String searchString, String replacement) {
		return replace(text, searchString, replacement, 1);
	}

	/**
	 * <p>
	 * Replaces all occurrences of a String within another String.
	 * </p>
	 * <p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.replace(null, *, *)        = null
	 * StringUtils.replace("", *, *)          = ""
	 * StringUtils.replace("any", null, *)    = "any"
	 * StringUtils.replace("any", *, null)    = "any"
	 * StringUtils.replace("any", "", *)      = "any"
	 * StringUtils.replace("aba", "a", null)  = "aba"
	 * StringUtils.replace("aba", "a", "")    = "b"
	 * StringUtils.replace("aba", "a", "z")   = "zbz"
	 * </pre>
	 *
	 * @param text         text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement  the String to replace it with, may be null
	 * @return the text with any replacements processed, {@code null} if null
	 * String input
	 * @see #replace(String text, String searchString, String replacement, int
	 * max)
	 */
	public static String replace(String text, String searchString, String replacement) {
		return replace(text, searchString, replacement, -1);
	}

	/**
	 * <p>
	 * Replaces a String with another String inside a larger String, for the
	 * first {@code max} values of the search String.
	 * </p>
	 * <p>
	 * <p>
	 * A {@code null} reference passed to this method is a no-op.
	 * </p>
	 * <p>
	 * <pre>
	 * StringUtils.replace(null, *, *, *)         = null
	 * StringUtils.replace("", *, *, *)           = ""
	 * StringUtils.replace("any", null, *, *)     = "any"
	 * StringUtils.replace("any", *, null, *)     = "any"
	 * StringUtils.replace("any", "", *, *)       = "any"
	 * StringUtils.replace("any", *, *, 0)        = "any"
	 * StringUtils.replace("abaa", "a", null, -1) = "abaa"
	 * StringUtils.replace("abaa", "a", "", -1)   = "b"
	 * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
	 * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
	 * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
	 * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
	 * </pre>
	 *
	 * @param text         text to search and replace in, may be null
	 * @param searchString the String to search for, may be null
	 * @param replacement  the String to replace it with, may be null
	 * @param max          maximum number of values to replace, or {@code -1} if no
	 *                     maximum
	 * @return the text with any replacements processed, {@code null} if null
	 * String input
	 */
	public static String replace(String text, String searchString, String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
			return text;
		}
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == INDEX_NOT_FOUND) {
			return text;
		}
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		StringBuilder buf = new StringBuilder(text.length() + increase);
		while (end != INDEX_NOT_FOUND) {
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0) {
				break;
			}
			end = text.indexOf(searchString, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/************************************************


	/**
	 * 判断有没有中文
	 *
	 * @param sequence
	 * @return
	 */
	public static boolean checkChinese(String sequence) {
		final String format = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D]";
		boolean result = false;
		Pattern pattern = Pattern.compile(format);
		Matcher matcher = pattern.matcher(sequence);
		result = matcher.find();
		return result;
	}

	public static String getMimeType(String fileName) {
		String name = fileName;
		try {
			name = URLEncoder.encode(name, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String extension = MimeTypeMap.getFileExtensionFromUrl(name).toLowerCase(Locale.CHINA);
		String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		if (StringUtil.isBlank(type)) {
			String extensionSelf = getMIMEType(name, false);
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extensionSelf);
		}
		return type;
	}


	/**
	 * 判断文件MimeType的方法
	 *
	 * @param isOpen 目的打开方式为true
	 * @return
	 */
	public static String getMIMEType(String fileName, boolean isOpen) {
		String type = "";
		String fName = fileName;
   /* 取得扩展名 */
		String end = "";
		if( fName.length() > 1){
			end = StringUtil.substring(fName, fName.lastIndexOf(".") + 1, fName.length());
		}
		if (end != null) {
			end.toLowerCase();
		}
//		String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
		if (isOpen) {
		   /* 依附档名的类型决定MimeType */
			if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
				type = "audio";
			} else if (end.equals("3gp") || end.equals("mp4")) {
				type = "video";
			} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
				type = "image";
			} else {
             /* 如果无法直接打开，就跳出软件列表给用户选择 */
				type = "*";
			}
			type += "/*";
		} else {
			if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
				type = "audio";
			} else if (end.equals("3gp") || end.equals("mp4")) {
				type = "video";
			} else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
				type = "image";
			} else if (end.equals("apk")) {
				type = "apk";
			} else {
				type = end;
			}
		}
		return type;
	}

    public static String[] split(String str, String regex) {
        if (str == null)
            return null;
        str = replace(str, " ", "");
        return str.split(regex);
    }

}
