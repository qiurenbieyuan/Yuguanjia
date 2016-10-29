/**
 * 鱼管家应用程序入口
 *
 * @版权所有 重庆祺璨科技有限公司
 * @开发者 陈波
 * @邮箱 qiurenbieyuan@gmail.com
 * @时间 2016-10-24
 */
package com.qican.ygj;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.qican.ygj.utils.CommonTools;
import com.videogo.openapi.EZOpenSDK;

import java.io.File;

public class YGJApp extends Application {
    private CommonTools myTool;
    //萤石APPKEY
    public static final String EZ_appKey = "db6b68c3975b4e8f932e752753b08e47";
    //so库存放位置
    public static final String loadLibraryAbsPath = Environment.getExternalStorageDirectory() + "/"
            + "YGJ/libs/";

    @Override
    public void onCreate() {
        super.onCreate();
        myTool = new CommonTools(this);
        //初始化ImageLoader
        initUIL();
        //初始化萤石云SDK
        initEZSDK();
    }

    //放到线程中优化启动
    private void initEZSDK() {
        File dirFile = new File(loadLibraryAbsPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        boolean success = EZOpenSDK.initLib(YGJApp.this, EZ_appKey, loadLibraryAbsPath);
        if (!success) {
            myTool.showInfo("EZOpenSDK初始化失败,请联系开发者,qq:1061315674！");
        }
    }

    private void initUIL() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "YGJ/cache/image");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheSize(50 * 1024 * 1024)
                .imageDownloader(new BaseImageDownloader(this, 20 * 60 * 60, 30 * 60 * 60))
                .diskCacheFileCount(100)
                .denyCacheImageMultipleSizesInMemory()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
