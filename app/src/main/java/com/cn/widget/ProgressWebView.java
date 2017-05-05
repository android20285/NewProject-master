package com.cn.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.cn.good.foodingredients.R;

@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar_web_bg));
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                context.getResources().getDisplayMetrics());
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height, 0, 0));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setProgress(newProgress);
                mPWebHandler.sendEmptyMessageDelayed(1, 200);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    private Handler mPWebHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (progressbar != null && progressbar.getVisibility() == VISIBLE) {
                progressbar.setVisibility(GONE);
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }


}
