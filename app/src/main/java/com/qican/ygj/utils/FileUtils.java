package com.qican.ygj.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.util.Log;

public class FileUtils {
    public static final String UTF_8 = "UTF-8";

    public static final String TAG = FileUtils.class.getSimpleName();

    private static final int MAX_BUFFER_LENGTH = 4096;

    public static StringBuffer readText(String filePath, String decoder) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead())
                return null;

            return readText(filePath, decoder, 0, (int) file.length());

        } catch (Exception e) {
            Log.e(TAG,
                    String.format("readText Error! message:%s", e.getMessage()));
            return null;
        }
    }

    /**
     * 根据编码读取文本
     *
     * @param filePath 文件路径
     * @param decoder  字符集名称 例：GBK UTF-8
     * @param offset   偏移量
     * @param length   长度
     * @return 读取的文本
     */
    public static StringBuffer readText(String filePath, String decoder,
                                        int offset, int length) {
        FileInputStream fileInputStream = null;
        BufferedInputStream buffReader = null;

        try {
            fileInputStream = new FileInputStream(filePath);
            buffReader = new BufferedInputStream(fileInputStream);

            StringBuffer buffer = new StringBuffer();

            byte[] bytesBuf = new byte[length];
            buffReader.skip(offset);
            buffReader.read(bytesBuf, 0, length);

            return buffer.append(new String(bytesBuf, decoder));
        } catch (Exception e) {
            Log.e(TAG,
                    String.format("readText Error!\te.getMessage:%s",
                            e.getMessage()));
        } finally {
            closeCloseable(fileInputStream);
            closeCloseable(buffReader);
        }

        return null;
    }

    public static byte[] getBuffer(String path, int off, int length) {
        byte[] cover = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(path);
            cover = new byte[length];
            fis.skip(off);
            fis.read(cover, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeCloseable(fis);
        }

        return cover;
    }

    public static byte[] getBuffer(String path) {
        File file = null;
        FileInputStream fis = null;
        byte[] cover = null;
        try {
            file = new File(path);
            int length = (int) file.length();
            fis = new FileInputStream(file);
            cover = new byte[length];
            fis.read(cover, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeCloseable(fis);
        }
        return cover;
    }

    /**
     * 关闭stream or reader
     *
     * @param closeObj
     */
    public static void closeCloseable(Closeable closeObj) {
        try {
            if (null != closeObj)
                closeObj.close();
        } catch (IOException e) {
            Log.e("ReadFileUtils Error",
                    "Method:readFile, Action:closeReader\t" + e.getMessage());
        }
    }

    public static String getContent(AssetManager assets, String fileName) {
        String ret = "";
        InputStream stream = null;
        try {
            stream = assets.open(fileName);
            ret = getContent(stream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭文件
            closeCloseable(stream);
        }
        return ret;
    }

    // 从流中, 获取数据
    private static String getContent(InputStream stream) {
        String ret = "";
        try {
            int len = stream.available();
            byte[] buffer = new byte[len];
            stream.read(buffer);

            ret = new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * 以 <b> UTF-8 </b>格式从文件开始处写入字符串,如果文件存在，则会被重写
     *
     * @param path    文件路径
     * @param content 待写入的字符串
     * @return 成功时返回true，失败返回false
     */
    public static boolean writeString(String path, String content) {
        String encoding = "UTF-8";
        File file = new File(path);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        return writeString(path, content, encoding);
    }

    /**
     * 从文件开始处写入字符串,如果文件存在，则会被重写
     *
     * @param path     文件路径
     * @param content  待写入的字符串
     * @param encoding String转换为byte[]编码
     * @return 成功时返回true，失败返回false
     */
    public static boolean writeString(String path, String content,
                                      String encoding) {
        FileOutputStream fos = null;
        boolean result = false;
        try {
            fos = new FileOutputStream(path);
            byte[] cover = content.getBytes(encoding);
            fos.write(cover, 0, cover.length);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FileUtils.closeCloseable(fos);
        }
        return result;
    }

    /**
     * 创建目标文件写入字节数组
     *
     * @param fileData 文件字节数组
     * @return 写入成功，返回true，否则返回false
     */
    public static boolean writeBytes(String targetFilePath, byte[] fileData) {
        boolean result = false;
        File targetFile = new File(targetFilePath);
        File parentFile = targetFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            targetFile.getParentFile().mkdirs();
        }
        if (targetFile.exists()) {
            targetFile.delete();
        }
        ByteArrayInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            fosfrom = new ByteArrayInputStream(fileData);
            fosto = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = fosfrom.read(buffer)) != -1) {
                fosto.write(buffer, 0, length);
            }
            fosto.flush();
            result = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeCloseable(fosto);
            closeCloseable(fosfrom);
        }
        return result;
    }

    /**
     * 复制文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return
     */
    public static boolean copyFile(String oldPath, String newPath) {
        boolean result = false;
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
            return result;
        }

        File parentFile = newFile.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            newFile.getParentFile().mkdirs();
        }
        if (newFile.exists()) {
            newFile.delete();
        }
        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            fosfrom = new FileInputStream(oldFile);
            fosto = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = fosfrom.read(buffer)) != -1) {
                fosto.write(buffer, 0, length);
            }
            fosto.flush();
            result = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeCloseable(fosto);
            closeCloseable(fosfrom);
        }
        return result;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteAllFiles(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteAllFiles(f);
            }
        }
    }
}
