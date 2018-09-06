package com.spectre.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.firebase.iid.FirebaseInstanceId;
import com.spectre.BuildConfig;
import com.spectre.LoginModel;
import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.activity_new.MasterAppCompactActivity;
import com.spectre.api.ApiClient;
import com.spectre.api.ApiInterface;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends MasterAppCompactActivity implements View.OnClickListener {

    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;

    private Context context;
    private EditText et_mail, et_pin;
    private Button btnLogIn;
    private TextView tv_forget_pin, tv_signup;
    private String email, password;
    private AlertBox alertBox;
    private String gcmId;
    private int indicator = 0;
    private String type = "0";
    private int forgetType = 0;
    private Dialog dd, dd1;
    private String mobile, newPass, otp;
    private String number;

    // Get start intent for Activity
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_login);
        context = this;
        // Utility.setContentView(context, R.layout.activity_login);
        Utility.setContentView(context, R.layout.act_login);

        // bind view using butter knife
        ButterKnife.bind(this);

        initView();
    }

    /* To initialize view & apply click Listener */
    private void initView() {
        et_mail = findViewById(R.id.et_email);
        et_pin = findViewById(R.id.et_password);
        btnLogIn = findViewById(R.id.btn_login);
        tv_forget_pin = findViewById(R.id.tv_forget_pin);
        tv_signup = findViewById(R.id.tv_signup);
        tv_forget_pin.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
        btnLogIn.setOnClickListener(this);
        alertBox = new AlertBox(context);

        // set title
        txtAppBarTitle.setText(getString(R.string.login));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (areFieldsValid()) {
                    //  login();
                    if (Utility.isConnectingToInternet(context)) {
                        new RegisterGCMId().execute();
                    } else {
                        alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                    }
                }
                // finish();
                break;
            case R.id.tv_signup:
                startActivity(new Intent(context, SignupActivity.class));
                break;
            case R.id.tv_forget_pin:
                //   showToast("Coming Soon");
                resetPassword(et_mail.getText().toString().trim());
                break;
        }
    }


    /*Open reset Password dialog and call reset Password API*/

    private void resetPassword(String number) {

        try {
            if (dd == null) {
                dd = new Dialog(context);
                dd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            }
            dd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dd.setContentView(R.layout.dialog_reset_password);
            dd.setCancelable(true);
            dd.getWindow().setLayout(-1, -2);
            dd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dd.show();


            final EditText etMobileNumber = (EditText) dd.findViewById(R.id.et_mob);
            etMobileNumber.setText(number);
            etMobileNumber.setSelection(number != null ? number.length() : 0);

            ((CustomTextView) dd.findViewById(R.id.txt_header)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd.dismiss();
                    dd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            });

            ((Button) dd.findViewById(R.id.btn_save_changes)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etMobileNumber.setError(null);

                    String regex = "\\d+";

                    if (etMobileNumber.getText().toString().trim().isEmpty()) {
                        etMobileNumber.setError(getString(R.string.blank_field));
                        return;
                    }

                    if (etMobileNumber.getText().toString().trim().matches(regex)) {
                        if (etMobileNumber.getText().toString().length() < 4) {
                            etMobileNumber.setError(getString(R.string.mobile_error));
                            return;
                        }

                        if (etMobileNumber.getText().toString().length() > 16) {
                            etMobileNumber.setError(getString(R.string.mobile_error));
                            return;
                        }
                        forgetType = 1;
                        // sendMessage(2);
                    } else {
                        if (!Utility.emailValidator(etMobileNumber.getText().toString().trim())) {
                            etMobileNumber.setError(getString(R.string.Enter_valid_mail));
                            return;
                        }

                        forgetType = 2;
                        //sendMessage(3);
                    }

                    callResetAPI(dd, etMobileNumber.getText().toString().trim(), 1);
                    // MyDialogProgress.open(context);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*call reset Password API*/

    private void callResetAPI(final Dialog dd, final String trim, final int type) {

        try {
            //Log.e("url.....", "" + url + " " + jsonInput);
            MyDialogProgress.open(context);
            JSONObject jsonInput = new JSONObject();
            jsonInput.put(Constant.USER_MOBILE, trim);
            jsonInput.put(Constant.TYPE, forgetType);
            jsonInput.put(Constant.LANGUAGE, SharedPrefUtils.getPreference(context, Constant.LANGUAGE, "en"));
            new AQuery(context).post(Urls.FORGOT_PASSWORD, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //  Log.e("result.....", "" + json);
                    printLog("result....." + url + " " + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                //  request.onSuccess(json, Message);
                                MyDialogProgress.close(context);
                                if (type == 1) {
                                    dd.dismiss();
                                    mobile = trim;
                                    ChangePassword();
                                } else {
                                    showToast(Message);
                                }

                            } else {
                                //  request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                                showToast(Message);
                                MyDialogProgress.close(context);
                            }
                        } catch (Exception e1) {
                            // request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            showToast(getString(R.string.something_wrong));
                            MyDialogProgress.close(context);
                            e1.printStackTrace();
                        }
                    } else {
                        MyDialogProgress.close(context);
                        nullCase(status, AjaxStatus.NETWORK_ERROR);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getString(R.string.something_wrong));
            MyDialogProgress.close(context);
        }
    }

    /*Show Toast Message method*/
    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /*Check Validation before calling login API*/
    private boolean areFieldsValid() {
        email = et_mail.getText().toString().trim();
        password = et_pin.getText().toString().trim();

        et_mail.setError(null);
        et_pin.setError(null);


        if (et_mail.length() == 0) {
            et_mail.setError(getString(R.string.please_enter_mobile_no));
            return false;
        }

        if (password.length() == 0) {
            et_pin.setError(getString(R.string.enter_valid_pin));
            et_pin.requestFocus();
            return false;
        }
        return true;
    }

    private void callLoginApi() {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        try {

            JSONObject paramObject = new JSONObject();
            paramObject.put(Constant.USER_EMAIL, et_mail.getText().toString().trim());
            paramObject.put(Constant.USER_PASSWORD, et_pin.getText().toString().trim());
            paramObject.put(Constant.USER_DEVICE_TYPE, Constant.ANDROID);
            paramObject.put(Constant.USER_DEVICE_ID, Build.SERIAL);
            paramObject.put(Constant.USER_DEVICE_TOKEN, gcmId);
            paramObject.put(Constant.LANGUAGE, SharedPrefUtils.getPreference(context, Constant.LANGUAGE, "en"));

            Utility.setLog("PARAM :" + paramObject);

            Call<LoginModel> call = service.loginUser(paramObject.toString());
            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    Utility.setLog("Response :" + "success");
//                    Intent intent = new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, type);
//                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    // startActFinish(HomeFormatActivity.getStartIntent(context, type));
                    startActFinish(HomeAct.getStartIntent(context, type));
                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {

                    Utility.setLog("Response :" + "failure");
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /*login API*/
    private void callAPI() {

        if (!Utility.isConnectingToInternet(context)) {
            alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
            return;
        }

        MyDialogProgress.open(context);

        JSONObject jsonObject = new JSONObject();
        number = et_mail.getText().toString().trim();
        if (number.startsWith("0")) {
            number = number.replaceFirst("0", "");
        } else {

        }
        try {
            jsonObject.put(Constant.USER_MOBILE, number);
            jsonObject.put(Constant.USER_PASSWORD, et_pin.getText().toString().trim());
            jsonObject.put(Constant.USER_DEVICE_TYPE, Constant.ANDROID);
            jsonObject.put(Constant.USER_DEVICE_ID, Build.SERIAL);
            jsonObject.put(Constant.USER_DEVICE_TOKEN, gcmId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AqueryCall request = new AqueryCall(this);
        request.postWithoutToken(Urls.LOGIN, jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                saveData(js, msg);
                SharedPrefUtils.setPreference(context, Constant.ISLOGIN, true);
            }
            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                if (msg.equalsIgnoreCase("Otp send Successfully.") || msg.equalsIgnoreCase("OTP sent.")) {
                    SharedPrefUtils.setPreference(context, Constant.USER_MOBILE, et_mail.getText().toString().trim());
                    startActivity(new Intent(context, OTPActivity.class).putExtra(Constant.TYPE, 1));
                } else
                    alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                MyDialogProgress.close(context);
                new AlertBox(context).openMessage(inactive, context.getString(R.string.ok), "", false);
            }
        });
    }

    /*Check Server Error*/
    private void nullCase(AjaxStatus status, int networkError) {
        if (status.getCode() == networkError) {
            //    request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.connection));
            showToast(getString(R.string.connection));
        } else {
            //  request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
            showToast(getString(R.string.something_wrong));
        }
    }

    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        onBackPressed();
    }

    /*Get Device Token Class*/
    class RegisterGCMId extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(appContext);
                // gcmId = gcm.register("182071453535");
                // gcmId = gcm.register("643819484965");
                gcmId = FirebaseInstanceId.getInstance().getToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (!gcmId.equals("")) {
                    callAPI();
                    //callLoginApi();
                    indicator = 0;
                } else {
                    if (indicator <= 2) {
                        indicator++;
                        new RegisterGCMId().execute();
                    } else {
                        MyDialogProgress.close(context);
                        alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                        //    MyDialog_Progress.close(appContext);
                        //    MyDialog.iPhone((getResources().getString(R.string.gcm_id_error)), appContext);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                MyDialogProgress.close(context);
                alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                //   MyDialog_Progress.close(appContext);
                //    MyDialog.iPhone((getResources().getString(R.string.gcm_id_error)), appContext);
            }
            super.onPostExecute(aVoid);
        }
    }

    /*Save Data in Preference*/

    private void saveData(JSONObject js, String msg) {

        /*
       {
    "status": "success",
    "message": "Successfully login",
    "data": {
        "user_id": 299,
        "user_name": "Sumit",
        "user_mobile": 8962493409,
        "user_image": "",
        "user_token": "9998887f01f513d6181702ed8ce1e6521517998290856"
    }
        */


        try {
            JSONObject jsonObject = js.getJSONObject(Constant.DATA);
            SharedPrefUtils.setPreference(context, Constant.USER_ID, jsonObject.getString(Constant.USER_ID));
            SharedPrefUtils.setPreference(context, Constant.USER_NAME, jsonObject.getString(Constant.USER_NAME));
            SharedPrefUtils.setPreference(context, Constant.USER_IMAGE, jsonObject.getString(Constant.USER_IMAGE));
            SharedPrefUtils.setPreference(context, Constant.USER_MOBILE, jsonObject.getString(Constant.USER_MOBILE));
            SharedPrefUtils.setPreference(context, Constant.USER_TOKEN, jsonObject.getString(Constant.USER_TOKEN));

            if (jsonObject.has(Constant.USER_EMAIL))
                SharedPrefUtils.setPreference(context, Constant.USER_EMAIL, jsonObject.getString(Constant.USER_EMAIL));

            if (jsonObject.has(Constant.USER_ADDRESS_))
                SharedPrefUtils.setPreference(context, Constant.USER_ADDRESS_, jsonObject.getString(Constant.USER_ADDRESS_));

            if (jsonObject.has(Constant.CAR_REPAIRE))
                SharedPrefUtils.setPreference(context, Constant.CAR_REPAIRE, jsonObject.getString(Constant.CAR_REPAIRE));

            if (jsonObject.has(Constant.EXPERTISE))
                SharedPrefUtils.setPreference(context, Constant.EXPERTISE, jsonObject.getString(Constant.EXPERTISE));

            if (jsonObject.has(Constant.GARAGE_IMAGE))
                SharedPrefUtils.setPreference(context, Constant.GARAGE_IMAGE, jsonObject.getString(Constant.GARAGE_IMAGE));

            if (jsonObject.has(Constant.MOBILE_CODE))
                SharedPrefUtils.setPreference(context, Constant.MOBILE_CODE, jsonObject.getString(Constant.MOBILE_CODE));

            if (jsonObject.has(Constant.SERVICE_TYPE)) {
                SharedPrefUtils.setPreference(context, Constant.SERVICE_TYPE, jsonObject.getString(Constant.SERVICE_TYPE));
            }

            MyDialogProgress.close(context);

            if (jsonObject.has(Constant.USER_TYPE)) {
                type = jsonObject.getString(Constant.USER_TYPE);
                SharedPrefUtils.setPreference(context, Constant.USER_TYPE, type);
                if (type.equalsIgnoreCase("2")) {
                    // startActFinish(HomeFormatActivity.getStartIntent(context, type));
                    startActFinish(HomeAct.getStartIntent(context, type));
                    return;
                }
            }
            // startActFinish(HomeFormatActivity.getStartIntent(context, type));
            startActFinish(HomeAct.getStartIntent(context, type));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utility.showToast(context, msg);
    }


    public void printLog(String a) {
        if (BuildConfig.DEBUG) {
            Log.e("Error Log", "" + a);
        }
    }

    /*Change Password Dialog & validation*/
    private void ChangePassword() {

        try {
            if (dd1 == null) {
                dd1 = new Dialog(context);
                dd1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dd1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }

            dd1.setContentView(R.layout.dialog_change_password);
            dd1.setCancelable(true);
            dd1.getWindow().setLayout(-1, -2);
            dd1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dd1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dd1.show();


            final EditText etPin = dd1.findViewById(R.id.et_new_pin);
            final EditText etCpin = dd1.findViewById(R.id.et_c_new_pin);
            final EditText etOtp = dd1.findViewById(R.id.et_otp);
            final TextView txtMsg = dd1.findViewById(R.id.txt_msg);
            String s = getResources().getString(R.string.msg, "<font color='#000000'> <b>" + mobile + "</b></font>");
            txtMsg.setText(Html.fromHtml(s));


            ((CustomTextView) dd1.findViewById(R.id.txt_header)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dd1.dismiss();
                    dd1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                }
            });


            ((Button) dd1.findViewById(R.id.btn_resend)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callResetAPI(dd, mobile, 2);
                }
            });


            ((Button) dd1.findViewById(R.id.btn_change_pass)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etPin.setError(null);
                    etCpin.setError(null);
                    etOtp.setError(null);

                    if (etPin.getText().toString().trim().isEmpty()) {
                        etPin.setError(getString(R.string.enter_valid_pin));
                        return;
                    }


                    if (etPin.getText().toString().trim().length() < 6) {
                        etPin.setError(getString(R.string.password_count));
                        return;
                    }

                    if (etCpin.getText().toString().trim().isEmpty()) {
                        etCpin.setError(getString(R.string.enter_valid_cpin));
                        return;
                    }

                    if (etCpin.getText().toString().trim().length() < 6) {
                        etCpin.setError(getString(R.string.con_password_count));
                        return;
                    }


                    if (!etCpin.getText().toString().trim().equalsIgnoreCase(etPin.getText().toString().trim())) {
                        etCpin.setError(getString(R.string.pin_does_not_match));
                        return;
                    }

                    if (etOtp.getText().toString().trim().isEmpty()) {
                        etOtp.setError(getString(R.string.enter_otp));
                        return;
                    }

                    newPass = etPin.getText().toString().trim();
                    otp = etOtp.getText().toString().trim();


                    MyDialogProgress.open(context);
                    new RegisterNewGCMId().execute();
                    // callResetAPI(dd1, etMobileNumber.getText().toString().trim());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*getDevice ID*/
    class RegisterNewGCMId extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(appContext);
                // gcmId = gcm.register("182071453535");
                // gcmId = gcm.register("643819484965");
                gcmId = FirebaseInstanceId.getInstance().getToken();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                if (!gcmId.equals("")) {
                    callChangePassAPI();
                    indicator = 0;
                } else {
                    if (indicator <= 2) {
                        indicator++;
                        new RegisterNewGCMId().execute();
                    } else {
                        MyDialogProgress.close(context);
                        showToast(getString(R.string.connection));
                        //  alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                        //    MyDialog_Progress.close(appContext);
                        //    MyDialog.iPhone((getResources().getString(R.string.gcm_id_error)), appContext);
                    }
                }

            } catch (Exception e) {
                MyDialogProgress.close(context);
                showToast(getString(R.string.connection));
                // alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                //   MyDialog_Progress.close(appContext);
                //    MyDialog.iPhone((getResources().getString(R.string.gcm_id_error)), appContext);
            }
            super.onPostExecute(aVoid);
        }
    }

    /*Change password API call*/
    private void callChangePassAPI() {
      /*  {
            "user_mobile":"8962493409",
                "otp":"1234",
                "user_password":"123456",
                "user_device_type":"andorid",
                "user_device_id":"1",
                "user_device_token":"1"
        }*/
        try {
            //Log.e("url.....", "" + url + " " + jsonInput);
            JSONObject jsonInput = new JSONObject();
            jsonInput.put(Constant.USER_MOBILE, mobile);
            jsonInput.put(Constant.MOBILE_OTP, otp);
            jsonInput.put(Constant.USER_PASSWORD, newPass);
            jsonInput.put(Constant.USER_DEVICE_TYPE, Constant.ANDROID);
            jsonInput.put(Constant.USER_DEVICE_ID, Build.SERIAL);
            jsonInput.put(Constant.USER_DEVICE_TOKEN, gcmId);
            jsonInput.put(Constant.TYPE, forgetType);
            jsonInput.put(Constant.LANGUAGE, SharedPrefUtils.getPreference(context, Constant.LANGUAGE, "en"));
            //    jsonInput.put(Constant.TYPE, gcmId);


            new AQuery(context).post(Urls.FORGOT_PASSWORD_MOBILE, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //  Log.e("result.....", "" + json);
                    printLog("result....." + url + " " + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                //  request.onSuccess(json, Message);
                                MyDialogProgress.close(context);
                                dd1.dismiss();
                                saveData(json, Message);
                            } else {
                                //  request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                                showToast(Message);
                                MyDialogProgress.close(context);
                            }
                        } catch (Exception e1) {
                            // request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            showToast(getString(R.string.something_wrong));
                            MyDialogProgress.close(context);
                            e1.printStackTrace();
                        }
                    } else {
                        MyDialogProgress.close(context);
                        nullCase(status, AjaxStatus.NETWORK_ERROR);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showToast(getString(R.string.something_wrong));
            MyDialogProgress.close(context);
        }


    }

}
