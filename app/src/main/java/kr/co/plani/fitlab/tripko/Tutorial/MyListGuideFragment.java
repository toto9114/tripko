package kr.co.plani.fitlab.tripko.Tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyListGuideFragment extends Fragment {


    public MyListGuideFragment() {
        // Required empty public constructor
    }

    View guideView;
    ImageView arrowView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_list_guide, container, false);
        guideView = view.findViewById(R.id.view_guide);
        arrowView = (ImageView) view.findViewById(R.id.image_my_list_arrow);

        guideView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.twinkle));
        arrowView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.up_down));
        guideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new TutorialEvent(TutorialEvent.TYPE_MY_LIST_GUIDE_FINISH,true));
                ((TutorialActivity)getActivity()).changeAddMyListGuide();
            }
        });
        return view;
    }
}
