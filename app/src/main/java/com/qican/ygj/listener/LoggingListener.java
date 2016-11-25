package com.qican.ygj.listener;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;

// 示例: .listener(new LoggingListener<String, GlideDrawable>())
public class LoggingListener<T, R> implements RequestListener<T, R> {
    @Override
    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                "onException(%s, %s, %s, %s)", e, model, target, isFirstResource), e);
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
        android.util.Log.d("GLIDE", String.format(Locale.ROOT,
                "onResourceReady(%s, %s, %s, %s, %s)", resource, model, target, isFromMemoryCache, isFirstResource));
        return false;
    }
}
