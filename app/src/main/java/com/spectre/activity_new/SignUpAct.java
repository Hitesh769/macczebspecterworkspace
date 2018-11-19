package com.spectre.activity_new;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;
import com.spectre.R;
import com.spectre.activity.LoginActivity;
import com.spectre.api.EndPoints;
import com.spectre.api.RequestParam;
import com.spectre.application.SpecterApplication;
import com.spectre.customView.MyDialogProgress;
import com.spectre.dialog.ProgressDialog;
import com.spectre.model.SignUpModel;
import com.spectre.other.Constant;
import com.spectre.utility.AppConstants;
import com.spectre.utility.ChatService;
import com.spectre.utility.PermissionUtility;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spectre.utility.Utility.getEditTextString;
import static com.spectre.utility.Utility.logout;
import static com.spectre.utility.Utility.makeToast;
import static com.spectre.utility.Utility.setLog;
import static com.spectre.utility.Utility.setToast;

public class SignUpAct extends MasterAppCompactActivity {

    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.txtCountryCode)
    TextView txtCountryCode;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtConfirmPassword)
    EditText edtConfirmPassword;
    @BindView(R.id.rbtBuyer)
    RadioButton rbtBuyer;
    @BindView(R.id.rbtGarage)
    RadioButton rbtGarage;
    @BindView(R.id.chkTerms)
    CheckBox chkTerms;
    @BindView(R.id.ccp)
    CountryCodePicker countryCodePicker;
    @BindView(R.id.rbtOwner)
    RadioButton rbtOwner;
    @BindView(R.id.rbtDealer)
    RadioButton rbtDealer;
    @BindView(R.id.rbtCertifiedPreOwned)
    RadioButton rbtCertifiedPreOwned;
    @BindView(R.id.iv_profile)
    ImageView ivProfile;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.rg_garage)
    RadioGroup rgGarage;
    @BindView(R.id.rg_buyer_garage)
    RadioGroup rgBbuyerGarage;
    @BindView(R.id.iv_remove)
    ImageView ivRemove;
    @BindView(R.id.edtCompanyName)
    EditText edtCompanyName;
    @BindView(R.id.iv_profilel)
    ImageView ivProfilel;
    @BindView(R.id.iv_removel)
    ImageView ivRemovel;
    @BindView(R.id.linupload_doc)
    LinearLayout linuploadDoc;
    @BindView(R.id.input_location)
    EditText input_location;
    @BindView(R.id.imgLocation)
    ImageView imgLocation;
    @BindView(R.id.input_layout_search)
    TextInputLayout inputLayoutSearch;
    @BindView(R.id.llLocation)
    RelativeLayout llLocation;
    @BindView(R.id.linLocation)
    LinearLayout linLocation;
    @BindView(R.id.lindoc)
    LinearLayout lindoc;

    // screen context
    private Context context;
    private String seller_type = "0";
    // progress dialog
    private Dialog dialog;

    private String countryAlpha = "AE", countryCode = "+971";

    //image pick variable
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE = 3;
    private static final int REQUEST_ACESS_CAMERA = 2;
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private static final int PLACE_PICKER_REQUEST = 999;
    private String latitude = "";
    private String longitude = "";
    private Uri uri;
    Bitmap bitmap = null;
    int isDealer = 0;

    // Get start intent for Activity
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SignUpAct.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_up);
        // bind view using butter knife
        ButterKnife.bind(this);
        // init controls
        initControls();
        // set all listener
        setListener();
    }

    /* [START] - User define function */
    private void initControls() {
        // Init screen context
        context = SignUpAct.this;

        // init progress dialog object
        dialog = ProgressDialog.createProgressDialog(context);

        // set title
        txtAppBarTitle.setText(getString(R.string.sign_up));

        rgGarage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getId() == R.id.rbtCertifiedPreOwned) {
                    seller_type = "3";
                    linuploadDoc.setVisibility(View.VISIBLE);
                } else if (rb.getId() == R.id.rbtOwner) {
                    linuploadDoc.setVisibility(View.GONE);
                    bitmap = null;
                    seller_type = "1";
                } else if (rb.getId() == R.id.rbtDealer) {
                    //bitmap = null;
                    linuploadDoc.setVisibility(View.VISIBLE);
                    seller_type = "2";
                }

                // textViewChoice.setText("You Selected " + rb.getText());
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        rgBbuyerGarage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb = (RadioButton) findViewById(checkedId);
                if (rb.getId() == R.id.rbtGarage) {
                    rgGarage.setVisibility(View.VISIBLE);
                    rbtOwner.setChecked(true);
                    linLocation.setVisibility(View.VISIBLE);
                    seller_type = "1";
                } else {
                    seller_type = "0";
                    bitmap = null;
                    linuploadDoc.setVisibility(View.GONE);
                    rgGarage.setVisibility(View.GONE);
                    linLocation.setVisibility(View.GONE);
                    // ivProfile.setVisibility(View.GONE);
                }
                // textViewChoice.setText("You Selected " + rb.getText());
                //Toast.makeText(getApplicationContext(), rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        getLocation();
    }

    private void setListener() {
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                try {
                    txtCountryCode.setText(countryCodePicker.getSelectedCountryCodeWithPlus());
                    countryCode = countryCodePicker.getSelectedCountryCodeWithPlus();
                    countryAlpha = countryCodePicker.getSelectedCountryNameCode();
                    countryCodePicker.setDefaultCountryUsingNameCode(countryCodePicker.getDefaultCountryNameCode());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // show progress dialog
    private void showProgressDialog() {
        if (dialog != null) {
            if (!dialog.isShowing())
                dialog.show();
        }
    }

    // hide progress dialog
    private void dismissProgressDialog() {
        if (dialog != null) {
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private boolean validation() {
        String message = "";
        String name = edtName.getText().toString().trim();
        String mob = edtPhone.getText().toString().trim();
        String mail = edtEmail.getText().toString().trim();
        String pin = edtPassword.getText().toString().trim();
        String cpin = edtConfirmPassword.getText().toString().trim();

        if (name.length() == 0 || name.length() < 1) {
            makeToast(context, getString(R.string.enter_valid_name));
            edtName.requestFocus();
            return false;
        }
        if (mob.trim().length() == 0) {
            makeToast(context, getString(R.string.please_enter_mobile_no));
            edtPhone.requestFocus();
            return false;
        }
        boolean isValid;
        if (!mob.trim().isEmpty()) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(mob.trim(), countryAlpha);
                isValid = phoneUtil.isValidNumber(swissNumberProto);
                PhoneNumberUtil.PhoneNumberType nrtype = phoneUtil.getNumberType(swissNumberProto);
                // PhoneNumberUtil.PhoneNumberType nrtype = phoneUtil.getNumberType(swissNumberProto);
                PhoneNumberUtil.ValidationResult validationResult = phoneUtil.isPossibleNumberForTypeWithReason(swissNumberProto, nrtype);
                if (validationResult.name().equalsIgnoreCase("TOO_SHORT")) {
                    message = "The phone mobileNumber you entered is too short for the country: " + txtCountryCode.getText().toString().trim() + ".";
                    isValid = false;
                } else if (validationResult.name().equalsIgnoreCase("TOO_LONG")) {
                    message = "The phone mobileNumber you entered is too long for the country: " + txtCountryCode.getText().toString().trim() + ".";
                    isValid = false;
                } else if (validationResult.name().equalsIgnoreCase("INVALID_LENGTH")) {
                    //message = "The phone mobileNumber you entered is too long for the country: " + txt_country_name.getText().toString().trim() + ".";
                    isValid = false;
                }
            } catch (Exception e) {
                if (e.getMessage().equalsIgnoreCase("The string supplied did not seem to be a phone mobileNumber.")) {
                    // alertBox.openMessage(getString(R.string.mobile_error), "Ok", "", false);
                    makeToast(context, getString(R.string.mobile_error));
                    return false;
                } else if (e.getMessage().equalsIgnoreCase("The string supplied is too short to be a phone mobileNumber.")) {
                    // alertBox.openMessage("The phone mobileNumber you entered is too short for the country: " + txt_country_name.getText().toString().trim() + ".", "Ok", "", false);
                    makeToast(context, "The phone mobileNumber you entered is too short for the country: " + txtCountryCode.getText().toString().trim() + ".");
                    return false;
                }
                isValid = false;
            }
            if (!isValid) {
                if (message.isEmpty())
                    makeToast(context, getString(R.string.mobile_error));
                else
                    makeToast(context, message);
                return false;
            }
        }
        if (!Utility.emailValidator(mail.trim())) {
            makeToast(context, getString(R.string.Enter_valid_mail));
            edtEmail.requestFocus();
            return false;
        }
        if (pin.length() == 0) {
            makeToast(context, getString(R.string.enter_valid_pin));
            edtPassword.requestFocus();
            return false;
        }
        if (pin.length() < 6) {
            makeToast(context, getString(R.string.password_count));
            edtPassword.requestFocus();
            return false;
        }
        if (cpin.length() == 0) {
            makeToast(context, getString(R.string.enter_valid_cpin));
            edtConfirmPassword.requestFocus();
            return false;
        }
        if (cpin.length() < 6) {
            makeToast(context, getString(R.string.con_password_count));
            edtConfirmPassword.requestFocus();
            return false;
        }
        if (!pin.equals(cpin)) {
            makeToast(context, getString(R.string.pin_does_not_match));
            edtConfirmPassword.requestFocus();
            return false;
        }
        if (!chkTerms.isChecked()) {
            makeToast(context, "Please agree the Terms and Conditions to proceed.");
            return false;
        }
        return true;
    }
    /* [END] - User define function */

    /* [START] - Butter knife listener */
    @OnClick({R.id.imgBack, R.id.txtSignIn, R.id.txtCountryCode, R.id.btnSubmit, R.id.iv_profile, R.id.iv_profilel, R.id.iv_remove, R.id.iv_removel, R.id.input_location, R.id.imgLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSubmit:
                if (validation()) {
                    userSignUp();
                }
                // startActFinish(new Intent(context, OTPAct.class).putExtra(Constant.TYPE, 1));
                break;
            case R.id.txtCountryCode:
                countryCodePicker.launchCountrySelectionDialog();
                break;
            case R.id.txtSignIn:
                // startActFinish(LoginAct.getStartIntent(context));
                startAct(LoginActivity.getStartIntent(context));
                break;
            case R.id.iv_profile:
                isDealer = 1;
                handleCamera(isDealer);
                break;
            case R.id.iv_profilel:
                isDealer = 2;
                handleCamera(isDealer);
                break;
            case R.id.iv_remove:
                ivProfile.setImageResource(R.drawable.default_image);
                ivRemove.setVisibility(View.GONE);
                break;
            case R.id.iv_removel:
                ivProfilel.setImageResource(R.drawable.default_image);
                ivRemovel.setVisibility(View.GONE);
                break;
            case R.id.input_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(SignUpAct.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.imgLocation:
                getLocation();
                break;
        }
    }
    /* [END] - Butter knife listener */

    /* [START] - All Volley request */
    private String mobileNumber = "";

    private void userSignUp() {
        mobileNumber = edtPhone.getText().toString().trim();
        if (mobileNumber.startsWith("0")) {
            mobileNumber = mobileNumber.replaceFirst("0", "");
        }

        if (Utility.isInternetAvailable(context)) {
            // show progress dialog before call api
            showProgressDialog();
            // display api in log
            setLog("API : " + EndPoints.URL_SIGN_UP);

            String encodedImageDataCertificate = "";
            String encodeImageCompanyLogo="";
            if (bitmap != null) {
                encodedImageDataCertificate = getEncoded64ImageStringFromBitmap(bitmap);
            }
            if (bitmap != null) {
                encodeImageCompanyLogo = getEncoded64ImageStringFromBitmap(bitmap);
            }
            Map<String, String> params = new HashMap<>();

            params.put(RequestParam.USER_NAME, getEditTextString(edtName));
            params.put(RequestParam.USER_MOBILE, mobileNumber);
            params.put(RequestParam.USER_EMAIL, getEditTextString(edtEmail));
            params.put(RequestParam.USER_PASSWORD, getEditTextString(edtPassword));
            params.put(RequestParam.USER_TYPE, rbtBuyer.isChecked() ? "1" : "2");
            params.put(RequestParam.SELLER_TYPE, seller_type);
            params.put(RequestParam.CERTIFICATE, encodedImageDataCertificate);
            params.put(RequestParam.LOCATION, input_location.getText().toString());
            params.put(RequestParam.COMPANYNAME, edtCompanyName.getText().toString());
            params.put(RequestParam.MOBILE_CODE, countryCode);
            params.put(RequestParam.LANGUAGE, AppConstants.ENGLISH);
            params.put(Constant.LATITUDE, latitude);
            params.put(Constant.LONGITUDE, longitude);
            params.put(Constant.COMPANYLOGO, encodeImageCompanyLogo);


            JSONObject parameters = new JSONObject(params);
            setLog("PARAM : " + parameters);

            // call volley json object request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_SIGN_UP, parameters,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // dismiss progress dialog after getting response
                            dismissProgressDialog();
                            try {
                                // check response is null or not
                                if (response != null) {
                                    // print api response in log
                                    setLog("Response : " + response);

                                    // convert response string to model class using Gson
                                    SignUpModel model = new Gson().fromJson(String.valueOf(response), SignUpModel.class);
                                    String status = model.status; // get response status
                                    String message = model.message;
                                    if (status.equalsIgnoreCase(AppConstants.SUCCESS)) {

                                        SharedPrefUtils.setPreference(context, Constant.USER_MOBILE, mobileNumber);
                                        // startActivity(new Intent(context, OTPActivity.class).putExtra(Constant.TYPE, 1));

                                        Intent intent=new Intent(getBaseContext(),ChatService.class);
                                        startService(intent);

                                        startActFinish(new Intent(context, OTPAct.class).putExtra(Constant.TYPE, 1));
                                    } else {
                                        makeToast(context, message);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                // print exception error in log
                                setLog("Error : " + ex.getMessage());
                                // display toast message for exception message
                                setToast(context, getString(R.string.toast_something_went_wrong) + "\n" + ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // dismiss progress dialog after getting response
                    dismissProgressDialog();
                    error.getStackTrace();
                    // error
                    setLog("Error.Response : " + error.getMessage());
                }
            });
            // Adding request to request queue
            SpecterApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            // set toast message for internet connection not available
            setToast(context, getString(R.string.toast_cant_connect_to_internet));
        }
    }

    private String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }
    /* [END] - All Volley request */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    uri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    try {
                        BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);
                        options.inJustDecodeBounds = false;
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

                        if (isDealer == 1) {
                            ivProfile.setImageBitmap(bitmap);
                            ivRemove.setVisibility(View.VISIBLE);
                        } else if (isDealer == 2) {
                            ivProfilel.setImageBitmap(bitmap);
                            ivRemovel.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("data")) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    uri = getImageUri(SignUpAct.this, bitmap);
                    File finalFile = new File(getRealPathFromUri(uri));

                    if (isDealer == 1) {
                        ivProfile.setImageBitmap(bitmap);
                        ivRemove.setVisibility(View.VISIBLE);
                    } else if (isDealer == 2) {
                        ivProfilel.setImageBitmap(bitmap);
                        ivRemovel.setVisibility(View.VISIBLE);
                    }

                } else if (data.getExtras() == null) {

                    Toast.makeText(getApplicationContext(),
                            "No extras to retrieve!", Toast.LENGTH_SHORT)
                            .show();

                    BitmapDrawable thumbnail = new BitmapDrawable(
                            getResources(), data.getData().getPath());

                    if (isDealer == 1) {
                        ivProfile.setImageDrawable(thumbnail);
                    } else if (isDealer == 2) {
                        ivProfile.setImageDrawable(thumbnail);
                    }


                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, SignUpAct.this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                Utility.setLog("LAT 2 : " + latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(", ");
                stBuilder.append(address);
                setLog("address : " + address);
                input_location.setText(stBuilder.toString());
                //Arraylist.clear();
                // callMethodEventList(0);
            }
        }
    }

    private String getRealPathFromUri(Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Uri getImageUri(SignUpAct signUpAct, Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(signUpAct.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACESS_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startDilog();
        }
        if (requestCode == REQUEST_ACESS_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCameraApp();
        }
    }

    private void openCameraApp() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(picIntent, CAMERA_REQUEST);
        }
    }

    private void startDilog() {
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(SignUpAct.this);
        myAlertDilog.setTitle("Upload picture option..");
        myAlertDilog.setMessage("Where to upload picture?");
        myAlertDilog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                picIntent.setType("image/*");
                picIntent.putExtra("return_data", true);
                startActivityForResult(picIntent, GALLERY_REQUEST);
            }
        });
        myAlertDilog.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission(Manifest.permission.CAMERA, SignUpAct.this)) {
                        openCameraApp();
                    } else {
                        requestPermission(SignUpAct.this, new String[]{Manifest.permission.CAMERA}, REQUEST_ACESS_CAMERA);
                    }
                } else {
                    openCameraApp();
                }
            }
        });
        myAlertDilog.show();
    }

    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(AppCompatActivity activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            Toast.makeText(activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }

    private void handleCamera(int isDealer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                startDilog();
            } else {
                requestPermission(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_ACESS_STORAGE);
            }
        } else {
            startDilog();
        }
    }

    private void getLocation() {
        // get location
        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
            PermissionUtility.requestPermission(SignUpAct.this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
                    PermissionUtility.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CONSTANT);
        } else {
            Utility.setLog("PERMISSION grant");
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l; // Found best last known location;
                }
            }
            if (bestLocation != null) {
                Utility.setLog("Lat : " + bestLocation.getLatitude() + " - Long : " + bestLocation.getLongitude());
                latitude = String.valueOf(bestLocation.getLatitude());
                Utility.setLog("LAT 1 : " + latitude);
                longitude = String.valueOf(bestLocation.getLongitude());
                Utility.setLog("Lat : " + getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
                input_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

            } else {
                Utility.setLog("Location is null");
            }
        }
    }

    private String getFullAddress(double lat, double lng) {
        Address address = getAddress(context, lat, lng);
        if (address == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(address.getAddressLine(0));
        buffer.append((address.getAdminArea() == null) ? "" : " ," + address.getLocality());
        buffer.append((address.getAdminArea() == null) ? "" : " ," + address.getAdminArea());
        buffer.append((address.getSubLocality() == null) ? "" : " ," + address.getSubLocality());
        buffer.append((address.getCountryName() == null) ? "" : " ," + address.getCountryName());
        buffer.append((address.getPostalCode() == null) ? "" : " ," + address.getPostalCode());
        return String.valueOf(buffer);
    }

    private Address getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        if (!geocoder.isPresent()) {
            // showToast(context, R.string.e_service_not_available);
            return null;
        }

        Address obj = null;
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0)
                obj = addresses.get(0);
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
