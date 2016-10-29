package com.qican.ygj.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;



public class BitmapUtils {

    public static final int RAW_SIZE = 128;

    private static int sWidth = 128;
    private static int sHeight = 128;

    public static void setSize(int width, int height) {
        int size = width > height ? width : height;
        sWidth = size;
        sHeight = size;
    }

    public static void reset() {
        sWidth = RAW_SIZE;
        sHeight = RAW_SIZE;
    }

    public static Bitmap getBitmap(byte[] coverByte) {
        try {
            if (coverByte == null)
                return null;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(coverByte, 0, coverByte.length, opts);
            opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, sWidth
                    * sHeight);

            opts.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(coverByte, 0,
                    coverByte.length, opts);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Bitmap getBitmap(InputStream is) {
        try {
            if (is == null)
                return null;

            int size = is.available();
            byte[] bs = new byte[size];
            is.read(bs, 0, bs.length);

            return getBitmap(bs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Bitmap getBitmap(String path, PackageUtils.FilePosition pos) {
        if (StringUtils.isEmpty(path))
            return null;
        else if (pos == null)
            return null;

        byte[] buffer = FileUtils.getBuffer(path, pos.getOffset(),
                pos.getSize());

        return getBitmap(buffer);
    }

    public static Bitmap getBitmap(String path) {
        if (StringUtils.isEmpty(path))
            return null;

        byte[] buffer = FileUtils.getBuffer(path);

        return getBitmap(buffer);
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;

        }
    }

    /**
     * 默认以jpg格式保存图片到指定路径
     *
     * @param bm
     * @param path
     * @return
     */
    public static boolean saveFile(Bitmap bm, String path) {
        if (bm == null || path == null)
            return false;
        File myCaptureFile = new File(path);
        File parentDile = myCaptureFile.getParentFile();
        if (!parentDile.exists()) {
            parentDile.mkdirs();
        }
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 以指定格式保存图片到指定路径
     *
     * @param bm
     * @param path
     * @param format
     * @return
     */
    public static boolean saveFile(Bitmap bm, String path,
                                   Bitmap.CompressFormat format) {
        if (bm == null || path == null)
            return false;
        File myCaptureFile = new File(path);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(format, 80, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Bitmap转byte[]
    public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(format, 100, baos);
        return baos.toByteArray();
    }

    // byte[]转Bitmap
    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap toRoundBitmap(Bitmap bitmap, int round) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            if (round != 0)
                roundPx = round;
            else
                roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            if (round != 0)
                roundPx = round;
            else
                roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static Bitmap getBitmap(Bitmap bitMap, int w, int h) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = w;
        int newHeight = h;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

        return bitMap;
    }

}
