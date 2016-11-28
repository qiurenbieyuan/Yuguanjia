/**
 * 头像选择
 */
package com.qican.ygj.ui.userinfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qican.ygj.R;
import com.qican.ygj.listener.OnDialogListener;
import com.qican.ygj.utils.CommonTools;


public class PicChooseDialog extends Dialog implements View.OnClickListener {

    public static final String FROM_FILE = "from_file";
    public static final String FORM_CAMERA = "from_camera";
    private Context mContext;
    private CommonTools myTool;
    private RelativeLayout rlFile, rlCamera;
    private OnDialogListener l;
    private ImageView ivCancel;

    public PicChooseDialog(Context context) {
        this(context, R.style.Translucent_NoTitle);
    }

    public PicChooseDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_headchoose);
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        rlCamera.setOnClickListener(this);
        rlFile.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
    }

    private void initData() {

    }


    private void initView() {
        rlCamera = (RelativeLayout) findViewById(R.id.rl_camera);
        rlFile = (RelativeLayout) findViewById(R.id.rl_file);

        ivCancel = (ImageView) findViewById(R.id.iv_cancel);

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
            case R.id.rl_file:
                if (l != null) {
                    l.dialogResult(this, FROM_FILE);
                }
                dismiss();
                break;
            case R.id.rl_camera:
                if (l != null) {
                    l.dialogResult(this, FORM_CAMERA);
                }
                dismiss();
                break;
            case R.id.iv_cancel:
                dismiss();
                break;
        }
    }


    public void setOnDialogListener(OnDialogListener l) {
        this.l = l;
    }
}
