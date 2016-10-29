/**
 * 添加池塘
 */
package com.qican.ygj.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddPondActivity extends Activity implements View.OnClickListener {
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final String TAG = "AddPondActivity";
    private LinearLayout llBack, llAddPond;
    private ImageView ivAddimg, ivClose;
    private CommonTools myTool;
    private ArrayList<String> mSelectPath = null;
    private ImageView ivImgDesc;
    private EditText edtPondName, edtPondDesc;

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
        llAddPond = (LinearLayout) findViewById(R.id.ll_add);

        ivAddimg = (ImageView) findViewById(R.id.iv_addimg);
        ivImgDesc = (ImageView) findViewById(R.id.iv_imgdesc);
        ivClose = (ImageView) findViewById(R.id.iv_close);

        edtPondName = (EditText) findViewById(R.id.edt_pond_name);
        edtPondDesc = (EditText) findViewById(R.id.edt_pond_desc);

        myTool = new CommonTools(this);
    }

    private void initEvent() {
        llBack.setOnClickListener(this);
        llAddPond.setOnClickListener(this);

        ivAddimg.setOnClickListener(this);
        ivImgDesc.setOnClickListener(this);
        ivClose.setOnClickListener(this);
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
            case R.id.iv_imgdesc:
                Bundle bundle = new Bundle();
                bundle.putString("path", mSelectPath.get(0));
                Intent intent = new Intent(this, PreviewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_close:
                ivImgDesc.setVisibility(View.GONE);
                ivClose.setVisibility(View.GONE);
                ivAddimg.setVisibility(View.VISIBLE);
                mSelectPath.clear();//去除照片地址信息
                break;
            case R.id.ll_add:
                //添加池塘
                addPond();
                break;
        }
    }

    private void addPond() {
        if (edtPondName.getText().toString().trim().isEmpty()) {
            myTool.showInfo("还没有添加池塘名字哦！");
            return;
        }
        if (edtPondDesc.getText().toString().trim().isEmpty()) {
            myTool.showInfo("还没有池塘描述唷！");
            return;
        }
        //添加池塘信息
        myTool.showInfo("添加成功！");
        finish();
        startActivity(new Intent(this, AddSuccessActivity.class));
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
                Log.i(TAG, "path: " + mSelectPath.toString());
                showImageByPath(mSelectPath);
            }
        }
    }

    /**
     * 显示池塘封面图片到图片预览上
     *
     * @param mSelectPath
     */
    private void showImageByPath(ArrayList<String> mSelectPath) {
        if (mSelectPath != null) {
            if (mSelectPath.size() != 0) {
                ivImgDesc.setVisibility(View.VISIBLE);
                ivAddimg.setVisibility(View.GONE);
                ivClose.setVisibility(View.VISIBLE);
                Glide.with(this).load(mSelectPath.get(0)).into(ivImgDesc);
            }
        }
    }
}
