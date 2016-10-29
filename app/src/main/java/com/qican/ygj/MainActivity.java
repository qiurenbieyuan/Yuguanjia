package com.qican.ygj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.qican.ygj.bean.Camera;
import com.qican.ygj.bean.Pond;
import com.qican.ygj.bean.Pump;
import com.qican.ygj.listener.OnFramentListener;
import com.qican.ygj.ui.SlideMenuFragment;
import com.qican.ygj.ui.tabview.CameraFragment;
import com.qican.ygj.ui.tabview.PondFragment;
import com.qican.ygj.ui.tabview.PumpFragment;
import com.qican.ygj.utils.CommonTools;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, OnFramentListener {

    private static final String TAG = "MainActivity";
    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private PondFragment pondFragment;
    private CameraFragment cameraFragment;
    private PumpFragment pumpFragment;
    private int tabCnt = 0;

    private int mPerScreenWidth;
    private int initLeftMargin = 20;
    private CommonTools myTool;

    /**
     * Tab显示内容TextView
     */
    private TextView mTabPondTv, mTabCameraTv, mTabPumpTv;
    private LinearLayout llPond, llCamera, llPump;//tab上面的点击按钮
    private ImageView ivPreview;
    /**
     * Tab的那个引导线
     */
    private ImageView mTabLineIv;

    /**
     * ViewPager的当前选中页
     */
    private int currentIndex;
    /**
     * 屏幕的宽度
     */
    private int screenWidth;
    private SlidingMenu menu;//侧滑菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();

        initTabLineWidth();
    }

    private void initSlidingMenu() {
        // configure the SlidingMenu
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.55f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.menu_frame);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, new SlideMenuFragment())
                .commit();
    }

    /**
     * 设置滑动条的宽度为屏幕的1/3(根据Tab的个数而定)
     */
    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / tabCnt - 2 * initLeftMargin;
        mPerScreenWidth = screenWidth / tabCnt;//每个tab的宽度

        lp.leftMargin = initLeftMargin;

        mTabLineIv.setLayoutParams(lp);

        //初始选中第一个
        setTvSelected(mTabPondTv);
    }

    private void toTabByIndex(int curTabIndex) {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        lp.width = screenWidth / tabCnt - 2 * initLeftMargin;
        lp.leftMargin = curTabIndex * screenWidth / tabCnt + initLeftMargin;

        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 处理文字的颜色变化
     *
     * @param tv
     */
    private void setTvUnselected(TextView tv) {
        tv.setTextColor(getResources().getColorStateList(R.color.tab_unselected));
    }

    private void setTvSelected(TextView tv) {
        resetTextView();
        tv.setTextColor(getResources().getColorStateList(R.color.tab_selected));
    }

    private void resetTextView() {
        setTvUnselected(mTabPondTv);
        setTvUnselected(mTabCameraTv);
        setTvUnselected(mTabPumpTv);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_tab);
        mTabLineIv = (ImageView) findViewById(R.id.id_tab_line_iv);//底部的tabLine

        mTabPondTv = (TextView) findViewById(R.id.tv_pood);
        mTabCameraTv = (TextView) findViewById(R.id.tv_camera);
        mTabPumpTv = (TextView) findViewById(R.id.tv_pump);

        llPond = (LinearLayout) findViewById(R.id.ll_pond);
        llCamera = (LinearLayout) findViewById(R.id.ll_camera);
        llPump = (LinearLayout) findViewById(R.id.ll_pump);

        ivPreview = (ImageView) findViewById(R.id.iv_video_preview);

        myTool = new CommonTools(this);

        initSlidingMenu();
        loadFragments();
    }

    private void initEvent() {
        mViewPager.setOnPageChangeListener(this);

        llPond.setOnClickListener(this);
        llCamera.setOnClickListener(this);
        llPump.setOnClickListener(this);
    }

    private void loadFragments() {

        //所有订单
        pondFragment = new PondFragment();
        cameraFragment = new CameraFragment();
        pumpFragment = new PumpFragment();

        mTabs.add(pondFragment);
        mTabs.add(cameraFragment);
        mTabs.add(pumpFragment);

        tabCnt = mTabs.size();

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabs.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabs.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onPageScrolled(int position, float offset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
                .getLayoutParams();
        if (offset > 0) {
            lp.leftMargin = (int) (offset * mPerScreenWidth + position
                    * mPerScreenWidth + initLeftMargin);
        }
        Log.i("leftMargin", "leftMargin: -------[" + lp.leftMargin + "]");
        mTabLineIv.setLayoutParams(lp);
    }

    /**
     * 根据当前位置变换字体颜色
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                setTvSelected(mTabPondTv);
                break;
            case 1:
                setTvSelected(mTabCameraTv);
                break;
            case 2:
                setTvSelected(mTabPumpTv);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 处理点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: ");
        switch (v.getId()) {
            case R.id.ll_pond:
                //带滑动效果
                mViewPager.setCurrentItem(0, true);
                setTvSelected(mTabPondTv);
//                toTabByIndex(0);
                break;
            case R.id.ll_camera:
                mViewPager.setCurrentItem(1, true);
                setTvSelected(mTabCameraTv);
//                toTabByIndex(1);
                break;
            case R.id.ll_pump:
                mViewPager.setCurrentItem(2, true);
                setTvSelected(mTabPumpTv);
//                toTabByIndex(2);
                break;

        }
    }

    /**
     * 来自各个fragment的回调信息
     *
     * @param fg
     * @param obj
     */
    @Override
    public void onMessage(Fragment fg, Object obj) {

        if (fg.getClass().equals(PondFragment.class)) {
            mTabPondTv.setText(((Pond) obj).getName());
            mTabCameraTv.setText("镜头");

            mViewPager.setCurrentItem(1, true);
            setTvSelected(mTabCameraTv);

            myTool.showImage(((Pond) obj).getImgUrl(), ivPreview);

            //通知子页面加载信息
            cameraFragment.loadCameraInfoByPond((Pond) obj);
        }

        if (fg.getClass().equals(CameraFragment.class)) {
            mTabCameraTv.setText(((Camera) obj).getName());
            mViewPager.setCurrentItem(2, true);
            setTvSelected(mTabPumpTv);

            myTool.showImage(((Camera) obj).getPreImgUrl(), ivPreview);
            //通知泵机界面加载信息
            pumpFragment.loadPumpByCamera((Camera) obj);
        }

        if (fg.getClass().equals(PumpFragment.class)) {
            myTool.showInfo(((Pump) obj).getName());
        }
    }

}
