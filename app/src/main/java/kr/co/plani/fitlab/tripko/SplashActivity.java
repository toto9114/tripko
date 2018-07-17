package kr.co.plani.fitlab.tripko;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;

import java.util.Locale;

import io.realm.Realm;
import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Data.AuthData;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Manager.PropertyManager;
import kr.co.plani.fitlab.tripko.Manager.RealmManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private static final int MESSAGE_BACK_KEY_TIME = 1000;
    private static final int RC_SIGN_IN = 100;
    private static  final String TAG = "SplashActivity";
    Handler mHandler = new Handler(Looper.myLooper());

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.e(TAG, "test");
//        mUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//            @Override
//            public void onComplete(@NonNull Task<GetTokenResult> task) {
//                if(task.isSuccessful()){
//                    String idToken = task.getResult().getToken();
//                    Log.e(TAG, idToken);
//                }else{
//                    Log.e(TAG, task.getException().toString());
//                }
//            }
//        });

//        throw new RuntimeException("this is a crash");
//        startActivityForResult(
//                AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(
//                        Arrays.asList(
//                                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
//                        )
//                ).build(),
//                RC_SIGN_IN
//        );

        imageView = (ImageView) findViewById(R.id.image_test);

        Call<AttractionData> call = NetworkManager.getInstance().getService().getAttraction("bearer vmWpPW3tx20Ybw5aN2e8UU0MlUTyRB", 1);
        call.enqueue(new Callback<AttractionData>() {
            @Override
            public void onResponse(Call<AttractionData> call, Response<AttractionData> response) {
                if(response.isSuccessful()){
                    AttractionData data = response.body();
                    Glide.with(MyApplication.getContext()).load(data.image_url).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<AttractionData> call, Throwable t) {

            }
        });

//        mHandler.postDelayed(runnable, MESSAGE_BACK_KEY_TIME);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            signup();
        }
    };

    String token = null;

    private void signup() {
        token = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("MainActivity", token);
        Call<ResponseBody> data = NetworkManager.getInstance().getService().anonymousSignUp(token);
        data.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    //success
                } else {
                    //fail
                }
                getToken();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getToken() {
        Call<AuthData> call = null;
        if (PropertyManager.getInstance().isLogin()) {
            String username = PropertyManager.getInstance().getUsername();
            String password = PropertyManager.getInstance().getPassword();
            call = NetworkManager.getInstance().getService().getToken(Constants.TYPE_PASSWORD_GRANT, Constants.CLIENT_ID, Constants.CLIENT_SECRET, username, password);
        } else {
            call = NetworkManager.getInstance().getService().getToken(Constants.TYPE_PASSWORD_GRANT, Constants.CLIENT_ID, Constants.CLIENT_SECRET, token, token);
        }
        call.enqueue(new Callback<AuthData>() {
            @Override
            public void onResponse(Call<AuthData> call, Response<AuthData> response) {
                final AuthData result = response.body();
                if (result != null) {
                    RealmManager.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            AuthData data = realm.where(AuthData.class).findFirst();
                            if (data != null) {
                                data.set_access_token(result.get_access_token());
                                data.set_refresh_token(result.get_refresh_token());
                            } else {
                                realm.copyToRealm(result);
                            }
                            Log.e("MainActivity", "token: " + result.get_access_token());
                            Log.e("MainActivity", "token: " + result.get_refresh_token());
                            setLocale();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AuthData> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }


    private void setLocale() {
        String locale = Locale.getDefault().getLanguage();
        if (!locale.equals("ko") | locale.equals("en") | locale.equals("zh")) {
            locale = "en";
        }
        if (!TextUtils.isEmpty(Constants.getAccessToken())) {
            Call<ResponseBody> data = NetworkManager.getInstance().getService().updateLocale(Constants.getAccessToken(), locale);
            data.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        doRealStart();
                    } else {
                        //fail
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse respone = IdpResponse.fromResultIntent(data);
            if(resultCode == ResultCodes.OK){
//                startActivity(MainActivity.createIntent(this,response));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void doRealStart() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        mHandler.removeCallbacks(runnable);
        finish();
    }
}
