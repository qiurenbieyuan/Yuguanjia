package com.qican.ygj.ui.tabview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.bean.PostResult;
import com.qican.ygj.listener.BeanCallBack;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.listener.OnTaskListener;
import com.qican.ygj.task.CommonTask;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


public class PondFragment extends Fragment {

    private static final int TASK_LOAD_POND = 001;
    private CommonTools myTool;
    private static final String TAG = "PondFragment";
    private GridView mGridView;
    private PondAdapter mAdapter;
    private List<Pond> mData = null;
    private OnFramentListener mCallBack;
    private ImageView ivProgress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pond, container, false);

        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mCallBack == null) {
            mCallBack = (OnFramentListener) activity;
        }
    }

    private void initData() {
        mData = myTool.getPondList();
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mAdapter = new PondAdapter(getActivity(), mData, R.layout.item_pond);
        mGridView.setAdapter(mAdapter);
        loadPonds();
    }

    private void loadPonds() {
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
                    public void onResponse(List<com.qican.ygj.beanfromzhu.Pond> ponds, int id) {
                        for (int i = 0; i < ponds.size(); i++) {
                            Pond pond = new Pond(ponds.get(i));
                            mData.add(pond);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.gridView);
        ivProgress = (ImageView) v.findViewById(R.id.iv_progress);

        myTool = new CommonTools(getActivity());
    }

    /**
     * 池塘数据适配器
     */
    class PondAdapter extends CommonAdapter<Pond> {
        public PondAdapter(Context context, List<Pond> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final Pond item) {

            RelativeLayout rlItem = helper.getView(R.id.rl_item);

            helper.setText(R.id.tv_pond_name, item.getName());
            helper.setImageByUrl(R.id.iv_pond_img, item.getImgUrl());

            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onMessage(PondFragment.this, item);
                    }
                }
            });
        }
    }
}
