/**
 * 项目名称: CoreLibrary
 * 文件名称: BaseActivity.java
 * 创建时间: 2014/3/28
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved.
 */
package com.cn.baseactivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.R;
import com.cn.progressdialog.BaseProgressDialog;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

/**
 * 所有页面activity的抽象基类
 *
 * @author Will.Wu </br> Create at 2014年2月19日 下午5:31:35
 * @version 1.0
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener, SwipeBackActivityBase {
    private String TAG = BaseActivity.class.getSimpleName();
    /**
     * 加载数据时的加载框...
     */
    private BaseProgressDialog mProgressDialog = null;
    private SystemBarTintManager tintManager = null;
    private SwipeBackActivityHelper mHelper = null;
    private BaseApplication myBaseApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        init();
        initView();
        initData();
        initListener();
    }

    /**
     * your <code>setContentView(layoutResID)</code> in here.
     */
    protected abstract void init();

    /**
     * your <code>findViewById(int id)</code> in here.
     */
    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    private void loadData() {
//        setStateBarColor();
//        initSystemBar();
        myBaseApplication = BaseApplication.getInstance();
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.base_push_right_in, R.anim.base_push_left_out);
    }

    @Override
    public void finish() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && mProgressDialog.cancelable()) {
            cancelProgressDialog();
            return;
        }
        super.finish();
        overridePendingTransition(R.anim.base_push_left_in, R.anim.base_push_right_out);
    }

    /***
     * 停止程序加载loading 框
     */
    public void stopProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.stop();
        }
        mProgressDialog = null;
    }

    /***
     * 关闭程序加载loading 框
     */
    protected void cancelProgressDialog() {
        if (mProgressDialog.cancel()) {
            mProgressDialog = null;
        }
    }

    /***
     * 程序加载loading 框
     */
    public void showProgressDialog(BaseProgressDialog.OnCancelListener cancelListener, boolean cancelable, String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new BaseProgressDialog(this, msg);
        if (cancelListener != null) {
            mProgressDialog.setOnCancelListener(cancelListener);
        }
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
    }

    /***
     * 程序加载loading 框
     */
    public void showProgressDialog(boolean cancelable, String msg) {
        showProgressDialog(null, cancelable, msg);
    }

    /***
     * 程序加载loading 框
     */
    public void showProgressDialog(String msg) {
        showProgressDialog(true, msg);
    }

    /***
     * 显示程序加载loading 框
     */
    public void showProgressDialog(boolean cancelable) {
        showProgressDialog(cancelable, "");
    }

    /**
     * @param title
     * @param msg
     * @param oklistener
     * @param cancellistener
     */
    protected void showDialogForPrompt(String title, String msg, DialogInterface.OnClickListener oklistener, DialogInterface.OnClickListener cancellistener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false).setPositiveButton(getString(R.string.base_progress_dialog_ok), oklistener);
        if (cancellistener != null) {
            builder.setNegativeButton(getString(R.string.base_progress_dialog_cancel), cancellistener);
        }
        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.show();
    }

    /**
     * @param title
     * @param msg
     */
    protected void showDialogForPrompt(String title, String msg) {
        showDialogForPrompt(title, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void addActivity(Activity activity) {
        myBaseApplication.addActivity(activity);
    }

    /***
     * 退出应用程序
     */
    public void exitApp() {
        String title = getString(R.string.base_progress_dialog_title);
        String msg = getString(R.string.base_exit_app);
        showDialogForPrompt(title, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                myBaseApplication.exit();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    /***
     * 改变通知栏颜色 ****** start
     */
    public void setStateBarColor() {
        int res = R.color.grass_green;
        if (-1 != getStateBarColor()) {
            res = getStateBarColor();
        }
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(res);
    }

    protected int getStateBarColor() {
        return -1;
    }
    /***  改变通知栏颜色 ****** end*/

    /***
     * 改变状态栏颜色***** start
     */
    public void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.grass_green);
        }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    /*** 改变状态栏颜色***** end */

    /**
     * 滑动关闭activity ****** start
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {//SwipeBackActivityBase接口中的方法
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {//SwipeBackActivityBase接口中的方法
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {//SwipeBackActivityBase接口中的方法
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    /**滑动关闭activity ****** end*/

}
