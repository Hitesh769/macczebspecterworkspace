package com.spectre.helper;

import android.app.Activity;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.spectre.BuildConfig;
import com.spectre.R;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.interfaces.RequestCallback;
import com.spectre.interfaces.RequestCallbackVersion;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;

import org.json.JSONObject;

import java.util.Map;


public class AqueryCall {

    private Activity context;
    AQuery aq;

    public AqueryCall(Activity context) {
        this.context = context;
    }


    private void nullCase(AjaxStatus status, int networkError, RequestCallback request) {
        if (status.getCode() == networkError) {
            request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.connection));
        } else {
            request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    private void nullCase(AjaxStatus status, int networkError, RequestCallbackVersion request) {
        if (status.getCode() == networkError) {
            request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.connection));
        } else {
            request.onNull(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void paramWithoutToken(String url, Map<String, Object> params, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            // Log.e("url.....", "" + url + " " + params);
            printLog("url....." + url + " " + params);
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    // Log.e("result.....", "" + json);
                    printLog("result....." + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void postWithoutToken(String url, JSONObject jsonInput, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            jsonInput.put(Constant.LANGUAGE,Utility.getLanguagePreference(context));
            //Log.e("url.....", "" + url + " " + jsonInput);
            printLog("url....." + url + " " + jsonInput);
            aq.post(url, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //  Log.e("result.....", "" + json);
                    printLog("result....." + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("1000")) {
                                request.onInactive(json, Message, "1");
                                //     request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void postWithJsonToken(String url, String token, JSONObject jsonInput, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            jsonInput.put(Constant.LANGUAGE,Utility.getLanguagePreference(context));
            //  Log.e("url.....", "" + url + " " + jsonInput);
            printLog("url....." + url + " " + jsonInput);
            aq.post(url, jsonInput, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    // Log.e("result.....", "" + json +" "+status.getCode()+" "+status.getError());
                    printLog("result..." + json + " " + status.getCode() + " " + status.getError());
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("1000")) {
                                //    request.onInactive(json, Message, "2");
                                SessionExpireDialog.openDialog(context, 1, Message);
                                MyDialogProgress.close(context);
                                //     request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            }.header(Constant.SECRETKEY, token));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void postWithParamToken(String url, String token, Map<String, Object> params, final RequestCallbackVersion request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            // Log.e("url.....", "" + url + " " + params + " auth " + Utility.getSharedPreferences(context.getApplicationContext(), Constant.USER_TOKEN));
            printLog("url....." + url + " " + params + " auth " + Utility.getSharedPreferences(context.getApplicationContext(), Constant.USER_TOKEN));
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //   Log.e("result.....", "" + json + " error " + status.getError());
                    printLog("result....." + json + " error " + status.getError());
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("1000")) {
                                request.onInactive(json, Message, "1");
                                //     request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            } else if (jsonStatus.equalsIgnoreCase("1001")) {
                                request.onVersionChange(json, Message);
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            }.header(Constant.SECRETKEY, token));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }


    public void postWithParamToken(String url, String token, Map<String, Object> params, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            //   Log.e("url.....", "" + url + " " + params + " auth " + Utility.getSharedPreferences(context.getApplicationContext(), Constant.USER_TOKEN));
            printLog("url....." + url + " " + params + " auth " + Utility.getSharedPreferences(context.getApplicationContext(), Constant.USER_TOKEN));
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //  Log.e("result.....", "" + json + " error " + status.getError());
                    printLog("result....." + json + " error " + status.getError());
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("1000")) {
                                request.onInactive(json, Message, "1");
                                //     request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            }.header(Constant.SECRETKEY, token));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void getWithToken(String url, String token, final RequestCallback request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            // Log.e("url.....", "" + url);
            printLog("url....." + url);
            aq.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //Log.e("result.....", "" + json);
                    printLog("result....." + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            }.header(Constant.SECRETKEY, token).method(AQuery.METHOD_GET));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void getWithoutToken(String url, Map<String, Object> params, final RequestCallbackVersion request) {
        aq = new AQuery(context.getApplicationContext());
        try {
            // Log.e("url.....", "" + url + " " + params);
            printLog(url + " " + params);
            aq.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

                @Override
                public void callback(String url, JSONObject json, AjaxStatus status) {
                    //  Log.e("result.....", "" + json);
                    printLog("result....." + json);
                    if (json != null) {
                        try {
                            String Message = json.getString("message");
                            String jsonStatus = json.getString("status");
                            if (jsonStatus.equalsIgnoreCase("success")) {
                                request.onSuccess(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("failed")) {
                                request.onFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("false")) {
                                request.onAuthFailed(json, Message);
                            } else if (jsonStatus.equalsIgnoreCase("1001")) {
                                request.onVersionChange(json, Message);
                            } else {
                                request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            }
                        } catch (Exception e1) {
                            request.onException(json, context.getApplicationContext().getString(R.string.something_wrong));
                            e1.printStackTrace();
                        }
                    } else {
                        nullCase(status, AjaxStatus.NETWORK_ERROR, request);
                    }
                }
            }.method(AQuery.METHOD_GET));
        } catch (Exception e) {
            e.printStackTrace();
            request.onException(new JSONObject(), context.getApplicationContext().getString(R.string.something_wrong));
        }
    }

    public void printLog(String a) {
        if (BuildConfig.DEBUG) {
            Log.e("Error Log", "" + a);
        }
    }
}

