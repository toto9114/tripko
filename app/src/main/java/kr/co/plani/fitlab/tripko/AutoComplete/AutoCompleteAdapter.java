package kr.co.plani.fitlab.tripko.AutoComplete;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.co.plani.fitlab.tripko.Data.AttractionData;

/**
 * Created by jihun on 2017-04-10.
 */

public class AutoCompleteAdapter extends BaseAdapter {
    List<AttractionData> items = new ArrayList<>();

    public void add(AttractionData data) {
        items.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<AttractionData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public AttractionData getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AutoCompleteItemView view = null;
        if (convertView == null) {
            view = new AutoCompleteItemView(parent.getContext());
        } else {
            view = (AutoCompleteItemView) convertView;
        }
        view.setData(items.get(position));
        return view;
    }
}
