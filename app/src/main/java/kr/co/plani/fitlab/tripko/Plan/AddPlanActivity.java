package kr.co.plani.fitlab.tripko.Plan;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Data.CurationResult;
import kr.co.plani.fitlab.tripko.Data.FromToData;
import kr.co.plani.fitlab.tripko.Data.PlanData;
import kr.co.plani.fitlab.tripko.Data.RecordData;
import kr.co.plani.fitlab.tripko.Data.RouteData;
import kr.co.plani.fitlab.tripko.DetailAttractionActivity;
import kr.co.plani.fitlab.tripko.Dialog.AIProgressDialog;
import kr.co.plani.fitlab.tripko.Dialog.CustomProgressDialog;
import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.MyRecord.RecordCreatedEvent;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Toolbar.OnDeleteMenuClickListener;
import kr.co.plani.fitlab.tripko.Toolbar.OnLeftMenuClickListener;
import kr.co.plani.fitlab.tripko.Toolbar.OnRightMenuClickListener;
import kr.co.plani.fitlab.tripko.Toolbar.OnShareBtnClickListener;
import kr.co.plani.fitlab.tripko.Tutorial.TutorialActivity;
import kr.co.plani.fitlab.tripko.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlanActivity extends AppCompatActivity {
    private static final int REQUEST_ATTRACTION = 100;

    public static final String EXTRA_RECORD_DATA = "myrecord";
    public static final String RESULT_EXTRA_DATA = "attraction";

    public static final String EXTRA_IS_TUTORIAL = "tutorial";

    boolean isTutorial = false;
    CustomToolbar toolbar;
    FrameLayout addPlanBtn, addPlanBtnBottom;
    LinearLayout introContainer;
    RelativeLayout contentContainer;
    FamiliarRecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecordPlanAdapter mAdapter;
    CustomProgressDialog progressDialog;
    RecordData recordData = null;
    AIProgressDialog aiProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        isTutorial = getIntent().getBooleanExtra(EXTRA_IS_TUTORIAL, false);
        if (isTutorial) startActivity(new Intent(AddPlanActivity.this, TutorialActivity.class).putExtra(TutorialActivity.EXTRA_TUTORIAL_TYPE, TutorialActivity.TYPE_ADD_PLAN));
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        introContainer = (LinearLayout) findViewById(R.id.intro_container);
        contentContainer = (RelativeLayout) findViewById(R.id.content_container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        progressDialog = new CustomProgressDialog();
        aiProgressDialog = new AIProgressDialog();
        recordData = (RecordData) getIntent().getSerializableExtra(EXTRA_RECORD_DATA);
        if (recordData != null) overridePendingTransition(R.anim.slide_up, R.anim.null_animation);
        else overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out_background);
        recyclerView = (FamiliarRecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new RecordPlanAdapter();
        layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        addPlanBtn = (FrameLayout) findViewById(R.id.add_plan_btn);
        addPlanBtnBottom = (FrameLayout) findViewById(R.id.add_plan_btn_bottom);
        addPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddPlanActivity.this, AddAttractionActivity.class), REQUEST_ATTRACTION);
            }
        });
        addPlanBtnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddPlanActivity.this, AddAttractionActivity.class), REQUEST_ATTRACTION);
            }
        });

        toolbar.setOnRightMenuClickListener(new OnRightMenuClickListener() {
            @Override
            public void OnRightMenuClick() {
                if (mAdapter.getItemCount() == 0) return;
//                if (progressLayout.getVisibility() == View.VISIBLE) return;

//                progressLayout.setVisibility(View.VISIBLE);
                if (!aiProgressDialog.isAdded()) {
                    Bundle args = new Bundle();
                    args.putInt(AIProgressDialog.EXTRA_TYPE, AIProgressDialog.EXTRA_ROUTE_UPLOAD);
                    aiProgressDialog.setArguments(args);
                    aiProgressDialog.show(getSupportFragmentManager(), "dialog");
                }
                RouteData routeData = new RouteData();
                List<String> nameList = new ArrayList<String>();
                for (int i = 0; i < mAdapter.getItemCount(); i++) {
                    PlanData data = mAdapter.getItemAtPosition(i);
                    if (data instanceof AttractionData) {
                        nameList.add(((AttractionData) data).name);
                    }
                }
                routeData.route = nameList;
                routeData.distance_matrix = mAdapter.getFromToDatas();
                if (recordData != null) {
                    updateRecord(routeData);
                } else {
                    uploadRecord(routeData);
                }

            }
        });

        toolbar.setOnLeftMenuClickListener(new OnLeftMenuClickListener() {
            @Override
            public void OnLeftMenuClick() {
                finish();
            }
        });
        toolbar.setOnDeleteMenuClickListener(new OnDeleteMenuClickListener() {
            @Override
            public void OnDeleteMenuClick() {
                AlertDialog dialog = new AlertDialog.Builder(AddPlanActivity.this)
                        .setTitle(R.string.delete_btn)
                        .setMessage(R.string.alert_delete_message)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog.show(getSupportFragmentManager(), "dialog");
                                deleteRecord();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
        toolbar.setOnShareBtnClickListener(new OnShareBtnClickListener() {
            @Override
            public void OnShareBtnClick() {
                share();
            }
        });
        initData();
    }

    @Override
    public void finish() {
        super.finish();
        if (recordData != null) overridePendingTransition(R.anim.null_animation, R.anim.slide_down);
        else overridePendingTransition(R.anim.slide_left_in_background, R.anim.slide_right_out);
    }

    private void initData() {
        if (recordData != null) {
            toolbar.setEditMode(R.drawable.ic_close, getString(R.string.planing_title));
            addPlanBtnBottom.setVisibility(View.GONE);
            introContainer.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);
            FromToData emptyData = new FromToData();

            mAdapter.add(emptyData);
            for (int i = 0; i < recordData.route.size(); i++) {
                mAdapter.add(recordData.route.get(i));
                if (i < recordData.route.size() - 1) {
                    FromToData fromToData = new FromToData();
                    if (recordData.distance_matrix.size() != 0) {
                        fromToData.distance = recordData.distance_matrix.get(i).distance;
                        fromToData.total_time = recordData.distance_matrix.get(i).total_time;
                        fromToData.count = recordData.distance_matrix.get(i).count;
                    }
                    mAdapter.add(fromToData);
                }
            }
        } else {
            toolbar.setToolbar(R.drawable.ic_keyboard_arrow_left_white, getString(R.string.planing_title), "Save");
        }
    }

    private void share() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_STREAM, recordData.link);
        i.putExtra(Intent.EXTRA_TEXT, recordData.link);
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.curation_share_content));
        startActivity(i);
    }

    private void deleteRecord() {
        List<Integer> idList = new ArrayList<>();
        idList.add(recordData.id);
        Call<ResponseBody> call = NetworkManager.getInstance().getService().deleteRecords(Constants.getAccessToken(), idList);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    BusProvider.getInstance().post(new RecordCreatedEvent(true));
                    progressDialog.dismiss();
                    finish();
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void uploadRecord(RouteData routeData) {

        Call<RecordData> call = NetworkManager.getInstance().getService().uploadRoute(Constants.getAccessToken(), routeData);
        call.enqueue(new Callback<RecordData>() {
            @Override
            public void onResponse(Call<RecordData> call, Response<RecordData> response) {
                if (response.isSuccessful()) {
                    RecordData data = response.body();
                    Toast.makeText(AddPlanActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    BusProvider.getInstance().post(new RecordCreatedEvent(true));
                    finish();
                }
//                progressLayout.setVisibility(View.GONE);
                if (aiProgressDialog.isAdded())
                    aiProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RecordData> call, Throwable t) {
//                progressLayout.setVisibility(View.GONE);
                if (aiProgressDialog.isAdded())
                    aiProgressDialog.dismiss();
            }
        });
    }

    private void updateRecord(RouteData routeData) {
        Call<RecordData> call = NetworkManager.getInstance().getService().updateRecord(Constants.getAccessToken(), recordData.id, routeData);
        call.enqueue(new Callback<RecordData>() {
            @Override
            public void onResponse(Call<RecordData> call, Response<RecordData> response) {
                if (response.isSuccessful()) {
                    RecordData data = response.body();
                    Toast.makeText(AddPlanActivity.this, "Success" + data.id, Toast.LENGTH_SHORT).show();
                    BusProvider.getInstance().post(new RecordCreatedEvent(true));
                    finish();
                }
//                progressLayout.setVisibility(View.GONE);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RecordData> call, Throwable t) {
//                progressLayout.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ATTRACTION && resultCode == RESULT_OK) {
            AttractionData att = (AttractionData) data.getSerializableExtra(RESULT_EXTRA_DATA);
            contentContainer.setVisibility(View.VISIBLE);
            introContainer.setVisibility(View.GONE);
            FromToData fromToData = new FromToData();

            mAdapter.add(fromToData);
            mAdapter.add(att);

            if (mAdapter.getItemCount() > 3) {
                int currentPosition = mAdapter.getItemCount() - 1;
                AttractionData currentData = (AttractionData) mAdapter.getItemAtPosition(currentPosition);
                AttractionData beforeData = (AttractionData) mAdapter.getItemAtPosition(currentPosition - 2);
                getDistance(beforeData, currentData);
            }
            Call<CurationResult> call = NetworkManager.getInstance().getService().getCuration(Constants.getAccessToken(), att.name);
            call.enqueue(new Callback<CurationResult>() {
                @Override
                public void onResponse(Call<CurationResult> call, Response<CurationResult> response) {
                    if (response.isSuccessful()) {
                        CurationResult data = response.body();
                        if (data != null) {
                            Intent i = new Intent(AddPlanActivity.this, DetailAttractionActivity.class);
                            i.putExtra(DetailAttractionActivity.EXTRA_ATTRACTION_DATA, data.attractionData);
                            i.putExtra(DetailAttractionActivity.EXTRA_CURATION_COUNT, data.count);
                            i.putExtra(DetailAttractionActivity.EXTRA_IS_CURATION, true);
                            startActivityForResult(i, REQUEST_ATTRACTION);
                        }
                    }
                }

                @Override
                public void onFailure(Call<CurationResult> call, Throwable t) {

                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getDistance(AttractionData origins, AttractionData destinations) {
        Call<FromToData> call = NetworkManager.getInstance().getService().getDistance(Constants.getAccessToken(), origins.id, destinations.id);
        call.enqueue(new Callback<FromToData>() {
            @Override
            public void onResponse(Call<FromToData> call, Response<FromToData> response) {
                if (response.isSuccessful()) {
                    FromToData fromToData = response.body();
                    int currentPosition = mAdapter.getItemCount() - 1;
                    mAdapter.setDistance(currentPosition - 1, fromToData);
                }
            }

            @Override
            public void onFailure(Call<FromToData> call, Throwable t) {

            }
        });
    }
}
