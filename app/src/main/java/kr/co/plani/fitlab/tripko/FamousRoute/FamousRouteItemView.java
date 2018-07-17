package kr.co.plani.fitlab.tripko.FamousRoute;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.plani.fitlab.tripko.Data.FromToData;
import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Utils;


/**
 * Created by jihun on 2017-02-23.
 */

public class FamousRouteItemView extends RecyclerView.ViewHolder {
    ImageView mainImageView;
    TextView routeView, countView, timeView;
    Context context;

    public FamousRouteItemView(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mainImageView = (ImageView) itemView.findViewById(R.id.image_main);
        routeView = (TextView) itemView.findViewById(R.id.text_route);
        countView = (TextView) itemView.findViewById(R.id.text_count_info);
        timeView = (TextView) itemView.findViewById(R.id.text_time);

//        mainImageView.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.image_opacity));

    }

    public void setData(TotalRecordData data) {
        if (!TextUtils.isEmpty(data.main_attraction.image_url)) {
            Glide.with(context).load(data.main_attraction.image_url).into(mainImageView);
        } else {
//            Glide.with(context).load(R.drawable.image_default).into(mainImageView);
        }

//        routeView.setText(Utils.makeRoute(data.route));
        routeView.setText(data.main_attraction.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countView.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.choose_count_info), data.count), Html.FROM_HTML_MODE_LEGACY));
        } else {
            countView.setText(Html.fromHtml(String.format(context.getResources().getString(R.string.choose_count_info), data.count)));
        }

        int count = 0;
        if (data.distance_matrix != null) {
            for (FromToData fromToData : data.distance_matrix) {
                if (!TextUtils.isEmpty(fromToData.total_time)) {
                    count += Integer.parseInt(fromToData.total_time);
                }
            }
        }
        timeView.setText(context.getString(R.string.total_time_prefix, Utils.secondToHour("" + count)));
    }
}
