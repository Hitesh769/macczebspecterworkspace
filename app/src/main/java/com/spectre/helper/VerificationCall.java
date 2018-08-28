package com.spectre.helper;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.spectre.BuildConfig;
import com.spectre.R;
import com.spectre.interfaces.VersionCheckCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.spectre.utility.Utility.setLog;


/**
 * Created by pc on 10-02-2017.
 */
public class VerificationCall {
    private Activity context;
    AQuery aq;
    String msg_string = "";

    public VerificationCall(Activity context) {
        this.context = context;
    }

    public void userFriendList(final VersionCheckCallback versionCheckCallback) {
        AQuery aq = new AQuery(context);
        // http://base3.engineerbabu.com/waggingpal/api/v1/index.php/AppVersion?app_version=1
        //  {"status":"success","message":"Successfull","data":[{"id":1,"version":1,"update_at":"2016-02-18"}]}(with valid version code )
        //  {"status":"failed","message":"Plz Update Old Version"} (with invalid version code)
        // {"status":"success","message":"We are currently in maintanance mode"} (under maintenance)

        try {
            String url = "https://mtjf.co/api/api/AppVersion"; //+ "?app_version=" + BuildConfig.VERSION_CODE + "&type=android";
            Map<String, Object> params = new HashMap<>();
            params.put("app_version", BuildConfig.VERSION_CODE + "");
            params.put("type", "android");
           // params.put("user_type", "1");

            setLog("value of url is : " + url);
            setLog("value of parmeter is : " + params);
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    Log.e("result.....", "" + json);
                    if (json != null) {
                        try {
                            msg_string = json.getString("message");
                            if (json.getString("status").equalsIgnoreCase("success")) {
                                if (msg_string.equalsIgnoreCase("Successfull")) {

                                } else if (msg_string.equalsIgnoreCase("We are currently in maintanance mode")) {
                                    versionCheckCallback.onSuccess(1, context.getString(R.string.maintenance));
                                }
                            } else if (json.getString("status").equalsIgnoreCase("failed")) {
                                versionCheckCallback.onSuccess(2, context.getString(R.string.version_));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                    }


                }

            }.method(AQuery.METHOD_POST));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
