/**
 * 添加池塘
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.ui.scan.CaptureActivity;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.qican.ygj.utils.YGJDatas;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Response;

public class AddCameraActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AddCameraActivity";
    private LinearLayout llBack, llAddCamera;
    private ImageView ivScan;
    private CommonTools myTool;
    private EditText edtCameraName;
    private ListView mListView;
    private List<Pond> mDatas;
    private PondAdapter mAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcamera);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
//        mDatas = YGJDatas.getPondsDatas();
        mDatas = new ArrayList<>();

        String url = ConstantValue.SERVICE_ADDRESS + "findPondByUser";
        Log.i(TAG, "addPond: userId[" + myTool.getUserId() + "]");

        OkHttpUtils
                .post()
                .url(url)
                .addParams("userId", myTool.getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.i(TAG, "onResponse: " + response);

                        JSONArray jsonArray = JSONArray.fromObject(response);
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject ob = jsonArray.getJSONObject(i);
                            Pond pond = new Pond();

                            pond.setId(ob.getString("pondId"));
                            pond.setName(ob.getString("pondName"));
                            pond.setDesc(ob.getString("pondDescrible"));

                            mDatas.add(pond);
                        }
                        mAdpater = new PondAdapter(AddCameraActivity.this, mDatas, R.layout.item_choose_pond);
                        mListView.setAdapter(mAdpater);
                    }
                });
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llAddCamera = (LinearLayout) findViewById(R.id.ll_add);

        edtCameraName = (EditText) findViewById(R.id.edt_camera_name);
        ivScan = (ImageView) findViewById(R.id.iv_scan);
        mListView = (ListView) findViewById(R.id.lv_mypond);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llAddCamera.setOnClickListener(this);
        ivScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_add:
                //添加相机
                addCamera();
                break;
            case R.id.iv_scan:
                startActivity(new Intent(this, CaptureActivity.class));
                break;
        }
    }

    private void addCamera() {
        if (edtCameraName.getText().toString().trim().isEmpty()) {
            //震动View
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .withListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            edtCameraName.setError("还没有添加池塘名字哦！");
                        }
                    })
                    .playOn(edtCameraName);
            return;
        }

        //添加池塘信息,成功的提示对话框
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("添加成功!")
                .setContentText("你的鱼塘尽在掌握之中!")
                .setConfirmText("完  成")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        new CountDownTimer(500, 500) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                finish();
                            }
                        }.start();
                    }
                })
                .show();
    }

    class PondAdapter extends CommonAdapter<Pond> {

        public PondAdapter(Context context, List<Pond> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final Pond item) {
            RelativeLayout rlItem = helper.getView(R.id.rl_item);

            helper.setText(R.id.tv_pond_name, item.getName())
                    .setText(R.id.tvpond_desc, item.getDesc());
            if (item.getImgUrl() != null) {
                helper.setImageByUrl(R.id.iv_pond_img, item.getImgUrl());
            }

            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = ConstantValue.SERVICE_ADDRESS + "uploadPondHeadImage";
                    Log.i(TAG, "addPond: userId[" + myTool.getUserId() + "]");

                    myTool.showInfo("pondId[" + item.getId() + "]");

                    OkHttpUtils
                            .post()
                            .url(url)
                            .addParams("pondId", item.getId())
                            .addFile("mFile", "pond_" + item.getId() + ".png", myTool.getDefaultPondImg())
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
            });
        }
    }

}
