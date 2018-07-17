package kr.co.plani.fitlab.tripko.Tutorial;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPlanGuideFragment extends Fragment {


    public AddPlanGuideFragment() {
        // Required empty public constructor
    }

    FrameLayout guideView;
    ImageView arrowView;
    CustomToolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_plan_guide, container, false);
        toolbar = (CustomToolbar) view.findViewById(R.id.toolbar);
        guideView = (FrameLayout) view.findViewById(R.id.view_guide);
        arrowView = (ImageView) view.findViewById(R.id.image_arrow);
        guideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.twinkle));
        arrowView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.down_up));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        guideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new TutorialEvent(TutorialEvent.TYPE_ADD_PLAN_GUIDE_FINISH,true));
                getActivity().finish();
            }
        });
        return view;
    }

}
