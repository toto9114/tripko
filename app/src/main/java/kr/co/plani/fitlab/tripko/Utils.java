package kr.co.plani.fitlab.tripko;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import kr.co.plani.fitlab.tripko.Data.AttractionData;


/**
 * Created by jihun on 2017-02-22.
 */

public class Utils {
    public static String makeRoute(List<AttractionData> data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
//            sb.append("\"" + data.get(i).name + "\"");
            sb.append(data.get(i).name);
            if (i < data.size() - 1) {
                sb.append(" \u2192 ");
            }
        }
        return sb.toString();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = MyApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = MyApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String dateParser(String date) {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String localeDateFormat = MyApplication.getContext().getString(R.string.locale_date_format);

        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(localeDateFormat, Locale.getDefault());

        String parsed_date = "";
        try {
            parsed_date = sdf.format(parser.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsed_date;
    }

    public static String meterToKilometer(String distance) {
        int meter = Integer.parseInt(distance);
        int kilo = meter / 1000;
        int rest = meter % 1000;
        if (kilo > 0) {
            if (rest / 100 > 0) {
                return kilo + "." + rest / 100 + "km";
            } else {
                return kilo + "km";
            }
        } else {
            return meter + "m";
        }
    }

    public static String secondToHour(String time) {
        int diff = Integer.parseInt(time);
        int hour = (int) (diff / (60 * 60));
        int minute = (int) (diff % (60 * 60)) / 60;
        int second = (int) (diff % (60 * 60)) % 60;
        String pastTime = "";
        StringBuilder sb = new StringBuilder();
        if (hour != 0) {
            sb.append(hour + MyApplication.getContext().getString(R.string.hour));
            sb.append(" " + minute + MyApplication.getContext().getString(R.string.minute));
        } else {
            if (minute != 0) {
                sb.append(minute + MyApplication.getContext().getString(R.string.minute) +" ");
            } else {
                if (second != 0) sb.append(second + MyApplication.getContext().getString(R.string.second)+" ");
            }
        }
        pastTime = sb.toString();

        return pastTime;
    }
}
