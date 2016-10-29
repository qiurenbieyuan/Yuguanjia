package com.qican.ygj.ui.tabview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qican.ygj.R;
import com.qican.ygj.bean.Camera;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.bean.PostResult;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.listener.OnTaskListener;
import com.qican.ygj.task.CommonTask;
import com.qican.ygj.ui.adapter.CommonAdapter;
import com.qican.ygj.ui.adapter.ViewHolder;
import com.qican.ygj.utils.CommonTools;
import com.videogo.openapi.bean.EZDeviceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CameraFragment extends Fragment implements OnTaskListener {
    private static final int TASK_LOAD_CAMERA = 001;
    private CommonTools myTool;
    private static final String TAG = "PondFragment";
    private GridView mGridView;
    private CameraAdapter mAdapter;
    private List<Camera> mData = null;
    private OnFramentListener mCallBack;
    private LoadCameraTask mLoadCameraTask = null;
    private ImageView ivProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_camera, container, false);

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
    }

    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.gridView);
        ivProgress = (ImageView) v.findViewById(R.id.iv_progress);

        myTool = new CommonTools(getActivity());
    }

    /**
     * 镜头数据适配器
     */
    class CameraAdapter extends CommonAdapter<Camera> {

        public CameraAdapter(Context context, List<Camera> mDatas, int itemLayoutId) {
            super(context, mDatas, itemLayoutId);
        }

        @Override
        public void convert(ViewHolder helper, final Camera item) {
            RelativeLayout rlItem = helper.getView(R.id.rl_item);

            helper.setText(R.id.tv_camera_name, item.getName());
            helper.setImageByUrl(R.id.iv_camera_img, item.getPreImgUrl());

            rlItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onMessage(CameraFragment.this, item);
                    }
                }
            });
        }
    }

    /**
     * @param pond
     */
    public void loadCameraInfoByPond(Pond pond) {
//        myTool.showInfo("正在加载池塘" + pond.getName() + "中的镜头信息，请稍后...");
        mLoadCameraTask = new LoadCameraTask(getActivity(), TASK_LOAD_CAMERA);
        mLoadCameraTask.setOnTaskFinishListener(this);
        mLoadCameraTask.setIvProgress(ivProgress);

        Map<String, String> map = new HashMap<>();
        mLoadCameraTask.execute(map);

    }

    class LoadCameraTask extends CommonTask<Camera> {

        public LoadCameraTask(Context context, int taskID) {
            super(context, taskID);
        }

        @Override
        public PostResult getResult(Map inputPara) {
            PostResult<List<Camera>> result = new PostResult<>();
            List<Camera> mData = new ArrayList<>();
            //模拟网络请求
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                result.setException(e);
                return result;
            }

            Camera camera1 = new Camera();
            camera1.setPreImgUrl("http://img2.imgtn.bdimg.com/it/u=2277679158,2108096310&fm=21&gp=0.jpg");
            camera1.setName("镜头1");
            mData.add(camera1);

            Camera camera2 = new Camera();
            camera2.setPreImgUrl("http://img2.imgtn.bdimg.com/it/u=3620995193,1712882299&fm=21&gp=0.jpg");
            camera2.setName("镜头2");
            mData.add(camera2);

            Camera camera3 = new Camera();
            camera3.setPreImgUrl("http://img0.imgtn.bdimg.com/it/u=803028758,3240392716&fm=21&gp=0.jpg");
            camera3.setName("镜头3");
            mData.add(camera3);

            Camera camera4 = new Camera();
            camera4.setPreImgUrl("http://img0.imgtn.bdimg.com/it/u=1449995627,271389355&fm=21&gp=0.jpg");
            camera4.setName("镜头4");
            mData.add(camera4);

            Camera camera5 = new Camera();
            camera5.setPreImgUrl("http://img2.imgtn.bdimg.com/it/u=182166815,1506771651&fm=21&gp=0.jpg");
            camera5.setName("镜头5");
            mData.add(camera5);

            result.setResult(mData);
            return result;
        }
    }

    @Override
    public void taskFailed(CommonTask t, Exception e) {

    }

    @Override
    public void taskSuccess(CommonTask t, Object result) {
        mData = (List<Camera>) result;

        mAdapter = new CameraAdapter(getActivity(), mData, R.layout.item_camera);
        mGridView.setAdapter(mAdapter);
    }
}
