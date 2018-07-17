package kr.co.plani.fitlab.tripko.Dialog;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.plani.fitlab.tripko.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AIProgressDialog extends DialogFragment {


    public AIProgressDialog() {
        // Required empty public constructor
    }

    public static final String EXTRA_TYPE = "type";
    public static final int EXTRA_ATTRACTIONS_LOAD = 0;
    public static final int EXTRA_ROUTE_UPLOAD = 1;


    int type;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null){
            type = getArguments().getInt(EXTRA_TYPE);
        }
    }

    ImageView progressView;
    TextView messageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aiprogress_dialog, container, false);
        progressView = (ImageView) view.findViewById(R.id.image_progress);
        messageView = (TextView) view.findViewById(R.id.text_message);
        progressView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_permanent));

        init();
        return view;
    }

    private void init(){
        switch (type){
            case EXTRA_ATTRACTIONS_LOAD:
                messageView.setText(R.string.ai_message_curation);
                break;
            case EXTRA_ROUTE_UPLOAD:
                messageView.setText(R.string.ai_message_training);
                break;
        }
    }

    @Override
    public void onStart() {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);

        getDialog().setCancelable(false);
        super.onStart();
    }

}
