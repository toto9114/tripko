package kr.co.plani.fitlab.tripko.Toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-11.
 */

public class CustomToolbar extends FrameLayout {

    private OnLeftMenuClickListener leftMenuClickListener;
    private OnRightMenuClickListener rightMenuClickListener;
    private OnDeleteMenuClickListener deleteMenuClickListener;
    private OnShareBtnClickListener shareBtnClickListener;


    public void setOnLeftMenuClickListener(OnLeftMenuClickListener listener) {
        leftMenuClickListener = listener;
    }

    public void setOnRightMenuClickListener(OnRightMenuClickListener listener) {
        rightMenuClickListener = listener;
    }

    public void setOnDeleteMenuClickListener(OnDeleteMenuClickListener listener) {
        deleteMenuClickListener = listener;
    }

    public void setOnShareBtnClickListener(OnShareBtnClickListener listener) {
        shareBtnClickListener = listener;
    }

    TextView titleView, rightMenuView;
    FrameLayout leftMenu;
    ImageView iconView;
    LinearLayout edit_menu;
    ImageView shareBtn, deleteBtn;

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_custom_toolbar, this);
        titleView = (TextView) findViewById(R.id.text_title);
        rightMenuView = (TextView) findViewById(R.id.text_right_menu);
        leftMenu = (FrameLayout) findViewById(R.id.left_menu);
        iconView = (ImageView) findViewById(R.id.image_icon);
        edit_menu = (LinearLayout) findViewById(R.id.edit_menu);
        deleteBtn = (ImageView) findViewById(R.id.btn_delete);
        shareBtn = (ImageView) findViewById(R.id.btn_share);

        rightMenuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightMenuClickListener != null) {
                    rightMenuClickListener.OnRightMenuClick();
                }
            }
        });

        leftMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftMenuClickListener != null) {
                    leftMenuClickListener.OnLeftMenuClick();
                }
            }
        });
        deleteBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMenuClickListener != null) {
                    deleteMenuClickListener.OnDeleteMenuClick();
                }
            }
        });
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareBtnClickListener != null) {
                    shareBtnClickListener.OnShareBtnClick();
                }
            }
        });
    }

    public void setToolbar(int resId, String title, String rightMenu) {
        iconView.setImageResource(resId);
        titleView.setText(title);
        rightMenuView.setText(rightMenu);
    }

    public void setToolbar(String title, String rightMenu) {
        titleView.setText(title);
        rightMenuView.setText(rightMenu);
    }

    public void setEditMode(int resId, String title) {
        iconView.setImageResource(resId);
        titleView.setText(title);
        rightMenuView.setVisibility(GONE);
        edit_menu.setVisibility(VISIBLE);

    }

}
