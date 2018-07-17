package kr.co.plani.fitlab.tripko.Recommend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AttractionsResult;
import kr.co.plani.fitlab.tripko.Main.AttractionAdapter;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {


    public RecommendFragment() {
        // Required empty public constructor
    }

    FamiliarRecyclerView recyclerView;
    AttractionAdapter mAdapter;
    LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        recyclerView = (FamiliarRecyclerView) view.findViewById(R.id.recyclerView);
        mAdapter = new AttractionAdapter();
        layoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        initData();

        return view;
    }

    private void initData() {
        Call<AttractionsResult> call = NetworkManager.getInstance().getService().getRecommendList(Constants.getAccessToken());
        call.enqueue(new Callback<AttractionsResult>() {
            @Override
            public void onResponse(Call<AttractionsResult> call, Response<AttractionsResult> response) {
                if (response.isSuccessful()) {
                    AttractionsResult result = response.body();
                    mAdapter.clear();
                    mAdapter.addAll(result.results);
                } else {
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttractionsResult> call, Throwable t) {

            }
        });
    }

}
