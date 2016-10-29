package com.qican.ygj.utils;

import java.io.UnsupportedEncodingException;

import android.text.TextUtils;
import android.widget.EditText;

public class StringUtils {
	public static boolean isBlank(String value){
		return TextUtils.isEmpty(value);
	}
	public static boolean isEmpty(CharSequence value) {
		return TextUtils.isEmpty(value);
	}

	public static boolean isEmpty(EditText editText) {
		return TextUtils.isEmpty(editText.getText().toString().trim());
	}

	public static boolean parseArrayInt(int[] array, String value, String split) {
		if (array == null || StringUtils.isEmpty(value))
			return false;

		boolean ret = false;
		String[] ss = value.split(split);
		for (int i = 0; i < array.length && i < ss.length; i++) {
			ret = true;
			array[i] = Integer.parseInt(ss[i]);
		}

		return ret;
	}

	public static boolean isEqual(String compa, String compb) {
		if (compa == null || compb == null) return false;
		
		return compa.equals(compb);
	}

	// 去除字符串尾部空格
	public static String trimEnd(String str) {
		return str.replaceAll("[\\s]*$", "");
	}

	// 将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）
	public static String convert(String str) {
		return str.replaceAll("[^\\x00-\\xff]", "**");
	}
	
	public static String getUTF8Dom() {
		byte [] buffer = new byte [3];
		buffer[0] = (byte)0xEF;
		buffer[1] = (byte)0xBB;
		buffer[2] = (byte)0xBF;
		
		try {
			return new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public static boolean parseArrayString(String[] array, String value,
			String split) {
		if (array == null || StringUtils.isEmpty(value))
			return false;

		boolean ret = false;
		String[] ss = value.split(split);
		for (int i = 0; i < array.length && i < ss.length; i++) {
			ret = true;
			array[i] = ss[i];
		}

		return ret;
	}

	public static boolean copyString(String[] src, String[] dest) {
		if (src == null || dest == null)
			return false;

		for (int i = 0; i < dest.length; i++) {
			dest[i] = "";
		}

		for (int i = 0; i < dest.length && i < src.length; i++) {
			dest[i] = src[i];
		}

		return true;
	}

	public static String getUrlName(String url) {
		if (StringUtils.isEmpty(url))
			return "";

		int index = url.indexOf("?");
		if (index > 0)
			url = url.substring(0, index);

		index = url.lastIndexOf("/");
		if (index < 0)
			index = url.indexOf("\\");

		if (index < 0)
			return url;

		return url.substring(index + 1);
	}

	public static String getTime(int tm) {
		int h = tm / 3600;
		int m = tm / 60;
		int s = tm % 60;

		if (h > 0)
			return String.format("%02d:%02d:%02d", h, m, s);

		else if (m > 0)
			return String.format("%02d:%02d", m, s);

		return String.format("%02d", s);
	}

	public static double parseDouble(String value, double default_value) {
		try {
			return Double.parseDouble(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return default_value;
	}

	public static int parseInt(String value, int default_value) {
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return default_value;
	}
	
	public static String subString(String src, String find, int second) {
		int index = - find.length(), n = 0;
		do {
			int end = src.indexOf(find, index + find.length());
			if (end < 0) break;
			
			n ++;
			index = end;
			if (n == second) return src.substring(0, index);
			
		} while (index > 0 && n < second);
		
		return src;
	}
	
	public static String delString(String src, String db, String de) {
		int index = - db.length();
		do {
			index = src.indexOf(db, index + db.length());
			if (index > 0) {
				int end = src.indexOf(de, index + de.length());
				if (end > 0)
					src = src.substring(0, index) + src.substring(end + de.length());
				else
					src = src.substring(0, index);
			}
		} while (index > 0);
		
		return src;
	}

	public static int parseInt(String value) {
		if (isEmpty(value)) 
			return 0;
		
		try {
			return Integer.parseInt(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return 0;
	}

	public static long parseLong(String value, long default_value) {
		try {
			return Long.parseLong(value);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return default_value;
	}
	
	public static boolean isASCII(String str) {
		if (isEmpty(str))
			return false;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= 128 || str.charAt(i) < 0) {
				return false;
			}
		}
		return true;
	}
	
	public static final int MIN_LEN = 4;
	public static String delUTF8Dom(StringBuffer xml) {
		int len = xml.length() > MIN_LEN ? MIN_LEN : xml.length();
		String tmp = xml.substring(0, len);
		int index = tmp.indexOf('<');
		
		if (index < 0)
			index = tmp.indexOf('{');
		
		if (index < 0)
			index = tmp.indexOf('[');
		
		if (index > 0)
		{
			for (int i = 0; i < index; i ++) {
				xml.setCharAt(i, ' ');
			}
		}
		
		return xml.toString();
	}
}
