package com.cn.good.foodingredients.ui.home.hometab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseActivity;
import com.cn.good.foodingredients.ui.home.homefragment.HomeFirstFragment;
import com.cn.good.foodingredients.ui.home.homefragment.HomeFourFragment;
import com.cn.good.foodingredients.ui.home.homefragment.HomeThreeFragment;
import com.cn.good.foodingredients.ui.home.homefragment.HomeTwoFragment;

/**
 * Created by will wu on 2016/10/8.
 */

public class HomeFragmentTabActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private String TAG = HomeFragmentTabActivity.class.getSimpleName();
    /**
     * 标示了当前位置
     */
    private final int POS_ONE = 0, POS_TWO = 1, POS_THREE = 2, POS_FOUR = 3;
    private ViewPager mVp;
    private RadioGroup mRg;
    private MainFPagerAdaper mainFPagerAdaper;
    private TextView titleText = null;

    @Override
    protected void init() {
        setContentView(R.layout.home_fragment_tab_activity);
    }

    @Override
    protected void initView() {
        mVp = (ViewPager) findViewById(R.id.mviewpager);
        mRg = (RadioGroup) findViewById(R.id.mnc_rg);
        titleText = (TextView) findViewById(android.R.id.title);
    }

    @Override
    protected void initData() {
        addActivity(this);
        setSwipeBackEnable(false);
        titleText.setText(getString(R.string.tab_tag_one));
        mainFPagerAdaper = new MainFPagerAdaper(getSupportFragmentManager());
        mVp.setOffscreenPageLimit(3);
        mVp.setAdapter(mainFPagerAdaper);
    }

    @Override
    protected void initListener() {
        mVp.setOnPageChangeListener(this);
        mRg.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSelectTab(BaseApplication.getInstance().sharedPreference.getInt("home_tab_key", 0));
    }

    private void refreshTitle(int pos) {
        switch (pos) {
            case POS_ONE:
                titleText.setText(getString(R.string.tab_tag_one));
                break;
            case POS_TWO:
                titleText.setText(getString(R.string.tab_tag_two));
                break;
            case POS_THREE:
                titleText.setText(getString(R.string.tab_tag_three));
                break;
            case POS_FOUR:
                titleText.setText(getString(R.string.tab_tag_four));
                break;
        }
    }

    private void saveTabPosition(int pos) {
        BaseApplication.getInstance().sharedPreference.put("home_tab_key", pos);
    }

    private void initSelectTab(int pos) {
        if (pos < POS_ONE || pos > POS_FOUR)
            return;
        RadioButton rb = findRadioButtonByPos(pos);
        if (rb != null) {
            rb.setChecked(true);
        }
        mVp.setCurrentItem(pos, false);
    }


    private RadioButton findRadioButtonByPos(int position) {
        switch (position) {
            case POS_ONE:
                return (RadioButton) mRg.findViewById(R.id.mnc_rbnt_one);
            case POS_TWO:
                return (RadioButton) mRg.findViewById(R.id.mnc_rbnt_two);
            case POS_THREE:
                return (RadioButton) mRg.findViewById(R.id.mnc_rbnt_three);
            case POS_FOUR:
                return (RadioButton) mRg.findViewById(R.id.mnc_rbnt_four);
        }
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton rb = findRadioButtonByPos(position);
        if (rb != null) {
            rb.setChecked(true);
        }
        /**不管是点击RadioButton还是滑动Viewpager，都会执行这段代码，所以在此处执行保存的操作*/
        saveTabPosition(position);
        refreshTitle(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.mnc_rbnt_one:
                if (POS_ONE != mVp.getCurrentItem()) {
                    mVp.setCurrentItem(POS_ONE, false);
                }
                break;
            case R.id.mnc_rbnt_two:
                if (POS_TWO != mVp.getCurrentItem()) {
                    mVp.setCurrentItem(POS_TWO, false);
                }
                break;
            case R.id.mnc_rbnt_three:
                if (POS_THREE != mVp.getCurrentItem()) {
                    mVp.setCurrentItem(POS_THREE, false);
                }
                break;
            case R.id.mnc_rbnt_four:
                if (POS_FOUR != mVp.getCurrentItem()) {
                    mVp.setCurrentItem(POS_FOUR, false);
                }
                break;
        }
    }

    /**
     * 页面适配器start
     */
    public class MainFPagerAdaper extends FragmentPagerAdapter {

        public MainFPagerAdaper(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment retFragment = null;
            switch (position) {
                case 0:
                    retFragment = new HomeFirstFragment();
                    break;
                case 1:
                    retFragment = new HomeTwoFragment();
                    break;
                case 2:
                    retFragment = new HomeThreeFragment();
                    break;
                case 3:
                    retFragment = new HomeFourFragment();
                    break;
            }
            return retFragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }
    }
    /**页面适配器end*/

}
