/**
 * 添加池塘
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.qican.ygj.R;

import me.xiaopan.sketch.SketchImageView;


public class PreviewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AddPondActivity";
    private String path;
    private SketchImageView sketchImageView;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Bundle bundle = getIntent().getExtras();
        path = bundle.getString("path");
        Log.i(TAG, "onCreate: " + path);

        initView();
        initEvent();
        initData();
    }

    private void initData() {
        // 通过文件路径设置
        sketchImageView.displayImage(path);
        sketchImageView.setSupportZoom(true);
    }

    private void initView() {
        sketchImageView = (SketchImageView) findViewById(R.id.iv_preview);
        ivBack = (ImageView) findViewById(R.id.iv_back);
    }

    private void initEvent() {
        sketchImageView.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_preview:
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
