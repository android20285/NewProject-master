package com.cn.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 网络状态工具类
 *
 * @author 续写经典
 * @version 1.0 2015/11/3
 */
public final class NetworkUtil {

    private static String TAG = NetworkUtil.class.getSimpleName();
    public static final int NONE = 0x00;
    public static final int WIFI = 0x01;
    public static final int CMWAP = 0x02;
    public static final int CMNET = 0x03;

    private NetworkUtil() {
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                    if (ping()) {
                        return true;
//                    } else {
//                        return false;
//                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前的网络连接类型 0：没有网络 1：WIFI网络2：wap 网络3：net网络
     */
    public static int getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NONE;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                return CMNET;
            } else {
                return CMWAP;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return WIFI;
        }
        return NONE;
    }

    /**
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     */
    public static final boolean ping() {
        String result = null;
        try {
            /**ping 的地址，可以换成任何一种可靠的外网 */
            String ip = "www.baidu.com";
            /**ping网址3次 */
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
            /**读取ping的内容，可以不加 */
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            LogUtil.e(TAG, "ping result content : " + stringBuffer.toString());
            /**ping的状态*/
            int status = p.waitFor();
            if (status == 0) {
                result = "success";
                return true;
            } else {
                result = "failed";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LogUtil.e(TAG, result);
        }
        return false;
    }
}
