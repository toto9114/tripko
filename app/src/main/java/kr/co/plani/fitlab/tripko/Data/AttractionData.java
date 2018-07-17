package kr.co.plani.fitlab.tripko.Data;

import java.io.Serializable;

/**
 * Created by toto9114 on 2017-01-25.
 */

public class AttractionData implements PlanData, Serializable{
    public int id;
    public String name;
    public float latitude;
    public float longitude;
    public String place_id;
    public String image_url;
    public String link;
}
