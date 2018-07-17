package kr.co.plani.fitlab.tripko;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class AndroidWebInterface {
    private final static String Tag = "WebAppInterface";
    Context mContext;
    WebView mWebView;

    AndroidWebInterface(Context c, WebView wv) {
        mContext = c;
        mWebView = wv;
    }

    @JavascriptInterface
    public void showDetailAttraction(String link) {
        Intent i = new Intent(mContext, DetailAttractionActivity.class);
        i.putExtra(DetailAttractionActivity.EXTRA_DETAIL_LINK, link);
        mContext.startActivity(i);
    }
}
