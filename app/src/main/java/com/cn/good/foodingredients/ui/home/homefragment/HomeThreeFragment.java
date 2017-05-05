package com.cn.good.foodingredients.ui.home.homefragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.good.foodingredients.R;
import com.cn.baseactivity.BaseFragment;
import com.cn.tools.OtherUtilities;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.task.LocalImageLoader;

import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by will wu on 2016/10/8.
 */

public class HomeThreeFragment extends BaseFragment {

    private Activity myActivity;
    private TextView makePhoto = null;
    private ImageView userPhoto = null;
    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myActivity = getActivity();
        View view = inflater.inflate(R.layout.home_three_fragment, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }


    private void initView(View view) {
        makePhoto = (TextView) view.findViewById(R.id.make_photo);
        userPhoto = (ImageView) view.findViewById(R.id.user_photo);
    }

    private void initData() {

    }

    private void initListener() {
        makePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoto();
            }
        });
    }

    private void makePhoto() {
        /**1. 使用默认风格，并指定选择数量：*/
        /**第一个参数Activity/Fragment； 第二个request_code； 第三个允许选择照片的数量，不填可以无限选择。*/
        /**Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 9);*/
        /**2. 使用默认风格，不指定选择数量：*/
        /**Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO); // 第三个参数不填的话，可以选择无数个。*/
        /**3. 指定风格，并指定选择数量，如果不想限制数量传入Integer.MAX_VALUE;*/
        Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 6/**指定选择数量。*/
                , ContextCompat.getColor(myActivity, R.color.grass_green) /**指定Toolbar的颜色。*/
                , ContextCompat.getColor(myActivity, R.color.grass_green));  /**指定状态栏的颜色。*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                /**判断是否成功。*/
                /**拿到用户选择的图片路径List：*/
                List<String> pathList = Album.parseResult(data);
                setImage(pathList);
            } else if (resultCode == RESULT_CANCELED) {
                /**用户取消选择。*/
                OtherUtilities.showToastText("取消了照片选择！");
            }
        }
    }

    private void setImage(List<String> photoList) {
        for (String url : photoList) {
            LocalImageLoader.getInstance().loadImage(userPhoto, url);
        }
    }
}
