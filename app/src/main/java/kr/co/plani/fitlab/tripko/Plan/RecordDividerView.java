package kr.co.plani.fitlab.tripko.Plan;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.plani.fitlab.tripko.Data.FromToData;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Utils;

/**
 * Created by jihun on 2017-04-18.
 */

public class RecordDividerView extends RecyclerView.ViewHolder {
    TextView distanceView, timeView, countView;
    ImageView iconView;
    Context context;

    public RecordDividerView(View itemView) {
        super(itemView);
        context = itemView.getContext();
        distanceView = (TextView) itemView.findViewById(R.id.text_distance);
        timeView = (TextView) itemView.findViewById(R.id.text_time);
        countView = (TextView) itemView.findViewById(R.id.text_count);
        iconView = (ImageView) itemView.findViewById(R.id.image_car);
    }

    public void setData(FromToData data) {
        if (data.count == null && data.distance == null && data.total_time == null) {
            iconView.setVisibility(View.GONE);
            distanceView.setVisibility(View.INVISIBLE);
            timeView.setVisibility(View.INVISIBLE);
            countView.setVisibility(View.INVISIBLE);
//            if (!TextUtils.isEmpty(data.count)) countView.setText(data.count + "명이 선택한 경로");
        }else{
            if (!TextUtils.isEmpty(data.distance))
                distanceView.setText(Utils.meterToKilometer(data.distance));
            if (!TextUtils.isEmpty(data.total_time))
                timeView.setText(Utils.secondToHour(data.total_time));

            if (!TextUtils.isEmpty(data.count)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    countView.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.choose_count_info), Integer.parseInt(data.count)), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    countView.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.choose_count_info), Integer.parseInt(data.count))));
                }
            }
        }
    }
}
