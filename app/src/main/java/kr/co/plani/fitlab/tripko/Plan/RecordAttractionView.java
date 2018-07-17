package kr.co.plani.fitlab.tripko.Plan;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-18.
 */

public class RecordAttractionView extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView;

    public RecordAttractionView(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image_attraction);
        nameView = (TextView) itemView.findViewById(R.id.text_name);
    }

    public void setData(AttractionData data) {
        if (!TextUtils.isEmpty(data.image_url)) {
            Glide.with(itemView.getContext()).load(data.image_url).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.img_default);
        }
        nameView.setText(data.name);
    }
}
