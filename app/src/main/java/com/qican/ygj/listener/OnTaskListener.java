package com.qican.ygj.listener;

import com.qican.ygj.task.CommonTask;

public interface OnTaskListener<T> {
    // 上传成功，返回地址结果
    void taskFailed(CommonTask t, Exception e); // 任务完成

    void taskSuccess(CommonTask t, T result); // 成功
}
