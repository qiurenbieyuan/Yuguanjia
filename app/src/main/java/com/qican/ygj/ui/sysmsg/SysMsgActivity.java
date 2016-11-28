/**
 * 查看我的池塘
 */
package com.qican.ygj.ui.sysmsg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.qican.ygj.R;
import com.qican.ygj.bean.Msg;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.listener.BeanCallBack;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.ui.mypond.PondInfoActivity;
import com.qican.ygj.ui.scan.CaptureActivity;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.qican.ygj.utils.YGJDatas;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SysMsgActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SysMsgActivity";
    private LinearLayout llBack, llAddCamera;
    private CommonTools myTool;
    private ListView mListView;
    private List<Msg> mDatas;
    private MsgAdapter mAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sysmsg);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mDatas = YGJDatas.getMsgsDatas();
        mAdpater = new MsgAdapter(SysMsgActivity.this, mDatas, R.layout.item_msg);
        mListView.setAdapter(mAdpater);
    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llAddCamera = (LinearLayout) findViewById(R.id.ll_add);

        mListView = (ListView) findViewById(R.id.lv_msg);

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


    class MsgAdapter extends CommonAdapter<Msg> {

        public MsgAdapter(Context context, List<Msg> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final Msg item) {
            RelativeLayout rlItem = helper.getView(R.id.rl_item);

            helper.setText(R.id.tv_msg_time, item.getTime())
                    .setText(R.id.tv_msg, item.getMsg());

            if (item.getImgUrl() != null) {
                Log.i(TAG, "URL: " + item.getImgUrl());
                helper.setImageByUrl(R.id.iv_msg_img, item.getImgUrl());
            }
            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myTool.showInfo(item.getTime() + ":" + item.getMsg());
                }
            });
        }
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
