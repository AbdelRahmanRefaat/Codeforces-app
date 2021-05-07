package com.example.codeforces.ui;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.codeforces.R;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";

    public static final String EXTRA_PROBLEM_REQUEST_CODE =
            "com.example.codeforces.ui.EXTRA_PROBLEM_REQUEST_CODE";
    public static final String EXTRA_CONTEST_REQUEST_CODE =
            "com.example.codeforces.ui.EXTRA_CONTEST_REQUEST_CODE";

    public static final String EXTRA_CONTEST_NAME =
            "com.example.codeforces.ui.EXTRA_CONTEST_NAME";

    public static final String EXTRA_PROBLEM_NAME =
            "com.example.codeforces.ui.EXTRA_PROBLEM_NAME";

    public static final String EXTRA_PROBLEM_INDEX =
            "com.example.codeforces.ui.EXTRA_PROBLEM_INDEX";

    public static final String EXTRA_PROBLEM_CONTEST_ID =
            "com.example.codeforces.ui.EXTRA_PROBLEM_CONTEST_ID";

    public static final int CONTEST_REQUEST_CODE = 1;
    public static final int PROBLEM_REQUEST_CODE = 2;

    public static final String EXTRA_REQUEST_CODE =
            "EXTRA_REQUEST_CODE";
    private WebView mWebView;

    @Override
    public void onBackPressed() {

        if(mWebView.canGoBack()) {

            mWebView.goBack();

        }else {

            super.onBackPressed();

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_web_view);


        String url = getIntent().getExtras().getString("url");

        mWebView = (WebView) findViewById(R.id.web_view);

        int requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, -1);
        Log.d(TAG, "onCreate: RequestCode is = " + requestCode);
        if(requestCode == -1)
            return ;

        if(requestCode == PROBLEM_REQUEST_CODE) {

            String index = getIntent().getStringExtra(EXTRA_PROBLEM_INDEX);
            String name = getIntent().getStringExtra(EXTRA_PROBLEM_NAME);
            int contestId = getIntent().getIntExtra(EXTRA_PROBLEM_CONTEST_ID,-1);
            setTitle("" + contestId + index + "." + name);

        }else if(requestCode == CONTEST_REQUEST_CODE){

            String contestName = getIntent().getStringExtra(EXTRA_CONTEST_NAME);
            setTitle(contestName);

        }

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(url);


    }
}
