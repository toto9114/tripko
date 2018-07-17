package kr.co.plani.fitlab.tripko.Tutorial;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.otto.Subscribe;

import kr.co.plani.fitlab.tripko.Data.ActivityResultEvent;
import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.DetailRouteActivity;
import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamousRouteGuideFragment extends Fragment {


    public FamousRouteGuideFragment() {
        // Required empty public constructor
    }

    private static final int REQUEST_CODE = 100;

    FrameLayout toolbar;
    View guideView;
    ImageView arrowView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_famus_route_guide, container, false);
        toolbar = (FrameLayout) view.findViewById(R.id.top_label);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        guideView = view.findViewById(R.id.view_guide);
        guideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.twinkle));
        arrowView = (ImageView) view.findViewById(R.id.image_arrow);
        arrowView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.down_up));

        guideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalRecordData data = ((TutorialActivity) getActivity()).getSampleData();
                Intent i = new Intent(getActivity(), DetailRouteActivity.class);
                i.putExtra(DetailRouteActivity.EXTRA_RECORD_DATA, data);
                i.putExtra(DetailRouteActivity.EXTRA_IS_SAMPLE, true);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        return view;
    }

    @Subscribe
    public void onActivityResult(ActivityResultEvent activityResultEvent) {
        onActivityResult(activityResultEvent.getRequestCode(), activityResultEvent.getResultCode(), activityResultEvent.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
            ((TutorialActivity)getActivity()).changeMyListGuide();
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
