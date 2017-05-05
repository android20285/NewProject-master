package com.cn.good.foodingredients.ui.login;

import android.widget.TextView;

import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseActivity;

/**
 * Created by Administrator on 2016/12/1.
 * 用户注册短袖验证
 */

public class UserRegisterVerificationActivity extends BaseActivity {

    private TextView titleText = null;

    @Override
    protected void init() {
        setContentView(R.layout.user_register_verification_activity);
    }

    @Override
    protected void initView() {
        titleText = (TextView) findViewById(android.R.id.title);
    }

    @Override
    protected void initData() {
        addActivity(this);
        titleText.setText("用户注册");
    }

    @Override
    protected void initListener() {

    }
}
