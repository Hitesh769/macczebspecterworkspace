package com.spectre.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;
import com.spectre.R;
import com.spectre.beans.AdPost;
import com.spectre.beans.CarName;
import com.spectre.beans.ImageData;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.helper.PathUtils;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.ConvetBitmap;
import com.spectre.utility.PermissionsUtils;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.zhihu.matisse.MimeType.JPEG;
import static com.zhihu.matisse.MimeType.PNG;

public class AddWorkActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private ArrayList<String> carType = new ArrayList<>();
    private Spinner spinner_name, spinner_model, spinner_version, spinner_year, spinner_car_type, spinner_color;

    private CustomRayMaterialTextView btn_save_changes, btn_delete, btn_delete_;
    private CustomEditText et_mileage, et_price, et_car_condition, et_problem;
    private RecyclerView recycler;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CHOOSE1 = 24;

    RecyclerView mRecyclerView, mRecyclerViewAfter;
    private LinearLayoutManager mLayoutManager, mLayoutManagerAfter;
    private RecyclerView.Adapter mAdapter, mAdapterAfter;
    private ArrayList<String> showImage = new ArrayList<>();
    private ArrayList<ImageData> bitMapList = new ArrayList<>();
    private ArrayList<ImageData> bitMapList1 = new ArrayList<>();

    ArrayList<CarName> names = new ArrayList<CarName>();
    ArrayList<ModelName> model = new ArrayList<ModelName>();
    ArrayList<VersionName> version = new ArrayList<VersionName>();
    ArrayList<String> years = new ArrayList<String>();
    private ProgressView progressDialog1, progressDialogAfter;
    ArrayAdapter<CarName> arrayAdapterCarName;
    ArrayAdapter<ModelName> arrayAdapterModel;
    ArrayAdapter<VersionName> arrayAdapterVersion;
    private int oldName = 0;
    private int oldModel = 0;
    private AlertBox alertBox;
    private AdPost adPost;
    private ActionBar actionBar;
    private boolean canEdit, refresh, canEdit2;
    private int position = -1;
    private AppCompatRadioButton radioyes, radioNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add_work);
        context = this;
        Utility.setContentView(context, R.layout.activity_add_work);
        actionBar = Utility.setUpToolbar_(context, getString(R.string.add_work), true);
        initView();
    }

    private void initView() {
        spinner_name = (Spinner) findViewById(R.id.spinner_name);
        spinner_model = (Spinner) findViewById(R.id.spinner_model);
        spinner_version = (Spinner) findViewById(R.id.spinner_version);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
        spinner_car_type = (Spinner) findViewById(R.id.spinner_car_type);
        spinner_color = (Spinner) findViewById(R.id.spinner_color);

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


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        progressDialog1 = (ProgressView) findViewById(R.id.progress_pv_linear_colors);
        progressDialogAfter = (ProgressView) findViewById(R.id.progress_pv_linear_colors_after);

        mRecyclerViewAfter = (RecyclerView) findViewById(R.id.recycler_after);
        mRecyclerViewAfter.setHasFixedSize(true);
        mLayoutManagerAfter = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewAfter.setLayoutManager(mLayoutManagerAfter);
        mRecyclerViewAfter.getItemAnimator().setChangeDuration(0);
        progressDialogAfter = (ProgressView) findViewById(R.id.progress_pv_linear_colors_after);


        //mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Utils.dpToPx(context, 100)));
       /* ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
        params.height = Utility.dpToPx(context, 105);
        mRecyclerView.setLayoutParams(params);*/

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerViewAfter.setNestedScrollingEnabled(false);


        et_car_condition = (CustomEditText) findViewById(R.id.et_car_condition);
        et_mileage = (CustomEditText) findViewById(R.id.et_mileage);
        et_price = (CustomEditText) findViewById(R.id.et_price);
        et_problem = (CustomEditText) findViewById(R.id.et_problem);
        btn_save_changes = (CustomRayMaterialTextView) findViewById(R.id.btn_save_changes);
        btn_delete = (CustomRayMaterialTextView) findViewById(R.id.btn_delete);
        btn_delete_ = (CustomRayMaterialTextView) findViewById(R.id.btn_delete_);
        btn_save_changes.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delete_.setOnClickListener(this);

        //   btn_delete_.setOnClickListener(this);
        alertBox = new AlertBox(context);
        radioyes = (AppCompatRadioButton) findViewById(R.id.radio_yes);
        radioNo = (AppCompatRadioButton) findViewById(R.id.radio_no);
        AppCompatRadioButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioyes.setChecked(radioyes == buttonView);
                    radioNo.setChecked(radioNo == buttonView);
                   /* if (radioyes.isChecked()) {
                        radioyes.setTextColor(Color.BLACK);
                        radioNo.setTextColor(getResources().getColor(R.color.colorlazy));
                    } else {
                        radioNo.setTextColor(Color.BLACK);
                        radioNo.setTextColor(getResources().getColor(R.color.colorlazy));
                    }*/
                }
            }
        };
        radioyes.setOnCheckedChangeListener(listener);
        radioNo.setOnCheckedChangeListener(listener);

        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {
            actionBar.setTitle(getString(R.string.edit_work));
            adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
            position = getIntent().getExtras().getInt(Constant.POSITION);
            et_mileage.setText(adPost.getMileage());
            et_price.setText(adPost.getPrice());
            et_problem.setText(adPost.getProblem());
            et_car_condition.setText(adPost.getCar_condition());
            spinner_color.setSelection(arrayAdapterCarColor.getPosition(adPost.getColor()));
            spinner_year.setSelection(arrayAdapterYear.getPosition(adPost.getYear()));
            spinner_car_type.setSelection(arrayAdapterCarType.getPosition(adPost.getCar_type()));
            btn_delete.setVisibility(View.VISIBLE);
            btn_delete_.setVisibility(View.VISIBLE);
            if (adPost.getCar_modified() != null && !adPost.getCar_modified().isEmpty()) {
                if (adPost.getCar_modified().equalsIgnoreCase("1")) {
                    radioyes.setChecked(true);
                    radioNo.setChecked(false);
                } else {
                    radioyes.setChecked(false);
                    radioNo.setChecked(true);
                }
            } else {
                radioyes.setChecked(false);
                radioNo.setChecked(true);
            }

            if (adPost.getDelete_status() != null && adPost.getDelete_status().equalsIgnoreCase("0")) {
                btn_delete.setText(getString(R.string.inactive));
            } else {
                btn_delete.setText(getString(R.string.active));
            }


            if (adPost.getImage().size() > 0) {
                canEdit = false;
                new DownloadImageTask(true).execute("1");
                //  getBase64(adPost.getImage());
            } else {
                ImageData imageData = new ImageData();
                bitMapList.add(imageData);
                mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
                mRecyclerView.setAdapter(mAdapter);
            }


            if (adPost.getAfter_image().size() > 0) {
                canEdit2 = false;
                new DownloadImageTask(false).execute("2");
                //  getBase64(adPost.getImage());
            } else {
                ImageData imageData = new ImageData();
                bitMapList1.add(imageData);
                mAdapterAfter = new ShowImagesAdapter(context, bitMapList1, 1);
                mRecyclerViewAfter.setAdapter(mAdapterAfter);
            }

        } else {
            ImageData imageData = new ImageData();
            bitMapList.add(imageData);
            mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
            mRecyclerView.setAdapter(mAdapter);

            ImageData imageData1 = new ImageData();
            bitMapList1.add(imageData1);
            mAdapterAfter = new ShowImagesAdapter(context, bitMapList1, 1);
            mRecyclerViewAfter.setAdapter(mAdapterAfter);
        }
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

                oldName = position;
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

                oldModel = position;
                clearVersion();
                if (model != null && !model.isEmpty() && model.size() > position)
                    getList(3, model.get(position).getId());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_changes:
                checkCondition();
                break;
           /* case R.id.btn_delete:
                callDeleteApi();
                break;*/
            case R.id.btn_delete:
                callDeleteApi(adPost.getDelete_status().equalsIgnoreCase("0") ? 1 : 0);
                break;
            case R.id.btn_delete_:
                callDeleteApi(2);
                break;

        }
    }

    private void checkCondition() {
        et_mileage.setError(null);
        et_car_condition.setError(null);
        et_price.setError(null);
        et_problem.setError(null);

        if (!canEdit && adPost != null) {
            Utility.showToast(context, getString(R.string.loading_work, getString(R.string.before_image)));
            return;
        }

        if (!canEdit2 && adPost != null) {
            Utility.showToast(context, getString(R.string.loading_work, getString(R.string.after_image)));
            return;
        }

        if (bitMapList.size() == 1) {
            Utility.showToast(context, getString(R.string.empty_before_image));
            return;
        }

        if (bitMapList1.size() == 1) {
            Utility.showToast(context, getString(R.string.empty_after_image));
            return;
        }

        if (bitMapList.size() != bitMapList1.size()) {
            Utility.showToast(context, getString(R.string.image_should_be_same));
            return;
        }

        if (bitMapList.size() == 5 && bitMapList1.size() == 5) {
            int count1 = 0, count2 = 0;
            for (ImageData imageData : bitMapList1) {
                if (imageData.getBytes() != null) {
                    count1++;
                }
            }

            for (ImageData imageData : bitMapList) {
                if (imageData.getBytes() != null) {
                    count2++;
                }
            }

            if (count1 != count2) {
                Utility.showToast(context, getString(R.string.image_should_be_same));
                return;
            }
        }

        if (spinner_name.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cname));
            return;
        }

        if (spinner_model.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cmodel));
            return;
        }

      /*  if (spinner_version.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cversion));
            return;
        }*/

        if (spinner_year.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cyear));
            return;
        }

        if (spinner_color.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cColor));
            return;
        }

        if (spinner_car_type.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_ctype));
            return;
        }


        if (et_mileage.getText().toString().trim().isEmpty()) {
            et_mileage.setError(getString(R.string.pls_select_cMileage));
            return;
        }

        if (et_price.getText().toString().trim().isEmpty()) {
            et_price.setError(getString(R.string.pls_select_cPrice));
            return;
        }

        if (et_car_condition.getText().toString().trim().isEmpty()) {
            et_car_condition.setError(getString(R.string.pls_select_cCondition));
            return;
        }

        if (et_problem.getText().toString().trim().isEmpty()) {
            et_problem.setError(getString(R.string.pls_enter_problem));
            return;
        }
        callApi();
    }

    private void callApi() {

        JSONObject jsonObject = new JSONObject();
        MyDialogProgress.open(context);
        /*
        // Add Post
        "car_name":tfCarName.text!,
                "model":tfModel.text!,
                "version":tfVersion.text!,
                "year":tfYear.text!,
                "car_name_id":carNameId,
                "model_id":carModelId,
                "version_id":carVersionId,
                "mileage":tfMilage.text!,
                "price":tfPrice.text!,
                "colour":tfColor.text!,
                "car_type":tfCarType.text!,
                "car_condition":tfCondition.text!,
                "image":imageArray

                //Edit Post
                "post_id" : postEntity.add_id,
                            "car_name":tfCarName.text!,
                            "model":tfModel.text!,
                            "version":tfVersion.text!,
                            "year":tfYear.text!,
                            "car_name_id":carNameId,
                            "model_id":carModelId,
                            "version_id":carVersionId,
                            "mileage":tfMilage.text!,
                            "price":tfPrice.text!,
                            "colour":tfColor.text!,
                            "car_type":tfCarType.text!,
                            "car_condition":tfCondition.text!,
                            "image":imageArray ] as [String: Any]

                */
        String Url = "";
        try {
            if (adPost != null) {
                jsonObject.put(Constant.ADD_ID, adPost.getAdd_id());
                Url = Urls.EDIT_GARAGE_WORK;
            } else {
                Url = Urls.GARAGE_WORK;
            }
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
            jsonObject.put(Constant.MILEAGE, et_mileage.getText().toString().trim());
            jsonObject.put(Constant.PRICE, et_price.getText().toString().trim());
            jsonObject.put(Constant.CAR_CONDITION, et_car_condition.getText().toString().trim());
            jsonObject.put(Constant.PROBLEM, et_problem.getText().toString().trim());
            jsonObject.put(Constant.CAR_MODIFIED, radioNo.isChecked() ? "0" : "1");
            int i = 0;
          /*  for (ImageData imageData : bitMapList) {
                if (imageData.getBytes() != null) {
                    jsonObject.put("image[" + i + "]", imageData.getBytes());
                }
                i++;
            }*/

            JSONArray jsonArray = new JSONArray();
            for (ImageData imageData : bitMapList) {
                if (imageData.getBytes() != null) {
                    jsonArray.put(imageData.getBytes());
                }
            }
            jsonObject.put(Constant.IMAGE, jsonArray);
            JSONArray jsonArray1 = new JSONArray();
            for (ImageData imageData : bitMapList1) {
                if (imageData.getBytes() != null) {
                    jsonArray1.put(imageData.getBytes());
                }
            }
            jsonObject.put(Constant.AFTER_IMAGE, jsonArray1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Url, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                /*refresh = true;
                alertBox.openMessageWithFinish(msg, "Okay", "", false);
                MyDialogProgress.close(context);*/
                setNewData(js, msg);
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


    private class ShowImagesAdapter extends RecyclerView.Adapter<ShowImagesAdapter.ViewHolder> {
        Context appContext;
        private int selectedPos = 0;
        ArrayList<ImageData> AL = new ArrayList<>();

        public ShowImagesAdapter(Context appContext, ArrayList<ImageData> arraylist, int selectedPos) {
            this.appContext = appContext;
            this.AL = arraylist;
            this.selectedPos = selectedPos;
        }

        @Override
        public ShowImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_images, parent, false);
            ShowImagesAdapter.ViewHolder viewHolder = new ShowImagesAdapter.ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ShowImagesAdapter.ViewHolder holder, final int position) {
            //    holder.itemView.btn_choose_file.
            if (AL.get(position).getBitmap() != null) {
                new AQuery(context).id(holder.btn_choose_file).image(AL.get(position).getBitmap());
                holder.btn_delete.setVisibility(View.VISIBLE);
                holder.btn_choose_file.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                holder.btn_choose_file.setImageResource(R.mipmap.postadd);
                holder.btn_choose_file.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.btn_delete.setVisibility(View.GONE);
            }

            holder.btn_choose_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AL.get(position).getBitmap() == null)
                        checkPermission(selectedPos);
                }
            });

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPos == 0) {
                        int size = bitMapList.size();
                        Log.d(AddWorkActivity.class.getName() + " List Size " + bitMapList.size(), bitMapList.toString() + "");
                        bitMapList.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        notifyDataSetChanged();

                        if (size == 5 && bitMapList.size() < 5 && bitMapList.get(bitMapList.size() - 1).getBitmap() != null) {
                            ImageData imageData2 = new ImageData();
                            imageData2.setBitmap(null);
                            imageData2.setBytes(null);
                            bitMapList.add(imageData2);
                            notifyDataSetChanged();
                        }
                    } else {
                        int size = bitMapList1.size();
                        Log.d(AddWorkActivity.class.getName() + " List Size " + bitMapList1.size(), bitMapList1.toString() + "");
                        bitMapList1.remove(position);
                        mAdapterAfter.notifyItemRemoved(position);
                        notifyDataSetChanged();

                        if (size == 5 && bitMapList1.size() < 5 && bitMapList1.get(bitMapList1.size() - 1).getBitmap() != null) {
                            ImageData imageData2 = new ImageData();
                            imageData2.setBitmap(null);
                            imageData2.setBytes(null);
                            bitMapList1.add(imageData2);
                            notifyDataSetChanged();
                        }
                    }
                }

            });
        }

        @Override
        public int getItemCount() {
            return AL.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView btn_choose_file;
            ImageView btn_delete;
            RelativeLayout layout_image;

            public ViewHolder(View itemView) {
                super(itemView);
                btn_choose_file = ((ImageView) itemView.findViewById(R.id.btn_choose_file));
                layout_image = ((RelativeLayout) itemView.findViewById(R.id.layout_image));
               /* int imageSide = Utility.dpToPx(context, 100);
                int imageBorderPadding = Utility.dpToPx(context, 2);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageSide, imageSide);
                layoutParams.setMargins(imageBorderPadding, 0, imageBorderPadding, 0);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(Utility.dpToPx(context, 104), Utility.dpToPx(context, 104));
                btn_choose_file.setLayoutParams(layoutParams);
                layout_image.setLayoutParams(layoutParams1);
                btn_choose_file.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            */
                btn_delete = ((ImageView) itemView.findViewById(R.id.btn_delete));
            }
        }
    }


    private void checkPermission(int selectedPos) {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).requiredPermissionsGranted(context)) {
                return;
            }
        }


        if (selectedPos == 0) {
            int size = 5 - bitMapList.size();

            if (bitMapList.get(0).getBitmap() == null || bitMapList.get(bitMapList.size() - 1).getBitmap() == null) {
                size++;
            }

            if (size <= 0) {
                return;
            }


            Matisse.from(this)
                    .choose(ofImage())
                    .countable(true)
                    .capture(true)
                    .maxSelectable(size)
                    .captureStrategy(new CaptureStrategy(true, getApplicationContext().getPackageName() + ".provider"))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Dracula)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE);
        } else {
            int size = 5 - bitMapList1.size();

            if (bitMapList1.get(0).getBitmap() == null || bitMapList1.get(bitMapList1.size() - 1).getBitmap() == null) {
                size++;
            }

            if (size <= 0) {
                return;
            }


            Matisse.from(this)
                    .choose(ofImage())
                    .countable(true)
                    .capture(true)
                    .maxSelectable(size)
                    .captureStrategy(new CaptureStrategy(true, getApplicationContext().getPackageName() + ".provider"))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .theme(R.style.Dracula)
                    .imageEngine(new GlideEngine())
                    .forResult(REQUEST_CODE_CHOOSE1);
        }

    }

    public static Set<MimeType> ofImage() {
        return EnumSet.of(JPEG, PNG);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            int size = bitMapList.size() - 1;
            // mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));

            List<Uri> list = Matisse.obtainResult(data);
            if (list != null && list.size() > 0) {

                for (Uri uri : list) {
                    ImageData imageData = setImage(uri);
                    if (imageData.getBitmap() != null && imageData.getBitmap().getByteCount() > 0) {
                        if (bitMapList.get(size).getBitmap() == null)
                            bitMapList.set(size, imageData);
                        else
                            bitMapList.add(imageData);
                    }
                }


            }

            if ((bitMapList.size() > 1 || bitMapList.get(0).getBitmap() != null) && bitMapList.size() < 5) {
                ImageData imageData = new ImageData();
                bitMapList.add(bitMapList.size(), imageData);
            }

            mAdapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_CHOOSE1 && resultCode == RESULT_OK) {
            int size = bitMapList1.size() - 1;
            // mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));

            List<Uri> list = Matisse.obtainResult(data);
            if (list != null && list.size() > 0) {

                for (Uri uri : list) {
                    ImageData imageData = setImage(uri);
                    if (imageData.getBitmap() != null && imageData.getBitmap().getByteCount() > 0) {
                        if (bitMapList1.get(size).getBitmap() == null)
                            bitMapList1.set(size, imageData);
                        else
                            bitMapList1.add(imageData);
                    }
                }

            }

            if ((bitMapList1.size() > 1 || bitMapList1.get(0).getBitmap() != null) && bitMapList1.size() < 5) {
                ImageData imageData = new ImageData();
                bitMapList1.add(bitMapList1.size(), imageData);
            }

            mAdapterAfter.notifyDataSetChanged();
        }
    }


    private ImageData setImage(Uri destination) {
        ImageData imageData = new ImageData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), destination);
            Log.d("Bitmap", bitmap.getHeight() + " " + bitmap.getWidth());
            bitmap = ConvetBitmap.Mytransform(bitmap);
            //Bitmap bitmap1 = Utility.rotateImage(bitmap, new File(destination.getPath()));
            String path = null;
            try {
                path = PathUtils.getPath(this, destination);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (path != null && !path.isEmpty()) {
                Bitmap bitmap1 = Utility.rotateImage(bitmap, new File(path));
                if (bitmap1 != null && bitmap1.getByteCount() > 0) {
                    bitmap = bitmap1;
                }
            }
            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, datasecond);
            Log.d("Bitmap", bitmap.getHeight() + " " + bitmap.getWidth());
            //  bitmap.compress(Bitmap.CompressFormat.PNG, 20, datasecond);
            String encodedString = Base64.encodeToString(datasecond.toByteArray(), Base64.DEFAULT);
            imageData.setBytes(encodedString);
            imageData.setBitmap(bitmap);

            //  ivProfile.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
            // img1.setImageBitmap(bitmap);
            return imageData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageData;
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

        new AqueryCall(this).postWithJsonToken(Url, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
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

        if (adPost != null && !adPost.getVersion().isEmpty()) {
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

    @Override
    public void finish() {
        if (refresh) {
            Intent intent = new Intent();
            intent.putExtra(Constant.POSITION, position);
            intent.putExtra(Constant.DATA, adPost);
            setResult(Activity.RESULT_OK, intent);
        }
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getBase64(final ArrayList<String> image, int type) {

        /*for (final String images : image) {
            Glide.with(this)
                    .load(images)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource.getByteCount() > 0) {
                                ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, datasecond);
                                //  bitmap.compress(Bitmap.CompressFormat.PNG, 20, datasecond);
                                String encodedString = Base64.encodeToString(datasecond.toByteArray(), Base64.DEFAULT);
                                ImageData imageData = new ImageData();
                                imageData.setBitmap(resource);
                                imageData.setBytes(encodedString);
                                imageData.setUrl(images);
                                bitMapList.add(imageData);
                            }
                        }
                    });
        }*/

        for (final String images : image) {
            //for(int i=0;i<=advertise.getAds_image().size();i++)


            Bitmap object = Utility.getBitmapFromUrl(context, images);
            Log.d("Before Bitmap", object.getHeight() + " " + object.getWidth() + " " + object.getByteCount());
            object = ConvetBitmap.Mytransform(object);
            ByteArrayOutputStream datasecond = new ByteArrayOutputStream();
            object.compress(Bitmap.CompressFormat.JPEG, 100, datasecond);
            Log.d("After Bitmap", object.getHeight() + " " + object.getWidth() + " " + object.getByteCount());
            String encodedString = Base64.encodeToString(datasecond.toByteArray(), Base64.DEFAULT);
            ImageData imageData = new ImageData();
            imageData.setBitmap(object);
            imageData.setBytes(encodedString);
            imageData.setUrl(images);

            if (type == 1)
                bitMapList.add(imageData);
            else
                bitMapList1.add(imageData);
        }

        if (image.size() > 4) {

        } else {
            ImageData imageData = new ImageData();
            if (type == 1)
                bitMapList.add(imageData);
            else
                bitMapList1.add(imageData);
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Void> {
        boolean type;

        public DownloadImageTask(boolean isFirst) {
            // super();
            type = isFirst;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (type)
                progressDialog1.start();
            else
                progressDialogAfter.start();
        }

        @Override
        protected Void doInBackground(String... params) {
            if (type)
                getBase64(adPost.getImage(), 1);
            else
                getBase64(adPost.getAfter_image(), 2);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (type) {
                mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
                mRecyclerView.setAdapter(mAdapter);
                progressDialog1.stop();
                canEdit = true;
            } else {
                mAdapterAfter = new ShowImagesAdapter(context, bitMapList1, 1);
                mRecyclerViewAfter.setAdapter(mAdapterAfter);
                progressDialogAfter.stop();
                canEdit2 = true;
            }
        }
    }


    //delete_post

    private void callDeleteApi(final int status) {

        JSONObject jsonObject = new JSONObject();
        MyDialogProgress.open(context);

        String Url = "";
        try {
            jsonObject.put(Constant.ADD_ID, adPost.getAdd_id());
            jsonObject.put(Constant.DELETE_STATUS, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //"delete_status":"1"

        new AqueryCall(this).postWithJsonToken(Urls.DELETE_GARAGE_WORK, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                refresh = true;
                adPost.setDelete_status(status + "");
                alertBox.openMessageWithFinish(msg, "Okay", "", false);
                // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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


    private void setNewData(JSONObject js, String msg) {
        refresh = true;
        if (adPost != null) {
            try {
                adPost = new Gson().fromJson(js.getJSONObject("data").toString(), AdPost.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        alertBox.openMessageWithFinish(msg, "Okay", "", false);
        // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        MyDialogProgress.close(context);
    }

 /*<com.spectre.customView.CustomTextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    android:paddingLeft="5dp"
    android:paddingRight="5dp"
    android:text="@string/problem"
    android:textColor="@color/black"
    android:textSize="14sp"
    app:edittextfont="Poppins-Regular.ttf"
    app:edittextfontHint="Poppins-Regular.ttf" />


                <com.spectre.customView.CustomEditText
    android:id="@+id/et_address"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="5dp"
    android:background="@drawable/semi_transparent_box"
    android:hint="@string/enter_problem"
    android:inputType="textCapSentences|textMultiLine"
    android:lines="3"
    android:maxLines="3"
    android:gravity="top"
    android:padding="8dp"
    android:textColor="@color/black"
    android:textColorHint="@color/light_gray"
    android:textSize="12sp"
    app:edittextfont="Poppins-Regular.ttf"
    app:edittextfontHint="Poppins-Regular.ttf" />*/
}
