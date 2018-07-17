package kr.co.plani.fitlab.tripko;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Tutorial.TutorialActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRouteActivity extends AppCompatActivity {
    public static final String EXTRA_RECORD_DATA = "record";
    public static final String EXTRA_IS_SAMPLE = "sample";

    WebView webView;
    TotalRecordData recordData;
    //    String detailLink;
    AppBarLayout appBarLayout;
    FrameLayout rightMenu, leftMenu;
    boolean isSample = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_route);
//        detailLink = getIntent().getStringExtra(EXTRA_RECORD_DATA);
        overridePendingTransition(R.anim.slide_up, R.anim.null_animation);
        recordData = (TotalRecordData) getIntent().getSerializableExtra("record");
        isSample = getIntent().getBooleanExtra(EXTRA_IS_SAMPLE, false);
        if(isSample) {
            Intent intent = new Intent(this, TutorialActivity.class);
            intent.putExtra(TutorialActivity.EXTRA_TUTORIAL_TYPE, TutorialActivity.TYPE_SHARE_RECORD);
            startActivity(intent);
        }

        webView = (WebView) findViewById(R.id.webView);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        leftMenu = (FrameLayout) findViewById(R.id.left_menu);
        rightMenu = (FrameLayout) findViewById(R.id.right_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarLayout.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }

        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webView.addJavascriptInterface(new AndroidWebInterface(DetailRouteActivity.this, webView), "Android");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient
                (
                        new WebChromeClient() {
                            //alter process
                            @Override
                            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                                new AlertDialog.Builder(DetailRouteActivity.this)
                                        .setTitle("Alert")
                                        .setMessage(message)
                                        .setPositiveButton(android.R.string.ok,
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        result.confirm();
                                                    }
                                                })
                                        .setCancelable(false)
                                        .create()
                                        .show();

                                return true;
                            }

                            //confirm process
                            @Override
                            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Confirm")
                                        .setMessage(message)
                                        .setPositiveButton("네",
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        result.confirm();
                                                    }
                                                })
                                        .setNegativeButton("아니오",
                                                new AlertDialog.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        result.cancel();
                                                    }
                                                })
                                        .setCancelable(false)
                                        .create()
                                        .show();

                                return true;
                            }
                        }
                );
        webView.loadUrl(recordData.link + "&is_webview=1");
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyRecord();
            }
        });
//        toolbar.setOnLeftMenuClickListener(new OnLeftMenuClickListener() {
//            @Override
//            public void OnLeftMenuClick() {
//
//            }
//        });
//        toolbar.setOnRightMenuClickListener(new OnRightMenuClickListener() {
//            @Override
//            public void OnRightMenuClick() {
//
//            }
//        });
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
        overridePendingTransition(R.anim.null_animation, R.anim.slide_down);
    }

    private void copyRecord() {
        AlertDialog dialog = new AlertDialog.Builder(DetailRouteActivity.this)
                .setTitle(R.string.alert_copy_record_title)
                .setMessage(String.format(getString(R.string.alert_copy_record_message), recordData.count))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int recordId = recordData.id;
                        Call<ResponseBody> call = NetworkManager.getInstance().getService().copyRecord(Constants.getAccessToken(), recordId);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(DetailRouteActivity.this, "Copy success", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
