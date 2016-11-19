/**
 * @Function：关于
 * @Author：残阳催雪
 * @Time：2016-8-9
 * @Email:qiurenbieyuan@gmail.com
 */
package com.qican.ygj.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.qican.ygj.R;
import com.qican.ygj.utils.CommonTools;


public class AboutActivity extends Activity implements View.OnClickListener {
    private CommonTools myTool;
    private LinearLayout llBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();

        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initEvent() {
        llBack.setOnClickListener(this);

    }

    private void initView() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);

        myTool = new CommonTools(this);
    }

    /**
     * 点击事件
     *
     * @param v：所点击的View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
