package com.cn.baseactivity;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cn.good.foodingredients.BaseApplication;
import com.cn.progressdialog.BaseProgressDialog;


/**
 * Created by will wu on 2016/10/11.
 */

public abstract class BaseFragment extends Fragment {

    private BaseApplication myBaseApplication = null;
    private BaseProgressDialog mProgressDialog = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    /**
     * @author Will.Wu
     */

    private void initView() {

    }

    private void initData() {
        myBaseApplication = BaseApplication.getInstance();
    }


    public void exitApp() {
        myBaseApplication.exit();
    }

    public void showProgressDialog(Activity activity, BaseProgressDialog.OnCancelListener cancelListener, boolean cancelable, String msg) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        }
        mProgressDialog = new BaseProgressDialog(activity, msg);
        if (cancelListener != null) {
            mProgressDialog.setOnCancelListener(cancelListener);
        }
        mProgressDialog.setCancelable(cancelable);
        mProgressDialog.show();
    }

    public void showProgressDialog(Activity activity, boolean cancelable, String msg) {
        showProgressDialog(activity, null, cancelable, msg);
    }

    public void showProgressDialog(Activity activity, boolean cancelable) {
        showProgressDialog(activity, cancelable, "");
    }

    public void stopProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.stop();
        }
        mProgressDialog = null;
    }
}
