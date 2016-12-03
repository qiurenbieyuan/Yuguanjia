/**
 * 添加池塘
 */
package com.qican.ygj.ui.mypond;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qican.ygj.R;

import me.xiaopan.sketch.SketchImageView;


public class PreviewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AddPondActivity";
    private String path;
    private SketchImageView sketchImageView;
    private LinearLayout llBack;

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
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        sketchImageView.setImageBitmap(bitmap);
        sketchImageView.setSupportZoom(true);
    }

    private void initView() {
        sketchImageView = (SketchImageView) findViewById(R.id.iv_preview);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
    }

    private void initEvent() {
        sketchImageView.setOnClickListener(this);
        llBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_preview:
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
