package kr.co.plani.fitlab.tripko.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by toto9114 on 2017-01-31.
 */

public class RecordData implements Serializable{
    public int id;
    public List<AttractionData> route;
    public String image_url;
    public String link;
    public List<FromToData> distance_matrix;

    @SerializedName("created_time")
    public String createTime;
    @SerializedName("modified_time")
    public String modifiedTime;
}
