package com.qican.ygj.utils;

import java.io.File;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.os.Vibrator;
import android.telephony.TelephonyManager;

public final class OSUtils {
    public enum NetWorkState {
        NoState, MobileState, WifiState,
    }

    ;

    private static int sScreenWidth = 480;
    private static int sScreenHeight = 800;
    // private static int sMaxWidth = 480 * 2;
    // private static int sMaxHeight = 800 * 2;
    private static String sIMEIId = "";
    private static String sMACId = "";
    private static String sIMSIId = "";
    private static String sPhoneNumber = "";
    private static String sSoftwareVersion = "";

    public static int getScreenWidth() {
        return sScreenWidth;
    }

    public static int getScreenHeight() {
        return sScreenHeight;
    }

    public static int getScenceWidth() {
        return sScreenWidth;
    }

    public static int getScenceHeight() {
        return sScreenHeight;
    }

    // <uses-permission
    // android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
    // <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    public static void InitOs(Activity a) {
        if (!StringUtils.isEmpty(sIMEIId))
            return;

        try {
            sScreenWidth = a.getWindowManager().getDefaultDisplay().getWidth();
            sScreenHeight = a.getWindowManager().getDefaultDisplay()
                    .getHeight();
            WifiManager wifi = (WifiManager) a
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            TelephonyManager tm = (TelephonyManager) a
                    .getSystemService(Activity.TELEPHONY_SERVICE);
            sMACId = info.getMacAddress();
            sIMEIId = tm.getDeviceId();
            // sPhoneNumber = tm.getLine1Number();
            sSoftwareVersion = tm.getDeviceSoftwareVersion();
            sIMSIId = tm.getSubscriberId();
            /*
			 * 当前使用的网络类型： 例如： NETWORK_TYPE_UNKNOWN 网络类型未知 0 NETWORK_TYPE_GPRS
			 * GPRS网络 1 NETWORK_TYPE_EDGE EDGE网络 2 NETWORK_TYPE_UMTS UMTS网络 3
			 * NETWORK_TYPE_HSDPA HSDPA网络 8 NETWORK_TYPE_HSUPA HSUPA网络 9
			 * NETWORK_TYPE_HSPA HSPA网络 10 NETWORK_TYPE_CDMA CDMA网络,IS95A 或
			 * IS95B. 4 NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5
			 * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6 NETWORK_TYPE_1xRTT
			 * 1xRTT网络 7
			 */
            tm.getNetworkType();
			/*
			 * 手机类型： 例如： PHONE_TYPE_NONE 无信号 PHONE_TYPE_GSM GSM信号
			 * PHONE_TYPE_CDMA CDMA信号
			 */
            tm.getPhoneType();
			/*
			 * Returns the MCC+MNC (mobile country code + mobile network code)
			 * of the provider of the SIM. 5 or 6 decimal digits.
			 * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字. SIM卡的状态必须是
			 * SIM_STATE_READY(使用getSimState()判断).
			 */
            tm.getSimOperator();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getIMEIId() {
        if (sIMEIId == null)
            return "";
        return sIMEIId;
    }

    public static String getMACId() {
        if (sMACId == null)
            return "";
        return sMACId;
    }

    public static String getIMSIId() {
        if (sIMSIId == null)
            return "";
        return sIMSIId;
    }

    public static String getPhoneNumber() {
        if (sPhoneNumber == null)
            return "";
        return sPhoneNumber;
    }

    public static String getSoftwareVersion() {
        return sSoftwareVersion;
    }

    public static boolean IsSdCardMounted() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getSdCardDirectory() {
        File path = Environment.getExternalStorageDirectory();
        return path.getPath();
    }

    public static StatFs getSdCardStatFs() {
        File path = Environment.getExternalStorageDirectory();
        return new StatFs(path.getPath());
    }

    public static boolean ExistSDCard() {
        boolean flag = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 手机震动提示
     */
    public static void mobileShake(Context context, int ms) {
        Object obj = context.getSystemService(Service.VIBRATOR_SERVICE);
        if (obj instanceof Vibrator) {
            ((Vibrator) obj).vibrate(ms);
        }
    }

}
