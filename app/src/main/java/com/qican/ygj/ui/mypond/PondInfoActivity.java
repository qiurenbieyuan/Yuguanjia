package com.qican.ygj.ui.mypond;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;


public class PondInfoActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "PondInfoActivity";
    private static final int REQUEST_POND_NAME = 1;//修改池塘名
    private static final int REQUEST_POND_DESC = 2;//修改池塘描述
    private static final int IMAGE_REQUEST_CODE = 3;//修改池塘封面

    private CommonTools myTool;
    private TextView tvPondInfo, tvPondName, tvPondDesc;
    private RelativeLayout rlPondInfo, rlBack, rlPondName, rlPondDesc, rlModifyCover;
    private ImageView ivPreview;
    private Pond mPond;
    private ArrayList<String> mSelectPath = null;
    private File pondFile;
    private ImageView ivPondImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pondinfo);

        initView();
        initData();
        initEvent();
    }

    private void initData() {
        rlBack.setVisibility(View.GONE);
        //获得上一页传过来的参数
        mPond = new Pond();
        mPond = (Pond) myTool.getParam(mPond);

        tvPondInfo.setText(mPond.getName() + "：" + mPond.getDesc());
        tvPondName.setText(mPond.getName());
        tvPondDesc.setText(mPond.getDesc());

        myTool.showImage(mPond.getImgUrl(), ivPondImg);
    }

    private void initView() {
        rlBack = (RelativeLayout) findViewById(R.id.rl_back);

        ivPreview = (ImageView) findViewById(R.id.iv_video_preview);
        tvPondInfo = (TextView) findViewById(R.id.tv_pond_info);
        tvPondName = (TextView) findViewById(R.id.tv_pond_name);
        tvPondDesc = (TextView) findViewById(R.id.tv_pond_desc);

        rlPondInfo = (RelativeLayout) findViewById(R.id.rl_pond_info);
        rlPondName = (RelativeLayout) findViewById(R.id.rl_pond_name);
        rlPondDesc = (RelativeLayout) findViewById(R.id.rl_pond_desc);
        rlModifyCover = (RelativeLayout) findViewById(R.id.rl_modify_cover);

        ivPondImg = (ImageView) findViewById(R.id.iv_pond_img);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        rlPondInfo.setOnClickListener(this);
        rlBack.setOnClickListener(this);
        rlPondName.setOnClickListener(this);
        rlPondDesc.setOnClickListener(this);
        rlModifyCover.setOnClickListener(this);
    }


    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.rl_pond_info:
                showBackMenu();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_pond_name:
                myTool.startActivityForResult(mPond, PondNameActivity.class, REQUEST_POND_NAME);
                break;
            case R.id.rl_pond_desc:
                myTool.startActivityForResult(mPond, PondDescActivity.class, REQUEST_POND_DESC);
                break;
            case R.id.rl_modify_cover:
                // 跳转到图片选择器
                MultiImageSelector.create(this)
                        .showCamera(true) // show camera or not. true by default
                        .single() // single mode
                        .origin(mSelectPath) // original select data set, used width #.multi()
                        .start(this, IMAGE_REQUEST_CODE);
                break;
        }
    }

    //显示退出菜单
    private void showBackMenu() {

        if (rlBack.getVisibility() == View.VISIBLE) {
            //重新开始计时
            hideBackTimer.cancel();
            hideBackTimer.start();
            return;
        }

        rlBack.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(rlBack);
        hideBackTimer.start();
    }

    private CountDownTimer hideBackTimer = new CountDownTimer(3000, 3000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            YoYo.with(Techniques.FadeOut)
                    .duration(1000)
                    .withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            rlBack.setVisibility(View.GONE);
                        }
                    })
                    .playOn(rlBack);
        }
    };

    private void uploadPondCover() {

        if (mSelectPath == null) {
            myTool.showInfo("mSelectPath为空！");
            return;
        }

        if (mSelectPath.size() == 0) {
            myTool.showInfo("图片选择失败，请重新选取！");
            return;
        }
        pondFile = new File(mSelectPath.get(0));

        String url = ConstantValue.SERVICE_ADDRESS + "uploadPondHeadImage";
        Log.i(TAG, "addPond: userId[" + myTool.getUserId() + "]");

        myTool.showInfo("pondId[" + mPond.getId() + "],url:[" + mPond.getImgUrl() + "]");

        //上传池塘头像
        OkHttpUtils
                .post()
                .url(url)
                .addParams("pondId", mPond.getId())
                .addFile("mFile", "pond_" + mPond.getId() + ".png", pondFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.showInfo("response[" + e.toString() + "]");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        myTool.showInfo("response[" + response + "]");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POND_NAME && resultCode == RESULT_OK) {
            String pondName = data.getExtras().getString(PondNameActivity.KEY_PONDNAME);
            tvPondName.setText(pondName);
            tvPondInfo.setText(pondName + "：" + mPond.getDesc());
        }
        if (requestCode == REQUEST_POND_DESC && resultCode == RESULT_OK) {
            String pondDesc = data.getExtras().getString(PondDescActivity.KEY_POND_DESC);
            tvPondDesc.setText(pondDesc);
            tvPondInfo.setText(mPond.getName() + "：" + pondDesc);
        }
        // 图片选择结果回调
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the result list of select image paths
            mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
            // do your logic ....
            Log.i(TAG, "path: " + mSelectPath.toString());
            uploadPondCover();
        }
    }
}
