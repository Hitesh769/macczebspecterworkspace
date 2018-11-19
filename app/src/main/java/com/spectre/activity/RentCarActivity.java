package com.spectre.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Spinner;
import com.spectre.R;
import com.spectre.adapter.CarNameListAdapter;
import com.spectre.adapter.ModelNameListAdapter;
import com.spectre.adapter.VersionNameListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.beans.CarName;
import com.spectre.beans.ImageData;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.helper.PathUtils;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.ConvetBitmap;
import com.spectre.utility.PermissionUtility;
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
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.zhihu.matisse.MimeType.JPEG;
import static com.zhihu.matisse.MimeType.PNG;

public class RentCarActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;
    private ArrayList<String> carType = new ArrayList<>();
    private Spinner spinner_name, spinner_model, spinner_version, spinner_year, spinner_car_type, spinner_color;

    private CustomRayMaterialTextView btn_save_changes, btn_delete, btn_delete_;
    private CustomEditText et_mileage, et_car_condition, et_car_desc, et_model;
    private EditText edtCaName,edtModel,edtVersion,edtCarSeries,edtPrice,et_car_to, et_car_from,tv_post_ad_location,et_price;
    private CustomTextView  txt_post_ad_header;
    private ImageView img_post_ad_current_location;
    private String latitude = "";
    private String longitude = "";
    private String currentLatitude = "0";
    private String currentLongitude = "0";

    //Declaration of Google API Client
    private GoogleApiClient mGoogleApiClient;

    private RecyclerView recycler;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private int position = -1;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<String> showImage = new ArrayList<>();
    private ArrayList<ImageData> bitMapList = new ArrayList<>();

    ArrayList<CarName> names = new ArrayList<CarName>();
    ArrayList<ModelName> model = new ArrayList<ModelName>();
    ArrayList<VersionName> version = new ArrayList<VersionName>();
    ArrayList<String> years = new ArrayList<String>();
    private ProgressView progressDialog1;
  //  ArrayAdapter<CarName> arrayAdapterCarName;
    CarNameListAdapter arrayAdapterCarName;
 //   ArrayAdapter<ModelName> arrayAdapterModel;
    ModelNameListAdapter arrayAdapterModel;
    //ArrayAdapter<VersionName> arrayAdapterVersion;
    VersionNameListAdapter arrayAdapterVersion;
    private int oldName = 0;
    private int oldModel = 0;
    private AlertBox alertBox;
    private AdPost adPost;
    private ActionBar actionBar;
    private boolean canEdit, refresh;
    ListView listView;
    private static final int PLACE_PICKER_REQUEST = 999;
    //Define a request code to send to Google Play services
    private static final int LOCATION_PERMISSION_CONSTANT = 101;

    private Dialog ddPostAd = null;
    private CustomRayMaterialTextView btn_delete_yes, btn_delete_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_rent_car);
        context = this;
        Utility.setContentView(context, R.layout.activity_rent_car);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>" + getString(R.string.add_car_for_rent) + "</font>", true);
        initView();
        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_CONSTANT:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted) {
                        // get location
                        Utility.setLog("PERMISSION grant");
                        getLocation();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(PermissionUtility.ACCESS_FINE_LOCATION)
                                    || shouldShowRequestPermissionRationale(PermissionUtility.ACCESS_COARSE_LOCATION)) {
                                showMessageOKCancel(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PermissionUtility.requestPermission(RentCarActivity.this,
                                                new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
                                                        PermissionUtility.ACCESS_COARSE_LOCATION},
                                                LOCATION_PERMISSION_CONSTANT);
                                    }
                                });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void getLocation() {
        // get location
        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
            PermissionUtility.requestPermission(RentCarActivity.this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
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
                longitude = String.valueOf(bestLocation.getLongitude());
                Utility.setLog("Lat : " + getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
                tv_post_ad_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

            } else {
                Utility.setLog("Location is null");
            }
        }
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

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(getString(R.string.yes))
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show();
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


       /* arrayAdapterCarName = new ArrayAdapter<CarName>(context, R.layout.spinner_custom_text, names);
        spinner_name.setAdapter(arrayAdapterCarName);
        arrayAdapterModel = new ArrayAdapter<ModelName>(context, R.layout.spinner_custom_text, model);
        spinner_model.setAdapter(arrayAdapterModel);
        arrayAdapterVersion = new ArrayAdapter<VersionName>(context, R.layout.spinner_custom_text, version);
        spinner_version.setAdapter(arrayAdapterVersion);*/
        arrayAdapterCarName = new CarNameListAdapter(this, names);

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
        //mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,Utils.dpToPx(context, 100)));
       /* ViewGroup.LayoutParams params = mRecyclerView.getLayoutParams();
        params.height = Utility.dpToPx(context, 105);
        mRecyclerView.setLayoutParams(params);*/

        mRecyclerView.setNestedScrollingEnabled(false);
        et_car_condition = (CustomEditText) findViewById(R.id.et_car_condition);
        et_car_desc = (CustomEditText) findViewById(R.id.et_car_desc);
        et_mileage = (CustomEditText) findViewById(R.id.et_mileage);
        et_price = (EditText) findViewById(R.id.edtPrice);
        //et_model = (CustomEditText) findViewById(R.id.et_model);

        et_car_to = (EditText) findViewById(R.id.et_car_to);

        edtCaName = (EditText) findViewById(R.id.edtCaName);
        edtModel = (EditText) findViewById(R.id.edtModel);
        edtVersion = (EditText) findViewById(R.id.edtVersion);
        edtCarSeries = (EditText) findViewById(R.id.edtCarSeries);
        edtPrice = (EditText) findViewById(R.id.edtPrice);

        et_car_from = (EditText) findViewById(R.id.et_car_from);
        tv_post_ad_location = (EditText) findViewById(R.id.edt_rent_location);
        img_post_ad_current_location = (ImageView) findViewById(R.id.img_post_ad_current_location);

        btn_save_changes = (CustomRayMaterialTextView) findViewById(R.id.btn_save_changes);
        btn_delete = (CustomRayMaterialTextView) findViewById(R.id.btn_delete);
        btn_delete_ = (CustomRayMaterialTextView) findViewById(R.id.btn_delete_);

        btn_save_changes.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delete_.setOnClickListener(this);

       /* edtCaName.setOnClickListener(this);
        edtModel.setOnClickListener(this);
        edtVersion.setOnClickListener(this);
        edtCarSeries.setOnClickListener(this);*/
        edtPrice.setOnClickListener(this);

        et_car_to.setOnClickListener(this);
        et_car_from.setOnClickListener(this);

        tv_post_ad_location.setOnClickListener(this);
        img_post_ad_current_location.setOnClickListener(this);

        alertBox = new AlertBox(context);
        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.edit_rented_car) + "</font>"));
            adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
            position = getIntent().getExtras().getInt(Constant.POSITION);
            //       et_mileage.setText(adPost.getMileage());
            et_price.setText(adPost.getPrice());
            edtCaName.setText(adPost.getCar_name());
            edtModel.setText(adPost.getModel());
            edtCarSeries.setText(adPost.getVersion());
            //et_model.setText(adPost.getModel());
            //        et_car_desc.setText(adPost.getDescription());
            et_car_to.setText(adPost.getYear_to());
            et_car_from.setText(adPost.getYear_from());
            tv_post_ad_location.setText(adPost.getLocation());
            //        et_car_condition.setText(adPost.getCar_condition());
            //         spinner_color.setSelection(arrayAdapterCarColor.getPosition(adPost.getColor()));
            //         spinner_year.setSelection(arrayAdapterYear.getPosition(adPost.getYear()));
            //        spinner_car_type.setSelection(arrayAdapterCarType.getPosition(adPost.getCar_type()));

            btn_delete.setVisibility(View.VISIBLE);
            btn_delete_.setVisibility(View.VISIBLE);
            if (adPost.getDelete_status() != null && adPost.getDelete_status().equalsIgnoreCase("0")) {
                btn_delete.setText(getString(R.string.inactive));
            } else {
                btn_delete.setText(getString(R.string.active));
                btn_delete.setVisibility(View.VISIBLE);
            }

            if (adPost.getImage().size() > 0) {
                canEdit = false;
                new DownloadImageTask().execute("");
                //  getBase64(adPost.getImage());
            } else {
                ImageData imageData = new ImageData();
                bitMapList.add(imageData);
                mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
                mRecyclerView.setAdapter(mAdapter);
            }
        } else {
            ImageData imageData = new ImageData();
            bitMapList.add(imageData);
            mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
            mRecyclerView.setAdapter(mAdapter);
        }
        getList(1, "");
        //listners();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
            case R.id.btn_delete:
                callDeleteApi(2);
                break;
            case R.id.btn_delete_:
                // callDeleteApi(adPost.getDelete_status().equalsIgnoreCase("0") ? 1 : 0);
                chooseDeleteAd();
                break;
            case R.id.et_car_to:
                // Utility.openCalendarDialogEdit(context, et_car_to);
                showDialogCalender(1,et_car_from,et_car_to);
                break;
            case R.id.et_car_from:
                // Utility.openCalendarDialogEdit(context, et_car_from);
                showDialogCalender(0,et_car_from,et_car_to);
                break;
            case R.id.edt_rent_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_post_ad_current_location:
                getLocation();
                break;
            case R.id.edtCaName:
                showDialog( RentCarActivity.this,"Select Car Name", "carName");
                edtModel.setText("");
                edtCarSeries.setText("");
                break;
            case R.id.edtModel:
                if (model.size()>1) {
                    showDialog(RentCarActivity.this, "Select Car Model", "model");
                }
                else {
                    Toast.makeText(context,"list not available",Toast.LENGTH_SHORT).show();
                }
                edtCarSeries.setText("");
                break;
            case R.id.edtCarSeries:
                if (version.size()>1) {
                    showDialog(RentCarActivity.this, "Select Car Series", "series");
                }else {
                    Toast.makeText(context, "list not available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edtCarType:
                showDialog(RentCarActivity.this, "Select Car Type", "carType");
                break;

        }
    }

    //start of delete dialog
    public void chooseDeleteAd() {

        ddPostAd = new Dialog(context);

        ddPostAd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ddPostAd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ddPostAd.setContentView(R.layout.dialog_delete);

        btn_delete_yes = ddPostAd.findViewById(R.id.btn_delete_yes);
        btn_delete_no = ddPostAd.findViewById(R.id.btn_delete_no);
      //  txt_post_ad_header = ddPostAd.findViewById(R.id.txt_post_ad_header);

//        if (!SharedPrefUtils.getPreference(context, Constant.USER_TYPE).isEmpty() && SharedPrefUtils.getPreference(context, Constant.USER_TYPE).equalsIgnoreCase("1")) {
//            btn_post_garage.setVisibility(View.GONE);
//        } else
//            btn_post_garage.setVisibility(View.VISIBLE);
        ddPostAd.setCancelable(false);
        ddPostAd.getWindow().setLayout(-1, -2);
        ddPostAd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ddPostAd.show();

        btn_delete_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDeleteApi(2);
                ddPostAd.dismiss();
            }
        });

        btn_delete_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RentCarActivity.class));
                ddPostAd.dismiss();
            }
        });
      /*  txt_post_ad_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddPostAd.dismiss();
            }
        });*/
    }

    private void checkCondition() {
        et_mileage.setError(null);
        et_car_condition.setError(null);
        et_price.setError(null);
//        et_car_desc.setError(null);
        tv_post_ad_location.setError(null);

        if (!canEdit && adPost != null) {
            Utility.showToast(context, getString(R.string.loading));
            return;
        }

        if (bitMapList.size() == 1) {
            Utility.showToast(context, getString(R.string.empty_image));
            return;
        }

        /*if (spinner_name.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cname));
            return;
        }

        if (spinner_model.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cmodel));
            return;
        }*/

        if (tv_post_ad_location.getText().toString().trim().isEmpty()) {
            Utility.showToast(context, getString(R.string.pls_select_location));
            return;
        }
        if (edtCaName.getText().toString().trim().isEmpty()) {
            et_mileage.setError(getString(R.string.pls_select_cname));
            return;
        }
        if (edtModel.getText().toString().trim().isEmpty()) {
            edtModel.setError(getString(R.string.pls_select_cmodel));
            return;
        }
        if (edtCarSeries.getText().toString().trim().isEmpty()) {
            edtCarSeries.setError(getString(R.string.pls_select_cversion));
            return;
        }
        if (et_price.getText().toString().trim().isEmpty()) {
            et_price.setError(getString(R.string.pls_select_cPrice));
            return;
        }
        if (et_car_from.getText().toString().trim().isEmpty()) {
            // et_car_condition.setError(getString(R.string.pls_select_cCondition));
            Utility.showToast(context, getString(R.string.pls_select_fromtype));
            return;
        }

        if (et_car_to.getText().toString().trim().isEmpty()) {
            // et_car_condition.setError(getString(R.string.pls_select_cCondition));
            Utility.showToast(context, getString(R.string.pls_select_toyear));
            return;
        }

       /* if (!Utility.checkDate(et_car_from.getText().toString().trim(), et_car_to.getText().toString().trim())) {
            Utility.showToast(context, getString(R.string.date_validation));
            return;
        }*/

        callApi();

    }

    private void callApi() {

        JSONObject jsonObject = new JSONObject();
        MyDialogProgress.open(context);
        String Url = "";
        try {
            if (adPost != null) {
                jsonObject.put(Constant.ADD_ID, adPost.getAdd_id());
                Url = Urls.EDIT_RENT;
            } else {
                Url = Urls.ADD_RENT;
            }
            jsonObject.put(Constant.CAR_NAME_ID, edtCaName.getText().toString());
            jsonObject.put(Constant.CAR_NAME, edtCaName.getText().toString());
            jsonObject.put(Constant.MODEL, edtModel.getText().toString());
            jsonObject.put(Constant.MODEL_ID, edtModel.getText().toString());
            jsonObject.put(Constant.VERSION, edtCarSeries.getText().toString());
            jsonObject.put(Constant.VERSION_ID, edtCarSeries.getText().toString());

            jsonObject.put(Constant.YEAR, "");
            jsonObject.put(Constant.CAR_TYPE, "");
            jsonObject.put(Constant.COLOUR, "");
            jsonObject.put(Constant.MILEAGE, "");
            jsonObject.put(Constant.PRICE, et_price.getText().toString().trim());
            //  jsonObject.put(Constant.MODEL, et_model.getText().toString().trim());
            jsonObject.put(Constant.CAR_CONDITION, "");
            jsonObject.put(Constant.YEAR_TO, et_car_to.getText().toString().trim());
            jsonObject.put(Constant.YEAR_FROM, et_car_from.getText().toString().trim());
            jsonObject.put(Constant.DESCRIPTION, "");
            jsonObject.put(Constant.LOCATION, tv_post_ad_location.getText().toString().trim());
            jsonObject.put(Constant.LATITUDE, latitude);
            jsonObject.put(Constant.LONGITUDE, longitude);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Url, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
               /* refresh = true;
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        if (location != null) {
            Utility.setLog("Lat : " + location.getLatitude() + " - Long : " + location.getLongitude());
        } else
            Utility.setLog("Location is null");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private class ShowImagesAdapter extends RecyclerView.Adapter<ShowImagesAdapter.ViewHolder> {
        Context appContext;
        private int selectedPos = 0;
        ArrayList<ImageData> AL = new ArrayList<>();

        public ShowImagesAdapter(Context appContext, ArrayList<ImageData> arraylist, int selectedPos) {
            this.appContext = appContext;
            this.AL = arraylist;
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
                        checkPermission();
                }
            });

            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= PermissionsUtils.SDK_INT_MARSHMALLOW) {
            if (!PermissionsUtils.getInstance(context).requiredPermissionsGranted(context)) {
                return;
            }
        }

        int size = 5 - bitMapList.size();

        if (bitMapList.get(0).getBitmap() == null || bitMapList.get(bitMapList.size() - 1).getBitmap() == null) {
            size++;
        }

        if (size <= 0) {
            return;
        }


        Matisse.from(RentCarActivity.this)
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
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(", ");
                stBuilder.append(address);
                tv_post_ad_location.setText(stBuilder.toString());
            }
        }
    }

    private ImageData setImage(Uri destination) {
        ImageData imageData = new ImageData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), destination);
            bitmap = ConvetBitmap.Mytransform(bitmap);
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
      /*  arrayAdapterCarName = new ArrayAdapter<CarName>(context, R.layout.dialoglistitem,R.id.tvName,names);
        spinner_name.setAdapter(arrayAdapterCarName);
*/
        arrayAdapterCarName = new CarNameListAdapter(this, names);

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
       /* arrayAdapterModel = new ArrayAdapter<ModelName>(context, R.layout.dialoglistitem,R.id.tvName, model);
        spinner_model.setAdapter(arrayAdapterModel);
*/
        arrayAdapterModel = new ModelNameListAdapter(this, model);
        listView.setAdapter(arrayAdapterModel);
        if (adPost != null) {
            ModelName modelName = new ModelName(adPost.getModel_id(), adPost.getModel());
        //    spinner_model.setSelection(arrayAdapterModel.getPosition(modelName));
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
       // arrayAdapterVersion = new ArrayAdapter<VersionName>(context, R.layout.dialoglistitem,R.id.tvName, version);
       // spinner_version.setAdapter(arrayAdapterVersion);
        arrayAdapterVersion = new VersionNameListAdapter(this, version);
        listView.setAdapter(arrayAdapterVersion);
       /* if (version.size() > 0) {
            // getList(3, model.get(1).getId());
            spinner_version.setSelection(1);
        }*/

        if (adPost != null && !adPost.getVersion().isEmpty()) {
            VersionName versionName = new VersionName(adPost.getVersion_id(), adPost.getVersion());
          //  spinner_version.setSelection(arrayAdapterVersion.getPosition(versionName));
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

    private void getBase64(final ArrayList<String> image) {

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
            bitMapList.add(imageData);
        }

        if (image.size() > 4) {

        } else {
            ImageData imageData = new ImageData();
            bitMapList.add(imageData);
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            (progressDialog1).start();
        }

        @Override
        protected Void doInBackground(String... params) {
            getBase64(adPost.getImage());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new ShowImagesAdapter(context, bitMapList, 0);
            mRecyclerView.setAdapter(mAdapter);
            progressDialog1.stop();
            canEdit = true;
        }
    }


    //delete_post

    private void callDeleteApi(final int status) {

        JSONObject jsonObject = new JSONObject();
        MyDialogProgress.open(context);

        String Url = "";
        try {
            jsonObject.put(Constant.ADD_ID, adPost.getAdd_id());
            jsonObject.put(Constant.DELETE_STATUS, status + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //"delete_status":"1"

        new AqueryCall(this).postWithJsonToken(Urls.DELETE_RENT_CAR, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                refresh = true;
                //adPost.setDelete_status(adPost.getDelete_status().equalsIgnoreCase("0") ? "1" : "0");
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
               /*Intent intent = new Intent(context, AuthDialogActivity.class);
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
    public void showDialog(final Activity activity, String title, final String Type){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dilog_listsearch);

        TextView texttitle = (TextView)dialog.findViewById(R.id.title);
        texttitle.setText(title);
        listView = (ListView) dialog.findViewById(R.id.List);
        listView.setItemChecked(0, true);
        //  listView.setOnItemClickListener((AdapterView.OnItemClickListener) activity);

        switch(Type)
        {
            case "carName":
                // listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                listView.setAdapter(arrayAdapterCarName);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CarName selItem = (CarName) parent.getItemAtPosition(position);
                        edtCaName.setText(selItem.getCar_name());
                        //Toast.makeText(PostAdActivity.this, "clicked item", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        if (position == 0) {
                            clearModel();
                            clearVersion();
                            if (names.size() == 1) {
                                getList(1, "0");
                            }
                            return;
                        }
                        if (names.get(position).getCar_name().equals("Other")) {
                            final Dialog dialog = new Dialog(activity);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog_other);
                            final EditText name=(EditText)dialog.findViewById(R.id.edtOther);
                            Button ok=(Button)dialog.findViewById(R.id.btn_ok);
                            Button cancel=(Button)dialog.findViewById(R.id.btn_cancel);
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!name.getText().toString().isEmpty()) {
                                        edtCaName.setText(name.getText().toString());
                                        dialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(RentCarActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog.show();
                            Toast.makeText(context, "other", Toast.LENGTH_SHORT).show();
                        }

                        oldName = position;
                        clearModel();
                        clearVersion();
                        if (names != null && !names.isEmpty() && names.size() > position)
                            getList(2, names.get(position).getId());
                    }
                });
                break;

            case "series":
                listView.setAdapter(arrayAdapterVersion);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VersionName selItem = (VersionName) parent.getItemAtPosition(position);
                        edtCarSeries.setText(selItem.getVersion_name());
                        dialog.dismiss();
                    }
                });
                break;
            case "model":
                listView.setAdapter(arrayAdapterModel);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ModelName selItem = (ModelName) parent.getItemAtPosition(position);
                        edtModel.setText(selItem.getModel_name());
                        dialog.dismiss();
                        if (position == 0) {
                            clearVersion();
                            return;
                        }
                        if (oldModel == position) {
                            return;
                        }
                        oldModel = position;
                        clearVersion();
                        if (model != null && !model.isEmpty() && model.size() > position)
                            getList(3, model.get(position).getId());
                    }
                });

                break;


        }



        dialog.show();

    }
    private void showDialogCalender(final int i, final EditText txtDatePickUp, final EditText txtDateDropUp) {

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        int dayofweek=mcurrentDate.get(Calendar.DAY_OF_WEEK);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                //  myCalendar.set(Calendar.DAY_OF_WEEK,);
                //String myFormat = "dd/MM/yy"; //Change as you need
                //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                SimpleDateFormat monthname = new SimpleDateFormat("MMM");
                String monthName=monthname.format(myCalendar.getTime());

                SimpleDateFormat weekname = new SimpleDateFormat("EEEE");
                Date date = new Date(selectedyear, selectedmonth, selectedday-1);
                String weekNameStr=weekname.format(date);

                int mDay = selectedday;
                int mMonth = selectedmonth;
                int mYear = selectedyear;
                if (i==0) {
                    txtDatePickUp.setText(mDay+"/"+mMonth+"/"+mYear);

                    /*txtMonthPickUp.setText("" + monthName);
                    txtYearPickUp.setText("" + mYear);
                    txtDayPickUp.setText(weekNameStr);*/
                }
                else{
                    txtDateDropUp.setText(mDay+"/"+mMonth+"/"+mYear);
                    /*txtMonthDropUp.setText("" + monthName);
                    txtYearDropUp.setText("" + mYear);
                    txtDayDropUp.setText(weekNameStr);*/
                }
            }
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

}
