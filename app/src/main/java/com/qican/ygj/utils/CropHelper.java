package com.qican.ygj.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

public class CropHelper {
    // SD卡不存在
    public static final String SDCARD_NOT_EXISTS = "SD卡不存在，无法拍照";

    // 设置头像
    public static final int HEAD_FROM_ALBUM = 2106;
    public static final int HEAD_FROM_CAMERA = 2107;
    public static final int HEAD_SAVE_PHOTO = 2108;

    // 头像设置提示
    public static final String HEAD_SET_CANCEL = "取消拍照";
    public static final String HEAD_UPLOAD_SUCCESS = "图片上传成功";
    public static final String HEAD_IMAGE_INVALID = "无效图片";
    public static final String UPLOAD_HEAD = "上传图片";

    private Activity mActivity = null;

    // 照片保存路径
    private String mTempPhotoPath;

    public CropHelper(Activity act, String photoPath) {
        mActivity = act;
        mTempPhotoPath = photoPath;
    }

    public Bitmap getBitmap(Intent data) {
        if (data == null) {
            return null;
        }
        return data.getParcelableExtra("data");
    }


    public void startCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        File temp = new File(mTempPhotoPath);
        if (temp.exists()) {
            temp.delete();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(temp));
        mActivity.startActivityForResult(intent, HEAD_FROM_CAMERA);
    }

    public void startAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        mActivity.startActivityForResult(intent, HEAD_FROM_ALBUM);
    }

    public void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 截图保存为临时文件
     *
     * @param data
     * @return
     */
    public boolean saveTempPhoto(Intent data) {
        if (data != null) {
            return BitmapUtils.saveFile(getBitmap(data), mTempPhotoPath);
        } else {
            showToast(HEAD_IMAGE_INVALID);
            return false;
        }
    }

    /**
     * 截图保存到指定目录
     *
     * @param data
     * @return
     */
    public boolean savePhoto(Intent data, String filePath) {
        if (data != null) {
            return BitmapUtils.saveFile(getBitmap(data), filePath);
        } else {
            showToast(HEAD_IMAGE_INVALID);
            return false;
        }
    }

    /**
     * 复制临时路径下的截图文件到指定路径
     *
     * @param filePath
     * @return
     */
    public boolean copyTempPhoto(String filePath) {
        return FileUtils.copyFile(mTempPhotoPath, filePath);
    }

    /**
     * 获取截图存放的临时路径
     *
     * @return
     */
    public String getTempPath() {
        return mTempPhotoPath;
    }
}
