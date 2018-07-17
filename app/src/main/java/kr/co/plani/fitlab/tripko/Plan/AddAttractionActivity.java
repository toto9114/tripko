package kr.co.plani.fitlab.tripko.Plan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import kr.co.plani.fitlab.tripko.AutoComplete.AutoCompleteAdapter;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Data.AutoCompleteData;
import kr.co.plani.fitlab.tripko.DetailAttractionActivity;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Toolbar.OnLeftMenuClickListener;
import kr.co.plani.fitlab.tripko.Toolbar.OnRightMenuClickListener;
import kr.co.plani.fitlab.tripko.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAttractionActivity extends AppCompatActivity {
    private static final int REQUEST_ATTRACTION = 100;

    CustomToolbar toolbar;
    EditText searchView;
    ListView listView;
    AutoCompleteAdapter mAdapter;

    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attraction);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out_background);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        searchView = (EditText) findViewById(R.id.edit_search);
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new AutoCompleteAdapter();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        toolbar.setToolbar(R.drawable.ic_keyboard_arrow_left_white, getString(R.string.add_attraction_title), "Save");

        listView.setAdapter(mAdapter);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    String keyword = s.toString();
                    Call<AutoCompleteData> call = NetworkManager.getInstance().getService().getAutoText(Constants.getAccessToken(), keyword);
                    call.enqueue(new Callback<AutoCompleteData>() {
                        @Override
                        public void onResponse(Call<AutoCompleteData> call, Response<AutoCompleteData> response) {
                            if (response.isSuccessful()) {
                                AutoCompleteData result = response.body();
                                mAdapter.clear();
                                mAdapter.addAll(result.result);
                            }
                        }

                        @Override
                        public void onFailure(Call<AutoCompleteData> call, Throwable t) {

                        }
                    });

                } else {
                    mAdapter.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AttractionData data = mAdapter.getItemAtPosition(position);
                Intent i = new Intent(AddAttractionActivity.this, DetailAttractionActivity.class);
                i.putExtra(DetailAttractionActivity.EXTRA_ATTRACTION_DATA, data);
                startActivityForResult(i, REQUEST_ATTRACTION);
            }
        });

        toolbar.setOnRightMenuClickListener(new OnRightMenuClickListener() {
            @Override
            public void OnRightMenuClick() {
                //save
                String keyword = searchView.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    dialog = new ProgressDialog(AddAttractionActivity.this);
                    dialog.setMessage("Creating..");
                    dialog.setCancelable(false);
                    dialog.show();
                    Call<AttractionData> call = NetworkManager.getInstance().getService().checkAttraction(Constants.getAccessToken(), keyword);
                    call.enqueue(new Callback<AttractionData>() {
                        @Override
                        public void onResponse(Call<AttractionData> call, Response<AttractionData> response) {
                            if (response.isSuccessful()) {
                                AttractionData data = response.body();
                                Intent i = new Intent();
                                i.putExtra(AddPlanActivity.RESULT_EXTRA_DATA, data);
                                setResult(RESULT_OK, i);
                                dialog.dismiss();
                                finish();
                            } else {
                                //fail
                                Toast.makeText(AddAttractionActivity.this, R.string.alert_not_found_attractions, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<AttractionData> call, Throwable t) {
                            dialog.dismiss();
                        }
                    });
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in_background, R.anim.slide_right_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ATTRACTION && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
