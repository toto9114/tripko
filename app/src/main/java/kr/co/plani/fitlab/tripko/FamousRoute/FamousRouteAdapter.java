package kr.co.plani.fitlab.tripko.FamousRoute;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.R;


/**
 * Created by jihun on 2017-02-23.
 */

public class FamousRouteAdapter extends RecyclerView.Adapter {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_famous_route_item, parent, false);
        view.getLayoutParams().height = parent.getHeight()/3;
        return new FamousRouteItemView(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FamousRouteItemView) holder).setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
