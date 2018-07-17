package kr.co.plani.fitlab.tripko.Search;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.Data.TotalRecordResult;
import kr.co.plani.fitlab.tripko.DetailRouteActivity;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Plan.AddPlanActivity;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Toolbar.OnLeftMenuClickListener;
import kr.co.plani.fitlab.tripko.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    public static final String EXTRA_ATTRACTION_ID = "attraction_id";
    public static final String EXTRA_SEARCH_KEYWORD = "keyword";
    private int attraction_id = -1;

    FamiliarRecyclerView recyclerView;
    SearchResultAdapter mAdapter;
    LinearLayoutManager layoutManager;
    CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out_background);
        attraction_id = getIntent().getIntExtra(EXTRA_ATTRACTION_ID, -1);
        String keyword = getIntent().getStringExtra(EXTRA_SEARCH_KEYWORD);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }

        toolbar.setToolbar(R.drawable.ic_keyboard_arrow_left_white, keyword + getString(R.string.search_result_title), "");

        recyclerView = (FamiliarRecyclerView) findViewById(R.id.recyclerView);

        mAdapter = new SearchResultAdapter();
        layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        toolbar.setOnLeftMenuClickListener(new OnLeftMenuClickListener() {
            @Override
            public void OnLeftMenuClick() {
                finish();
            }
        });

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Intent i = new Intent(SearchResultActivity.this, DetailRouteActivity.class);
                TotalRecordData data = mAdapter.getItemAtPosition(position);
                i.putExtra(DetailRouteActivity.EXTRA_RECORD_DATA, data);
                startActivity(i);
            }
        });

        mAdapter.setOnBtnClickListener(new OnBtnClickListener() {
            @Override
            public void OnBtnClick() {
                startActivity(new Intent(SearchResultActivity.this, AddPlanActivity.class));
            }
        });
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in_background, R.anim.slide_right_out);
    }

    private void initData() {
        if (attraction_id == -1) {
            return;
        }
        Call<TotalRecordResult> call = NetworkManager.getInstance().getService().findRecord(Constants.getAccessToken(), attraction_id);
        call.enqueue(new Callback<TotalRecordResult>() {
            @Override
            public void onResponse(Call<TotalRecordResult> call, Response<TotalRecordResult> response) {
                if (response.isSuccessful()) {
                    TotalRecordResult result = response.body();
                    mAdapter.clear();
                    mAdapter.addAll(result.results);
                } else {
                    // fail
                }
            }

            @Override
            public void onFailure(Call<TotalRecordResult> call, Throwable t) {

            }
        });
    }

}
