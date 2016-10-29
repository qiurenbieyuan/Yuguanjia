package com.qican.ygj.utils;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.webkit.JsResult;

public class AlertInstance {
	private static Builder mBuilder = null;
	private static Dialog mDialog = null;
	private static Context mContext = null;
	private static JsResult mResult = null;

	private AlertInstance() {

	}

	private static AlertInstance mInstance = null;

	public static AlertInstance getAlertInstance(Context cxt) {
		mContext = cxt;
		if (mInstance == null) {
			mInstance = new AlertInstance();
		}
		return mInstance;
	}

	public void showAlert(String title, String msg, final JsResult result,
			OnClickListener nListener) {
		if (mBuilder != null && mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
			mBuilder = null;
		}
		mResult = result;
		mBuilder = new Builder(mContext);

		mBuilder.setMessage(msg);
		if (StringUtils.isEqual("", title)) {
			mBuilder.setTitle("提示");
		}
		mBuilder.setTitle(title);

		mBuilder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mResult != null) {
					mResult.confirm();
					mResult = null;
				}
				dialog.dismiss();
			}
		});
		if (nListener != null)
			mBuilder.setNegativeButton("取消", nListener);
		mBuilder.setCancelable(false);
		mDialog = mBuilder.create();
		mDialog.show();
	}

	public void showAlert() {
		if (mBuilder != null && mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
			mBuilder = null;
		}
		mBuilder = new Builder(mContext);

		mBuilder.setMessage("获取应用列表中...");

		mBuilder.setTitle("请稍后");

		mBuilder.setCancelable(false);
		mDialog = mBuilder.create();

		mDialog.show();
	}

	public void dismissAlert() {
		if (mDialog != null && mDialog.isShowing()) {
			if (mResult != null)
				mResult.confirm();
			mResult = null;
			mDialog.dismiss();
		}
	}

}
