package com.cn.nohttp;

import com.cn.good.foodingredients.BaseApplication;

/**
 * Created by Administrator on 2016/12/1.
 */

public  interface HttpApi {
    /**
     * 使用YiXinApp.isDebug来标记是测试还是正式的
     * {@link BaseApplication}
     *
     */
    /*使用同一的开关来标识测试还是正式
    * :左侧为测试网
    * :右侧为正式网
    * */
    String unionPayMode = BaseApplication.isDebug ? "01" : "00";
    String ROOT_HOST_NAME = BaseApplication.isDebug ? "http://210.14.72.52" : "http://firstaid.skyhospital.net";
    String URL_BASE = BaseApplication.isDebug ? "/firstaid/1.0/" : "/firstaid/1.0/";
    String URL_WEB = BaseApplication.isDebug ? "/shopweb/index.php/" : "/shopweb/index.php/";

    /**
     * 用户登录
     */
    String lOGIN_URL = ROOT_HOST_NAME + URL_BASE + "login.php";

}
