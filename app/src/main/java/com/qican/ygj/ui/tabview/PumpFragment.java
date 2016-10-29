package com.qican.ygj.ui.tabview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.qican.ygj.R;
import com.qican.ygj.bean.Camera;
import com.qican.ygj.bean.PostResult;
import com.qican.ygj.bean.Pump;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.listener.OnTaskListener;
import com.qican.ygj.task.CommonTask;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.utils.CommonTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PumpFragment extends Fragment implements OnTaskListener {

    private static final String TAG = "PumpFragment";
    private static final int TASK_LOAD_PUMP = 001;
    private CommonTools myTool;
    private List<Pump> mData;
    private PumpAdapter mAdapter;
    private GridView mGridView;
    //通知主Activity的回调接口
    private OnFramentListener mCallBack;
    private LoadPumpTask mLoadPumpTask;
    private ImageView ivProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pump, container, false);

        initView(view);

        return view;
    }


    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.gridView);
        ivProgress = (ImageView) v.findViewById(R.id.iv_progress);

        myTool = new CommonTools(getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity != null) {
            mCallBack = (OnFramentListener) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 通过镜头加载增氧泵信息
     *
     * @param camera
     */
    public void loadPumpByCamera(Camera camera) {
        mLoadPumpTask = new LoadPumpTask(getActivity(), TASK_LOAD_PUMP);
        mLoadPumpTask.setOnTaskFinishListener(this);
        mLoadPumpTask.setIvProgress(ivProgress);

        Map<String, String> map = new HashMap<>();
        mLoadPumpTask.execute(map);
    }

    @Override
    public void taskFailed(CommonTask t, Exception e) {
        switch (t.getTaskID()) {
            case TASK_LOAD_PUMP:
                break;
        }
    }

    @Override
    public void taskSuccess(CommonTask t, Object result) {
        switch (t.getTaskID()) {
            case TASK_LOAD_PUMP:
                mData= (List<Pump>) result;

                mAdapter = new PumpAdapter(getActivity(), mData, R.layout.item_pump);
                mGridView.setAdapter(mAdapter);
                break;
        }
    }

    private void initData() {
        mData = new ArrayList<>();
    }

    /**
     * 池塘数据适配器
     */
    class PumpAdapter extends CommonAdapter<Pump> {

        public PumpAdapter(Context context, List<Pump> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final Pump item) {
            helper.setText(R.id.tv_pump_name, item.getName());

            final ImageView pumpImg = helper.getView(R.id.iv_pump_img);
            final ImageView fans = helper.getView(R.id.iv_pump_flabellum);

            //延时启动状态显示，避免UI卡顿
            new CountDownTimer(500, 500) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    myTool.showPumpByState(item.getRunningState(), pumpImg, fans);
                }
            }.start();

            helper.getView(R.id.rl_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onMessage(PumpFragment.this, item);
                    }
                }
            });
        }
    }

    class LoadPumpTask extends CommonTask<Pump> {

        public LoadPumpTask(Context context, int taskID) {
            super(context, taskID);
        }

        @Override
        public PostResult getResult(Map inputPara) {
            PostResult<List<Pump>> result = new PostResult<>();
            List<Pump> mData = new ArrayList<>();
            //模拟网络请求
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                result.setException(e);
                return result;
            }

            Pump pump1 = new Pump();
            pump1.setName("增氧机1");
            pump1.setRunningState("关");
            mData.add(pump1);

            Pump pump2 = new Pump();
            pump2.setName("增氧机2");
            pump2.setRunningState("开");
            mData.add(pump2);

            Pump pump3 = new Pump();
            pump3.setName("增氧机3");
            pump3.setRunningState("开");
            mData.add(pump3);

            Pump pump4 = new Pump();
            pump4.setName("增氧机4");
            pump4.setRunningState("关");
            mData.add(pump4);

            Pump pump5 = new Pump();
            pump5.setName("增氧机5");
            pump5.setRunningState("关");
            mData.add(pump5);

            Pump pump6 = new Pump();
            pump6.setName("增氧机6");
            pump6.setRunningState("开");
            mData.add(pump6);

            result.setResult(mData);
            return result;
        }
    }

}
