package kr.co.plani.fitlab.tripko;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Dialog.AIProgressDialog;
import kr.co.plani.fitlab.tripko.Plan.AddPlanActivity;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Toolbar.OnLeftMenuClickListener;
import kr.co.plani.fitlab.tripko.Toolbar.OnRightMenuClickListener;

public class DetailAttractionActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    public static final String EXTRA_ATTRACTION_DATA = "attaction";
    public static final String EXTRA_CURATION_COUNT = "count";
    public static final String EXTRA_DETAIL_LINK = "detail_link";
    public static final String EXTRA_IS_CURATION = "is_curation";
    CustomToolbar toolbar;
    WebView webView;
    AttractionData attractionData;
    int peopleCount;
    boolean isCuration = false;
    String detailLink = null;
    ProgressBar progressBar;
    AIProgressDialog progressDialog = null;
    GoogleApiClient mGoogleApiClient;
    MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_attraction);
        overridePendingTransition(R.anim.slide_up, R.anim.null_animation);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressDialog = new AIProgressDialog();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        attractionData = (AttractionData) getIntent().getSerializableExtra(EXTRA_ATTRACTION_DATA);

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, mMapFragment);
        fragmentTransaction.commit();

        mMapFragment.getMapAsync(this);
        //
//        mGoogleApiClient = new GoogleApiClient.Builder(DetailAttractionActivity.this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(DetailAttractionActivity.this, DetailAttractionActivity.this)
//                .build();
//        Places.GeoDataApi.getPlaceById(mGoogleApiClient, String.valueOf(attractionData.place_id))
//                .setResultCallback(new ResultCallback<PlaceBuffer>() {
//                    @Override
//                    public void onResult(@NonNull PlaceBuffer places) {
//                        if (places.getStatus().isSuccess() && places.getCount() >=0){
//                            final Place myPlace = places.get(0);
//                            Log.e("DetailAttraction", "place found"+myPlace.getName());
//                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                            builder.setLatLngBounds(myPlace.getViewport());
//                            try {
//                                startActivityForResult(builder.build(DetailAttractionActivity.this),1);
//                            } catch (GooglePlayServicesRepairableException e) {
//                                e.printStackTrace();
//                            } catch (GooglePlayServicesNotAvailableException e) {
//                                e.printStackTrace();
//                            }
//                        }else{
//                            Log.e("DetailAttration", "Place not found");
//                        }
//                        places.release();
//                    }
//                });
        peopleCount = getIntent().getIntExtra(EXTRA_CURATION_COUNT, 0);
        isCuration = getIntent().getBooleanExtra(EXTRA_IS_CURATION, false);
        detailLink = getIntent().getStringExtra(EXTRA_DETAIL_LINK);

        if (!isCuration) {
            if (detailLink != null) {
                toolbar.setToolbar(R.drawable.ic_close, getString(R.string.detail_attraction_title), "");
            } else {
                toolbar.setToolbar(R.drawable.ic_close, getString(R.string.add_attraction_title), "Save");
            }
        } else {
            //count add
            String count = "";
            if (peopleCount != 0) count = String.valueOf(peopleCount);
            toolbar.setToolbar(R.drawable.ic_close, getString(R.string.curation_title, count), "Add");
        }

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return false;
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                progressBar.setVisibility(View.VISIBLE);
                if (isCuration && !progressDialog.isAdded()) {
                    Bundle args = new Bundle();
                    args.putInt(AIProgressDialog.EXTRA_TYPE, AIProgressDialog.EXTRA_ATTRACTIONS_LOAD);
                    progressDialog.setArguments(args);
                    progressDialog.show(getSupportFragmentManager(), "dialog");
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                progressBar.setVisibility(View.GONE);
                if (isCuration && progressDialog.isAdded()) {
                    progressDialog.dismiss();
                }
                super.onPageFinished(view, url);
            }
        });
        if (detailLink != null) {
            webView.loadUrl(detailLink);
        } else {
            if (attractionData.link != null) {
                webView.loadUrl(attractionData.link);
            }
        }

        toolbar.setOnRightMenuClickListener(new OnRightMenuClickListener() {
            @Override
            public void OnRightMenuClick() {
                if (attractionData != null) {
                    Intent i = new Intent();
                    i.putExtra(AddPlanActivity.RESULT_EXTRA_DATA, attractionData);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

        toolbar.setOnLeftMenuClickListener(new OnLeftMenuClickListener() {
            @Override
            public void OnLeftMenuClick() {
                finish();
            }
        });
    }

    GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(attractionData.latitude, attractionData.longitude);
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(latLng).title(attractionData.name).icon(getMarkerIcon()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

    }

    public BitmapDescriptor getMarkerIcon() {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor("#fec804"), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.null_animation, R.anim.slide_down);
    }
}
