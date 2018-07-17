package kr.co.plani.fitlab.tripko.Login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AuthData;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Manager.PropertyManager;
import kr.co.plani.fitlab.tripko.Manager.RealmManager;
import kr.co.plani.fitlab.tripko.R;
import kr.co.plani.fitlab.tripko.Toolbar.CustomToolbar;
import kr.co.plani.fitlab.tripko.Toolbar.OnLeftMenuClickListener;
import kr.co.plani.fitlab.tripko.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailView, passwordView;
    CustomToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar);
        toolbar.setToolbar(getString(R.string.login_title), "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setPadding(0, Utils.getStatusBarHeight(), 0, 0);
        }
        emailView = (EditText) findViewById(R.id.edit_email);
        passwordView = (EditText) findViewById(R.id.edit_password);

        Button btn = (Button) findViewById(R.id.btn_sign_up);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        btn = (Button) findViewById(R.id.btn_sign_in);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login
                final String username = emailView.getText().toString();
                final String password = passwordView.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    Call<ResponseBody> call = NetworkManager.getInstance().getService().signin(username, password);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                getToken(username, password);
                                PropertyManager.getInstance().setIsLogin(true);
                                PropertyManager.getInstance().setUserNmae(username);
                                PropertyManager.getInstance().setPassword(password);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, R.string.alert_incorrect_login_info, Toast.LENGTH_SHORT).show();
                            } //fail
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            //fail
                        }
                    });
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

    private void getToken(String username, String password) {
        Call<AuthData> data = NetworkManager.getInstance().getService().getToken(Constants.TYPE_PASSWORD_GRANT, Constants.CLIENT_ID, Constants.CLIENT_SECRET, username, password);
        data.enqueue(new Callback<AuthData>() {
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
                                realm.where(AuthData.class).findAll().deleteAllFromRealm();
                                realm.copyToRealm(result);
                            }
                            Log.e("MainActivity", "token: " + result.get_access_token());
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
}
