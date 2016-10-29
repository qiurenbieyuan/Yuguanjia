package com.qican.ygj.utils;

import java.util.List;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfoUtils
{	
	//private static final String TAG = "AppStatus";
	public enum AppStatus {
		FOREGROUND,
		VISIBLE,
		SERVICE,
		BACKGROUND,
		EMPTY,
		UNKNOWN
	}
	
	public static void init()
	{
	}
	
	public static String getAppName()
	{
		Context context = NetworkUtils.getApplicationContext();
		if (null == context) return null;
		
		PackageInfo info = null;
		try
		{
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		}
		catch (NameNotFoundException e) 
		{
			e.printStackTrace();
		}  
		
		if (null == info) return null;
		return info.packageName;
	}
	
	public static String getAppVersionName()
	{
		PackageInfo info = null;
		Context context = NetworkUtils.getApplicationContext();
		if (context == null)
			return "";
		try
		{
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}  
		
		if (null == info) return "";		
		return info.versionName;
	}
	
	public static String getAppVersionName(String archiveFilePath)
	{
		Context context = NetworkUtils.getApplicationContext();
        PackageManager pm = context.getPackageManager();     
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES); 
        
        if(null == info) return null;
        return info.versionName;
    } 
	public static int getVersionCode() {
		int versionCode = 0;
		if (NetworkUtils.getApplicationContext() == null)
			return versionCode;
		
		PackageManager pm = NetworkUtils.getApplicationContext()
				.getPackageManager();
		try {
			PackageInfo pinfo = pm.getPackageInfo(NetworkUtils
					.getApplicationContext().getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	public static boolean isAppOnForeground() {
		Context context = NetworkUtils.getApplicationContext();
		if(context == null)
			return false;
		ActivityManager mActivityManager;
		String mPackageName;
		
		mActivityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		if(mActivityManager == null)
			return false;
		mPackageName = context.getPackageName();
		List<RunningAppProcessInfo> appProcesses = mActivityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(mPackageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}
	public static boolean isScreenLocked() {
		Context context = NetworkUtils.getApplicationContext();
		KeyguardManager mKeyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
			//Log.v(TAG, "锁屏");
			return true;
		}
		//Log.v(TAG, "唤醒");
		return false;

	}
}
