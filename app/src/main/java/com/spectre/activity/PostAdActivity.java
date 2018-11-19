package com.spectre.activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhihu.matisse.MimeType.JPEG;
import static com.zhihu.matisse.MimeType.PNG;

public class PostAdActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.edtCaName)
    EditText edtCaName;
    @BindView(R.id.edtModel)
    EditText edtModel;
    @BindView(R.id.edtCarSeries)
    EditText edtCarSeries;
    @BindView(R.id.edtYear)
    EditText edtYear;
    @BindView(R.id.edtCarType)
    EditText edtCarType;
    @BindView(R.id.edtMileage)
    EditText edtMileage;
    @BindView(R.id.edtPrice)
    EditText edtPrice;
    @BindView(R.id.spinner_color)
    Spinner spinnerColor;
    @BindView(R.id.edt_color)
    EditText edtColor;
    @BindView(R.id.et_car_condition)
    EditText etCarCondition;
    @BindView(R.id.edt_buy_location)
    EditText edtBuyLocation;
    @BindView(R.id.btn_save_changes)
    CustomRayMaterialTextView btnSaveChanges;
    @BindView(R.id.nested_view)
    NestedScrollView nestedView;
    private Context context;
    Unbinder unbinder;
   // private ArrayList<String> carType = new ArrayList<>();
    private Spinner spinner_name, spinner_model, spinner_version, spinner_year, spinner_car_type, spinner_color;
    ArrayAdapter<String> arrayAdapterCarType, arrayAdapterCarColor, arrayAdapterYear;

    //private com.rey.material.widget.Spinner spinner_name;
    private CustomRayMaterialTextView btn_save_changes, btn_delete, btn_delete_;
    private CustomEditText et_mileage1, et_price1, et_model1, et_version;
    private RecyclerView recycler;
    private static final int REQUEST_CODE_CHOOSE = 23;

    private CustomTextView txt_post_ad_header;
    //private EditText edt_ad_buy_location, et_car_condition, et_mileage, et_price, et_model;
    private ImageView img_post_ad_buy_current_location;
    private String latitude = "";
    private String longitude = "";

    //Declaration of Google API Client
    private GoogleApiClient mGoogleApiClient;

    private static final int PLACE_PICKER_REQUEST = 999;
    //Define a request code to send to Google Play services
    private static final int LOCATION_PERMISSION_CONSTANT = 101;

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
   // ArrayAdapter<CarName> arrayAdapterCarName;
    CarNameListAdapter arrayAdapterCarName;
   // ArrayAdapter<ModelName> arrayAdapterModel;
    ModelNameListAdapter arrayAdapterModel;
   // ArrayAdapter<VersionName> arrayAdapterVersion;
    VersionNameListAdapter arrayAdapterVersion;
    private int oldName = 0;
    private int oldModel = 0;
    private int position = -1;
    private AlertBox alertBox;
    private AdPost adPost;
    private ActionBar actionBar;
    private boolean canEdit, refresh;

    private Dialog ddPostAd = null;
     Dialog dialog;
    private CustomRayMaterialTextView btn_delete_yes, btn_delete_no;
    List<String> carColor,carType;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.activity_post_ad);
        context = this;
        Utility.setContentView(context, R.layout.activity_post_ad);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>" + getString(R.string.post_ad) + "</font>", true);
        unbinder = ButterKnife.bind(this);
        initView();
        getLocation();
    }

    private void initView() {
        spinner_name = (Spinner) findViewById(R.id.spinner_name);
        spinner_model = (Spinner) findViewById(R.id.spinner_model);
        spinner_version = (Spinner) findViewById(R.id.spinner_version);
        spinner_year = (Spinner) findViewById(R.id.spinner_year);
        spinner_car_type = (Spinner) findViewById(R.id.spinner_car_type);
        spinner_color = (Spinner) findViewById(R.id.spinner_color);

        String arrayCarType[] = getResources().getStringArray(R.array.car_type);
        carType = Arrays.asList(arrayCarType);

        String arraycarColor[] = getResources().getStringArray(R.array.car_color);
         carColor = Arrays.asList(arraycarColor);

        names.add(Utility.getCarName(context));
        model.add(Utility.getModelName(context));
        version.add(Utility.getVersionName(context));
        years.add(getString(R.string.select_year));

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        // years.add("Select Year");
        for (int i = thisYear; i >= 1950; i--) {
            years.add(Integer.toString(i));
        }

         //arrayAdapterCarName = new ArrayAdapter<CarName>(context, R.layout.dialoglistitem, names);
        //spinner_name.setAdapter(arrayAdapterCarName);
        arrayAdapterCarName = new CarNameListAdapter(this, names);


       // arrayAdapterModel = new ArrayAdapter<ModelName>(context, R.layout.spinner_custom_text, model);
       // spinner_model.setAdapter(arrayAdapterModel);
        //arrayAdapterVersion = new ArrayAdapter<VersionName>(context, R.layout.spinner_custom_text, version);
      //  spinner_version.setAdapter(arrayAdapterVersion);

        arrayAdapterCarType = new ArrayAdapter<String>(context, R.layout.dialoglistitem,R.id.tvName,carType);
        spinner_car_type.setAdapter(arrayAdapterCarType);
        arrayAdapterCarColor = new ArrayAdapter<String>(context, R.layout.dialoglistitem,R.id.tvName, carColor);
        spinner_color.setAdapter(arrayAdapterCarColor);
        arrayAdapterYear = new ArrayAdapter<String>(context, R.layout.dialoglistitem, R.id.tvName,years);
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
      //  et_model = (EditText) findViewById(R.id.edtModel);
        // et_version = (CustomEditText) findViewById(R.id.et_version);
     //   et_car_condition = (EditText) findViewById(R.id.et_car_condition);
     //   et_mileage = (EditText) findViewById(R.id.edtMileage);
     //   et_price = (EditText) findViewById(R.id.edtPrice);
        btn_save_changes = (CustomRayMaterialTextView) findViewById(R.id.btn_save_changes);
        btn_delete = (CustomRayMaterialTextView) findViewById(R.id.btn_delete);
        btn_delete_ = (CustomRayMaterialTextView) findViewById(R.id.btn_delete_);

     //   edt_ad_buy_location = (EditText) findViewById(R.id.edt_buy_location);
        img_post_ad_buy_current_location = (ImageView) findViewById(R.id.img_post_ad_buy_current_location);


        edtBuyLocation.setOnClickListener(this);
     //   edtCaName.setOnClickListener(this);
        edtColor.setOnClickListener(this);
   //     edtCarSeries.setOnClickListener(this);
   //     edtModel.setOnClickListener(this);
        edtYear.setOnClickListener(this);
        edtCarType.setOnClickListener(this);
        etCarCondition.setOnClickListener(this);

        img_post_ad_buy_current_location.setOnClickListener(this);

        btn_save_changes.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delete_.setOnClickListener(this);

        alertBox = new AlertBox(context);
        if (getIntent().getExtras() != null && getIntent().getExtras().get(Constant.DATA) != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.edit_ad) + "</font>"));
            adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
            position = getIntent().getExtras().getInt(Constant.POSITION);
            edtMileage.setText(adPost.getMileage());
            edtPrice.setText(adPost.getPrice());
            edtCaName.setText(adPost.getCar_name());
            edtModel.setText(adPost.getModel());
            edtYear.setText(adPost.getYear());
            edtCarSeries.setText(adPost.getVersion());
            edtCarType.setText(adPost.getCar_type());
            edtColor.setText(adPost.getColor());

//            et_model.setText(adPost.getModel());
//            et_version.setText(adPost.getVersion());
            etCarCondition.setText(adPost.getCar_condition());

            edtBuyLocation.setText(adPost.getLocation());
            spinner_color.setSelection(arrayAdapterCarColor.getPosition(adPost.getColor()));
            spinner_year.setSelection(arrayAdapterYear.getPosition(adPost.getYear()));
            spinner_car_type.setSelection(arrayAdapterCarType.getPosition(adPost.getCar_type()));
            btn_delete.setVisibility(View.VISIBLE);
            btn_delete_.setVisibility(View.VISIBLE);

            if (adPost.getDelete_status() != null && adPost.getDelete_status().equalsIgnoreCase("0")) {
                btn_delete.setText(getString(R.string.inactive));
            } else {
                btn_delete.setVisibility(View.VISIBLE);
                btn_delete.setText(getString(R.string.active));
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
                if (names.get(position).equals("Other")) {
                    Toast.makeText(context, "other", Toast.LENGTH_SHORT).show();
                }
                oldName = position;
                clearModel();
                clearVersion();
                if (names != null && !names.isEmpty() && names.size() > position)
                    getList(2, names.get(position).getId());
            }
        });
     /*   spinner_name.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

 /*      spinner_model.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }

       });*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_changes:
                checkCondition();
                break;
          /*  case R.id.btn_delete:
                callDeleteApi(2);
                break;*/
            case R.id.btn_delete_:
                chooseDeleteAd();
                break;
            case R.id.edt_buy_location:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.img_post_ad_buy_current_location:
                getLocation();
                break;
            case R.id.edtCaName:
                showDialog( PostAdActivity.this,"Select Car Name", "carName");
                edtModel.setText("");
                edtCarSeries.setText("");
                break;
            case R.id.edt_color:
                showDialog(PostAdActivity.this, "Select Color", "color");
                break;
            case R.id.edtCarSeries:
                if (version.size()>1) {
                    showDialog(PostAdActivity.this, "Select Car Series", "series");
                }else {
                    Toast.makeText(context, "list not available", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.edtModel:
                if (model.size()>1) {
                    showDialog(PostAdActivity.this, "Select Car Model", "model");
                }
                else {
                    Toast.makeText(context,"list not available",Toast.LENGTH_SHORT).show();
                }
                edtCarSeries.setText("");
                break;
            case R.id.edtYear:
                showDialog(PostAdActivity.this, "Select Year", "year");
                break;
            case R.id.edtCarType:
                showDialog(PostAdActivity.this, "Select Car Type", "carType");
                break;


        }
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
                                        PermissionUtility.requestPermission(PostAdActivity.this,
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
            PermissionUtility.requestPermission(PostAdActivity.this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
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
                edtBuyLocation.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

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


    //start of delete dialog
    public void chooseDeleteAd() {

        ddPostAd = new Dialog(context);

        ddPostAd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ddPostAd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ddPostAd.setContentView(R.layout.dialog_delete);

        btn_delete_yes = ddPostAd.findViewById(R.id.btn_delete_yes);
        btn_delete_no = ddPostAd.findViewById(R.id.btn_delete_no);
        txt_post_ad_header = ddPostAd.findViewById(R.id.txt_post_ad_header);

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
        txt_post_ad_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddPostAd.dismiss();
            }
        });
    }

    private void checkCondition() {
        edtMileage.setError(null);
        etCarCondition.setError(null);
        edtPrice.setError(null);
        edtCaName.setError(null);
        edtModel.setError(null);
        edtYear.setError(null);
        edtCarSeries.setError(null);
        edtCarType.setError(null);
        edtColor.setError(null);
        edtBuyLocation.setError(null);
//            et_model.setText(adPost.getModel());
//            et_version.setText(adPost.getVersion());
      //  etCarCondition.setText(adPost.getCar_condition());

       // edtBuyLocation.setText(adPost.getLocation());

//        et_model.setError(null);
//        et_version.setError(null);


        if (!canEdit && adPost != null) {
            Utility.showToast(context, getString(R.string.loading));
            return;
        }

        if (bitMapList.size() == 1) {
            Utility.showToast(context, getString(R.string.empty_image));
            return;
        }

       /* if (spinner_name.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cname));
            return;
        }*/

      /*  if (spinner_model.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cmodel));
            return;
        }*/

       /* if (spinner_version.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cversion));
            return;
        }*/

     /*   if (spinner_year.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cyear));
            return;
        }*/

       /* if (spinner_car_type.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_ctype));
            return;
        }

        if (spinner_color.getSelectedItemPosition() == 0) {
            Utility.showToast(context, getString(R.string.pls_select_cColor));
            return;
        }
*/
        if (edtCaName.getText().toString().trim().isEmpty()) {
            edtCaName.setError(getString(R.string.pls_select_cname));
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
        if (edtYear.getText().toString().trim().isEmpty()) {
            edtYear.setError(getString(R.string.pls_select_cyear));
            return;
        }
        if (edtMileage.getText().toString().trim().isEmpty()) {
            edtMileage.setError(getString(R.string.pls_select_cMileage));
            return;
        }

        if (edtPrice.getText().toString().trim().isEmpty()) {
            edtPrice.setError(getString(R.string.pls_select_cPrice));
            return;
        }
        if (etCarCondition.getText().toString().trim().isEmpty()) {
            etCarCondition.setError(getString(R.string.pls_select_cCondition));
            return;
        }
//        if (et_model.getText().toString().trim().isEmpty()) {
//            et_model.setError(getString(R.string.pls_enter_model));
//            return;
//        }
//
//        if (et_version.getText().toString().trim().isEmpty()) {
//            et_version.setError(getString(R.string.pls_enter_version));
//            return;
//        }

        if (edtBuyLocation.getText().toString().trim().isEmpty()) {
            Utility.showToast(context, getString(R.string.pls_select_location));
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
                Url = Urls.EDIT_POST;
            } else {
                Url = Urls.ADD_POST;
            }
            jsonObject.put(Constant.CAR_NAME_ID, "");
            jsonObject.put(Constant.CAR_NAME, edtCaName.getText().toString());
            jsonObject.put(Constant.MODEL, edtModel.getText().toString());
            jsonObject.put(Constant.MODEL_ID, "");
            jsonObject.put(Constant.VERSION, edtCarSeries.getText().toString());
            jsonObject.put(Constant.VERSION_ID, "");
         /*   if (spinner_version.getSelectedItemPosition() != 0) {
                jsonObject.put(Constant.VERSION, edtCarSeries.getText().toString());
                jsonObject.put(Constant.VERSION_ID, "");
            } else {
                jsonObject.put(Constant.VERSION, "");
                jsonObject.put(Constant.VERSION_ID, 0);
            }*/

            jsonObject.put(Constant.YEAR, edtYear.getText().toString());
            jsonObject.put(Constant.CAR_TYPE, edtCarType.getText().toString());
            jsonObject.put(Constant.COLOUR, edtColor.getText().toString());
            jsonObject.put(Constant.MILEAGE, edtMileage.getText().toString().trim());
            jsonObject.put(Constant.PRICE, edtPrice.getText().toString().trim());
//            jsonObject.put(Constant.MODEL, et_model.getText().toString().trim());
//            jsonObject.put(Constant.VERSION, et_version.getText().toString().trim());
            jsonObject.put(Constant.CAR_CONDITION, etCarCondition.getText().toString().trim());
            jsonObject.put(Constant.LOCATION, edtBuyLocation.getText().toString().trim());
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
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        MyDialogProgress.close(context);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_images, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
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

        Matisse.from(PostAdActivity.this)
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
                edtBuyLocation.setText(stBuilder.toString());
            }
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
        if (names.size() > 1) {
            names.add(names.size(), Utility.getOtherName(context));
        }

        //arrayAdapterCarName = new ArrayAdapter<CarName>(context, R.layout.spinner_custom_text, names);
       // spinner_name.setAdapter(arrayAdapterCarName);
        arrayAdapterCarName = new CarNameListAdapter(this, names);
        //listView.setAdapter(arrayAdapterCarName);
        if (adPost != null) {
            CarName carName = new CarName(adPost.getCar_name_id(), adPost.getCar_name());
           // spinner_name.setSelection(arrayAdapterCarName.getPosition(carName));
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
       // arrayAdapterModel = new ArrayAdapter<ModelName>(context, R.layout.spinner_custom_text, model);
       // spinner_model.setAdapter(arrayAdapterModel);

        arrayAdapterModel = new ModelNameListAdapter(this, model);
        listView.setAdapter(arrayAdapterModel);

        if (adPost != null) {
            ModelName modelName = new ModelName(adPost.getModel_id(), adPost.getModel());
           // spinner_model.setSelection(arrayAdapterModel.getPosition(modelName));
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

        arrayAdapterVersion = new VersionNameListAdapter(this, version);
        listView.setAdapter(arrayAdapterVersion);

        // arrayAdapterVersion = new ArrayAdapter<VersionName>(context, R.layout.spinner_custom_text, version);
        //      spinner_version.setAdapter(arrayAdapterVersion);

       /* if (version.size() > 0) {
            // getList(3, model.get(1).getId());
            spinner_version.setSelection(1);
        }*/

        if (adPost != null && !adPost.getVersion().isEmpty()) {
            VersionName versionName = new VersionName(adPost.getVersion_id(), adPost.getVersion());
            //spinner_version.setSelection(arrayAdapterVersion.getPosition(versionName));
            //getList(3, versionName.getId());
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
            jsonObject.put(Constant.DELETE_STATUS, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //"delete_status":"1"

        new AqueryCall(this).postWithJsonToken(Urls.DELETE_POST, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
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
                                            Toast.makeText(PostAdActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
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
                case "color":
                    listView.setAdapter(arrayAdapterCarColor);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            edtColor.setText(carColor.get(position));
                            dialog.dismiss();
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
                case "year":
                    listView.setAdapter(arrayAdapterYear);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            edtYear.setText(years.get(position));
                            dialog.dismiss();
                        }
                    });
                    break;
                case "carType":
                    listView.setAdapter(arrayAdapterCarType);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          edtCarType.setText(carType.get(position));
                          dialog.dismiss();
                        }
                    });

                    break;

            }



            dialog.show();

        }


}
