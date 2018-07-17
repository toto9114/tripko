package kr.co.plani.fitlab.tripko.Record;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.plani.fitlab.tripko.Data.FromToData;
import kr.co.plani.fitlab.tripko.Data.RecordData;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Utils;


/**
 * Created by toto9114 on 2017-01-31.
 */

public class RecordItemView extends RecyclerView.ViewHolder {


    ImageView imageView;
    TextView routeView, dateView, countView, timeView;
    Context context;

    public RecordItemView(View itemView) {
        super(itemView);
        context = itemView.getContext();
        imageView = (ImageView) itemView.findViewById(R.id.image_main);
        routeView = (TextView) itemView.findViewById(R.id.text_route);
        dateView = (TextView) itemView.findViewById(R.id.text_date);
        countView = (TextView) itemView.findViewById(R.id.text_count);
        timeView = (TextView) itemView.findViewById(R.id.text_time);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageView.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.image_opacity));
    }

    public void setData(RecordData data) {
        if (!TextUtils.isEmpty(data.image_url)) {
            Glide.with(context).load(data.image_url).into(imageView);
        }
        routeView.setText(Utils.makeRoute(data.route));
        dateView.setText(Utils.dateParser(data.createTime));
//        countView.setText("" + data.route.size() + "곳의 여행지 방문");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countView.setText(Html.fromHtml(context.getString(R.string.record_visit_count, data.route.size()), Html.FROM_HTML_MODE_LEGACY));
        } else {
            countView.setText(Html.fromHtml(context.getString(R.string.record_visit_count, data.route.size())));
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
