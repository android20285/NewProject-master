package com.cn.tools;

/**
 * Created by will wu on 2016/7/26.
 */

public class Constants {
    /**
     * 根目录
     */
    public static final String ROOT_PATH = String.format("%s%s", SDCardUtils.getESDString(), "/CoreLibraryWill/");
    /**
     * 日志目录
     */
    public static final String LOG_PATH = String.format("%slog/", ROOT_PATH);
    /**
     * 头像目录
     */
    public static final String AVATAR_PATH = String.format("%savatar/", ROOT_PATH);
    /**
     * 临时文件存放的目录
     */
    public static final String TEMP_FILE_PATH = String.format("%stemp/", ROOT_PATH);
    /**
     * 日志过期时间，默认为10天
     */
    public static final int LOG_EXPIRED_TIME = 10;
}
