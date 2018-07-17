package kr.co.plani.fitlab.tripko.Settings;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;
import kr.co.plani.fitlab.tripko.Constants;
import kr.co.plani.fitlab.tripko.Data.AuthData;
import kr.co.plani.fitlab.tripko.Data.ProfileData;
import kr.co.plani.fitlab.tripko.Login.LoginActivity;
import kr.co.plani.fitlab.tripko.Manager.NetworkManager;
import kr.co.plani.fitlab.tripko.Manager.PropertyManager;
import kr.co.plani.fitlab.tripko.Manager.RealmManager;
import kr.co.plani.fitlab.tripko.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }

    TextView loginView, opensourceView, versionView;
    private static final int PICK_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        loginView = (TextView) view.findViewById(R.id.text_login);
        opensourceView = (TextView) view.findViewById(R.id.text_opensource);
        versionView = (TextView) view.findViewById(R.id.text_version);
        ImageView profileView = (ImageView) view.findViewById(R.id.image_profile);
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, ""), PICK_IMAGE);
            }
        });
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_text = loginView.getText().toString();
                if (btn_text.equals(getString(R.string.login_title))) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    //logout
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.logout_title)
                            .setMessage(R.string.logout_message)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logout();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });

        opensourceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), OpenSourceLicenseActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void logout() {
        Call<ResponseBody> call = NetworkManager.getInstance().getService().revokeToken(Constants.TYPE_REVOKE_GRANT
                , Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.getAccessToken());
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    RealmManager.getInstance().getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(AuthData.class).findAll().deleteAllFromRealm();
                            loginView.setText(R.string.login_title);
                            PropertyManager.getInstance().setIsLogin(false);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void initData() {
        PackageManager packageManager = getActivity().getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_META_DATA);
            versionView.setText("Version: " + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (PropertyManager.getInstance().isLogin()) {
            loginView.setText(R.string.logout_title);
        } else {
            loginView.setText(R.string.login_title);
        }
    }

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode != Activity.RESULT_CANCELED) {
            Uri selectedImageUri = data.getData();
            Cursor c = getContext().getContentResolver().query(selectedImageUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (!c.moveToNext()) {
                c.close();
                return;
            }
            String path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            c.close();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bmp = resizeBitmapImage(BitmapFactory.decodeFile(path, bmOptions), 256);
            File file = bmpToFile(bmp);

//            File file = new File(temp_file,"image.png");


            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));


            Call<ProfileData> call = NetworkManager.getInstance().getService().uploadProfile(Constants.getAccessToken(), 1, body);
            call.enqueue(new Callback<ProfileData>() {
                @Override
                public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {
                    if (response.isSuccessful()) {
                        Log.e("Setting", "profile upload success");
//                        Glide.with(MyApplication.getContext()).load(response.bo)
                    }
                }

                @Override
                public void onFailure(Call<ProfileData> call, Throwable t) {

                }
            });
        }

    }

    private static final String FILE_NAME = "image/png";

    private File bmpToFile(Bitmap bmp) {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pafarm");
        File saveFile = new File(dir, FILE_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            if (bmp != null) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                bmp.recycle();
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveFile;
    }

    public Bitmap resizeBitmapImage(
            Bitmap bmpSource, int maxResolution) {
        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이
        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이
        int newWidth = iWidth;
        int newHeight = iHeight;
        float rate = 0.0f;

        //이미지의 가로 세로 비율에 맞게 조절
        if (iWidth > iHeight) {
            if (maxResolution < iWidth) {
                rate = maxResolution / (float) iWidth;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        } else {
            if (maxResolution < iHeight) {
                rate = maxResolution / (float) iHeight;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(
                bmpSource, newWidth, newHeight, true);
    }
}
