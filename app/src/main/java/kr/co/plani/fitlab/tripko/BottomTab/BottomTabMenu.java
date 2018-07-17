package kr.co.plani.fitlab.tripko.BottomTab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-03-21.
 */

public class BottomTabMenu extends FrameLayout {
    private OnTabMenuSelectedListener tabMenuSelectedListener;

    public void setOnTabMenuSelectedListener(OnTabMenuSelectedListener listener) {
        tabMenuSelectedListener = listener;
    }

    ImageView iconView;
    TextView menuNameView;

    public BottomTabMenu(Context context) {
        super(context);
        inflate(getContext(), R.layout.view_bottom_menu, this);

        iconView = (ImageView) findViewById(R.id.image_icon);
        menuNameView = (TextView) findViewById(R.id.text_menu_name);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabMenuSelectedListener != null) {
                    tabMenuSelectedListener.OnTabMenuSelected(position);
                    menuClicked();
                }
            }
        });
    }

    int position;

    public void setMenu(int resId, String menuName, int position) {
        int unselectColor = ContextCompat.getColor(getContext(), R.color.unselect_tab);
        iconView.setImageResource(resId);
        menuNameView.setText(menuName);
//        menuNameView.setTextColor(unselectColor);
//        iconView.setColorFilter(unselectColor, PorterDuff.Mode.SRC_IN);
        this.position = position;
    }

    private void menuClicked() {

//        if (position == 2) {
//            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
//            iconView.startAnimation(anim);
//        } else {
            YoYo.with(Techniques.RubberBand)
                    .duration(500)
                    .playOn(iconView);
//        }
//        iconView.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN);
    }
}
