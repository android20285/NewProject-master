package com.cn.good.foodingredients.ui;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseActivity;
import com.cn.greendao.DBHelper;
import com.cn.good.foodingredients.greendao.UserInfo;
import com.cn.good.foodingredients.greendao.dao.UserInfoDao;
import com.cn.tools.OtherUtilities;
import com.cn.good.foodingredients.ui.home.hometab.HomeFragmentTabActivity;
import com.cn.good.foodingredients.ui.login.UserLoginActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Will Wu on 2016/8/17.
 */

public class SplashActivity extends BaseActivity {

    private String TAG = SplashActivity.class.getSimpleName();
    private View startView = null;
    private AlphaAnimation loadAlphaAnimation = null;


    @Override
    protected void init() {
        startView = View.inflate(this, R.layout.splash_activity, null);
        setContentView(startView);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        addActivity(this);
        setSwipeBackEnable(false);
        loadPage();
    }


    @Override
    protected void initListener() {

    }

    private void loadPage() {
        loadAlphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        loadAlphaAnimation.setDuration(1500);
        startView.setAnimation(loadAlphaAnimation);
        loadAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!OtherUtilities.checkInternetConnection(SplashActivity.this)) {
                }
                startActivity();
            }
        });
    }

    private void startActivity() {
        //根据条件查询多条数据
        String uid = BaseApplication.getInstance().sharedPreference.getString("user_Uid", "");
        UserInfo userInfo = DBHelper.getPersonalInfo(uid);
        if (userInfo != null) {
            startActivity(new Intent(this, HomeFragmentTabActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
            finish();
        }
    }

}
