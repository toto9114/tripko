package kr.co.plani.fitlab.tripko.Login;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

import io.realm.Realm;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AuthData;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Manager.RealmManager;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Toolbar.OnLeftMenuClickListener;
import kr.co.plani.fitlab.tripko.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText emailView, passwordView, confirmView;
    CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setToolbar(getString(R.string.sign_up), "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        emailView = (EditText) findViewById(R.id.edit_email);
        passwordView = (EditText) findViewById(R.id.edit_password);
        confirmView = (EditText) findViewById(R.id.edit_confirm_password);

        Button btn = (Button) findViewById(R.id.btn_sign_up);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RealmManager.getInstance().getRealm().where(AuthData.class).findAll().size() == 0) {
                    signup();
                } else {
                    doRealStart();
                }
            }
        });

        toolbar.setOnLeftMenuClickListener(new OnLeftMenuClickListener() {
            @Override
            public void OnLeftMenuClick() {
                finish();
            }
        });

    }

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
        Call<AuthData> call = NetworkManager.getInstance().getService().getToken(Constants.TYPE_PASSWORD_GRANT, Constants.CLIENT_ID, Constants.CLIENT_SECRET, token, token);
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
        if (!TextUtils.isEmpty(Constants.getAccessToken())) {
            Call<ResponseBody> data = NetworkManager.getInstance().getService().updateLocale(Constants.getAccessToken(), locale);
            data.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        doRealStart();
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private void doRealStart() {
        String username = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String confirm_password = confirmView.getText().toString();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirm_password)) {
            Call<ResponseBody> call = NetworkManager.getInstance().getService().signup(Constants.getAccessToken(), username, password, confirm_password);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        finish();
                    } else {

                    }//fail
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
}
