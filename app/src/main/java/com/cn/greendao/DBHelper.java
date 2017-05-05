package com.cn.greendao;

import com.cn.good.foodingredients.BaseApplication;
import com.cn.good.foodingredients.greendao.UserInfo;
import com.cn.good.foodingredients.greendao.dao.UserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */

public class DBHelper {
    /**
     * 通过 BaseApplication 类提供的 getDaoSession() 获取具体 Dao
     *
     * @return
     */
    private static UserInfoDao userInfoDao = BaseApplication.getInstance().getDaoSession().getUserInfoDao();

    /**
     * 插入一条数据
     */
    public static void insetPersonalInfo(UserInfo userInfo) {
        userInfoDao.insertOrReplace(userInfo);
    }

    /**
     * 查询单条数据
     */
    public static UserInfo getPersonalInfo(String currUid) {
        QueryBuilder<UserInfo> queryBuilder = userInfoDao.queryBuilder().where(UserInfoDao.Properties.Uid.eq(currUid));
        List<UserInfo> listData = queryBuilder.list();
        UserInfo piInfo = null;
        if (null != listData && listData.size() > 0) {
            return listData.get(0);
        } else {
            return piInfo;
        }
    }

    /**
     * 删除一条数据
     */
    public static synchronized void deletePersonalInfo(UserInfo userInfo) {
        QueryBuilder<UserInfo> queryBuilder = userInfoDao.queryBuilder().where(UserInfoDao.Properties.Uid.eq(userInfo.getUid()));
        List<UserInfo> listData = queryBuilder.list();
        if (listData != null && listData.size() > 0) {
            for (UserInfo info : listData) {
                userInfoDao.delete(info);
            }
        }
    }

    /**
     * 更新一条数据
     */
    public static synchronized void updatePersonalInfo(UserInfo userInfo) {
        userInfoDao.update(userInfo);
    }

}
