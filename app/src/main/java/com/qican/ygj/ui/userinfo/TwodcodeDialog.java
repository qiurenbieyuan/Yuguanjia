/**
 * 二维码显示框
 */
package com.qican.ygj.ui.userinfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;


public class TwodcodeDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private ImageView ivHeadImg;
    private CommonTools myTool;
    private TextView tvNickName;
    private LinearLayout llDialog;


    public TwodcodeDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public TwodcodeDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_twodcode);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        llDialog.setOnClickListener(this);
    }

    private void initData() {
        myTool.showImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=571129321,501172449&fm=21&gp=0.jpg", ivHeadImg);
        tvNickName.setText(myTool.getNickName());
    }

    private void initView() {
        ivHeadImg = (ImageView) findViewById(R.id.iv_headimg);
        tvNickName = (TextView) findViewById(R.id.tv_nickname);

        llDialog = (LinearLayout) findViewById(R.id.ll_dialog);

        myTool = new CommonTools(mContext);
    }


    @Override
    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.show();
            } catch (Exception e) {
            }
        }

    }

    @Override
    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void cancel() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            try {
                super.cancel();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_dialog:
                dismiss();
                break;
        }
    }
}
