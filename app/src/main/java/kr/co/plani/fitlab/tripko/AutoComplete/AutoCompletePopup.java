package kr.co.plani.fitlab.tripko.AutoComplete;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-10.
 */

public class AutoCompletePopup extends PopupWindow {
    FamiliarRecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AutoCompleteAdapter mAdapter;

    public AutoCompletePopup(Context context) {
        super(context);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setOutsideTouchable(true);

        View view = LayoutInflater.from(context).inflate(R.layout.view_auto_complete_popup, null);



        setContentView(view);
    }

    public void setData(List<AttractionData> list) {
        mAdapter.clear();
        if (list.size() != 0) {
            mAdapter.addAll(list);
        }
    }
}
