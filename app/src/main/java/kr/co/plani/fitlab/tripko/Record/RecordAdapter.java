package kr.co.plani.fitlab.tripko.Record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.plani.fitlab.tripko.Data.RecordData;
import kr.co.plani.fitlab.tripko.R;


/**
 * Created by toto9114 on 2017-01-31.
 */

public class RecordAdapter extends RecyclerView.Adapter {
    List<RecordData> items = new ArrayList<>();

    public void add(RecordData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<RecordData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public RecordData getItemAtPosition(int position) {
        return items.get(position);
    }

    public void clear() {
        items.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_record_item, parent, false);
        view.getLayoutParams().height = parent.getHeight()/3;
        return new RecordItemView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecordItemView) holder).setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
