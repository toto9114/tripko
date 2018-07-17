package kr.co.plani.fitlab.tripko;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import kr.co.plani.fitlab.tripko.BottomTab.BottomTabLayout;
import kr.co.plani.fitlab.tripko.BottomTab.OnTabMenuSelectedListener;
import kr.co.plani.fitlab.tripko.FamousRoute.FamousRouteFragment;
import kr.co.plani.fitlab.tripko.Manager.BusProvider;
import kr.co.plani.fitlab.tripko.Manager.PropertyManager;
import kr.co.plani.fitlab.tripko.MyRecord.MyRecordFragment;
import kr.co.plani.fitlab.tripko.Plan.AddAttractionActivity;
import kr.co.plani.fitlab.tripko.Plan.AddPlanActivity;
import kr.co.plani.fitlab.tripko.Settings.SettingsFragment;
import kr.co.plani.fitlab.tripko.TripMate.TripMateFragment;
import kr.co.plani.fitlab.tripko.Tutorial.TutorialActivity;
import kr.co.plani.fitlab.tripko.Tutorial.TutorialEvent;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MENU_TYPE = "mylist";

    private static final int REQUEST_MY_LIST_TUTORIAL = 100;

    public static final int TYPE_AI_TRIPKO = 0;
    public static final int TYPE_MYLIST = 1;
    public static final int TYPE_SETTING = 2;
    BottomTabLayout bottomTabLayout;
    boolean isFirst = false;

    //    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PropertyManager.getInstance().setFirst(false);

        isFirst = PropertyManager.getInstance().isFirst();
        bottomTabLayout = (BottomTabLayout) findViewById(R.id.bottom_layout);

        bottomTabLayout.addTabMenu(R.drawable.selector_ai_tripko, "AI TripKo");
        bottomTabLayout.addTabMenu(R.drawable.selector_settings,"Mate");
        bottomTabLayout.addTabMenu(R.drawable.selector_my_list, "My List");
        bottomTabLayout.addTabMenu(R.drawable.selector_settings, "Settings");

//        position = getIntent().getIntExtra(EXTRA_MENU_TYPE, TYPE_AI_TRIPKO);
        bottomTabLayout.init();
        bottomTabLayout.setOnTabMenuSelectedListener(new OnTabMenuSelectedListener() {
            @Override
            public void OnTabMenuSelected(int position) {
                switch (position) {
                    case 0:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new FamousRouteFragment())
                                .commit();
                        break;
                    case 1:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new TripMateFragment())
                                .commit();
                        break;
                    case 2:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new MyRecordFragment())
                                .commit();
                        break;
                    case 3:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new SettingsFragment())
                                .commit();
                        break;
                }
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new FamousRouteFragment())
                    .commit();

        }
        BusProvider.getInstance().register(this);
        if (isFirst) {
            startActivity(new Intent(this, TutorialActivity.class));
        }
        initData();
    }

    @Subscribe
    public void onFinishedTutorial(TutorialEvent event) {
        int type = event.getType();
        switch (type) {
            case TutorialEvent.TYPE_MY_LIST_GUIDE_FINISH:
                if (event.isCreated()) {
                    bottomTabLayout.OnTabMenuSelected(TYPE_MYLIST);
                }
                break;
            case TutorialEvent.TYPE_ADD_MY_LIST_GUIDE_FINISH:
                if (event.isCreated()) {
                    startActivity(new Intent(MainActivity.this, AddPlanActivity.class).putExtra(AddPlanActivity.EXTRA_IS_TUTORIAL, true));
                }
                break;
            case TutorialEvent.TYPE_ADD_PLAN_GUIDE_FINISH:
                startActivity(new Intent(MainActivity.this, AddAttractionActivity.class));
                finishTutorial();
                break;
        }

    }

    private void finishTutorial(){
        PropertyManager.getInstance().setFirst(false);
    }

    @Override
    public void finish() {
        BusProvider.getInstance().unregister(this);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MY_LIST_TUTORIAL && resultCode == RESULT_OK) {
            startActivity(new Intent(this, TutorialActivity.class).putExtra(TutorialActivity.EXTRA_TUTORIAL_TYPE, TutorialActivity.TYPE_ADD_MY_LIST));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {

    }

    private static final int MESSAGE_BACK_KEY = 100;
    private static final int BACK_KEY_TIMEOUT = 2000;
    private boolean isBackPressed = false;

    Handler mHandler = new Handler(Looper.myLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_BACK_KEY:
                    isBackPressed = false;
                    return true;
            }
            return false;
        }
    });

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (!isBackPressed) {
            Toast.makeText(this, R.string.back_pressed_message, Toast.LENGTH_SHORT).show();
            isBackPressed = true;
            mHandler.sendEmptyMessageDelayed(MESSAGE_BACK_KEY, BACK_KEY_TIMEOUT);
        } else {
            mHandler.removeMessages(MESSAGE_BACK_KEY);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
