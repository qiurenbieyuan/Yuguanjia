/**
 * 添加池塘
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddPondActivity extends Activity implements View.OnClickListener {
    private static final int IMAGE_REQUEST_CODE = 0011;
    private static final String TAG = "AddPondActivity";
    private LinearLayout llBack;
    private ImageView ivAddimg;
    private CommonTools myTool;
    private ArrayList<String> mSelectPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpond);
        initView();
        initEvent();
        initImageSelCon();
    }

    private void initImageSelCon() {

    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        ivAddimg = (ImageView) findViewById(R.id.iv_addimg);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        ivAddimg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_addimg:
                // 跳转到图片选择器
                MultiImageSelector.create(this)
                        .showCamera(true) // show camera or not. true by default
                        .single() // single mode
                        .origin(mSelectPath) // original select data set, used width #.multi()
                        .start(this, IMAGE_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the result list of select image paths
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                Log.i(TAG, "path: "+mSelectPath.toString());

                Glide.with(this).load(mSelectPath.get(0)).into(ivAddimg);
            }
        }
    }
}
