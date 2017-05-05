package com.cn.nohttp;

import com.alibaba.fastjson.JSON;
import com.cn.tools.LogUtil;
import com.cn.tools.OtherUtilities;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.rest.StringRequest;

/**
 * Created by Will Wu on 2016/7/27.
 */

public class BeanJsonRequest<E> extends RestRequest<E> {
    private String TAG = BeanJsonRequest.class.getSimpleName();

    private Class<E> clazz;

    public BeanJsonRequest(String url, RequestMethod requestMethod, Class<E> clazz) {
        super(url, requestMethod);
        this.clazz = clazz;
        setAccept(Headers.HEAD_VALUE_ACCEPT_APPLICATION_JSON);
    }

    @Override
    public E parseResponse(Headers responseHeaders, byte[] responseBody) throws Throwable {
        String string = StringRequest.parseResponseString(responseHeaders, responseBody);
        try {
            LogUtil.e(TAG, "string=" + OtherUtilities.gBDecodeUnicode(string));
            return JSON.parseObject(OtherUtilities.gBDecodeUnicode(string), clazz);
        } catch (Exception e) {
            E instance = null;
            try {
                /**服务端返回数据格式错误时，返回一个空构造*/
                /**但是前提是传进来的JavaBean必须提供了默认实现*/
                instance = clazz.newInstance();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return instance;
        }
    }
}
