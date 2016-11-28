/**
 * 头像选择
 */
package com.qican.ygj.ui.mypond;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.listener.OnDialogListener;
import com.qican.ygj.utils.CommonTools;


public class HandlePondDialog extends Dialog implements View.OnClickListener {

    public static final String DELETE = "DELETE";
    public static final String TOP = "TOP";
    private Context mContext;
    private CommonTools myTool;
    private RelativeLayout rlFile, rlCamera;
    private OnDialogListener l;
    private ImageView ivCancel;
    private Pond pond;

    public HandlePondDialog(Context context) {
        this(context, R.style.Translucent_NoTitle);
    }

    public HandlePondDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public HandlePondDialog(Context context, int theme, Pond pond) {
        this(context, theme);
        this.mContext = context;
        this.pond = pond;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_handlepond);
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
                    l.dialogResult(this, TOP);
                }
                dismiss();
                break;
            case R.id.rl_camera:
                if (l != null) {
                    l.dialogResult(this, DELETE);
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

    public Pond getPond() {
        return pond;
    }

    public void setPond(Pond pond) {
        this.pond = pond;
    }
}
