package kr.co.plani.fitlab.tripko.AutoComplete;

import android.content.Context;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.R;

/**
 * Created by jihun on 2017-04-10.
 */

public class AutoCompleteItemView extends FrameLayout {

    TextView nameView;

    public AutoCompleteItemView(Context context) {
        super(context);
        inflate(context, R.layout.view_auto_complete_item,this);
        nameView = (TextView) findViewById(R.id.text_name);
    }

    private AttractionData data = null;

    public void setData(AttractionData data) {
        this.data = data;
        if(!TextUtils.isEmpty(data.name)) nameView.setText(data.name);
    }
}
