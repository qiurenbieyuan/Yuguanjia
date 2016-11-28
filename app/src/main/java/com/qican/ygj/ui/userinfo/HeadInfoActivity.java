/**
 * 添加池塘
 */
package com.qican.ygj.ui.userinfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;

import me.xiaopan.sketch.SketchImageView;


public class HeadInfoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "AddPondActivity";
    private String path;
    private SketchImageView sketchImageView;
    private CommonTools myTool;
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headinfo);

        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        // 通过文件路径设置
        sketchImageView.displayImage(myTool.getUserHeadURL());
        sketchImageView.setSupportZoom(true);
    }

    private void initView() {
        sketchImageView = (SketchImageView) findViewById(R.id.iv_preview);
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        myTool = new CommonTools(this);
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
