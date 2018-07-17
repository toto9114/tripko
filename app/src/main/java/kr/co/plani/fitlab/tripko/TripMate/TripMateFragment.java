package kr.co.plani.fitlab.tripko.TripMate;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.TripMatePost;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripMateFragment extends Fragment {


    public TripMateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_mate, container, false);
        Button btn = (Button) view.findViewById(R.id.btn_upload);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPost();
            }
        });
        return view;
    }

    private void uploadPost(){
        TripMatePost post = new TripMatePost();
        post.title = "title";
        post.content = "content";
        Call<TripMatePost> call = NetworkManager.getInstance().getService().uploadTripMatePost(Constants.getAccessToken(), post);
        call.enqueue(new Callback<TripMatePost>() {
            @Override
            public void onResponse(Call<TripMatePost> call, Response<TripMatePost> response) {
                if(response.isSuccessful()){
                    TripMatePost result = response.body();
                }
            }

            @Override
            public void onFailure(Call<TripMatePost> call, Throwable t) {

            }
        });
    }

}
