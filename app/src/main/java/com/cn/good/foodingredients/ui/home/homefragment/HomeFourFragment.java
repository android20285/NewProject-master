package com.cn.good.foodingredients.ui.home.homefragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cn.baseactivity.BaseFragment;
import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.R;
import com.cn.good.foodingredients.ui.login.UserLoginActivity;
import com.cn.greendao.DBHelper;
import com.cn.good.foodingredients.greendao.UserInfo;

/**
 * Created by will wu on 2016/10/8.
 */

public class HomeFourFragment extends BaseFragment {
    private Context mContext;
    private RelativeLayout logoutLayout = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        View view = inflater.inflate(R.layout.home_four_fragment, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        logoutLayout = (RelativeLayout) view.findViewById(R.id.logout_layout);
    }

    private void initData() {

    }

    private void initListener() {
        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });
    }

    private void startActivity() {
        String uid = BaseApplication.getInstance().sharedPreference.getString("user_Uid", "");
        UserInfo userInfo = DBHelper.getPersonalInfo(uid);
        if (userInfo != null) {
            BaseApplication.getInstance().sharedPreference.clear();
            DBHelper.deletePersonalInfo(userInfo);
            startActivity(new Intent(getActivity(), UserLoginActivity.class));
            exitApp();
        }
    }
}
