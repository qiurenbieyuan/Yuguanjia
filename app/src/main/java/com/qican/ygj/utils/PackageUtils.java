package com.qican.ygj.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;



import android.net.Uri;
import android.util.Log;



public class PackageUtils {
	public static final String TAG = PackageUtils.class.getSimpleName();

	public static class FilePosition {
		private int mOffset = 0;
		private int mSize = 0;

		public FilePosition(int offset, int size) {
			mOffset = offset;
			mSize = size;
		}

		public int getOffset() {
			return mOffset;
		}

		public int getSize() {
			return mSize;
		}
	};	
	
	public static int openPackage(String packagePath) {
		return jni_openPackage(packagePath);
	}
	
	public static void closePackage(int handle) {
		jni_closePackage(handle);
	}

	public static FilePosition getFilePosition(int handle, byte[] md5) {
		String tmp = jni_getIndexStr(handle, md5);
		if (null == tmp || tmp.equals(""))
			return null;

		String[] strList = tmp.split("\\|");
		if (strList.length != 2)
			return null;

		try {
			return new FilePosition(Integer.parseInt(strList[0]),
					Integer.parseInt(strList[1]));
		} catch (Exception e) {
			Log.e(TAG,
					String.format("getFilePosition Error!string:%s", tmp));
			return null;
		}

	}

	public static FilePosition getFilePosition(int handle, String key) {
		if (null == key || key.equals(""))
			return null;
		
		key = Uri.decode(key);

		return getFilePosition(handle, CryptoUtils.getMd5(key));
	}

	public final static String getMd5(String key) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] md = CryptoUtils.getMd5(key);
			char str[] = new char[md.length * 2];
			int k = 0;
			for (int i = 0; i < md.length; i++) {
				byte b = md[i];
				// 将每个(int)b进行双字节加密
				str[k++] = hexDigits[b >> 4 & 0xf]; // 取字节中高 4 位的数字转换,
													// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[b & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	public static StringBuffer readFile(int handle, String key) {
		if (StringUtils.isEmpty(key))
			return new StringBuffer();
		
		int fd = jni_fopen(handle, CryptoUtils.getMd5(key));
		if (fd == 0) return new StringBuffer();
		
		jni_fseek(fd, 0, SEEK_END);
		int file_size = jni_ftell(fd);
		if (file_size == 0) new StringBuilder();
		
		byte [] buffer = new byte[file_size];
		jni_fseek(fd, 0, SEEK_SET);
		jni_fread(fd, buffer, buffer.length);
		jni_fclose(fd);
		
		StringBuffer builder = new StringBuffer();
		try {
			builder.append(new String(buffer, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return builder;
	}
	
	public static boolean writeFile(int handle, String src, String dest) {
		if (StringUtils.isEmpty(src))
			return false;		
		File f = new File(dest);
		if(f.exists())
			return true;	
		if(!f.getParentFile().exists()) 
			f.getParentFile().mkdirs();
		
		int fd = jni_fopen(handle, CryptoUtils.getMd5(src));
		if (fd == 0) return false;
		
		byte [] buffer = new byte[64 * 1024];
		jni_fseek(fd, 0, SEEK_SET);
		
		boolean ret = true;
		FileOutputStream steam = null;
		try {
			steam = new FileOutputStream(dest);
			
			int n = 0;
			do {
				n = jni_fread(fd, buffer, buffer.length);
				steam.write(buffer, 0, n);
			}
			while (n > 0);
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		finally {
			FileUtils.closeCloseable(steam);
			jni_fclose(fd);
		}
		
		return ret;
	}
	
	public static byte [] readBuffer(int handle, String key) {
		if (null == key || key.equals(""))
			return null;
		
		int fd = jni_fopen(handle, CryptoUtils.getMd5(key));
		if (fd == 0) new StringBuilder();
		
		jni_fseek(fd, 0, SEEK_END);
		int file_size = jni_ftell(fd);
		if (file_size == 0) new StringBuilder();
		
		byte [] buffer = new byte[file_size];
		jni_fseek(fd, 0, SEEK_SET);
		jni_fread(fd, buffer, buffer.length);
		jni_fclose(fd);
		
		return buffer;
	}
	
	public static int checkSum(String data) {
		if (StringUtils.isEmpty(data))
			return 0;
		
		byte [] buffer = data.getBytes();
		return jni_checkSum(buffer);
	}

	private static native String 	jni_getIndexStr(int handle, byte[] md5);

	private static native int       jni_openPackage(String packagePath);

	private static native void 		jni_closePackage(int handle);

	private static native int 		jni_checkSum(byte[] data);
	
	public static final int  SEEK_CUR =   1;
	public static final int  SEEK_END =   2;
	public static final int  SEEK_SET =   0;
	
	public static native int jni_InitLauncher(String procName,String pkgName,String launchers);
	private static native int     jni_fopen(int handle, byte[] md5);
	private static native void    jni_fclose(int handle);
	private static native int     jni_fseek(int handle, int offset, int model);
	private static native int     jni_ftell(int handle);
	private static native int     jni_fread(int handle, byte[] data, int size);
	public  static native String  jni_CryptoRc4(String data, String pass);

	public  static native int     jni_getAudioLevel(byte[] data, int size);
	
	static {
		System.loadLibrary("PackageUtils");
	//	System.load("/system/lib/libPackageUtils.so");
	}
}
