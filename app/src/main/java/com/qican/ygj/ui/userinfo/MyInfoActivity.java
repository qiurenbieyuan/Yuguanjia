/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.userinfo;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qican.ygj.R;
import com.qican.ygj.listener.OnSexDialogListener;
import com.qican.ygj.utils.CommonTools;


public class MyInfoActivity extends Activity implements View.OnClickListener, OnSexDialogListener {
    private CommonTools myTool;
    private LinearLayout llBack;
    private RelativeLayout rlNickName, rlSignature, rlUserId, rlTwodcode, rlSex;
    private TextView tvNickName, tvSignature, tvSex;
    private TwodcodeDialog mTwodcodeDialog;
    private SexChooseDialog mSexDialog;
    private ImageView ivHeadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        tvNickName.setText(myTool.getNickName());
        tvSignature.setText(myTool.getSignature());
        tvSex.setText(myTool.getUserSex());
        myTool.showImage("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=571129321,501172449&fm=21&gp=0.jpg", ivHeadImg);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlSignature.setOnClickListener(this);
        rlUserId.setOnClickListener(this);
        rlTwodcode.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        mSexDialog.setOnSexDialogListener(this);
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
        rlSignature = (RelativeLayout) findViewById(R.id.rl_autograph);
        rlUserId = (RelativeLayout) findViewById(R.id.rl_userid);
        rlTwodcode = (RelativeLayout) findViewById(R.id.rl_twodcode);
        rlSex = (RelativeLayout) findViewById(R.id.rl_sex);

        tvNickName = (TextView) findViewById(R.id.tv_nickname);
        tvSignature = (TextView) findViewById(R.id.tv_signature);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        ivHeadImg = (ImageView) findViewById(R.id.iv_headimg);

        mTwodcodeDialog = new TwodcodeDialog(this, R.style.Translucent_NoTitle);
        mSexDialog = new SexChooseDialog(this, R.style.Translucent_NoTitle);

        myTool = new CommonTools(this);
    }

    /**
     * 点击事件
     *
     * @param v：所点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.rl_nickname:
                myTool.startActivity(NickNameActivity.class);
                break;
            case R.id.rl_autograph:
                myTool.startActivity(SignatureActivity.class);
                break;
            case R.id.rl_twodcode:
                mTwodcodeDialog.show();
                break;
            case R.id.rl_sex:
                mSexDialog.show();
                break;
        }
    }

    /**
     * 性别更新
     */
    @Override
    public void sexChangedSuccess() {
        tvSex.setText(myTool.getUserSex());
    }
}
