package com.spectre.activity_new;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.spectre.R;
import com.spectre.api.EndPoints;
import com.spectre.api.RequestParam;
import com.spectre.application.SpecterApplication;
import com.spectre.customView.AlertBox;
import com.spectre.dialog.ProgressDialog;
import com.spectre.model.LoginModel;
import com.spectre.other.Constant;
import com.spectre.utility.AppConstants;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spectre.utility.Utility.makeToast;
import static com.spectre.utility.Utility.setLog;
import static com.spectre.utility.Utility.setToast;

public class LoginAct extends MasterAppCompactActivity {

    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;
    @BindView(R.id.edtPhone)
    EditText edtPhone;
    @BindView(R.id.edtPassword)
    EditText edtPassword;

    // screen context
    private Context context;

    // progress dialog
    private Dialog dialog;

    private String gcmId;
    private int indicator = 0;
    private AlertBox alertBox;

    // Get start intent for Activity
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginAct.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

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
        context = LoginAct.this;

        // init progress dialog object
        dialog = ProgressDialog.createProgressDialog(context);

        // set title
        txtAppBarTitle.setText(getString(R.string.login));

        alertBox = new AlertBox(context);
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

    private boolean validation() {
        String email = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (edtPhone.length() == 0) {
            makeToast(context, getString(R.string.please_enter_mobile_no));
            edtPhone.requestFocus();
            return false;
        }

        if (password.length() == 0) {
            makeToast(context, getString(R.string.enter_valid_pin));
            edtPassword.requestFocus();
            return false;
        }
        return true;
    }

    /*Get Device Token Class*/
    class RegisterGCMId extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

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
                    userLogin();
                    //callLoginApi();
                    indicator = 0;
                } else {
                    if (indicator <= 2) {
                        indicator++;
                        new RegisterGCMId().execute();
                    } else {
                        // dismiss progress dialog after getting response
                        dismissProgressDialog();
                        alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // dismiss progress dialog after getting response
                dismissProgressDialog();
                alertBox.openMessage(getString(R.string.connection), "Ok", "", false);
            }
            super.onPostExecute(aVoid);
        }
    }

    private void saveData(LoginModel loginModel, String msg) {
        SharedPrefUtils.setPreference(context, Constant.USER_ID, loginModel.data.userId);
        SharedPrefUtils.setPreference(context, Constant.USER_NAME, loginModel.data.userName);
        SharedPrefUtils.setPreference(context, Constant.USER_IMAGE, loginModel.data.userImage);
        SharedPrefUtils.setPreference(context, Constant.USER_MOBILE, loginModel.data.userMobile);
        SharedPrefUtils.setPreference(context, Constant.USER_TOKEN, loginModel.data.userToken);

        if (loginModel.data.userEmail != null)
            SharedPrefUtils.setPreference(context, Constant.USER_EMAIL, loginModel.data.userEmail);

        if (loginModel.data.userAddress != null)
            SharedPrefUtils.setPreference(context, Constant.USER_ADDRESS_, loginModel.data.userAddress);

        if (loginModel.data.carRepaire != null)
            SharedPrefUtils.setPreference(context, Constant.CAR_REPAIRE, loginModel.data.carRepaire);

        if (loginModel.data.expertise != null)
            SharedPrefUtils.setPreference(context, Constant.EXPERTISE, loginModel.data.expertise);

        if (loginModel.data.garageImage != null)
            SharedPrefUtils.setPreference(context, Constant.GARAGE_IMAGE, loginModel.data.garageImage);

        if (loginModel.data.mobileCode != null)
            SharedPrefUtils.setPreference(context, Constant.MOBILE_CODE, loginModel.data.mobileCode);

        if (loginModel.data.serviceType != null) {
            SharedPrefUtils.setPreference(context, Constant.SERVICE_TYPE, loginModel.data.serviceType);
        }

        // dismiss progress dialog after getting response
        dismissProgressDialog();

        String type = "";
        if (loginModel.data.userType != null) {
            type = loginModel.data.userType;
            SharedPrefUtils.setPreference(context, Constant.USER_TYPE, type);
//            if (type.equalsIgnoreCase("2")) {
//                // startActFinish(HomeFormatActivity.getStartIntent(context, type));
//                startActClearTop(HomeAct.getStartIntent(context, type));
//                return;
//            }
        }
        // startActFinish(HomeFormatActivity.getStartIntent(context, type));
        startActClearTop(HomeAct.getStartIntent(context, type));

        Utility.showToast(context, msg);
    }
    /* [END] - User define function */

    /* [START] - Butter knife listener */
    /*@OnClick({R.id.imgBack, R.id.btnSignIn, R.id.txtForgotPassword, R.id.txtSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.btnSignIn:
                if(validation()) {
                    new RegisterGCMId().execute();
                }
                break;
            case R.id.txtForgotPassword:
                break;
            case R.id.txtSignUp:
                startActFinish(SignUpAct.getStartIntent(context));
                break;
        }
    }*/
    /* [END] - Butter knife listener */

    /* [START] - All Volley request */
    private void userLogin() {
        String number = edtPhone.getText().toString().trim();
        if (number.startsWith("0")) {
            number = number.replaceFirst("0", "");
        }

        if (Utility.isInternetAvailable(context)) {
            // show progress dialog before call api
            showProgressDialog();
            // display api in log
            setLog("API : " + EndPoints.URL_LOGIN);

            Map<String, String> params = new HashMap<>();

            params.put(RequestParam.USER_MOBILE, number);
            params.put(RequestParam.USER_PASSWORD,  edtPassword.getText().toString().trim());
            params.put(RequestParam.USER_DEVICE_TYPE, AppConstants.ANDROID);
            params.put(RequestParam.USER_DEVICE_ID, Build.SERIAL);
            params.put(RequestParam.USER_DEVICE_TOKEN, gcmId);
            params.put(RequestParam.LANGUAGE, AppConstants.ENGLISH);

            JSONObject parameters = new JSONObject(params);
            setLog("PARAM : " + parameters);

            // call volley json object request
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    EndPoints.URL_LOGIN, parameters,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // check response is null or not
                                if (response != null) {
                                    // print api response in log
                                    setLog("Response : " + response);

                                    // convert response string to model class using Gson
                                    LoginModel model = new Gson().fromJson(String.valueOf(response), LoginModel.class);
                                    String status = model.status; // get response status
                                    String message = model.message;
                                    if (status.equalsIgnoreCase(AppConstants.SUCCESS)) {
                                        saveData(model, message);
                                    } else {
                                        // dismiss progress dialog after getting response
                                        dismissProgressDialog();
                                        makeToast(context, message);
                                    }
                                }
                            } catch (Exception ex) {
                                // dismiss progress dialog after getting response
                                dismissProgressDialog();

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
    /* [END] - All Volley request */
}
