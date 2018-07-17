package kr.co.plani.fitlab.tripko;


import kr.co.plani.fitlab.tripko.Data.AuthData;
import kr.co.plani.fitlab.tripko.Manager.RealmManager;

/**
 * Created by jihun on 2017-02-16.
 */

public class Constants {
    public static final String TYPE_PASSWORD_GRANT = "password";
    public static final String TYPE_REVOKE_GRANT = "revoke_token";
    public static final String CLIENT_ID = "eRSUzzOgDKNaVSmV6wJPDJA0mALlknYoPc89y8BG";
    public static final String CLIENT_SECRET = "4JpLji5ZQWdpWX390ASE7KOs4LgxGRx6z6xVoAnARIbMNMw4tUJbKDGWiJRIRXcK5nO4KNkseH8xpk0EAi2SHrcwpPaNNLwljswT0LwBTUTaR7DHiQBoVsGOt4pP8mJ2";

    public static String getAccessToken(){
        AuthData authData = RealmManager.getInstance().getRealm().where(AuthData.class).findFirst();
        if (authData!=null) {
            return "bearer " + authData.get_access_token();
        }else{
            return "";
        }
    }
}
