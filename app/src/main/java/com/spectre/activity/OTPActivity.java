package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;
import com.spectre.R;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.helper.AqueryCall;
import com.spectre.hintText.MaskedEditText;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private static MaskedEditText mask_text_verify;
    private String gcmId = "";
    private String otp;
    private AlertBox alertBox;
    private int indicator = 0;
    private CustomTextView txtMsg;
    private String type = "0";
    private CustomRayMaterialTextView btn_resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_otp);
        Utility.setUpToolbar_(context, "<font color='#ffffff'>Verification</font>", true);
        initView();
    }

    private void initView() {
        mask_text_verify = (MaskedEditText) findViewById(R.id.mask_text_verify);
        btn_resend = (CustomRayMaterialTextView) findViewById(R.id.btn_resend);
        btn_resend.setOnClickListener(this);
        txtMsg = (CustomTextView) findViewById(R.id.txt_msg);
        String s = getResources().getString(R.string.verify_number, "<font color='#000000'> <b>" + Utility.getSharedPreferences(context, Constant.USER_MOBILE) + "</b></font>");
        txtMsg.setText(Html.fromHtml(s));
        alertBox = new AlertBox(context);
        mask_text_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null) {
                    // Toast.makeText(context, mask_text_verify.getRawText(), Toast.LENGTH_SHORT).show();
                    //  mask_text_verify.getRawText();
                    String text = editable.toString().replaceAll(" ", "").replaceAll("–", "");
                    if (text.length() == 4 && editable.length() == 10) {
                        //   Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                        otp = mask_text_verify.getRawText();
                        if (Utility.isConnectingToInternet(context)) {
                            new RegisterGCMId().execute();
                        } else {
                            alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                        }
                    }
                }
            }
        });

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mask_text_verify.setText("");
                mask_text_verify.setText("1234");
            }
        }, 1000);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resend:
                callResend();
                break;
        }
    }


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
                MyDialogProgress.close(context);
                alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                //   MyDialog_Progress.close(appContext);
                //    MyDialog.iPhone((getResources().getString(R.string.gcm_id_error)), appContext);
            }
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    private void callAPI() {

        if (!Utility.isConnectingToInternet(context)) {
            alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
            return;
        }

        if (!MyDialogProgress.isOpen(context)) {
            MyDialogProgress.open(context);
        }


        JSONObject jsonObject = new JSONObject();
        /*{
            "user_id":"1",
                "mobile_otp":"123456",
                "user_device_type":"android",
                "user_device_id":"13131665464666",
                "user_device_token":"sdf4asd6f4s6fs6f4sd6f4sad6fsd6f4sdf6sdf65s4fs65fs46fs654f654sdf465dsf"
        }*/
        try {
            jsonObject.put(Constant.USER_MOBILE, Utility.getSharedPreferences(context, Constant.USER_MOBILE));
            jsonObject.put(Constant.MOBILE_OTP, otp);
            jsonObject.put(Constant.USER_DEVICE_TYPE, Constant.ANDROID);
            jsonObject.put(Constant.USER_DEVICE_ID, Build.SERIAL);
            jsonObject.put(Constant.USER_DEVICE_TOKEN, gcmId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        AqueryCall request = new AqueryCall(this);
        request.postWithoutToken(Urls.OTP_VERIFICATION, jsonObject, new RequestCallback() {

            @Override
            public void onSuccess(JSONObject js, String msg) {
                saveData(js);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
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

    private void saveData(JSONObject js) {

        /*   "data": {
        "user_id": 300,
        "user_name": "Sumit",
        "user_mobile": 8962494409,
"user_type":1
        "user_image": "",
        "user_token": "d76fcf4d5bb41b6c458f0dccf1216adf1517996204645"
    }*/
        try {
            JSONObject jsonObject = js.getJSONObject(Constant.DATA);
            Utility.setSharedPreference(context, Constant.USER_ID, jsonObject.getString(Constant.USER_ID));
            Utility.setSharedPreference(context, Constant.USER_NAME, jsonObject.getString(Constant.USER_NAME));
            Utility.setSharedPreference(context, Constant.USER_IMAGE, jsonObject.getString(Constant.USER_IMAGE));
            Utility.setSharedPreference(context, Constant.USER_MOBILE, jsonObject.getString(Constant.USER_MOBILE));
            Utility.setSharedPreference(context, Constant.USER_TOKEN, jsonObject.getString(Constant.USER_TOKEN));

            if (jsonObject.has(Constant.USER_EMAIL))
                Utility.setSharedPreference(context, Constant.USER_EMAIL, jsonObject.getString(Constant.USER_EMAIL));

            if (jsonObject.has(Constant.USER_ADDRESS_))
                Utility.setSharedPreference(context, Constant.USER_ADDRESS_, jsonObject.getString(Constant.USER_ADDRESS_));

            if (jsonObject.has(Constant.CAR_REPAIRE))
                Utility.setSharedPreference(context, Constant.CAR_REPAIRE, jsonObject.getString(Constant.CAR_REPAIRE));

            if (jsonObject.has(Constant.EXPERTISE))
                Utility.setSharedPreference(context, Constant.EXPERTISE, jsonObject.getString(Constant.EXPERTISE));

            if (jsonObject.has(Constant.GARAGE_IMAGE))
                Utility.setSharedPreference(context, Constant.GARAGE_IMAGE, jsonObject.getString(Constant.GARAGE_IMAGE));

            if (jsonObject.has(Constant.MOBILE_CODE))
                Utility.setSharedPreference(context, Constant.MOBILE_CODE, jsonObject.getString(Constant.MOBILE_CODE));

            if (jsonObject.has(Constant.SERVICE_TYPE)) {
                Utility.setSharedPreference(context, Constant.SERVICE_TYPE, jsonObject.getString(Constant.SERVICE_TYPE));
            }

            if (jsonObject.has(Constant.USER_TYPE)) {
                type = jsonObject.getString(Constant.USER_TYPE);
                Utility.setSharedPreference(context, Constant.USER_TYPE, type);

                MyDialogProgress.close(context);

                if (type.equalsIgnoreCase("2")) {
                    //Intent intent = new Intent(context, GarageHomeActivity.class);
                    Intent intent = new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, type);
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    return;
                }
            }

            Intent intent = new Intent(context, HomeActivity.class).putExtra(Constant.TYPE, type);

            // Intent intent = new Intent(context, HomeActivity.class);
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callResend() {

        if (!Utility.isConnectingToInternet(context)) {
            alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
            return;
        }

        if (!MyDialogProgress.isOpen(context)) {
            MyDialogProgress.open(context);
        }


        JSONObject jsonObject = new JSONObject();
        /*{
            "user_id":"1",
                "mobile_otp":"123456",
                "user_device_type":"android",
                "user_device_id":"13131665464666",
                "user_device_token":"sdf4asd6f4s6fs6f4sd6f4sad6fsd6f4sdf6sdf65s4fs65fs46fs654f654sdf465dsf"
        }*/
        try {
            jsonObject.put(Constant.USER_MOBILE, Utility.getSharedPreferences(context, Constant.USER_MOBILE));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AqueryCall request = new AqueryCall(this);
        request.postWithoutToken(Urls.RESEND_OTP, jsonObject, new RequestCallback() {

            @Override
            public void onSuccess(JSONObject js, String msg) {
                //  saveData(js);
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
                // alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
                //   alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
                //  alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                MyDialogProgress.close(context);
                new AlertBox(context).openMessage(inactive, context.getString(R.string.ok), "", false);
            }
        });
    }
}
