package kr.co.plani.fitlab.tripko.Data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by jihun on 2017-05-10.
 */

public class CurationResult implements Serializable{
    @SerializedName("result")
    public AttractionData attractionData;
    public int count;
}
