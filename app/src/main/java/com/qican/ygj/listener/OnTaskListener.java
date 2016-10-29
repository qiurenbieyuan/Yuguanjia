package com.qican.ygj.listener;

import com.qican.ygj.task.CommonTask;

/**
 * Created by Administrator on 2016/10/26 0026.
 */
public interface OnTaskListener<T> {
    // 上传成功，返回地址结果
    void taskFailed(CommonTask t, Exception e); // 任务完成

    void taskSuccess(CommonTask t, T result); // 成功
}
