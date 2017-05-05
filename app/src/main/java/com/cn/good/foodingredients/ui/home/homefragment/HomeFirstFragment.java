package com.cn.good.foodingredients.ui.home.homefragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseFragment;
import com.cn.nohttp.CallServer;
import com.cn.tools.OtherUtilities;
import com.cn.good.foodingredients.ui.home.hometab.HomeFragmentTabActivity;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

/**
 * Created by will wu on 2016/10/8.
 */

public class HomeFirstFragment extends BaseFragment {

    private String TAG = HomeFirstFragment.class.getSimpleName();

    /**
     * 获取父类activity,对象用户显示加载数据load框
     */
    private HomeFragmentTabActivity HomeFragmentTabActivity = null;
    /**
     * 用于当HomeFirstFragment 未创建时加载数据时
     */
    private boolean isFirstLoadData = true;
    /**
     * RollPagerView 广告图片滚动播放
     */
    private RollPagerView mRollViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_first_fragment, container, false);
        initView(view);
        initListener();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        HomeFragmentTabActivity = (HomeFragmentTabActivity) getActivity();
    }

    private void initView(View view) {
        mRollViewPager = (RollPagerView) view.findViewById(R.id.roll_view_pager);
    }

    private void initData() {
        /**设置播放时间间隔*/
        mRollViewPager.setPlayDelay(2500);
        /**设置透明度*/
        mRollViewPager.setAnimationDurtion(1000);
        /**设置适配器*/
        mRollViewPager.setAdapter(new HomeFirstFragment.TestNormalAdapter());
        /**设置圆点指示器颜色*/
        mRollViewPager.setHintView(new ColorPointHintView(HomeFragmentTabActivity, Color.YELLOW, Color.WHITE));
    }

    private void initListener() {
        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                OtherUtilities.showToastText("Item " + position + " clicked");
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstLoadData) {
                onFragmentVisible();
                isFirstLoadData = false;
            }
        } else {
            onFragmentInvisible();
        }
    }

    /**
     * Fragment可见
     */
    protected void onFragmentVisible() {
        if (HomeFragmentTabActivity != null) {
            initData();
        }
    }

    /**
     * Fragment不可见
     */
    protected void onFragmentInvisible() {
        stopProgressDialog();
        CallServer.getRequestInstance().cancelBySign(this);
    }

    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
        };

        @Override
        public int getCount() {
            return imgs.length;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }
    }
}
