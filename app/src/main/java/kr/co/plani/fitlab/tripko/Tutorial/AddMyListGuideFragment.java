package kr.co.plani.fitlab.tripko.Tutorial;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddMyListGuideFragment extends Fragment {


    public AddMyListGuideFragment() {
        // Required empty public constructor
    }

    View guidView;
    ImageView arrowView;
    RelativeLayout topLabel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_my_list_guide, container, false);
        guidView = view.findViewById(R.id.view_guide);
        arrowView = (ImageView) view.findViewById(R.id.image_arrow);
        topLabel = (RelativeLayout) view.findViewById(R.id.top_label);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            topLabel.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        guidView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.twinkle));
        arrowView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.down_up));

        guidView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new TutorialEvent(TutorialEvent.TYPE_ADD_MY_LIST_GUIDE_FINISH, true));
                getActivity().finish();
            }
        });
        return view;
    }

}
