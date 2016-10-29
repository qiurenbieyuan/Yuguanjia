package com.qican.ygj.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


import android.util.Log;

public class ZipUtils {
	private static final String TAG = "unZipFile";

	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下. 默认接收字符缓冲区大小为 1024字节
	 */
	public int upZipFile(File zipFile, String folderPath) throws ZipException,
			IOException {
		return upZipFile(zipFile, folderPath, 1024);
	}

	/**
	 * 
	 * 解压缩功能. 将zipFile文件解压到targetFolderPath目录下.
	 * 
	 * @param zipFile
	 *            待解压文件
	 * @param targetFolderPath
	 *            目标目录
	 * @param bufLength
	 *            接收字符缓冲区大小
	 * @return 如果解压成功返回 0
	 * @throws ZipException
	 * @throws IOException
	 */
	public int upZipFile(File zipFile, String targetFolderPath, int bufLength)
			throws ZipException, IOException {

		ZipEntry ze = null;

		ZipFile zFile = new ZipFile(zipFile);
		Enumeration<? extends ZipEntry> zList = zFile.entries();

		while (zList.hasMoreElements()) {

			ze =  zList.nextElement();
			if (ze.isDirectory()) {
				// 创建目录
				createDirectory(targetFolderPath, ze.getName());
				continue;
			}
			// 解压文件
			unCompressFile(targetFolderPath, ze.getName(), ze, zFile, bufLength);
		}
		zFile.close();
		return 0;
	}

	private void createDirectory(String rootDir, String subDir)
			throws UnsupportedEncodingException {
		String absDirPath = rootDir + File.separator + subDir;
		absDirPath = new String(absDirPath.getBytes("ISO8859_1"), "GB2312");
		File directory = new File(absDirPath);
		directory.mkdir();
	}

	private void unCompressFile(String targetFolderPath, String relFileName,
			ZipEntry ze, ZipFile zFile, int bufLength) {

		OutputStream os = null;
		InputStream is = null;
		InputStream zis = null;
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(getRealFileName(targetFolderPath, relFileName));
			os = new BufferedOutputStream( fos );
			zis = zFile.getInputStream(ze);
			is = new BufferedInputStream(zis);

			byte[] buf = new byte[bufLength];
			int readLen = 0;
			while ((readLen = is.read(buf, 0, bufLength)) != -1) {
				os.write(buf, 0, readLen);
			}
			os.flush();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			FileUtils.closeCloseable(os);
			FileUtils.closeCloseable(fos);
			FileUtils.closeCloseable(is);
			FileUtils.closeCloseable(zis);
		}
		
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param targetFolderPath
	 *            指定目标目录
	 * @param relFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 * @throws UnsupportedEncodingException
	 */
	public static File getRealFileName(String targetFolderPath,
			String relFileName) throws UnsupportedEncodingException {
		String[] dirs = relFileName.split("/");
		File retDir = new File(targetFolderPath);
		String subDir = null;
		String fileName = null;

		if (dirs.length >= 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				subDir = dirs[i];
				subDir = new String(subDir.getBytes("ISO8859_1"), "GB2312");
				retDir = new File(retDir, subDir);
			}
			Log.d(TAG, "retDir = " + retDir);
			if (!retDir.exists())
				retDir.mkdirs();

			fileName = dirs[dirs.length - 1];
			fileName = new String(fileName.getBytes("ISO8859_1"), "GB2312");
			Log.d(TAG, "fileName = " + fileName);

			// 返回目录 retDir 下文件 fileName
			return new File(retDir, fileName);
		}
		return retDir;
	}
}
