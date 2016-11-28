/**
 * 查看我的池塘
 */
package com.qican.ygj.ui.mypond;

import android.app.Activity;
import android.app.Dialog;
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
import com.qican.ygj.bean.User;
import com.qican.ygj.listener.BeanCallBack;
import com.qican.ygj.listener.OnDialogListener;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.ui.scan.CaptureActivity;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.qican.ygj.utils.YGJDatas;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class MyPondActivity extends Activity implements View.OnClickListener, OnDialogListener {
    private static final String TAG = "MyPondActivity";
    private LinearLayout llBack, llAddCamera;
    private CommonTools myTool;
    private ListView mListView;
    private List<Pond> mDatas;
    private PondAdapter mAdpater;
    private HandlePondDialog handlePondDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypond);
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
                .execute(new BeanCallBack<List<com.qican.ygj.beanfromzhu.Pond>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {


                    }

                    @Override
                    public void onResponse(List<com.qican.ygj.beanfromzhu.Pond> pondList, int id) {
                        for (int i = 0; i < pondList.size(); i++) {
                            Pond pond = new Pond(pondList.get(i));
                            mDatas.add(pond);
                        }
                        mAdpater = new PondAdapter(MyPondActivity.this, mDatas, R.layout.item_choose_pond);
                        mListView.setAdapter(mAdpater);
                    }
                });
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llAddCamera = (LinearLayout) findViewById(R.id.ll_add);

        mListView = (ListView) findViewById(R.id.lv_mypond);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llAddCamera.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.ll_add:
                //添加相机
                break;
            case R.id.iv_scan:
                startActivity(new Intent(this, CaptureActivity.class));
                break;
        }
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
                Log.i(TAG, "URL: " + item.getImgUrl());
//                myTool.showImageByOkHttp(item.getImgUrl(), (ImageView) helper.getView(R.id.iv_pond_img));
                helper.setImageByUrl(R.id.iv_pond_img, item.getImgUrl());
            }

            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toPondInfoActivity(item);
                }
            });
            rlItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    toHandlePond(item);
                    return false;
                }
            });
        }
    }

    private void toHandlePond(Pond pond) {
        handlePondDialog = new HandlePondDialog(this, R.style.Translucent_NoTitle, pond);
        handlePondDialog.setOnDialogListener(this);
        handlePondDialog.show();
    }

    @Override
    public void dialogResult(Dialog dialog, String msg) {
        switch (msg) {
            case HandlePondDialog.DELETE:
                //删除池塘
                deletePond(((HandlePondDialog) dialog).getPond());
                break;
            case HandlePondDialog.TOP:
                break;
        }
    }

    private void deletePond(final Pond pond) {
        String url = ConstantValue.SERVICE_ADDRESS + "deletePond";
        // 更新用户信息到本地
        OkHttpUtils
                .post()
                .url(url)
                .addParams("pondId", pond.getId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        myTool.showInfo("删除失败，请稍后重试！异常：【" + e.toString() + "】");
                    }

                    @Override
                    public void onResponse(String result, int id) {
                        switch (result) {
                            case "success":
                                mDatas.remove(pond);
                                mAdpater.notifyDataSetChanged();
                                break;
                            case "error":
                                myTool.showInfo("删除失败，请稍后重试！");
                                break;
                        }
                    }
                });
    }

    /**
     * 启动详细页面
     *
     * @param pond
     */
    private void toPondInfoActivity(Pond pond) {
        myTool.startActivity(pond, PondInfoActivity.class);
    }
}
