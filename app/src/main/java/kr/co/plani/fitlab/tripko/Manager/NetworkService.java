package kr.co.plani.fitlab.tripko.Manager;

import java.util.List;

import kr.co.plani.fitlab.tripko.Data.AttractionData;
import kr.co.plani.fitlab.tripko.Data.AttractionsResult;
import kr.co.plani.fitlab.tripko.Data.AuthData;
import kr.co.plani.fitlab.tripko.Data.AutoCompleteData;
import kr.co.plani.fitlab.tripko.Data.CurationResult;
import kr.co.plani.fitlab.tripko.Data.FromToData;
import kr.co.plani.fitlab.tripko.Data.ProfileData;
import kr.co.plani.fitlab.tripko.Data.RecordData;
import kr.co.plani.fitlab.tripko.Data.RecordResult;
import kr.co.plani.fitlab.tripko.Data.RouteData;
import kr.co.plani.fitlab.tripko.Data.TotalRecordResult;
import kr.co.plani.fitlab.tripko.Data.TripMatePost;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by toto9114 on 2017-01-20.
 */

public interface NetworkService {
    @GET("api/attraction/{id}/")
    Call<AttractionData> getAttraction(@Header("Authorization") String accessToken, @Path("id") int id);

    @GET("api/attraction/")
    Call<AttractionsResult> getAttractionList(@Header("Authorization") String accessToken, @Query("page") int page);

    @GET("api/auto_complete/")
    Call<AutoCompleteData> getAutoText(@Header("Authorization") String accessToken, @Query("text") String text);

    @GET("api/recommend/")
    Call<AttractionsResult> getRecommendList(@Header("Authorization") String accessToken);

    @GET("api/record/")
    Call<RecordResult> getMyRecord(@Header("Authorization") String accessToken, @Query("page") int page);

    @POST("api/upload_record/")
    Call<RecordData> uploadRoute(@Header("Authorization") String accessToken, @Body RouteData routeData);

    @GET("api/curation/")
    Call<CurationResult> getCuration(@Header("Authorization") String accessToken, @Query("name") String name);

    @GET("api/curation_route/")
    Call<TotalRecordResult> getCurationRoute(@Header("Authorization") String accessToken);

    @DELETE("api/record/")
    Call<ResponseBody> deleteRecords(@Header("Authorization") String accessToken, @Query("id") List<Integer> idList);

    @GET("api/find_record/")
    Call<TotalRecordResult> findRecord(@Header("Authorization") String accessToken, @Query("id") int id);

    @PUT("api/record/{id}/")
    Call<RecordData> updateRecord(@Header("Authorization") String accessToken, @Path("id") int id, @Body RouteData routeData);

    @FormUrlEncoded
    @POST("api/save_record/")
    Call<ResponseBody> copyRecord(@Header("Authorization") String accessToken, @Field("id") int id);

    @FormUrlEncoded
    @PUT("api/locale/")
    Call<ResponseBody> updateLocale(@Header("Authorization") String accessToken, @Field("locale") String locale);

    @FormUrlEncoded
    @POST("/o/token/")
    Call<AuthData> getToken(@Field("grant_type") String grantType, @Field("client_id") String clientId
            , @Field("client_secret") String clientSecret, @Field("username") String userName, @Field("password") String password);

    @FormUrlEncoded
    @POST("/o/revoke_token/")
    Call<ResponseBody> revokeToken(@Field("grant_type") String grantType, @Field("client_id") String clientId
            , @Field("client_secret") String clientSecret, @Field("token") String accessToken);

    @FormUrlEncoded
    @POST("/api/anonymous/")
    Call<ResponseBody> anonymousSignUp(@Field("username") String deviceToken);

    @FormUrlEncoded
    @POST("api/signup/")
    Call<ResponseBody> signup(@Header("Authorization") String accessToken, @Field("username") String username, @Field("password") String password, @Field("confirm_password") String confirm_password);

    @FormUrlEncoded
    @POST("/api/signin/")
    Call<ResponseBody> signin(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/check_attraction/")
    Call<AttractionData> checkAttraction(@Header("Authorization") String accessToken, @Field("name") String name);

    @GET("api/distance/")
    Call<FromToData> getDistance(@Header("Authorization") String accessToken, @Query("origins") int origins, @Query("destinations") int destinations);

    @POST("api/tripmate/")
    Call<TripMatePost> uploadTripMatePost(@Header("Authorization") String accessToken, @Body TripMatePost post);

    @FormUrlEncoded
    @POST("api/join/")
    Call<ResponseBody> requestJoin(@Header("Authorization") String accessToken, @Field("post_id") int postId);

    @Multipart
    @PUT("api/profiles/{id}/")
    Call<ProfileData> uploadProfile(@Header("Authorization") String accessToken, @Path("id") long id, @Part MultipartBody.Part requestBody);


//    @Headers({
//            "Accept: application/json",
//            "appKey: 4fa24891-48c0-3616-8cd5-a86e8d323d41"
//    })
//    @FormUrlEncoded
//    @POST("https://apis.skplanetx.com/tmap/routes")
//    Call<ResponseBody> getDistance(@Query("version") int version, @Field("resCoordType") String resCoordType, @Field("reqCoordType") String reqCoordType, @Field("startX") String startX, @Field("startY") String startY, @Field("endX") String endX, @Field("endY") String endY);

}
