package com.cn.good.foodingredients.ui.login;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseActivity;
import com.cn.tools.OtherUtilities;


/**
 * Created by will wu on 2016/12/1.
 * 用户注册
 */

public class UserRegisterActivity extends BaseActivity {
    /**
     * 界面标题
     */
    private TextView titleText = null;
    /**
     * 返回按钮
     */
    private Button backButton = null;
    /**
     * 手机号码
     */
    private EditText moblieNoEditText = null;
    /**
     * 验证码
     */
    private EditText verifictionCodeEditText = null;
    /**
     * 获取验证码按钮
     */
    private TextView getVerifictionCodeButton = null;
    /**
     * 密码一
     */
    private EditText passwordOneEditText = null;
    /**
     * 密码二
     */
    private EditText passwordTwoEditText = null;
    /**
     * 注册协议
     */
    private CheckBox checkBox = null;
    /**
     * 用户注册协议
     */
    private TextView tvRegisterProtocol;
    /**
     * 完成用户注册按钮
     */
    private TextView registerButton;
    /**
     * 计时器
     */
    private TimeCount time;

    private String moblieNoStr = null;

    @Override
    protected void init() {
        setContentView(R.layout.user_register_activity);
    }

    @Override
    protected void initView() {
        this.titleText = (TextView) findViewById(android.R.id.title);
        this.backButton = (Button) findViewById(android.R.id.button1);
        this.registerButton = (TextView) findViewById(R.id.register_button);
        this.tvRegisterProtocol = (TextView) findViewById(R.id.tv_register_protocol);
        this.checkBox = (CheckBox) findViewById(R.id.checkbox);
        this.moblieNoEditText = (EditText) findViewById(R.id.moblie_no_edittext);
        this.verifictionCodeEditText = (EditText) findViewById(R.id.verifiction_code);
        this.getVerifictionCodeButton = (TextView) findViewById(R.id.get_verifiction_code);
        this.passwordOneEditText = (EditText) findViewById(R.id.password_one_edittext);
        this.passwordTwoEditText = (EditText) findViewById(R.id.password_two_edittext);
    }

    @Override
    protected void initData() {
        addActivity(this);
        this.titleText.setText("用户注册");
        this.backButton.setVisibility(View.VISIBLE);
        this.backButton.setTextColor(Color.WHITE);
        this.backButton.setText("返回");
        this.getVerifictionCodeButton.setText("获取短信验证码");
        this.getVerifictionCodeButton.setTextSize(15);
        /**构造CountDownTimer对象*/
        time = new TimeCount(60000, 1000);
    }

    @Override
    protected void initListener() {
        this.backButton.setOnClickListener(this);
        this.registerButton.setOnClickListener(this);
        this.tvRegisterProtocol.setOnClickListener(this);
        this.getVerifictionCodeButton.setOnClickListener(this);
        this.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1:
                finish();
                break;

            case R.id.get_verifiction_code:
                moblieNoStr = moblieNoEditText.getText().toString().trim();
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
                time.start();
                break;

            case R.id.register_button:
                moblieNoStr = moblieNoEditText.getText().toString().trim();
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

            case R.id.tv_register_protocol:


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
