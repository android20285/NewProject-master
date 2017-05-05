package com.cn.good.foodingredients;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.cn.good.foodingredients.greendao.dao.DaoMaster;
import com.cn.good.foodingredients.greendao.dao.DaoSession;
import com.cn.tools.MySharedPreference;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by will wu on 2016/10/8.
 */

public class BaseApplication extends Application {

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 4) {
                initDB();
            }
        }
    };

    public static Context myContext = null;
    public static BaseApplication baseApplicition;
    public static MySharedPreference sharedPreference;
    public static DaoMaster daoMaster = null;
    public static DaoSession daoSession = null;
    public static SQLiteDatabase db;
    public static DaoMaster.DevOpenHelper helper;

    /**
     * 用来标记测试还是正式版本
     * <p>
     * true-->测试版本
     * false-->正式发布版本
     */
    public static final boolean isDebug = true;

    /**
     * 记录当前栈里所有activity
     */
    private List<Activity> activities = new ArrayList<Activity>();
    /**
     * 记录需要一次性关闭的页面
     */
    private List<Activity> activitys = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        myContext = getApplicationContext();
        Message msg = handler.obtainMessage();
        msg.what = 4;
        msg.sendToTarget();
    }


    public static BaseApplication getInstance() {
        if (baseApplicition == null) {
            baseApplicition = new BaseApplication();
        }
        return baseApplicition;
    }

    private static void initDB() {
        MultiDex.install(myContext);
        sharedPreference = new MySharedPreference(myContext);
        NoHttp.initialize(myContext, new NoHttp.Config()
                /** 设置全局连接超时时间，单位毫秒*/
                .setConnectTimeout(30 * 1000)
                /** 设置全局服务器响应超时时间，单位毫秒*/
                .setReadTimeout(30 * 1000)
        );
        Logger.setDebug(true);
        Logger.setTag("GoodFoodIngredients");
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;

        helper = new DaoMaster.DevOpenHelper(myContext, "GoodFoodIngredients-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * 新建了一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 给临时Activitys
     * 添加activity
     */
    public void addTemActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 删除临时Activitys 中的某个Activity
     * Activity
     */
    public void finishTemActivity(Activity activity) {
        if (activity != null) {
            this.activitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 退出指定的Activity
     */
    public void exitActivitys() {
        for (Activity activity : activitys) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 应用退出，结束所有的activity
     */
    public void exit() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

}
