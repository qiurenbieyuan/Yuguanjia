package com.qican.ygj.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class IniUtils {

	private static List<String> keyNameList = null;
	private static SharedPreferences sShare = null;

	private static void addList(String key) {
		if (keyNameList != null) {
			if (!keyNameList.contains(key)) {
				keyNameList.add(key);
			}
		}
	}

	// 清除数据
	public static boolean clear(String key) {
		if (sShare != null) {
			Editor edit = sShare.edit();
			
			edit.remove(key).commit();

		}

		return false;
	}

	public static void createFile(String proName) {
		Context context = NetworkUtils.getApplicationContext();
		if (sShare == null && keyNameList == null) {
			keyNameList = new ArrayList<String>();
			sShare = context.getSharedPreferences(proName, 0);
		}
	}

	// 获取boolean
	public static boolean getBoolean(String key, boolean defValue) {
		if (null == sShare)
			return defValue;

		return sShare.getBoolean(key, defValue);
	}

	// 获取float
	public static float getFloat(String key, float defValue) {
		if (null == sShare)
			return defValue;

		return sShare.getFloat(key, defValue);
	}

	// 获取integer
	public static int getInt(String key, int defValue) {
		if (null == sShare)
			return defValue;

		return sShare.getInt(key, defValue);
	}

	// 获取long
	public static long getLong(String key, long defValue) {
		if (null == sShare)
			return defValue;

		return sShare.getLong(key, defValue);
	}

	// 获取string
	public static String getString(String key, String defValue) {
		if (null == sShare)
			return defValue;

		return sShare.getString(key, defValue);
	}

	// 设置boolean
	public static boolean putBoolean(String key, boolean value) {
		if (null == sShare)
			return false;
		addList(key);
		Editor edit = sShare.edit();
		edit.putBoolean(key, value);
		return edit.commit();
	}

	// 设置float
	public static boolean putFloat(String key, float value) {
		if (null == sShare)
			return false;
		addList(key);
		Editor edit = sShare.edit();
		edit.putFloat(key, value);
		return edit.commit();
	}

	// 设置integer
	public static boolean putInt(String key, int value) {
		if (null == sShare)
			return false;
		addList(key);
		Editor edit = sShare.edit();
		edit.putInt(key, value);
		return edit.commit();
	}

	// 设置long
	public static boolean putLong(String key, long value) {
		if (null == sShare)
			return false;
		addList(key);
		Editor edit = sShare.edit();
		edit.putLong(key, value);
		return edit.commit();
	}

	// 设置string
	public static boolean putString(String key, String value) {
		if (null == sShare)
			return false;
		addList(key);
		Editor edit = sShare.edit();
		edit.putString(key, value);

		return edit.commit();
	}

	public static void clearAllCache() {
		if (keyNameList != null && keyNameList.size() != 0) {
			for (String item : keyNameList) {
				clear(item);
			}
			keyNameList.clear();
		}

	}
}
