package com.qican.ygj.utils;

import java.io.FileOutputStream;
import java.io.OutputStream;


import android.util.Log;

public class Logging {
	
	private static String sLoggingPath = "";
	
	public static void setLogPath(String path) {
		sLoggingPath = path;
	}
	
	public static void writeLog(String data) {
		if (StringUtils.isEmpty(sLoggingPath))
			return;
		final String LF = "\r\n";
		OutputStream stream = null;		
		try {
			stream = new FileOutputStream(sLoggingPath, true);
			byte [] buffer = data.getBytes();
			stream.write(buffer);
			stream.write(LF.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtils.closeCloseable(stream);
		}
	}

	public static void e(String tag, String msg) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Exception e) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.e(tag, msg + "error: " + e);		
	}

	public static void d(String tag, String msg) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.d(tag, msg);
	}
	
	public static void d(String tag, String msg, Exception e) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.d(tag, msg, e);
	}

	public static void i(String tag, String msg) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.i(tag, msg);		
	}

	public static boolean isDebugLogging() {
		return true;
	}

	public static void w(String tag, String msg, Exception e) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.w(tag, msg, e);
		
	}

	public static void w(String tag, String msg) {
		tag += "(" + Thread.currentThread().getId() + ")"; 
		Log.w(tag, msg);
		
	}
}
