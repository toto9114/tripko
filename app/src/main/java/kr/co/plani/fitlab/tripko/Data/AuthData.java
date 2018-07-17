package kr.co.plani.fitlab.tripko.Data;

import io.realm.RealmObject;

/**
 * Created by sony on 2016-09-29.
 */

public class AuthData extends RealmObject {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private String scope;

    public String get_access_token() {
        return access_token;
    }

    public String get_refresh_token() {
        return refresh_token;
    }

    public void set_access_token(String access_token) {
        this.access_token = access_token;
    }

    public void set_refresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
