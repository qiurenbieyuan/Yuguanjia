package com.qican.ygj.ui.devicelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ScrollView;
import android.widget.TableLayout;

//import com.videogo.cameralist.CameraUtil;
//import com.videogo.device.DeviceModel;
//import com.videogo.devicelist.ChooseDeviceModelAdapter.OnItemClickListener;
//import com.videogo.stat.HikAction;
import com.qican.ygj.ui.scan.RootActivity;
import com.videogo.stat.HikStatPageConstant;
import com.videogo.widget.CheckTextButton;
import com.videogo.widget.TitleBar;

/**
 * 一键连接设备重置界面
 *
 * @author chengjuntao
 * @data 2014-4-9
 */
public class ChooseDeviceModeActivity extends RootActivity implements OnClickListener {

    public static final String SUPPORT_WIFI = "support_Wifi";

    public static final String SUPPORT_NET_WORK = "support_net_work";

    public static final String DEVICE_TYPE = "device_type";

    /**
     * 标题栏
     */
    private TitleBar mTitleBar;
    /**
     * 网络连接方式选择 TableLayout
     */
    private TableLayout mConnMethodsTableLayout;
    /**
     * 支持无线设备按钮
     */
    private Button mWifiButton;
    /**
     * 支持有线设备按钮
     */
    private Button mLineButton;
    /**
     * 路由器设备按钮
     */
    private Button mRouterButton;
    /**
     * 设备类型选择
     */
    private ExpandableListView mListView;
    /**
     * 按网络连接方式选择
     */
    private ViewGroup mConnMethodsLayout;
    private CheckTextButton mConnMethodsButton;
    /**
     * 按设备型号选择
     */
    private ViewGroup mDeviceModelLayout;
    private CheckTextButton mDeviceModelButton;

    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        // 页面统计
        super.setPageKey(HikStatPageConstant.HIK_STAT_PAGE_NEW_DEVICETYPE_CHOOSE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_wifi_choose_mode_activity);

        findViews();
        initData();
        initTitleBar();
        initViews();
    */
    }

    /**
     * 控件关联
     */
    private void findViews() {/*
        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mConnMethodsTableLayout = (TableLayout) findViewById(R.id.connection_methods_table);
        mWifiButton = (Button) findViewById(R.id.wifi_button);
        mLineButton = (Button) findViewById(R.id.line_button);
        mRouterButton = (Button) findViewById(R.id.router_button);
        mListView = (ExpandableListView) findViewById(R.id.listview);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mConnMethodsButton = (CheckTextButton) findViewById(R.id.conn_methods_caption);
        mDeviceModelButton = (CheckTextButton) findViewById(R.id.device_model_caption);
        mConnMethodsLayout = (ViewGroup) findViewById(R.id.conn_methods_layout);
        mDeviceModelLayout = (ViewGroup) findViewById(R.id.device_model_layout);
    */
    }

    /**
     * 初始化数据
     */
    private void initData() {

    }

    /**
     * 初始化标题栏
     */
    private void initTitleBar() {/*
        mTitleBar.setTitle(R.string.choose_model);
        mTitleBar.addBackButton(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    */
    }

    /**
     * 初始化控件
     */
    private void initViews() {/*
        mWifiButton.setOnClickListener(this);
        mLineButton.setOnClickListener(this);
        mRouterButton.setOnClickListener(this);

        ChooseDeviceModelAdapter adapter = new ChooseDeviceModelAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        for (int i = 0; i < adapter.getGroupCount(); i++)
            mListView.expandGroup(i);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, DeviceModel deviceModel) {
                HikStat.onEvent(ChooseDeviceModeActivity.this, HikAction.ACTION_choose_deviceMode_specific);
                new ChooseDeviceItem(deviceModel.getDisplay(), deviceModel.getDrawable1ResId());
                Intent intent;
                if (CameraUtil.isRouter(deviceModel)) {
                    intent = new Intent(ChooseDeviceModeActivity.this, RouterIntroduceActivity.class);
                    intent.putExtras(getIntent().getExtras());
                } else if (CameraUtil.isSupportWifi(deviceModel)) {
                    intent = new Intent(ChooseDeviceModeActivity.this, AutoWifiPrepareStepOneActivity.class);
                    intent.putExtras(getIntent().getExtras());
                    intent.putExtra(SUPPORT_WIFI, true);
                    intent.putExtra(SUPPORT_NET_WORK, CameraUtil.isSupportNetWork(deviceModel));
                    intent.putExtra(ChooseDeviceModeActivity.DEVICE_TYPE, deviceModel.getKey()[0]);
                } else {
                    intent = new Intent(ChooseDeviceModeActivity.this, AutoWifiConnectingActivity.class);
                    intent.putExtras(getIntent().getExtras());
                    intent.putExtra(ChooseDeviceModeActivity.SUPPORT_WIFI, false);
                    intent.putExtra(ChooseDeviceModeActivity.SUPPORT_NET_WORK, true);
                }

                startActivity(intent);
            }
        });
        mScrollView.smoothScrollTo(0, 0);

        mConnMethodsButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mConnMethodsTableLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
        mDeviceModelButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (isChecked)
                    mScrollView.smoothScrollTo(0, 0);
            }
        });

        mConnMethodsButton.setChecked(true);
        mDeviceModelButton.setChecked(false);
        mListView.setVisibility(View.GONE);

        mConnMethodsLayout.setOnClickListener(this);
        mDeviceModelLayout.setOnClickListener(this);
    */
    }

    @Override
    public void onClick(View v) {/*
        Intent intent;
        switch (v.getId()) {
            case R.id.conn_methods_layout:
            case R.id.device_model_layout:
                mConnMethodsButton.toggle();
                mDeviceModelButton.toggle();
                break;

            case R.id.wifi_button:
                HikStat.onEvent(ChooseDeviceModeActivity.this, HikAction.ACTION_choose_deviceMode_wifi);
                intent = new Intent(ChooseDeviceModeActivity.this, AutoWifiPrepareStepOneActivity.class);
                intent.putExtras(getIntent().getExtras());
                intent.putExtra(SUPPORT_WIFI, true);
                intent.putExtra(SUPPORT_NET_WORK, false);
                startActivity(intent);
                break;

            case R.id.line_button:
                HikStat.onEvent(ChooseDeviceModeActivity.this, HikAction.ACTION_choose_deviceMode_line);
                intent = new Intent(ChooseDeviceModeActivity.this, AutoWifiConnectingActivity.class);
                intent.putExtras(getIntent().getExtras());
                intent.putExtra(SUPPORT_WIFI, false);
                intent.putExtra(SUPPORT_NET_WORK, true);
                startActivity(intent);
                break;

            case R.id.router_button:
                HikStat.onEvent(ChooseDeviceModeActivity.this, HikAction.ACTION_choose_deviceMode_link);
                intent = new Intent(ChooseDeviceModeActivity.this, RouterIntroduceActivity.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
                break;
        }
    */
    }
}