package com.cn.good.foodingredients.ui.login;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseActivity;
import com.cn.entity.BaseBean;
import com.cn.good.foodingredients.bean.UserLoginInfo;
import com.cn.greendao.DBHelper;
import com.cn.good.foodingredients.greendao.UserInfo;
import com.cn.good.foodingredients.greendao.dao.UserInfoDao;
import com.cn.nohttp.BeanJsonRequest;
import com.cn.nohttp.CallServer;
import com.cn.nohttp.HttpApi;
import com.cn.nohttp.HttpListener;
import com.cn.tools.OtherUtilities;
import com.cn.good.foodingredients.ui.home.hometab.HomeFragmentTabActivity;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by will wu on 2016/10/8.
 */

public class UserLoginActivity extends BaseActivity {
    private String TAG = UserLoginActivity.class.getSimpleName();
    /**
     * 手机号码
     */
    private EditText moblieNoEditText;
    /**
     * 密码
     */
    private EditText passwordEditText;
    /**
     * 登录按钮
     */
    private Button userlogin;
    /**
     * 找回密码
     */
    private Button forgetPassword = null;
    /**
     * 去注册
     */
    private Button registerNewAcccount = null;

    private String mobileStr = null;

    @Override
    protected void init() {
        setContentView(R.layout.user_login_activity);
    }

    @Override
    protected void initView() {
        this.userlogin = (Button) findViewById(R.id.login_button);
        this.passwordEditText = (EditText) findViewById(R.id.password_edittext);
        this.moblieNoEditText = (EditText) findViewById(R.id.moblie_no_edittext);
        this.forgetPassword = (Button) findViewById(R.id.forget_password);
        this.registerNewAcccount = (Button) findViewById(R.id.tv_regist_new_acccount);
    }

    @Override
    protected void initData() {
        addActivity(this);
        setSwipeBackEnable(false);
    }

    @Override
    protected void initListener() {
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClass(UserLoginActivity.this, ForgetPasswordActivity.class);
                startActivity(myIntent);
            }
        });

        registerNewAcccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClass(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(myIntent);
            }
        });

        this.userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 如果网络不可用，则给出提示*/
                if (!OtherUtilities.checkInternetConnection(UserLoginActivity.this)) {
                    return;
                }
                String moblieNoStr = moblieNoEditText.getText().toString().trim();
                String passWordStr = passwordEditText.getText().toString().trim();
                if (TextUtils.isEmpty(moblieNoStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_name_empty));
                    moblieNoEditText.setFocusable(true);
                    return;
                }
                if (!OtherUtilities.checkMobile(moblieNoStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_name_format_error));
                    moblieNoEditText.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(passWordStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_account_pwd_tip));
                    passwordEditText.setFocusable(true);
                    return;
                }
                if (!OtherUtilities.checkPwd(passWordStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_pwd_format_error));
                    return;
                }
                showProgressDialog(false);
                userlogin(moblieNoStr, passWordStr);
            }
        });
    }

    private void userlogin(String moblieNo, String passWord) {
        BeanJsonRequest<BaseBean> beanJsonRequest = new BeanJsonRequest<>(HttpApi.lOGIN_URL, RequestMethod.POST, BaseBean.class);
        beanJsonRequest.add("un", moblieNo);
        beanJsonRequest.add("pw", passWord);
        beanJsonRequest.add("mid", moblieNo);
        beanJsonRequest.add("pushsvc", "2");
        beanJsonRequest.add("ct", "1");
        beanJsonRequest.setTag(this);
        beanJsonRequest.setCancelSign(this);
        mobileStr = moblieNo;
        CallServer.getRequestInstance().add(0, beanJsonRequest, httpListener);
    }

    HttpListener<BaseBean> httpListener = new HttpListener<BaseBean>() {
        @Override
        public void onSucceed(int what, Response<BaseBean> response) {
            stopProgressDialog();
            CallServer.getRequestInstance().cancelBySign(this);
            if (response != null && response.get() != null) {
                BaseBean bean = response.get();
                if (bean != null && "1".equals(bean.getCode())) {
                    UserLoginInfo loginInfo = JSON.parseObject(bean.getResult().toString(), UserLoginInfo.class);
                    saveUserInfo(loginInfo);
                } else {
                    OtherUtilities.showToastText(bean.getMsg());
                }
            }
        }

        @Override
        public void onFailed(int what, Response<BaseBean> response) {
            stopProgressDialog();
            CallServer.getRequestInstance().cancelBySign(this);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CallServer.getRequestInstance().cancelBySign(this);
    }

    private void saveUserInfo(UserLoginInfo loginInfo) {
        UserInfo info = new UserInfo();
        info.setSs(loginInfo.getSs());
        info.setUid(Long.valueOf(loginInfo.getUid()));
        info.setMobile(mobileStr);
        info.setId(System.currentTimeMillis());
        String uid = BaseApplication.getInstance().sharedPreference.getString("user_Uid", "");
        UserInfo userInfo = DBHelper.getPersonalInfo(uid);
        BaseApplication.getInstance().sharedPreference.put("user_Uid", loginInfo.getUid());
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getSs())) {
            if (loginInfo.getUid().equals(userInfo.getUid())) {
                DBHelper.updatePersonalInfo(userInfo);
            } else {
                DBHelper.insetPersonalInfo(info);
            }
        } else {
            DBHelper.insetPersonalInfo(info);
        }
        startActivity();
    }

    private void startActivity() {
        startActivity(new Intent(this, HomeFragmentTabActivity.class));
        finish();
    }
}
