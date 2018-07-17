package kr.co.plani.fitlab.tripko.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import kr.co.plani.fitlab.tripko.R;


public class OpenSourceLicenseActivity extends AppCompatActivity {

    TextView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_license);
        contentView = (TextView) findViewById(R.id.text_content);
//        contentView.setText(readTxt());
        readTxt();

    }

//    private String readTxt() {
//        String data = null;
//        InputStream inputStream = getResources().openRawResource(R.raw.opensource_license);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        int i;
//        try {
//            i = inputStream.read();
//            while (i != -1) {
//                byteArrayOutputStream.write(i);
//                i = inputStream.read();
//            }
//
//            data = new String(byteArrayOutputStream.toByteArray(), "MS949");
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

    private void readTxt() {
        BufferedReader in;
        InputStream inputStream = getResources().openRawResource(R.raw.opensource_license);
        StringBuffer strBuffer = new StringBuffer();
        String str = null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));  // file이 utf-8 로 저장되어 있다면 "UTF-8"
            while ((str = in.readLine()) != null)                      // file이 KSC5601로 저장되어 있다면 "KSC5601"
            {
                strBuffer.append(str + "\n");
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        contentView.setText(strBuffer);
    }
}
