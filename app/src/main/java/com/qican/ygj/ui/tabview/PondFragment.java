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

import com.qican.ygj.R;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.bean.PostResult;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.listener.OnTaskListener;
import com.qican.ygj.task.CommonTask;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.utils.CommonTools;
import com.qican.ygj.utils.ConstantValue;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


public class PondFragment extends Fragment implements OnTaskListener {

    private static final int TASK_LOAD_POND = 001;
    private CommonTools myTool;
    private static final String TAG = "PondFragment";
    private GridView mGridView;
    private PondAdapter mAdapter;
    private List<Pond> mData = null;
    private OnFramentListener mCallBack;
    private LoadPondTask mLoadPondTask;
    private ImageView ivProgress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pond, container, false);

        initView(view);
        initData();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (mCallBack == null) {
            mCallBack = (OnFramentListener) activity;
        }
    }

    private void initData() {
        mData = new ArrayList<>();

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
                    }
                });

        mLoadPondTask = new LoadPondTask(getActivity(), TASK_LOAD_POND);
        mLoadPondTask.setOnTaskFinishListener(this);
        mLoadPondTask.setIvProgress(ivProgress);

        Map<String, String> map = new HashMap<>();
        mLoadPondTask.execute(map);

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

    class LoadPondTask extends CommonTask<List<Pond>> {

        public LoadPondTask(Context context, int taskID) {
            super(context, taskID);
        }

        @Override
        public PostResult getResult(Map inputPara) {
            PostResult<List<Pond>> result = new PostResult<>();
            List<Pond> mData = new ArrayList<>();

            //模拟网络请求
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                result.setException(e);
                return result;
            }

            Pond pond1 = new Pond();
            pond1.setImgUrl("http://img5.imgtn.bdimg.com/it/u=1481365631,1136133954&fm=21&gp=0.jpg");
            pond1.setName("池塘1");
            mData.add(pond1);

            Pond pond2 = new Pond();
            pond2.setImgUrl("http://img4.imgtn.bdimg.com/it/u=3849379134,1749767660&fm=21&gp=0.jpg");
            pond2.setName("池塘2");
            mData.add(pond2);

            Pond pond3 = new Pond();
            pond3.setImgUrl("http://img5.imgtn.bdimg.com/it/u=1432240924,2436835975&fm=21&gp=0.jpg");
            pond3.setName("池塘3");
            mData.add(pond3);

            result.setResult(mData);

            return result;
        }
    }

    @Override
    public void taskFailed(CommonTask t, Exception e) {
        switch (t.getTaskID()) {
            case TASK_LOAD_POND:
                break;
        }
    }

    @Override
    public void taskSuccess(CommonTask t, Object result) {
        switch (t.getTaskID()) {
            case TASK_LOAD_POND:
                mData = (List<Pond>) result;

                mAdapter = new PondAdapter(getActivity(), mData, R.layout.item_pond);
                mGridView.setAdapter(mAdapter);
                break;
        }
    }
}
