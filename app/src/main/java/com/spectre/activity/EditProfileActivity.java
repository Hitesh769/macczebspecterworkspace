package com.spectre.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.soundcloud.android.crop.Crop;
import com.spectre.R;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.ConvetBitmap;
import com.spectre.utility.PermissionsUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private CustomEditText etName, etEmail, etAddress, etExpertise, etCarRepaired;
    CustomTextView etMob, edit_banner;
    private CircleImageView ivProfile;
    private ImageView btnCamera;
    private AlertBox alertBox;

    private String TAG = EditProfileActivity.class.getName();
    private static final int PICK_FROM_FILE = 2;
    private static final int PICK_FROM_CAMERA = 1;
    public int isForCamera = 0;
    private Uri mImageCaptureUri;
    private String mCurrentPhotoPath;
    private byte[] byteArray1, byteArray2;
    private Bitmap bitmap;
    private LinearLayout ll_is_garage;
    private ImageView imv_banner;
    private int imageType = -1;
    private AppCompatRadioButton radio_main, radio_repair, radio_repair_service, radio_both;
    private String radioInput = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_edit_profile);
        context = this;
        Utility.setContentView(context, R.layout.activity_edit_profile);
        //  Utility.setUpToolbarWithColor(context, "<font color='#ffffff'>Change Password</font>", true);
        Utility.setUpToolbarWithColor(context, "<font color='#ffffff'>"+getString(R.string.update_profile)+"</font>", getResources().getColor(R.color.transparent));
        initView();
    }

    private void initView() {
        //  Utility.showToast(context, "Coming Soon");
        etName = (CustomEditText) findViewById(R.id.et_name);
        etEmail = (CustomEditText) findViewById(R.id.et_email);
        etMob = (CustomTextView) findViewById(R.id.et_mob);
        edit_banner = (CustomTextView) findViewById(R.id.edit_banner);
        etExpertise = (CustomEditText) findViewById(R.id.et_expertise);
        etCarRepaired = (CustomEditText) findViewById(R.id.et_repair);
        ll_is_garage = (LinearLayout) findViewById(R.id.ll_is_garage);



      /*etMob.setEnabled(false);
        etMob.setFocusable(false);
        etMob.setLongClickable(false);*/

        etAddress = (CustomEditText) findViewById(R.id.et_address);
        ivProfile = (CircleImageView) findViewById(R.id.iv_profile);
        btnCamera = (ImageView) findViewById(R.id.btn_camera);
        imv_banner = (ImageView) findViewById(R.id.imv_banner);

        etName.setText(Utility.getSharedPreferences(context, Constant.USER_NAME));
        etEmail.setText(Utility.getSharedPreferences(context, Constant.USER_EMAIL));
        etMob.setText(Utility.getSharedPreferences(context, Constant.USER_MOBILE));
        etAddress.setText(Utility.getSharedPreferences(context, Constant.USER_ADDRESS_));

        ivProfile.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        edit_banner.setOnClickListener(this);

        radio_main = (AppCompatRadioButton) findViewById(R.id.radio_main);
        radio_repair = (AppCompatRadioButton) findViewById(R.id.radio_repair);
        radio_both = (AppCompatRadioButton) findViewById(R.id.radio_both);
        radio_repair_service = (AppCompatRadioButton) findViewById(R.id.radio_repair_service);

        AppCompatRadioButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radio_main.setChecked(radio_main == buttonView);
                    radio_repair.setChecked(radio_repair == buttonView);
                    radio_both.setChecked(radio_both == buttonView);
                    radio_repair_service.setChecked(radio_repair_service == buttonView);

                    if (radio_both.isChecked()) radioInput = "0";
                    if (radio_main.isChecked()) radioInput = "1";
                    if (radio_repair.isChecked()) radioInput = "2";
                    if (radio_repair_service.isChecked()) radioInput = "3";
                }
            }
        };
        radio_main.setOnCheckedChangeListener(listener);
        radio_repair.setOnCheckedChangeListener(listener);
        radio_both.setOnCheckedChangeListener(listener);
        radio_repair_service.setOnCheckedChangeListener(listener);


        alertBox = new AlertBox(context);
        //  ((CustomEditText)findViewById(R.id.et_address)).setText(Utility.getSharedPreferences(context, Constant.USER_MOBILE));
        ((CustomRayMaterialTextView) findViewById(R.id.btn_save_changes)).setOnClickListener(this);


        // Remove Comments to Make things like earlier

        if (!Utility.getSharedPreferences(context, Constant.USER_TYPE).isEmpty() && Utility.getSharedPreferences(context, Constant.USER_TYPE).equalsIgnoreCase("1")) {
            ll_is_garage.setVisibility(View.GONE);
            imv_banner.setVisibility(View.GONE);
            edit_banner.setVisibility(View.GONE);
        } else {
            ll_is_garage.setVisibility(View.VISIBLE);
            etCarRepaired.setText(Utility.getSharedPreferences(context, Constant.CAR_REPAIRE));
            etExpertise.setText(Utility.getSharedPreferences(context, Constant.EXPERTISE));
            imv_banner.setVisibility(View.VISIBLE);
            edit_banner.setVisibility(View.VISIBLE);

            if (Utility.getSharedPreferences(context, Constant.SERVICE_TYPE).equalsIgnoreCase("1")) {
                radio_main.setChecked(true);
                radio_repair.setChecked(false);
                radio_both.setChecked(false);
                radioInput = "1";
            } else if (Utility.getSharedPreferences(context, Constant.SERVICE_TYPE).equalsIgnoreCase("2")) {
                radio_main.setChecked(false);
                radio_repair.setChecked(true);
                radio_both.setChecked(false);
                radioInput = "2";
            } else if (Utility.getSharedPreferences(context, Constant.SERVICE_TYPE).equalsIgnoreCase("0")) {
                radio_main.setChecked(false);
                radio_repair.setChecked(true);
                radio_both.setChecked(true);
                radioInput = "0";
            }

            if (!Utility.getSharedPreferences(context, Constant.GARAGE_IMAGE).isEmpty())
                new AQuery(context).id(imv_banner).image(Utility.getSharedPreferences(context, Constant.GARAGE_IMAGE), true, true, 0, 0);
            else
                ivProfile.setImageResource(R.mipmap.gestuser);
        }


        if (!Utility.getSharedPreferences(context, Constant.USER_IMAGE).isEmpty())
            new AQuery(context).id(ivProfile).image(Utility.getSharedPreferences(context, Constant.USER_IMAGE), true, true, 0, R.mipmap.gestuser);
        else
            ivProfile.setImageResource(R.mipmap.gestuser);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera:
            case R.id.iv_profile:
                imageType = 1;
                alertBox.openDialogImage();
                break;
            case R.id.btn_save_changes:
                checkValidation();
                break;
            case R.id.edit_banner:
                // checkValidation();
                imageType = 2;
                alertBox.openDialogImage();

                break;
        }
    }

    private void checkValidation() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String expertise = etExpertise.getText().toString().trim();
        String carRepaired = etCarRepaired.getText().toString().trim();

        etAddress.setError(null);
        etEmail.setError(null);
        etName.setError(null);

        if (name.isEmpty()) {
            etName.setError(getString(R.string.enter_valid_name));
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.Enter_valid_mail));
            return;
        }

        if (!Utility.emailValidator(email.trim())) {
            etEmail.setError(getString(R.string.Enter_valid_mail));
            return;
        }

        if (address.isEmpty()) {
            etAddress.setError(getString(R.string.please_enter_address));
            return;
        }

        if (ll_is_garage.getVisibility() == View.VISIBLE) {
            etCarRepaired.setError(null);
            etExpertise.setError(null);


            if (expertise.isEmpty()) {
                etExpertise.setError(getString(R.string.enter_expertise));
                return;
            }

            if (carRepaired.isEmpty()) {
                etCarRepaired.setError(getString(R.string.enter_car_repair));
                return;
            }
        } else {
            etExpertise.setText("");
            etCarRepaired.setText("");
        }

        clickValidation();

    }

    private void clickValidation() {


        JSONObject jsonObject = new JSONObject();
        MyDialogProgress.open(context);


        try {
            jsonObject.put(Constant.USER_NAME, etName.getText().toString().trim());
            jsonObject.put(Constant.USER_EMAIL, etEmail.getText().toString().trim());
            jsonObject.put(Constant.USER_MOBILE, etMob.getText().toString().trim());
            jsonObject.put(Constant.EXPERTISE, etExpertise.getText().toString().trim());
            jsonObject.put(Constant.CAR_REPAIRE, etCarRepaired.getText().toString().trim());
            if (!Utility.getSharedPreferences(context, Constant.USER_TYPE).isEmpty() && Utility.getSharedPreferences(context, Constant.USER_TYPE).equalsIgnoreCase("1")) {
                jsonObject.put(Constant.SERVICE_TYPE, radioInput + "");
            } else {
                jsonObject.put(Constant.SERVICE_TYPE, radioInput + "");
            }
            String encodedString = "";
            String encodedString1 = "";
            if (byteArray1 != null) {
                encodedString = Base64.encodeToString(byteArray1, Base64.DEFAULT);
            } else {
                encodedString = "";
            }

            if (byteArray2 != null) {
                encodedString1 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
            } else {
                encodedString1 = "";
            }

            jsonObject.put(Constant.image, encodedString);
            jsonObject.put(Constant.GARAGE_IMAGE, encodedString1);
            jsonObject.put(Constant.USER_ADDRESS, etAddress.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AqueryCall(this).postWithJsonToken(Urls.UPDATE_PROFILE, Utility.getSharedPreferences(context, Constant.USER_TOKEN), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                saveResponse(js);
                // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                alertBox.openMessageWithFinish(msg, "Okay", "", false);
                MyDialogProgress.close(context);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                //alertBox.openMessage(msg, "Ok", "", false);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
               /* Intent intent = new Intent(context, AuthDialogActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);*/

                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                //alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                // alertBox.openMessage(msg, "Ok", "", false);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                // MyDialogProgress.close(context);
            }
        });
    }

    private void saveResponse(JSONObject js) {
/*"user_id":326,
"user_name":"Sumit User",
"user_mobile":81090590,
"user_email":"s@yopmail.com","user_address":"",
"user_type":1,
"user_image":"http:\/\/34.208.171.55\/spectre_app\/uploads\/user_image\/img1_1519044633309326.jpeg",
"user_token":"51b4eaf9ce0d9948c9b299cb89651bdf1519024207406"*/

        try {
            JSONObject jsonObject = js.getJSONObject(Constant.DATA);
            Utility.setSharedPreference(context, Constant.USER_ID, jsonObject.getString(Constant.USER_ID));
            Utility.setSharedPreference(context, Constant.USER_NAME, jsonObject.getString(Constant.USER_NAME));
            Utility.setSharedPreference(context, Constant.USER_IMAGE, jsonObject.getString(Constant.USER_IMAGE));
            Utility.setSharedPreference(context, Constant.USER_MOBILE, jsonObject.getString(Constant.USER_MOBILE));
            Utility.setSharedPreference(context, Constant.USER_TOKEN, jsonObject.getString(Constant.USER_TOKEN));

            if (jsonObject.has(Constant.USER_ADDRESS_))
                Utility.setSharedPreference(context, Constant.USER_ADDRESS_, jsonObject.getString(Constant.USER_ADDRESS_));
            if (jsonObject.has(Constant.CAR_REPAIRE))
                Utility.setSharedPreference(context, Constant.CAR_REPAIRE, jsonObject.getString(Constant.CAR_REPAIRE));
            if (jsonObject.has(Constant.EXPERTISE))
                Utility.setSharedPreference(context, Constant.EXPERTISE, jsonObject.getString(Constant.EXPERTISE));

            if (jsonObject.has(Constant.USER_EMAIL))
                Utility.setSharedPreference(context, Constant.USER_EMAIL, jsonObject.getString(Constant.USER_EMAIL));

            if (jsonObject.has(Constant.GARAGE_IMAGE))
                Utility.setSharedPreference(context, Constant.GARAGE_IMAGE, jsonObject.getString(Constant.GARAGE_IMAGE));


            if (jsonObject.has(Constant.SERVICE_TYPE)) {
                Utility.setSharedPreference(context, Constant.SERVICE_TYPE, jsonObject.getString(Constant.SERVICE_TYPE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void pickFromCamera() {
        isForCamera = 1;
        //   isForCamera = true;
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).requiredPermissionsGranted(context)) {
                return;
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT <= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/mtjf.jpg"));
        } else {
           /* mImageCaptureUri = FileProvider.getUriForFile(ProfileActivity.this,
                     "com.mateapp.provider",
                    createImageFile());*/
            mImageCaptureUri = FileProvider.getUriForFile(context, getApplicationContext().getPackageName() + ".provider", createImageFile());
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }


    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public void pickFromGallery() {
        isForCamera = 2;
        // isForCamera = false;
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).isPermissionGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, "Write External Storage", 0)) {
                return;
            }
        }

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_FROM_FILE);

    }


   /* private void beginCrop(Uri source) {
        try {
            Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/mtjfcrop.jpg"));
            Crop.of(source, destination).asSquare().start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void beginCrop(Uri source) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), source);
            int MAX_BITMAP_SIZE = 100 * 1024 * 1024;
            int bitmapSize = bitmap.getByteCount();
            if (bitmapSize > MAX_BITMAP_SIZE) {
                setImage(source);
            } else {
                Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/mtjfcrop.jpg"));
                Crop.of(source, destination).asSquare().start(this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImage(Uri destination) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), destination);
            bitmap = ConvetBitmap.Mytransform(bitmap);
            bitmap = Utility.rotateImage(bitmap, new File(destination.getPath()));
            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, datasecond);
            //  bitmap.compress(Bitmap.CompressFormat.PNG, 20, datasecond);

            if (imageType == 1) {
                byteArray1 = datasecond.toByteArray();
                ivProfile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
            } else {
                byteArray2 = datasecond.toByteArray();
                imv_banner.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
            }


            // img1.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionsUtils.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Log.d("Checking", "permissions: " + Arrays.asList(permissions) + ", grantResults:" + Arrays.asList(grantResults));
                if (PermissionsUtils.getInstance(context).areAllPermissionsGranted(grantResults)) {
                    if (isForCamera == 1) {
                        pickFromCamera();
                    } else if (isForCamera == 2) {
                        pickFromGallery();
                    } else if (isForCamera == 3) {
                        // completeProfileclick();
                    }
                } else {
                    Utility.checkIfPermissionsGranted(context);
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {

            switch (requestCode) {
                case PICK_FROM_CAMERA:
                    beginCrop(mImageCaptureUri);
                    break;
                case PICK_FROM_FILE:
                    mImageCaptureUri = data.getData();
                    beginCrop(mImageCaptureUri);
                    break;
                case Crop.REQUEST_CROP:
                    Uri destination = Crop.getOutput(data);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), destination);
                        bitmap = ConvetBitmap.Mytransform(bitmap);
                        bitmap = Utility.rotateImage(bitmap, new File(destination.getPath()));
                        ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, datasecond);
                        //  bitmap.compress(Bitmap.CompressFormat.PNG, 20, datasecond);
                        //     byteArray1 = datasecond.toByteArray();
                        //  ivProfile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));

                        if (imageType == 1) {
                            byteArray1 = datasecond.toByteArray();
                            ivProfile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
                        } else {
                            byteArray2 = datasecond.toByteArray();
                            imv_banner.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
                        }

                        // img1.setImageBitmap(bitmap);
                        // check();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void check() {
        try {
            JSONObject jsonObject = new JSONObject();
            String encodedString = Base64.encodeToString(byteArray1, Base64.DEFAULT);
            jsonObject.put("image", encodedString);
            new AQuery(context).post("http://35.177.240.239/kishk/seller_Api/update_image", jsonObject, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Log.e("result.....", "" + json);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
