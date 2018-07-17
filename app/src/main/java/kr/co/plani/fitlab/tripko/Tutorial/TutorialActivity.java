package kr.co.plani.fitlab.tripko.Tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Subscribe;

import kr.co.plani.fitlab.tripko.Data.ActivityResultEvent;
import kr.co.plani.fitlab.tripko.Data.TotalRecordData;
import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.R;

public class TutorialActivity extends AppCompatActivity {

    public static final String EXTRA_SAMPLE_ROUTE = "sample";
    public static final String EXTRA_TUTORIAL_TYPE = "tutorial";

    public static final int TYPE_SHARE_RECORD = 0;
    public static final int TYPE_ADD_MY_LIST = 1;
    public static final int TYPE_ADD_PLAN = 2;

    int tutorialType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        BusProvider.getInstance().register(this);

        getIntent().getSerializableExtra(EXTRA_SAMPLE_ROUTE);
        tutorialType = getIntent().getIntExtra(EXTRA_TUTORIAL_TYPE, -1);

        if (savedInstanceState == null) {
            switch (tutorialType){
                case TYPE_SHARE_RECORD:
                    changeRecordShareGuide();
                    break;
                case TYPE_ADD_MY_LIST:
                    changeAddMyListGuide();
                    break;
                case TYPE_ADD_PLAN:
                    changeAddPlanGuide();
                    break;
                default:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new AITripKoGuideFragment())
                            .commit();
                    break;
            }

        }
    }


    public void changeFamousRouteGuide() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new FamousRouteGuideFragment())
                .commit();
    }

    public void changeRecordShareGuide() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new RecordShareGuideFragment())
                .commit();
    }

    public void changeMyListGuide() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new MyListGuideFragment())
                .commit();
    }

    public void changeAddMyListGuide(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddMyListGuideFragment())
                .commit();
    }

    public void changeAddPlanGuide(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AddPlanGuideFragment())
                .commit();
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private TotalRecordData sampleData = null;

    @Subscribe
    public void onSampleRecord(TotalRecordData recordData) {
        sampleData = recordData;
    }

    public TotalRecordData getSampleData() {
        return sampleData;
    }

    @Override
    public void finish() {
        BusProvider.getInstance().unregister(this);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }
}
