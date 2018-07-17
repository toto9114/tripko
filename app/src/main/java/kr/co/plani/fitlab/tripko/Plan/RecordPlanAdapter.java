package kr.co.plani.fitlab.tripko.Plan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Data.FromToData;
import kr.co.plani.fitlab.tripko.Data.PlanData;
import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-18.
 */

public class RecordPlanAdapter extends RecyclerView.Adapter {
    private static final int TYPE_ATTRACTION = 0;
    private static final int TYPE_FROM_TO = 1;

    List<PlanData> items = new ArrayList<>();

    public void add(PlanData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public PlanData getItemAtPosition(int position) {
        return items.get(position);
    }

    public void setDistance(int position, FromToData data) {
        items.set(position, data);
        notifyItemChanged(position);
    }

    public List<HashMap> getFromToDatas() {
        List<HashMap> list = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof FromToData && i > 1) {
                AttractionData from = (AttractionData) items.get(i - 1);
                AttractionData to = (AttractionData) items.get(i + 1);
                String key = from.id + "-" + to.id;
                HashMap map = new HashMap();
                map.put(key, items.get(i));
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof AttractionData) {
            return TYPE_ATTRACTION;
        } else {
            return TYPE_FROM_TO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        if (viewType == TYPE_ATTRACTION) {
            view = inflater.inflate(R.layout.view_record_attraction, parent, false);
            return new RecordAttractionView(view);
        } else {
            view = inflater.inflate(R.layout.view_record_divider, parent, false);
            return new RecordDividerView(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position % 2 == 0) {
            ((RecordDividerView) holder).setData((FromToData) items.get(position));
        } else {
            ((RecordAttractionView) holder).setData((AttractionData) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
