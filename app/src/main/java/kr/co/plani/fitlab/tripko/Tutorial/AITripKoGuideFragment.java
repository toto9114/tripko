package kr.co.plani.fitlab.tripko.Tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import kr.co.plani.fitlab.tripko.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AITripKoGuideFragment extends Fragment {


    public AITripKoGuideFragment() {
        // Required empty public constructor
    }

    ImageView arrowView;
    View tabView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aitrip_ko_guide, container, false);
        arrowView = (ImageView)view.findViewById(R.id.image_ai_tripko_arrow);
        tabView = view.findViewById(R.id.ai_tripko_guide_tab);
        tabView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TutorialActivity)getActivity()).changeFamousRouteGuide();
            }
        });
        initData();
        return view;
    }
    private void initData(){
        arrowView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.up_down));
        tabView.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.twinkle));
    }

}
