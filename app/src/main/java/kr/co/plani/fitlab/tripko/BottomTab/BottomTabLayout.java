package kr.co.plani.fitlab.tripko.BottomTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-03-21.
 */

public class BottomTabLayout extends FrameLayout implements OnTabMenuSelectedListener {
    private OnTabMenuSelectedListener tabMenuSelectedListener;

    public void setOnTabMenuSelectedListener(OnTabMenuSelectedListener listener) {
        tabMenuSelectedListener = listener;
    }

    LinearLayout bottomLayout;

    public BottomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.view_bottom_layout, this);
        bottomLayout = (LinearLayout) findViewById(R.id.linear_bottom_layout);
    }

    public void addTabMenu(int resId, String menuName) {
        int count = bottomLayout.getChildCount();
        BottomTabMenu menu = new BottomTabMenu(getContext());
        menu.setMenu(resId, menuName, count);
        menu.setOnTabMenuSelectedListener(this);
        menu.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        bottomLayout.addView(menu);
    }

    @Override
    public void OnTabMenuSelected(int position) {
        if (tabMenuSelectedListener != null) {
            tabMenuSelectedListener.OnTabMenuSelected(position);
//            int unselectColor = ContextCompat.getColor(getContext(), R.color.unselect_tab);
//            int selectColor = ContextCompat.getColor(getContext(), R.color.select_tab);
            for (int i = 0; i < bottomLayout.getChildCount(); i++) {
                if (position != i) {    //unselect
                    ImageView imageView = (ImageView) bottomLayout.getChildAt(i).findViewById(R.id.image_icon);
                    imageView.setSelected(false);
//                    TextView textView = (TextView) bottomLayout.getChildAt(i).findViewById(R.id.text_menu_name);
//                    textView.setTextColor(unselectColor);
//                    imageView.setColorFilter(unselectColor, PorterDuff.Mode.SRC_IN);

                } else {    //select
                    ImageView imageView = (ImageView) bottomLayout.getChildAt(i).findViewById(R.id.image_icon);
                    imageView.setSelected(true);
//                    TextView textView = (TextView) bottomLayout.getChildAt(i).findViewById(R.id.text_menu_name);
//                    textView.setTextColor(selectColor);
//                    imageView.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN);
                }
            }
        }
    }

    public void init() {
//        int selectColor = ContextCompat.getColor(getContext(), R.color.select_tab);
        ImageView imageView = (ImageView) bottomLayout.getChildAt(0).findViewById(R.id.image_icon);
        imageView.setSelected(true);
//        TextView textView = (TextView) bottomLayout.getChildAt(0).findViewById(R.id.text_menu_name);
//        textView.setTextColor(selectColor);
//        imageView.setColorFilter(selectColor, PorterDuff.Mode.SRC_IN);
    }
}
