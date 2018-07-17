package kr.co.plani.fitlab.tripko.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jihun on 2017-02-23.
 */

public class TotalRecordData implements Serializable {
    public int id;
    public List<AttractionData> route;
    public AttractionData main_attraction;
    public String image_url;
    public String link;
    public int count;
    public List<FromToData> distance_matrix;
    @SerializedName("created_time")
    public String createTime;
    public String modifiedTime;
}
