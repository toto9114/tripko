package kr.co.plani.fitlab.tripko.Search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.FamousRoute.FamousRouteItemView;
import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-19.
 */

public class SearchResultAdapter extends RecyclerView.Adapter implements OnBtnClickListener {
    private static final int FOOTER_SIZE = 1;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    List<TotalRecordData> items = new ArrayList<>();

    public void add(TotalRecordData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<TotalRecordData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public TotalRecordData getItemAtPosition(int position) {
        return items.get(position);
    }

    public void clear() {
        items.clear();
    }


    @Override
    public int getItemViewType(int position) {
        if (items.size() == position) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.view_famous_route_item, parent, false);
            view.getLayoutParams().height = parent.getHeight() / 3;
            return new FamousRouteItemView(view);
        } else {
            view = inflater.inflate(R.layout.view_search_result_footer, parent, false);
            return new SearchResultFooter(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < items.size()) {
            ((FamousRouteItemView) holder).setData(items.get(position));
        } else {
            ((SearchResultFooter) holder).setOnBtnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    private OnBtnClickListener btnClickListener;

    public void setOnBtnClickListener(OnBtnClickListener listener) {
        btnClickListener = listener;
    }

    @Override
    public void OnBtnClick() {
        if (btnClickListener != null) {
            btnClickListener.OnBtnClick();
        }
    }
}
