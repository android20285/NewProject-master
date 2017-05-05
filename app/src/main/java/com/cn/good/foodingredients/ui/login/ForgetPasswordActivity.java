package com.cn.good.foodingredients.ui.login;


import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseActivity;
import com.cn.tools.OtherUtilities;

/**
 * Created by will wu on 2016/12/1.
 * 找回密码
 */

public class ForgetPasswordActivity extends BaseActivity {
    /**
     * 界面标题
     */
    private TextView titleTextView = null;
    /**
     * 返回按钮
     */
    private Button backButton = null;
    /**
     * 电话号码输入框
     */
    private EditText moblieNoEdittext;
    /**
     * 验证码输入框
     */
    private EditText verifictionCodeEditText;
    /**
     * 获取验证码按钮
     */
    private TextView getVerifictionCodeButton;
    /**
     * 密码输入框
     */
    private EditText passwordOneEditText;
    /**
     * 密码输入框
     */
    private EditText passwordTwoEditText;
    /**
     * 确认按钮
     */
    private TextView determineButton;
    /**
     * 临时存放手机号码
     */
    private String moblieNoStr = null;
    /**
     * 计时器
     */
    private TimeCount time;

    @Override
    protected void init() {
        setContentView(R.layout.forget_password_activity);
    }

    @Override
    protected void initView() {
        this.titleTextView = (TextView) findViewById(android.R.id.title);
        this.backButton = (Button) findViewById(android.R.id.button1);
        this.determineButton = (TextView) findViewById(R.id.determine_button);
        this.passwordTwoEditText = (EditText) findViewById(R.id.password_two);
        this.passwordOneEditText = (EditText) findViewById(R.id.password_one);
        this.getVerifictionCodeButton = (TextView) findViewById(R.id.get_verifiction_code);
        this.verifictionCodeEditText = (EditText) findViewById(R.id.verifiction_code);
        this.moblieNoEdittext = (EditText) findViewById(R.id.moblie_no_edittext);
    }

    @Override
    protected void initData() {
        addActivity(this);
        titleTextView.setText("找回密码");
        backButton.setVisibility(View.VISIBLE);
        backButton.setTextColor(Color.WHITE);
        backButton.setText("返回");
        getVerifictionCodeButton.setText("获取短信验证码");
        getVerifictionCodeButton.setTextSize(15);
        /**构造CountDownTimer对象*/
        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void initListener() {
        backButton.setOnClickListener(this);
        determineButton.setOnClickListener(this);
        getVerifictionCodeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                finish();
                break;
            case R.id.get_verifiction_code:
                moblieNoStr = moblieNoEdittext.getText().toString().trim();
                if (TextUtils.isEmpty(moblieNoStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_name_empty));
                    return;
                }
                if (!OtherUtilities.checkMobile(moblieNoStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_name_format_error));
                    return;
                }

                time.start();
                break;

            case R.id.determine_button:
                moblieNoStr = moblieNoEdittext.getText().toString().trim();
                if (TextUtils.isEmpty(moblieNoStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_name_empty));
                    return;
                }
                if (!OtherUtilities.checkMobile(moblieNoStr)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_name_format_error));
                    return;
                }

                String verifictionCodeStr = verifictionCodeEditText.getText().toString().trim();
                if (TextUtils.isEmpty(verifictionCodeStr)) {
                    OtherUtilities.showToastText(getString(R.string.verifiction_code_not_null));
                    return;
                }

                String passwordOneSter = passwordOneEditText.getText().toString().trim();
                if (TextUtils.isEmpty(passwordOneSter)) {
                    OtherUtilities.showToastText(getString(R.string.password_not_null));
                    return;
                }

                if (!OtherUtilities.checkPwd(passwordOneSter)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_pwd_format_error));
                    return;
                }

                String passwordtwoSter = passwordTwoEditText.getText().toString().trim();
                if (TextUtils.isEmpty(passwordtwoSter)) {
                    OtherUtilities.showToastText(getString(R.string.confirm_password_not_null));
                    return;
                }

                if (!OtherUtilities.checkPwd(passwordtwoSter)) {
                    OtherUtilities.showToastText(getString(R.string.login_prompt_pwd_format_error));
                    return;
                }

                if (!passwordOneSter.equals(passwordtwoSter)) {
                    OtherUtilities.showToastText(getString(R.string.confirm_password_inconformity));
                    return;
                }
                break;

        }
    }

    /**
     * 定义一个倒计时的内部类
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            getVerifictionCodeButton.setTextSize(15);
            getVerifictionCodeButton.setText("重新获取验证码");
            getVerifictionCodeButton.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            getVerifictionCodeButton.setClickable(false);
            getVerifictionCodeButton.setTextSize(18);
            getVerifictionCodeButton.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
