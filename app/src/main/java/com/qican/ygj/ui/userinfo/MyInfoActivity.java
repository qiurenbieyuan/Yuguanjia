/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.userinfo;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.qican.ygj.R;
import com.qican.ygj.listener.OnDialogListener;
import com.qican.ygj.listener.OnSexDialogListener;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.qican.ygj.utils.YGJDatas;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.sf.json.JSONObject;

import java.io.File;

import okhttp3.Call;


public class MyInfoActivity extends TakePhotoActivity implements View.OnClickListener, OnSexDialogListener, OnDialogListener {
    private static final String TAG = "MyInfoActivity";
    private CommonTools myTool;
    private LinearLayout llBack;
    private RelativeLayout rlNickName, rlSignature, rlUserId, rlTwodcode, rlSex, rlChooseHeadImg;
    private TextView tvNickName, tvSignature, tvSex;
    private TwodcodeDialog mTwodcodeDialog;
    private SexChooseDialog mSexDialog;
    private PicChooseDialog mHeadDialog;
    private ImageView ivHeadImg;
    private TakePhoto takePhoto;
    private ProgressBar pbUploadHead;
    CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(200 * 1024).setMaxPixel(800).create();
    CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();

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
        if (!"".equals(myTool.getUserHeadURL())) {
            myTool.showImage(myTool.getUserHeadURL(), ivHeadImg, R.drawable.defaultheadpic);
        }
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlSignature.setOnClickListener(this);
        rlUserId.setOnClickListener(this);
        rlTwodcode.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        mSexDialog.setOnSexDialogListener(this);
        mHeadDialog.setOnDialogListener(this);
        rlChooseHeadImg.setOnClickListener(this);
        ivHeadImg.setOnClickListener(this);
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        rlNickName = (RelativeLayout) findViewById(R.id.rl_nickname);
        rlSignature = (RelativeLayout) findViewById(R.id.rl_autograph);
        rlUserId = (RelativeLayout) findViewById(R.id.rl_userid);
        rlTwodcode = (RelativeLayout) findViewById(R.id.rl_twodcode);
        rlSex = (RelativeLayout) findViewById(R.id.rl_sex);
        rlChooseHeadImg = (RelativeLayout) findViewById(R.id.rl_choose_headpic);

        tvNickName = (TextView) findViewById(R.id.tv_nickname);
        tvSignature = (TextView) findViewById(R.id.tv_signature);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        ivHeadImg = (ImageView) findViewById(R.id.iv_headimg);
        pbUploadHead = (ProgressBar) findViewById(R.id.pb_uploadhead);

        mTwodcodeDialog = new TwodcodeDialog(this, R.style.Translucent_NoTitle);
        mSexDialog = new SexChooseDialog(this, R.style.Translucent_NoTitle);
        mHeadDialog = new PicChooseDialog(this, R.style.Translucent_NoTitle);

        takePhoto = getTakePhoto();

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
            case R.id.rl_choose_headpic:
                mHeadDialog.show();
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
            case R.id.iv_headimg:
                myTool.startActivity(HeadInfoActivity.class);
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

    @Override
    public void takeSuccess(final TResult result) {
        super.takeSuccess(result);
        //上传头像至服务器
        File userHeadImgFile = new File(result.getImage().getPath());
        pbUploadHead.setVisibility(View.VISIBLE);

        String url = ConstantValue.SERVICE_ADDRESS + "userHeadImage";
        //上传池塘头像
        OkHttpUtils
                .post()
                .url(url)
                .addParams("userId", myTool.getUserId())
                .addFile("mFile", myTool.getUserId() + "_头像.png", userHeadImgFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pbUploadHead.setVisibility(View.GONE);
                        myTool.showInfo("上传失败，请稍后再试！");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        pbUploadHead.setVisibility(View.GONE);
                        Log.i(TAG, "onResponse: " + response);

                        JSONObject obj = JSONObject.fromObject(response);
                        String msg = obj.getString("message");

                        switch (msg) {
                            case "success":
                                Bitmap bitmap = BitmapFactory.decodeFile(result.getImage().getPath());
                                ivHeadImg.setImageBitmap(bitmap);
                                myTool.showInfo("上传成功！");
                                // 上传成功后，更新用户信息到本地
                                YGJDatas.updateInfoFromService(MyInfoActivity.this);
                                break;
                            case "error":
                                myTool.showInfo("上传失败，请稍后再试！");
                                break;
                            case "overSize":
                                myTool.showInfo("上传失败，图像超过最大限制了！");
                                break;
                        }
                    }
                });
        Log.i(TAG, "takeSuccess: " + result.getImage().getPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        myTool.showInfo("选择失败:" + msg);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        myTool.showInfo("取消选择");
    }

    @Override
    public void dialogResult(Dialog dialog, String msg) {
        switch (msg) {
            case PicChooseDialog.FORM_CAMERA:
                takePhoto.onEnableCompress(compressConfig, false);
                takePhoto.onPickFromCaptureWithCrop(myTool.getUserHeadFileUri(), cropOptions);
                break;
            case PicChooseDialog.FROM_FILE:
                takePhoto.onEnableCompress(compressConfig, false);
                takePhoto.onPickFromDocumentsWithCrop(myTool.getUserHeadFileUri(), cropOptions);
                break;
        }
    }
}
