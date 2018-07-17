package kr.co.plani.fitlab.tripko.MyRecord;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.RecordData;
import kr.co.plani.fitlab.tripko.Data.RecordResult;
import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Plan.AddPlanActivity;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Record.RecordAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyRecordFragment extends Fragment {


    public MyRecordFragment() {
        // Required empty public constructor
    }

    ImageView rightMenu;
    FamiliarRecyclerView recyclerView;
    TextView titleView;
    LinearLayoutManager layoutManager;
    RecordAdapter mAdapter;

    boolean isMoreData = false;
    boolean isLast = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_record, container, false);
        rightMenu = (ImageView) view.findViewById(R.id.right_menu);
        titleView = (TextView) view.findViewById(R.id.text_title);
        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new RecordAdapter();
        layoutManager = new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        rightMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddPlanActivity.class));
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isLast) {
                    if (isMoreData) {
                        getMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (layoutManager.findLastVisibleItemPosition() >= (mAdapter.getItemCount() - 1)) {
                    isLast = true;
                } else {
                    isLast = false;
                }
            }
        });

        recyclerView.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                RecordData data = mAdapter.getItemAtPosition(position);
                Intent i = new Intent(getActivity(), AddPlanActivity.class);
                i.putExtra(AddPlanActivity.EXTRA_RECORD_DATA, data);
                startActivity(i);
            }
        });

        initData();
        return view;
    }

    int page = 1;
    Call<RecordResult> call = NetworkManager.getInstance().getService().getMyRecord(Constants.getAccessToken(), page);

    private void initData() {

        call.enqueue(new Callback<RecordResult>() {
            @Override
            public void onResponse(Call<RecordResult> call, Response<RecordResult> response) {
                if (response.isSuccessful()) {
                    RecordResult result = response.body();
                    mAdapter.clear();
                    mAdapter.addAll(result.results);
                    if (!TextUtils.isEmpty(result.next)) isMoreData = true;
                    titleView.setText(String.format(getString(R.string.my_record_title), result.count));
                }
            }

            @Override
            public void onFailure(Call<RecordResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDetach() {
        call.cancel();
        super.onDetach();
    }

    private void getMoreData() {
        if (!isMoreData) return;
        page++;

        Call<RecordResult> newCall = NetworkManager.getInstance().getService().getMyRecord(Constants.getAccessToken(), page);
        newCall.enqueue(new Callback<RecordResult>() {
            @Override
            public void onResponse(Call<RecordResult> call, Response<RecordResult> response) {
                if (response.isSuccessful()) {
                    RecordResult result = response.body();
                    mAdapter.addAll(result.results);
                    if (!TextUtils.isEmpty(result.next)) isMoreData = true;
                }
            }

            @Override
            public void onFailure(Call<RecordResult> call, Throwable t) {

            }
        });
    }

    @Subscribe
    public void createdRecord(RecordCreatedEvent createdEvent) {
        if (createdEvent.isCreated()) {
            mAdapter.clear();
            call.clone().enqueue(new Callback<RecordResult>() {
                @Override
                public void onResponse(Call<RecordResult> call, Response<RecordResult> response) {
                    if (response.isSuccessful()) {
                        RecordResult result = response.body();
                        if (!TextUtils.isEmpty(result.next)) isMoreData = true;
                        else isMoreData = false;
                        mAdapter.addAll(result.results);
                        titleView.setText(String.format(getString(R.string.my_record_title,result.count) ));
                    }
                }

                @Override
                public void onFailure(Call<RecordResult> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BusProvider.getInstance().register(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        BusProvider.getInstance().unregister(this);
        super.onDestroyView();
    }
}
