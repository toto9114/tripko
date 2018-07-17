package kr.co.plani.fitlab.tripko.Recommend;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.R;

/**
 * Created by toto9114 on 2017-01-31.
 */

public class RecommendItemView extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView titleView;

    public RecommendItemView(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_main);
        titleView = (TextView) itemView.findViewById(R.id.text_title);
    }

    public void setData(AttractionData data) {
        if (!TextUtils.isEmpty(data.image_url)) {
            Glide.with(itemView.getContext()).load(data.image_url).into(imageView);
        } else {
//            Glide.with(itemView.getContext()).load(R.drawable.image_default).into(imageView);
//            Glide.clear(imageView);
//            imageView.setImageDrawable(null);
        }
        titleView.setText(data.name);
        imageView.setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.image_opacity));
    }
}
