package com.spectre.activity_new;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.spectre.R;
import com.spectre.customView.AlertBox;
import com.spectre.dialog.ProgressDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.hintText.MaskedEditText;
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

public class OTPAct extends MasterAppCompactActivity implements View.OnClickListener {

    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;

    private static MaskedEditText mask_text_verify;
    private String gcmId = "";
    private String otp;
    private AlertBox alertBox;
    private int indicator = 0;
    private TextView txtMsg;
    private String type = "0";
    private Button btn_resend;

    // screen context
    private Context context;

    // progress dialog
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_otp);

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
        context = OTPAct.this;

        // init progress dialog object
        dialog = ProgressDialog.createProgressDialog(context);

        // set title
        txtAppBarTitle.setText(getString(R.string.verification));

        mask_text_verify = findViewById(R.id.mask_text_verify);
        btn_resend = findViewById(R.id.btn_resend);
        btn_resend.setOnClickListener(this);
        txtMsg = findViewById(R.id.txt_msg);
        String s = getResources().getString(R.string.verify_number, "<font color='#000000'> <b>" + SharedPrefUtils.getPreference(context, Constant.USER_MOBILE, "") + "</b></font>");
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
    }

    private void setListener() {

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
    /* [END] - User define function */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_resend:
                callResend();
                break;
        }
    }

    /* [START] - Butter knife listener */
    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        onBackPressed();
    }
    /* [END] - Butter knife listener */


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
                        dismissProgressDialog();
                        alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                        //    MyDialog_Progress.close(appContext);
                        //    MyDialog.iPhone((getResources().getString(R.string.gcm_id_error)), appContext);
                    }
                }

            } catch (Exception e) {
                dismissProgressDialog();
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

        showProgressDialog();

        JSONObject jsonObject = new JSONObject();
        /*{
            "user_id":"1",
                "mobile_otp":"123456",
                "user_device_type":"android",
                "user_device_id":"13131665464666",
                "user_device_token":"sdf4asd6f4s6fs6f4sd6f4sad6fsd6f4sdf6sdf65s4fs65fs46fs654f654sdf465dsf"
        }*/
        try {
            jsonObject.put(Constant.USER_MOBILE, SharedPrefUtils.getPreference(context, Constant.USER_MOBILE, ""));
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
                dismissProgressDialog();
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

            }

            @Override
            public void onNull(JSONObject js, String msg) {
                dismissProgressDialog();
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                dismissProgressDialog();
                alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                dismissProgressDialog();
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

            if (jsonObject.has(Constant.USER_TYPE)) {
                type = jsonObject.getString(Constant.USER_TYPE);
                SharedPrefUtils.setPreference(context, Constant.USER_TYPE, type);

                dismissProgressDialog();

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
    }

    private void callResend() {

        if (!Utility.isConnectingToInternet(context)) {
            alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
            return;
        }

        showProgressDialog();

        JSONObject jsonObject = new JSONObject();
        /*{
            "user_id":"1",
                "mobile_otp":"123456",
                "user_device_type":"android",
                "user_device_id":"13131665464666",
                "user_device_token":"sdf4asd6f4s6fs6f4sd6f4sad6fsd6f4sdf6sdf65s4fs65fs46fs654f654sdf465dsf"
        }*/
        try {
            jsonObject.put(Constant.USER_MOBILE, SharedPrefUtils.getPreference(context, Constant.USER_MOBILE, ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AqueryCall request = new AqueryCall(this);
        request.postWithoutToken(Urls.RESEND_OTP, jsonObject, new RequestCallback() {

            @Override
            public void onSuccess(JSONObject js, String msg) {
                //  saveData(js);
               dismissProgressDialog();
                Utility.showToast(context, msg);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                dismissProgressDialog();
                Utility.showToast(context, msg);
                // alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

            }

            @Override
            public void onNull(JSONObject js, String msg) {
                dismissProgressDialog();
                Utility.showToast(context, msg);
                //   alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                dismissProgressDialog();
                Utility.showToast(context, msg);
                //  alertBox.openMessage(msg, "Ok", "", false);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                dismissProgressDialog();
                new AlertBox(context).openMessage(inactive, context.getString(R.string.ok), "", false);
            }
        });
    }
}
