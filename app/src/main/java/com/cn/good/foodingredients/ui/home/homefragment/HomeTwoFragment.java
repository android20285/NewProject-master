package com.cn.good.foodingredients.ui.home.homefragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cn.baseactivity.BaseFragment;
import com.cn.good.foodingredients.R;
import com.cn.tools.ImageUtil;

/**
 * Created by will wu on 2016/10/8.
 */

public class HomeTwoFragment extends BaseFragment {
    private Activity myActivity;
    private ImageView myPhoto=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myActivity = getActivity();
        View view = inflater.inflate(R.layout.home_two_fragment, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }


    private void initView(View view) {
        myPhoto= (ImageView) view.findViewById(R.id.my_photo);
    }

    private void initData() {
        ImageUtil.setRoundImageView(myPhoto, "http://tva3.sinaimg.cn/crop.0.2.750.750.180/d17adff1jw8f45pufy55ij20ku0kzdh5.jpg", R.mipmap.ic_launcher, myActivity);
    }

    private void initListener() {

    }
}
