package com.qican.ygj.utils;


import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import android.content.res.AssetManager;


public class ReadFileUtils {
    public static final String UTF_8 = "UTF-8";

    public static final String TAG = ReadFileUtils.class.getSimpleName();

    private static final int MAX_BUFFER_LENGTH = 4096;

    public static StringBuilder readText(String filePath, String decoder) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) return null;

            return readText(filePath, decoder, 0, (int) file.length());

        } catch (Exception e) {
            Logging.e(TAG, String.format("readText Error! message:%s", e.getMessage()));
            return null;
        }
    }

    /**
     * 根据编码读取文本
     *
     * @param filePath 文件路径
     * @param decoder  字符集名称  例：GBK  UTF-8
     * @param offset   偏移量
     * @param length   长度
     * @return 读取的文本
     */
    public static StringBuilder readText(String filePath, String decoder, int offset, int length) {
        FileInputStream fileInputStream = null;
        BufferedInputStream buffReader = null;

        try {
            fileInputStream = new FileInputStream(filePath);
            buffReader = new BufferedInputStream(fileInputStream);

            StringBuilder sBuilder = new StringBuilder();

            byte[] bytesBuf = new byte[length];
            buffReader.skip(offset);
            buffReader.read(bytesBuf, 0, length);
            return sBuilder.append(new String(bytesBuf, decoder));
        } catch (Exception e) {
            Logging.e(TAG, String.format("readText Error!\te.getMessage:%s", e.getMessage()));
        } finally {
            closeCloseable(fileInputStream);
            closeCloseable(buffReader);
        }

        return null;
    }

    @SuppressWarnings("resource")
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
            ReadFileUtils.closeCloseable(fis);
        }

        return cover;
    }

    @SuppressWarnings("resource")
    public static byte[] getBuffer(String path) {
        File file = new File(path);
        FileInputStream fis = null;
        byte[] cover = null;
        try {
            int length = (int) file.length();
            fis = new FileInputStream(file);
            cover = new byte[length];
            fis.read(cover, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
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
            if (null != closeObj) closeObj.close();
        } catch (IOException e) {
            Logging.e("PackageUtils Error", "Method:readFile, Action:closeReader\t" + e.getMessage());
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
        }

        // 关闭文件
        closeCloseable(stream);

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
     * 从包中读取json字符串
     *
     * @param handler     指针
     * @param packagePath 包名即文件夹路径
     * @param fileName    文件名
     * @return
     */
    public static StringBuilder readFileFromPck(int handler, String packagePath, String fileName) {
        if (handler == 0) return null;

        byte[] md5 = CryptoUtils.getMd5(fileName);
        PackageUtils.FilePosition pos = PackageUtils.getFilePosition(handler, md5);
        if (pos == null) return null;

        return readText(packagePath, "UTF-8", pos.getOffset(), pos.getSize());
    }
}
