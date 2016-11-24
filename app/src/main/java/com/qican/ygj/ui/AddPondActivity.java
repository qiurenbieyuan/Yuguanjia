/**
 * 添加池塘
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;

public class AddPondActivity extends Activity implements View.OnClickListener {
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final String TAG = "AddPondActivity";
    private LinearLayout llBack, llAddPond;
    private ImageView ivAddimg, ivClose;
    private CommonTools myTool;
    private ArrayList<String> mSelectPath = null;
    private ImageView ivImgDesc;
    private EditText edtPondName, edtPondDesc;
    private String pondName, pondDesc;
    private SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpond);
        initView();
        initEvent();
        initImageSelCon();
    }

    private void initImageSelCon() {

    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llAddPond = (LinearLayout) findViewById(R.id.ll_add);

        ivAddimg = (ImageView) findViewById(R.id.iv_addimg);
        ivImgDesc = (ImageView) findViewById(R.id.iv_imgdesc);
        ivClose = (ImageView) findViewById(R.id.iv_close);

        edtPondName = (EditText) findViewById(R.id.edt_pond_name);
        edtPondDesc = (EditText) findViewById(R.id.edt_pond_desc);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llAddPond.setOnClickListener(this);

        ivAddimg.setOnClickListener(this);
        ivImgDesc.setOnClickListener(this);
        ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_addimg:
                // 跳转到图片选择器
                MultiImageSelector.create(this)
                        .showCamera(true) // show camera or not. true by default
                        .single() // single mode
                        .origin(mSelectPath) // original select data set, used width #.multi()
                        .start(this, IMAGE_REQUEST_CODE);
                break;
            case R.id.iv_imgdesc:
                Bundle bundle = new Bundle();
                bundle.putString("path", mSelectPath.get(0));
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_close:
                ivImgDesc.setVisibility(View.GONE);
                ivClose.setVisibility(View.GONE);
                ivAddimg.setVisibility(View.VISIBLE);
                mSelectPath.clear();//去除照片地址信息
                break;
            case R.id.ll_add:
                //添加池塘
                addPond();
                break;
        }
    }

    private void addPond() {
        //没有登录就不能添加
        if (!myTool.isLoginWithDialog()) {
            return;
        }

        pondName = edtPondName.getText().toString().trim();
        pondDesc = edtPondDesc.getText().toString();

        if (pondName.isEmpty()) {
            //震动View
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(edtPondName);
            return;
        }

        if (pondDesc.isEmpty()) {
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .playOn(edtPondDesc);
            return;
        }

        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在添加···");
        mDialog.show();

        File pondFile = null;
        if (mSelectPath == null) {
            pondFile = myTool.getDefaultPondImg();
        } else {
            if (mSelectPath.size() != 0) {
                pondFile = new File(mSelectPath.get(0));
            } else {
                pondFile = myTool.getDefaultPondImg();
            }
        }

        Log.i(TAG, "addPond: " + pondFile.toString());

        String url = ConstantValue.SERVICE_ADDRESS + "addPond";
        Log.i(TAG, "addPond: pondName[" + pondName + "],pondDesc[" + pondDesc + "],userId[" + myTool.getUserName() + "]");

        OkHttpUtils
                .post()
                .url(url)
                .addParams("userId", myTool.getUserName())
                .addParams("pondName", pondName)
                .addParams("pondDescrible", pondDesc)
//                .addFile("mFile", pondName + "封面.png", pondFile)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mDialog.setTitleText("添加失败")
                                .setContentText("添加失败，异常信息 e-->[" + e.toString() + "]")
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        switch (response) {
                            case "error":
                                //添加池塘信息,成功的提示对话框
                                mDialog.setTitleText("添加失败!")
                                        .setContentText("服务器出现了错误，请稍后再试!")
                                        .setConfirmText("好  的")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        }).changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                break;
                            case "success":
                                //添加池塘信息,成功的提示对话框
                                mDialog.setTitleText("添加成功!")
                                        .setContentText("你的鱼塘尽在掌握之中,记得及时回来打理唷! ")
                                        .setConfirmText("完  成")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                new CountDownTimer(1000, 1000) {
                                                    @Override
                                                    public void onTick(long millisUntilFinished) {
                                                    }

                                                    @Override
                                                    public void onFinish() {
                                                        finish();
                                                    }
                                                }.start();
                                            }
                                        }).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                break;
                        }
                        //        startActivity(new Intent(this, AddSuccessActivity.class));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                Log.i(TAG, "path: " + mSelectPath.toString());
                showImageByPath(mSelectPath);
            }
        }
    }

    /**
     * 显示池塘封面图片到图片预览上
     *
     * @param mSelectPath
     */
    private void showImageByPath(ArrayList<String> mSelectPath) {
        if (mSelectPath != null) {
            if (mSelectPath.size() != 0) {
                ivImgDesc.setVisibility(View.VISIBLE);
                ivAddimg.setVisibility(View.GONE);
                ivClose.setVisibility(View.VISIBLE);
                Glide.with(this).load(mSelectPath.get(0)).into(ivImgDesc);
            }
        }
    }
}
