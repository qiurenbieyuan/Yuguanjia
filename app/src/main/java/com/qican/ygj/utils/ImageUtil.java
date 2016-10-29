package com.qican.ygj.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {
	public static void saveFile(Bitmap bm, String path,
			Bitmap.CompressFormat format) {
		if (bm == null || path == null)
			return;
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Bitmap getLocalBitmap(String path) {
		File f = new File(path);
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			is = new FileInputStream(f);
			bitmap = BitmapFactory.decodeStream(is).copy(
					Bitmap.Config.ARGB_8888, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return bitmap;
		}
		return bitmap;
	}

	public static boolean deleteImage(String path) {
		File f = new File(path);
		if (f.exists())
			return f.delete();
		return false;
	}

}
