package kr.co.plani.fitlab.tripko.Search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-19.
 */

public class SearchResultFooter extends RecyclerView.ViewHolder {
    private OnBtnClickListener btnClickListener;

    public void setOnBtnClickListener(OnBtnClickListener listener) {
        btnClickListener = listener;
    }

    RelativeLayout addBtn;

    public SearchResultFooter(View itemView) {
        super(itemView);
        addBtn = (RelativeLayout) itemView.findViewById(R.id.add_record_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnClickListener != null) {
                    btnClickListener.OnBtnClick();
                }
            }
        });
    }
}
