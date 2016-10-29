/**
 * 服务器请求，通用类
 *
 * @作者：残阳催雪
 * @时间：2016-7-22
 */
package com.qican.ygj.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.qican.ygj.R;
import com.qican.ygj.bean.PostResult;
import com.qican.ygj.listener.OnTaskListener;
import com.qican.ygj.utils.CommonTools;

import java.util.Map;

public abstract class CommonTask<ResultType> extends AsyncTask<Map, Void, PostResult> {

    private Context mContext;
    private ProgressBar mProgressBar;
    protected CommonTools myTool;
    private OnTaskListener<ResultType> onTaskListener;
    private boolean isShowExceptionState = false; // 是否显示网络异常状态
    private ImageView ivProgress;
    private int taskID;

    /**
     * 设置旋转动画
     */
    final RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f);
    private RotateAnimation loadingAnim;

    public CommonTask(Context context) {
        mContext = context;
    }

    /**
     * 上下文，以及任务ID
     *
     * @param context
     * @param taskID
     */
    public CommonTask(Context context, int taskID) {
        mContext = context;
        this.taskID = taskID;
    }

    /**
     * 通用任务构造函数
     *
     * @param context  上下文
     * @param inputMap 输入Map
     */
    public CommonTask(Context context, Map inputMap) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        myTool = new CommonTools(mContext);

        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        if (ivProgress != null) {
            ivProgress.setVisibility(View.VISIBLE);

            loadingAnim = (RotateAnimation) AnimationUtils.loadAnimation(mContext, R.anim.data_loading);
            ivProgress.startAnimation(loadingAnim);
        }
    }

    @Override
    protected PostResult doInBackground(Map... params) {
        return getResult(params[0]);
    }

    /**
     * 返回请求的收货地址列表
     *
     * @param result
     */
    @Override
    protected void onPostExecute(PostResult result) {
        super.onPostExecute(result);

        // 如果设置了进度条，则使进度条不见
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

        if (ivProgress != null) {
            loadingAnim.cancel();
            ivProgress.setAnimation(null);//必须设置为空否则setVisibility()将失效
            ivProgress.setVisibility(View.GONE);
        }

        // 设置回调
        if (onTaskListener != null) {
            if (result.getResult() != null) {
                // 完成过后返回结果和网络状态
                onTaskListener.taskSuccess(this, (ResultType) result.getResult());
            } else {
                // 完成过后返回结果和网络状态
                onTaskListener.taskFailed(this, result.getException());
            }
        }
    }

    public CommonTask<ResultType> setOnTaskFinishListener(OnTaskListener onTaskFinishListener) {
        this.onTaskListener = onTaskFinishListener;
        return this;
    }

    public CommonTask<ResultType> setProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
        return this;
    }

    public void setShowExceptionState(boolean showExceptionState) {
        isShowExceptionState = showExceptionState;
    }

    /**
     * 需要用户实现，从json中得到返回的数据
     *
     * @param inputPara 输入的请求参数
     * @return
     */
    public abstract PostResult getResult(Map inputPara);

    public int getTaskID() {
        return taskID;
    }

    public CommonTask<ResultType> setTaskID(int taskID) {
        this.taskID = taskID;
        return this;
    }

    public ImageView getIvProgress() {
        return ivProgress;
    }

    public CommonTask<ResultType> setIvProgress(@NonNull ImageView ivProgress) {
        this.ivProgress = ivProgress;
        return this;
    }
}
