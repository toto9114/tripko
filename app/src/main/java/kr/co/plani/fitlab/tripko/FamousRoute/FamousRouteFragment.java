package kr.co.plani.fitlab.tripko.FamousRoute;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import kr.co.plani.fitlab.tripko.AutoComplete.AutoCompleteAdapter;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Data.AutoCompleteData;
import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.Data.TotalRecordResult;
import kr.co.plani.fitlab.tripko.DetailRouteActivity;
import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Search.SearchResultActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamousRouteFragment extends Fragment {


    public FamousRouteFragment() {
        // Required empty public constructor
    }

    LinearLayout hintView;
    FamiliarRecyclerView recyclerView;
    FamousRouteAdapter mAdapter;
    LinearLayoutManager layoutManager;
    EditText searchView;
    AutoCompleteAdapter autoCompleteAdapter;
    //    ArrayAdapter<AttractionData> autoCompleteAdapter;
    ListPopupWindow popupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_famous_route, container, false);
        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.recycler);
        searchView = (EditText) view.findViewById(R.id.edit_search);
        hintView = (LinearLayout) view.findViewById(R.id.hint_frame);

        popupWindow = new ListPopupWindow(getContext());
        autoCompleteAdapter = new AutoCompleteAdapter();
//        autoCompleteAdapter = new ArrayAdapter<AttractionData>(getContext(), android.R.layout.simple_list_item_1);
        popupWindow.setAdapter(autoCompleteAdapter);
        popupWindow.setAnchorView(searchView);

        mAdapter = new FamousRouteAdapter();
        layoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                TotalRecordData data = mAdapter.getItemAtPosition(position);
                Intent i = new Intent(getActivity(), DetailRouteActivity.class);
                i.putExtra(DetailRouteActivity.EXTRA_RECORD_DATA, data);
                startActivity(i);
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) hintView.setVisibility(View.GONE);
                else hintView.setVisibility(View.VISIBLE);
            }
        });

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count >= 2) {
                    String keyword = s.toString();
                    if (TextUtils.isEmpty(keyword)) popupWindow.dismiss();
                    else {
                        Call<AutoCompleteData> call = NetworkManager.getInstance().getService().getAutoText(Constants.getAccessToken(), keyword);
                        call.enqueue(new Callback<AutoCompleteData>() {
                            @Override
                            public void onResponse(Call<AutoCompleteData> call, Response<AutoCompleteData> response) {
                                if (response.isSuccessful()) {
                                    AutoCompleteData result = response.body();
                                    autoCompleteAdapter.clear();
                                    autoCompleteAdapter.addAll(result.result);
                                    popupWindow.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AutoCompleteData> call, Throwable t) {

                            }
                        });
                    }
                } else {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AttractionData data = autoCompleteAdapter.getItemAtPosition(position);
//                AttractionData data = autoCompleteAdapter.getItem(position);
                Intent i = new Intent(getActivity(), SearchResultActivity.class);
                i.putExtra(SearchResultActivity.EXTRA_ATTRACTION_ID, data.id);
                i.putExtra(SearchResultActivity.EXTRA_SEARCH_KEYWORD, autoCompleteAdapter.getItemAtPosition(position).name);
//                i.putExtra(SearchResultActivity.EXTRA_SEARCH_KEYWORD, autoCompleteAdapter.getItem(position).name);
                searchView.setText("");
                startActivity(i);
            }
        });
        initData();

        return view;
    }

    Call<TotalRecordResult> call = NetworkManager.getInstance().getService().getCurationRoute(Constants.getAccessToken());

    private void initData() {

        call.enqueue(new Callback<TotalRecordResult>() {
            @Override
            public void onResponse(Call<TotalRecordResult> call, Response<TotalRecordResult> response) {
                if (response.isSuccessful()) {
                    mAdapter.clear();
                    TotalRecordResult result = response.body();
                    mAdapter.addAll(result.results);
                    if(mAdapter.getItemCount() > 0)
                        BusProvider.getInstance().post(mAdapter.getItemAtPosition(0));
                }
            }

            @Override
            public void onFailure(Call<TotalRecordResult> call, Throwable t) {
                Log.e("FamousRoute", t.getMessage());
            }
        });
    }

    @Override
    public void onDetach() {
        call.cancel();
        super.onDetach();
    }

    public TotalRecordData getSampleRecord(){
        return mAdapter.getItemAtPosition(0);
    }
}
