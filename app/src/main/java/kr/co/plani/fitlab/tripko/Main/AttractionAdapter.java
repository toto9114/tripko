package kr.co.plani.fitlab.tripko.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Recommend.RecommendItemView;


/**
 * Created by toto9114 on 2017-01-31.
 */

public class AttractionAdapter extends RecyclerView.Adapter {
    List<AttractionData> items = new ArrayList<>();

    public void add(AttractionData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<AttractionData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public AttractionData getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_recommend_item, parent, false);
        view.getLayoutParams().height = parent.getHeight() / 3;

        return new RecommendItemView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecommendItemView) holder).setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
