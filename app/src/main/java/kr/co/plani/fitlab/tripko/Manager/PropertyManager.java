package kr.co.plani.fitlab.tripko.Manager;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import kr.co.plani.fitlab.tripko.MyApplication;


/**
 * Created by jihun on 2017-02-16.
 */

public class PropertyManager {
    private static PropertyManager instance;

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPref;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        mEditor = mPref.edit();
    }

    private static final String REG_ACCESS_TOKEN = "access_token";

    public void setAccessToken(String token) {
        mEditor.putString(REG_ACCESS_TOKEN, token);
        mEditor.commit();
    }

    public String getAccessToken() {
        return mPref.getString(REG_ACCESS_TOKEN, "");
    }

    private static final String REG_IS_LOGIN = "isLogin";

    public void setIsLogin(boolean isLogin) {
        mEditor.putBoolean(REG_IS_LOGIN, isLogin);
        mEditor.commit();
    }

    public boolean isLogin() {
        return mPref.getBoolean(REG_IS_LOGIN, false);
    }

    private static final String REG_USERNAME = "username";

    public void setUserNmae(String userNmae) {
        mEditor.putString(REG_USERNAME, userNmae);
        mEditor.commit();
    }

    public String getUsername() {
        return mPref.getString(REG_USERNAME, "");
    }

    private static final String REG_PASSWORD = "password";

    public void setPassword(String password) {
        mEditor.putString(REG_PASSWORD, password);
        mEditor.commit();
    }

    public String getPassword() {
        return mPref.getString(REG_PASSWORD, "");
    }

    private static final String REG_IS_FIRST = "isFirst";

    public void setFirst(boolean isFirst){
        mEditor.putBoolean(REG_IS_FIRST,isFirst);
        mEditor.commit();
    }

    public boolean isFirst(){
        return mPref.getBoolean(REG_IS_FIRST,true);
    }
}
