package com.cn.baseactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.cn.good.foodingredients.R;
import com.cn.widget.ProgressWebView;


/***
 * 通用的webview Activity
 * 1、可以有自定义的title，也可以没有，通过useComTitleLayout()来实现
 * 2、可以Reload，也可以不reload；
 * 3、可以进行js交互，也可以不进行JS交互，还可对JS交互进行扩展
 */
public class ComWebViewActivity extends BaseActivity {

    protected ProgressWebView webView;
    public static String HAS_TITLE = "intent.key.has.title"; // true 代表使用通用标题栏
    public static String TITLE = "intent.key.web.title";
    public static String RELOAD_FLAG = "intent.key.web.action.reload";
    public static String WEB_URL = "intent.key.web.url";
    public static String HAS_RIGHT_BTN = "intent.key.has.right.btn";

    private boolean hasTitle = false;
    private String title;
    private boolean reload = false;
    private String webUrl;
    private boolean hasRightBtn;//标题栏右侧是否显示图标
    public static String SHARE_CONTENT = "intent.key.share.content";
    public static String SHARE_TITLE = "intent.key.share.title";
    public static String SHARE_URL = "intent.key.share.url";
    public static String SHARE_ICON_NAME = "intent.key.share.icon.name";
    private String content;
    private String shareTitle;
    private String shareUrl;
    private String shareIconName;

    private Button leftButton = null;
    private Button rightButton = null;
    private TextView titleText = null;
    //使用DEMO 函数
//	public void goToWebView(String url,String title,String shareTitle,String content,String shareUrl,String shareIconName) {
//		Intent inten = new Intent(mContext, ComWebViewActivity.class);
//		inten.putExtra(ComWebViewActivity.HAS_TITLE, true);
//		inten.putExtra(ComWebViewActivity.TITLE, title);
//		inten.putExtra(ComWebViewActivity.WEB_URL, url);
//		inten.putExtra(ComWebViewActivity.HAS_RIGHT_BTN,true);
//		inten.putExtra(ComWebViewActivity.SHARE_TITLE,shareTitle);
//		inten.putExtra(ComWebViewActivity.SHARE_CONTENT,content);
//		inten.putExtra(ComWebViewActivity.SHARE_URL,shareUrl);
//		inten.putExtra(ComWebViewActivity.SHARE_ICON_NAME,shareIconName);
//		startActivity(inten);
//	}


    @Override
    protected void init() {
        preInitIntent();
        setContentView(R.layout.com_webview_activity);
        initView();
        initData();
        initTitleEvent();
    }

    private void preInitIntent() {
        Intent intent = getIntent();
        hasTitle = intent.getBooleanExtra(HAS_TITLE, false);
        title = intent.getStringExtra(TITLE);
        content = intent.getStringExtra(SHARE_CONTENT);
        shareTitle = intent.getStringExtra(SHARE_TITLE);
        shareUrl = intent.getStringExtra(SHARE_URL);
        shareIconName = intent.getStringExtra(SHARE_ICON_NAME);
        reload = intent.getBooleanExtra(RELOAD_FLAG, false);
        webUrl = intent.getStringExtra(WEB_URL);
        hasRightBtn = intent.getBooleanExtra(HAS_RIGHT_BTN, false);
    }


    protected boolean useComTitleLayout() {
        return hasTitle;
    }

    /**
     * 初始化view
     */
    protected void initView() {
        webView = (ProgressWebView) findViewById(R.id.pgwebview);
        titleText = (TextView) findViewById(android.R.id.title);
        leftButton = (Button) findViewById(android.R.id.button1);
        rightButton = (Button) findViewById(android.R.id.button3);
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void initData() {
        WebSettings wsettings = webView.getSettings();
        wsettings.setJavaScriptEnabled(true);
        wsettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new MyWebViewClient());
        addJavascript();
        webView.loadUrl("http://blog.csdn.net/scry5566/article/details/51671919");
    }

    @Override
    protected void initListener() {

    }


    /**
     * 初始化title
     */
    private void initTitleEvent() {
        if (useComTitleLayout()) {
            leftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        ComWebViewActivity.this.finish();
                    }
                }
            });
        }
        if (hasRightBtn) {
            rightButton.setVisibility(View.VISIBLE);
            rightButton.setBackgroundResource(R.mipmap.share);
            rightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (OtherUtilities.saveAssetsPictureToSD(shareIconName) == true) {
//                        //share(shareUrl,shareTitle,content, HttpWebUrl.getShareIconUrl());
//                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (reload) {
            webView.reload();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 每次返回的时候，返回到上一个页面中。
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (useComTitleLayout()) {
                String titleFromWebview = view.getTitle();
                if (!TextUtils.isEmpty(titleFromWebview)) {
                    titleText.setText(titleFromWebview);
                } else if (!TextUtils.isEmpty(title)) {
                    titleText.setText(title);
                } else {
                    titleText.setText("");
                }
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    //////////////////////////////// 可以重写该类实现不同js调用
    public class WebAppInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void logout() {
            ComWebViewActivity.this.finish();
        }

        @JavascriptInterface
        public void logoutRefresh() {
            setResult(RESULT_OK);
            ComWebViewActivity.this.finish();
        }

        @JavascriptInterface
        public void gotoClient() {
            setResult(RESULT_OK);
            ComWebViewActivity.this.finish();
        }


        @JavascriptInterface
        public void gotoClinicserver() {//预约服务
            ComWebViewActivity.this.finish();
        }


        @JavascriptInterface
        public void go_to_yixin() {
            ComWebViewActivity.this.finish();
        }
    }

    /**
     * 添加js的接口
     * 默认调用WebAppInterface的JS接口；
     * 如果不需要JS通讯或者JS接口不一样，可以扩展class WebAppInterface，同时，重写addJavascript
     */
    protected void addJavascript() {
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
    }
}
