package com.spectre.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.Spinner;
import com.spectre.R;
import com.spectre.activity.AddWorkSectionActivity;
import com.spectre.beans.AdPost;
import com.spectre.beans.CarName;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class WorkBasicFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Spinner spinner_name, spinner_model, spinner_version, spinner_year, spinner_car_type, spinner_color;
    private CustomRayMaterialTextView btn_basic_next;
    ArrayList<CarName> names = new ArrayList<CarName>();
    ArrayList<ModelName> model = new ArrayList<ModelName>();
    ArrayList<VersionName> version = new ArrayList<VersionName>();
    ArrayList<String> years = new ArrayList<String>();
    ArrayAdapter<CarName> arrayAdapterCarName;
    ArrayAdapter<ModelName> arrayAdapterModel;
    ArrayAdapter<VersionName> arrayAdapterVersion;
    private AdPost adPost;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_work_basic, container, false);
        context = getActivity();
        initView();
        return view;
    }

    private void initView() {
        btn_basic_next = (CustomRayMaterialTextView) view.findViewById(R.id.btn_basic_next);
        btn_basic_next.setOnClickListener(this);

        spinner_name = (Spinner) view.findViewById(R.id.spinner_name);
        spinner_model = (Spinner) view.findViewById(R.id.spinner_model);
        spinner_version = (Spinner) view.findViewById(R.id.spinner_version);
        spinner_year = (Spinner) view.findViewById(R.id.spinner_year);
        spinner_car_type = (Spinner) view.findViewById(R.id.spinner_car_type);
        spinner_color = (Spinner) view.findViewById(R.id.spinner_color);

        String arrayCarType[] = getResources().getStringArray(R.array.car_type);
        List<String> carType = Arrays.asList(arrayCarType);

        String arraycarColor[] = getResources().getStringArray(R.array.car_color);
        List<String> carColor = Arrays.asList(arraycarColor);


        names.add(Utility.getCarName(context));
        model.add(Utility.getModelName(context));
        version.add(Utility.getVersionName(context));
        years.add(getString(R.string.select_year));

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        // years.add("Select Year");
        for (int i = thisYear; i >= 1950; i--) {
            years.add(Integer.toString(i));
        }


        arrayAdapterCarName = new ArrayAdapter<CarName>(context, R.layout.spinner_custom_text, names);
        spinner_name.setAdapter(arrayAdapterCarName);
        arrayAdapterModel = new ArrayAdapter<ModelName>(context, R.layout.spinner_custom_text, model);
        spinner_model.setAdapter(arrayAdapterModel);
        arrayAdapterVersion = new ArrayAdapter<VersionName>(context, R.layout.spinner_custom_text, version);
        spinner_version.setAdapter(arrayAdapterVersion);


        ArrayAdapter<String> arrayAdapterCarType = new ArrayAdapter<String>(context, R.layout.spinner_custom_text, carType);
        spinner_car_type.setAdapter(arrayAdapterCarType);
        ArrayAdapter<String> arrayAdapterCarColor = new ArrayAdapter<String>(context, R.layout.spinner_custom_text, carColor);
        spinner_color.setAdapter(arrayAdapterCarColor);
        ArrayAdapter<String> arrayAdapterYear = new ArrayAdapter<String>(context, R.layout.spinner_custom_text, years);
        spinner_year.setAdapter(arrayAdapterYear);

        getList(1, "");
        listners();
    }

    private void listners() {
        spinner_name.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if (position == 0) {
                    clearModel();
                    clearVersion();
                    if (names.size() == 1) {
                        getList(1, "0");
                    }
                    return;
                }

               /* if (oldName == position) {
                    return;
                }*/

                clearModel();
                clearVersion();

                if (names != null && !names.isEmpty() && names.size() > position)
                    getList(2, names.get(position).getId());
            }
        });


        spinner_model.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                if (position == 0) {
                    clearVersion();
                    return;
                }
              /*  if (oldModel == position) {
                    return;
                }*/

                clearVersion();
                if (model != null && !model.isEmpty() && model.size() > position)
                    getList(3, model.get(position).getId());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_basic_next:
                checkCondition(1);
                break;

        }
    }


    private void checkCondition(int type) {


        if (spinner_name.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cname));
            return;
        }

        if (spinner_model.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cmodel));
            return;
        }

       /* if (spinner_version.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cversion));
            return;
        }*/

        if (spinner_year.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cyear));
            return;
        }


        if (spinner_car_type.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_ctype));
            return;
        }

        if (spinner_color.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cColor));
            return;
        }

        if (type == 1)
            ((AddWorkSectionActivity) context).setPosition(1, true);
    }


    //Type 1 = Brand list,2 = Model,3=Version
    private void getList(final int type, String id) {

        if (!Utility.isConnectingToInternet(context)) {
            Utility.showToast(context, getString(R.string.connection));
            return;
        }


        JSONObject jsonObject = new JSONObject();
        String Url = "";

        try {
            if (type == 1) {
                MyDialogProgress.open(context);
                Url = Urls.CAR_NAME_LIST;
            } else if (type == 2) {
                Url = Urls.MODEL_NAME_LIST;
                jsonObject.put("car_name_id", id);
            } else if (type == 3) {
                Url = Urls.VERSION_NAME_LIST;
                jsonObject.put("model_id", id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AqueryCall(getActivity()).postWithJsonToken(Url, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {

                saveData(type, js);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {

                closeProgressBar(type);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                closeProgressBar(type);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                closeProgressBar(type);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(JSONObject js, String msg) {
                closeProgressBar(type);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {
                closeProgressBar(type);
            }
        });
    }


    private void saveData(int type, JSONObject js) {
        JSONArray jsonArray;
        try {
            jsonArray = js.getJSONArray("data");
            if (jsonArray.length() > 0) {
                if (type == 1) {
                    setBrand(jsonArray);
                } else if (type == 2) {
                    setModel(jsonArray);
                } else {
                    setVersion(jsonArray);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        closeProgressBar(type);
    }

    private void setBrand(JSONArray jsonArray) {
        Type type = new TypeToken<List<CarName>>() {
        }.getType();
        ArrayList<CarName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
        names.addAll(tempListNewsFeeds);
        arrayAdapterCarName = new ArrayAdapter<CarName>(context, R.layout.spinner_custom_text, names);
        spinner_name.setAdapter(arrayAdapterCarName);

        if (adPost != null) {
            CarName carName = new CarName(adPost.getCar_name_id(), adPost.getCar_name());
            spinner_name.setSelection(arrayAdapterCarName.getPosition(carName));
            //    getList(2, carName.getId());
        }

      /*  if (names.size() > 0) {
            spinner_name.setSelection(1);
            // getList(2, names.get(1).getId());
        }*/
    }


    private void setModel(JSONArray jsonArray) {
        Type type = new TypeToken<List<ModelName>>() {
        }.getType();
        ArrayList<ModelName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
        if (tempListNewsFeeds.size() > 0) {
            clearModel();
        }
        model.addAll(tempListNewsFeeds);
        arrayAdapterModel = new ArrayAdapter<ModelName>(context, R.layout.spinner_custom_text, model);
        spinner_model.setAdapter(arrayAdapterModel);
        if (adPost != null) {
            ModelName modelName = new ModelName(adPost.getModel_id(), adPost.getModel());
            spinner_model.setSelection(arrayAdapterModel.getPosition(modelName));
            //    getList(3, modelName.getId());
        }

       /* if (model.size() > 0) {
            // getList(3, model.get(1).getId());
            spinner_model.setSelection(1);
        }*/
    }


    private void setVersion(JSONArray jsonArray) {
        Type type = new TypeToken<List<VersionName>>() {
        }.getType();
        ArrayList<VersionName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);

        if (tempListNewsFeeds.size() > 0) {
            clearVersion();
        }
        version.addAll(tempListNewsFeeds);
        arrayAdapterVersion = new ArrayAdapter<VersionName>(context, R.layout.spinner_custom_text, version);
        spinner_version.setAdapter(arrayAdapterVersion);

       /* if (version.size() > 0) {
            // getList(3, model.get(1).getId());
            spinner_version.setSelection(1);
        }*/

        if (adPost != null) {
            VersionName versionName = new VersionName(adPost.getVersion_id(), adPost.getVersion());
            spinner_version.setSelection(arrayAdapterVersion.getPosition(versionName));
            // getList(3, versionName.getId());
        }

    }


    private void closeProgressBar(int type) {
        if (type == 1)
            MyDialogProgress.close(context);
    }

    private void clearModel() {
        model.clear();
        model.add(Utility.getModelName(context));
        spinner_model.setSelection(0);
    }

    private void clearVersion() {
        version.clear();
        version.add(Utility.getVersionName(context));
        spinner_version.setSelection(0);
    }


    public JSONObject getData() {
        JSONObject jsonObject = ((AddWorkSectionActivity) context).jsonObject;

        // checkCondition(2);
        try {
            jsonObject.put(Constant.CAR_NAME_ID, ((CarName) spinner_name.getSelectedItem()).getId());
            jsonObject.put(Constant.CAR_NAME, ((CarName) spinner_name.getSelectedItem()).getCar_name());
            jsonObject.put(Constant.MODEL, ((ModelName) spinner_model.getSelectedItem()).getModel_name());
            jsonObject.put(Constant.MODEL_ID, ((ModelName) spinner_model.getSelectedItem()).getId());

            if (spinner_version.getSelectedItemPosition() != 0) {
                jsonObject.put(Constant.VERSION, ((VersionName) spinner_version.getSelectedItem()).getVersion_name());
                jsonObject.put(Constant.VERSION_ID, ((VersionName) spinner_version.getSelectedItem()).getId());
            } else {
                jsonObject.put(Constant.VERSION, "");
                jsonObject.put(Constant.VERSION_ID, 0);
            }

            jsonObject.put(Constant.YEAR, ((String) spinner_year.getSelectedItem()));
            jsonObject.put(Constant.CAR_TYPE, ((String) spinner_car_type.getSelectedItem()));
            jsonObject.put(Constant.COLOUR, ((String) spinner_color.getSelectedItem()));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }

}
