package com.spectre.activity_new;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectre.R;
import com.spectre.beans.AdPost;
import com.spectre.beans.CarName;
import com.spectre.beans.FilterResponse;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.dialog.ProgressDialog;
import com.spectre.fragment.BuyFragment;
import com.spectre.fragment.GarageFragment;
import com.spectre.fragment.RentFilterFragment;
import com.spectre.fragment.RentFragment;
import com.spectre.fragment_new.MoreFrg;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.AppConstants;
import com.spectre.utility.PermissionUtility;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spectre.utility.Utility.setLog;
import static com.spectre.utility.Utility.setToast;

public class HomeAct extends MasterAppCompactActivity implements LocationListener {

    @BindView(R.id.txt_AppBar_Title)
    public TextView txtAppBarTitle;
    @BindView(R.id.imgBuy)
    ImageView imgBuy;
    @BindView(R.id.txtBuy)
    TextView txtBuy;
    @BindView(R.id.imgRent)
    ImageView imgRent;
    @BindView(R.id.txtRent)
    TextView txtRent;
    @BindView(R.id.imgGarage)
    ImageView imgGarage;
    @BindView(R.id.txtGarage)
    TextView txtGarage;
    @BindView(R.id.imgMore)
    ImageView imgMore;
    @BindView(R.id.txtMore)
    TextView txtMore;
    @BindView(R.id.imgBack)
    public AppCompatImageView imgBack;
    @BindView(R.id.appBarMain)
    public AppBarLayout appBarMain;
    @BindView(R.id.rlAppBarMain)
    public RelativeLayout rlAppBarMain;

    // screen context
    private Context context;

    // progress dialog
    private Dialog dialog;

    // menu name for change color
    public static final String MENU_RENT = "rent";
    public static final String MENU_BUY = "buy";
    public static final String MENU_GARAGE = "garage";
    public static final String MENU_MORE = "more";

    private boolean backToExitPressed = false;
    private String lastFragment = "";

    /* [START] - Old objects */
    // private DrawerLayout drawerLayout;
//    private ViewPager viewPager;
//    private TabLayout tabLayout;
    private BuyFragment buyFragment;
    private RentFragment rentFragment;
    private GarageFragment garageFragment;
    //    private CircleImageView ivProfile;
    MenuItem item = null;
    private Dialog ddFilter = null;
    private Dialog ddFilterRent = null;
    private Dialog ddGarageFilter = null;
    private Dialog ddPostAd = null;
    private Dialog ddManageAd = null;
    private CustomRayMaterialTextView btn_garage_reset, btn_rent_reset, btn_buy_reset;
    private CustomRayMaterialTextView btn_garage_done, btn_rent_done, btn_buy_done, btn_post_buy, btn_post_rent, btn_post_garage;
    private CustomRayMaterialTextView btn_manage_buy, btn_manage_rent, btn_manage_garage;

    private TextView spinner_name, spinner_model, spinner_version, spinner_car_type, spinner_color, spinner_price, spinner_year_from, spinner_year_to, btn_done;
    private TextView spinner_name_rent, spinner_model_rent, spinner_version_rent, spinner_price_rent, btn_done__rent, spinner_from_rent, spinner_to_rent;

    private Spinner spinnerCarName, spinnerCarModel, spinnerCarVersion, spinnerCarType, spinnerColor, spinnerYearTo, spinnerYearFrom, spinnerPrice;
    private Spinner spinnerCarNameRent, spinnerCarModelRent, spinnerCarVersionRent, spinnerPriceRent;

    private ArrayList<CarName> listBrandName = new ArrayList<>();
    private ArrayList<ModelName> listModelName = new ArrayList<>();
    private ArrayList<ModelName> listModelName1 = new ArrayList<>();
    private ArrayList<VersionName> listVersionName = new ArrayList<>();
    private ArrayList<VersionName> listVersionName1 = new ArrayList<>();

    private ArrayList<CarName> listBrandNameRent = new ArrayList<>();
    private ArrayList<ModelName> listModelNameRent = new ArrayList<>();
    private ArrayList<ModelName> listModelName1Rent = new ArrayList<>();
    private ArrayList<VersionName> listVersionNameRent = new ArrayList<>();
    private ArrayList<VersionName> listVersionName1Rent = new ArrayList<>();

    private ArrayAdapter<CarName> arrayAdapterCarName;
    private ArrayAdapter<ModelName> arrayAdapterCarModel;
    private ArrayAdapter<VersionName> arrayAdapterCarVersion;

    private ArrayAdapter<CarName> arrayAdapterCarNameRent;
    private ArrayAdapter<ModelName> arrayAdapterCarModelRent;
    private ArrayAdapter<VersionName> arrayAdapterCarVersionRent;

    private FilterResponse filterResponse;
    private FilterResponse filterResponseRent;

    ArrayAdapter<String> arrayAdapterCarType, arrayAdapterCarColor, arrayAdapterYearTo, arrayAdapterYearFrom, arrayAdapterPrice;
    ArrayAdapter<String> arrayAdapterPriceRent;

    private AppCompatRadioButton radio_main, radio_repair, radio_both, radio_repair_service;
    private String garageType = "0";
    private TextView txt_header, txt_post_ad_header, txt_manage_ad_header;
    private TextView tvManageWork;

    //Define a request code to send to Google Play services
    private int status;
    private ArrayList<AdPost> arraylist;

    /* All bundle key */
    private static final String KEY_TYPE = "type";

    /* All bundle value */
    private String type = "0";
    /* [START] - Old objects */

    // Get start intent for Activity
    public static Intent getStartIntent(Context context, String type) {
        Intent intent = new Intent(context, HomeAct.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_home);

        // bind view using butter knife
        ButterKnife.bind(this);

        // Init screen context
        context = HomeAct.this;

        // get current location
        getLocation();

        // get bundle data
        getBundleData();

        // init controls
        initControls();

        // set all listener
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();

//        if (!type.equalsIgnoreCase("0") || !SharedPrefUtils.getPreference(context, Constant.USER_NAME, "").isEmpty())
//            tvName.setText(SharedPrefUtils.getPreference(context, Constant.USER_NAME, ""));
//        else
//            tvName.setText("Guest User");

//        if (!type.equalsIgnoreCase("0") || !SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, "").isEmpty())
//            new AQuery(context).id(ivProfile).image(SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, ""), true, true, 0, R.mipmap.gestuser);
//        else
//            ivProfile.setImageResource(R.mipmap.gestuser);

        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragments = getSupportFragmentManager();
        Fragment homeFrag = fragments.findFragmentByTag(BuyFragment.TAG);

        if (fragments.getBackStackEntryCount() > 1) {
            fragments.popBackStack();
        } else if (homeFrag == null || !homeFrag.isVisible()) {
            // We aren't showing the home screen, so that is the next stop on the back journey
            startHomeFragment();
        } else {
            // We are already showing the home screen, so the next stop is out of the app.
            if (backToExitPressed) {
                super.onBackPressed();
                return;
            }
            pressAgainToExit();
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
                        setLog("PERMISSION grant");
                        getLocation();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(PermissionUtility.ACCESS_FINE_LOCATION)
                                    || shouldShowRequestPermissionRationale(PermissionUtility.ACCESS_COARSE_LOCATION)) {
                                showMessageOKCancel(new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PermissionUtility.requestPermission(HomeAct.this,
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

    @Override
    public void onLocationChanged(Location location) {

        // Called when a new location is found by the network location provider.
        if (location != null) {
            setLog("Lat : " + location.getLatitude() + " - Long : " + location.getLongitude());
        } else
            setLog("Location is null");

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

    /* [START] - User define function */
    // get bundle data
    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(KEY_TYPE) != null)
                type = bundle.getString(KEY_TYPE, ""); // type data
        }
    }

    private void initControls() {
        // init progress dialog object
        dialog = ProgressDialog.createProgressDialog(context);

        /* [START] - Old code */
        filterResponse = new FilterResponse();
        filterResponseRent = new FilterResponse();

        if (!type.equalsIgnoreCase(AppConstants.TYPE_GUEST)) {
//            tvGarageRegi.setVisibility(View.GONE);
//            tvUserRegi.setVisibility(View.GONE);
//            tvLogin.setVisibility(View.GONE);
        } else {
            // tvLocation.setVisibility(View.GONE);
        }
        if (type.equalsIgnoreCase(AppConstants.TYPE_BUYER)) {
//            tvLocation.setVisibility(View.VISIBLE);
//            tvLocation.setText(getString(R.string.as_buyer));
        }
        if (type.equalsIgnoreCase(AppConstants.TYPE_SELLER)) {
//            tvLocation.setVisibility(View.VISIBLE);
//            tvLocation.setText(getString(R.string.as_seller));
        }

        setCarNames();
        setCarNamesRent();
        /* [END] - Old code */

        // start Buy fragment at first time
        if (getIntent().getStringExtra("isChange")!=null&&getIntent().getStringExtra("isChange").equals("1")) {
            startNewFragment(new RentFilterFragment(), RentFilterFragment.TAG);
        }else {
            startHomeFragment();
        }
    }

    private void setListener() {
//        tvLogin.setOnClickListener(this);
//        tvUserRegi.setOnClickListener(this);
//        tvGarageRegi.setOnClickListener(this);
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

    // press again to exit on back pressed
    private void pressAgainToExit() {
        backToExitPressed = true;
        setToast(context, getString(R.string.toast_again_to_exit));
        int backToExitPressedTimeOut = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backToExitPressed = false;
            }
        }, backToExitPressedTimeOut);
    }

    public void changeBottomMenuColor(String selectedMenu) {
        // change bottom menu text color
        txtBuy.setTextColor(getResources().getColor(R.color.hint_color));
        txtRent.setTextColor(getResources().getColor(R.color.hint_color));
        txtGarage.setTextColor(getResources().getColor(R.color.hint_color));
        txtMore.setTextColor(getResources().getColor(R.color.hint_color));

        // change bottom menu tint color for image view
        imgBuy.setColorFilter(ContextCompat.getColor(context, R.color.hint_color), PorterDuff.Mode.SRC_IN);
        imgRent.setColorFilter(ContextCompat.getColor(context, R.color.hint_color), PorterDuff.Mode.SRC_IN);
        imgGarage.setColorFilter(ContextCompat.getColor(context, R.color.hint_color), PorterDuff.Mode.SRC_IN);
        imgMore.setColorFilter(ContextCompat.getColor(context, R.color.hint_color), PorterDuff.Mode.SRC_IN);

        if (TextUtils.equals(selectedMenu, MENU_BUY)) {
            txtBuy.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            imgBuy.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        } else if (TextUtils.equals(selectedMenu, MENU_RENT)) {
            txtRent.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            imgRent.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        } else if (TextUtils.equals(selectedMenu, MENU_GARAGE)) {
            txtGarage.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            imgGarage.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        } else if (TextUtils.equals(selectedMenu, MENU_MORE)) {
            txtMore.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            imgMore.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        }
    }

    private void setCarNames() {
        listBrandName.clear();
        listBrandName.add(new CarName("0", getString(R.string.car_name)));
        getList(1, "");
    }

    private void setCarNamesRent() {
        listBrandNameRent.clear();
        listBrandNameRent.add(new CarName("0", getString(R.string.car_name)));

        getListRent(1, "");
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
    }

    private void setBrand(JSONArray jsonArray) {
        Type type = new TypeToken<List<CarName>>() {
        }.getType();

        if (jsonArray.length() > 0) {
            ArrayList<CarName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
            listBrandName.addAll(tempListNewsFeeds);
            arrayAdapterCarName = new ArrayAdapter<CarName>(context, android.R.layout.select_dialog_singlechoice, listBrandName);
            if (spinnerCarName != null)
                spinnerCarName.setAdapter(arrayAdapterCarName);
        }
    }

    private void setModel(JSONArray jsonArray) {
        Type type = new TypeToken<List<ModelName>>() {
        }.getType();
        ArrayList<ModelName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);

        if (tempListNewsFeeds.size() > 0) {
            listModelName.addAll(tempListNewsFeeds);
            arrayAdapterCarModel.notifyDataSetChanged();
        }
    }

    private void setVersion(JSONArray jsonArray) {
        Type type = new TypeToken<List<VersionName>>() {
        }.getType();
        ArrayList<VersionName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
        if (tempListNewsFeeds.size() > 0) {
            listVersionName.addAll(tempListNewsFeeds);
            arrayAdapterCarVersion.notifyDataSetChanged();
        }
    }

    private void saveDataRent(int type, JSONObject js) {
        JSONArray jsonArray;
        try {
            jsonArray = js.getJSONArray("data");
            if (jsonArray.length() > 0) {
                if (type == 1) {
                    setBrandRent(jsonArray);
                } else if (type == 2) {
                    setModelRent(jsonArray);
                } else {
                    setVersionRent(jsonArray);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setBrandRent(JSONArray jsonArray) {
        Type type = new TypeToken<List<CarName>>() {
        }.getType();

        if (jsonArray.length() > 0) {
            ArrayList<CarName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
            listBrandNameRent.addAll(tempListNewsFeeds);
            arrayAdapterCarNameRent = new ArrayAdapter<CarName>(context, android.R.layout.select_dialog_singlechoice, listBrandNameRent);
            if (spinnerCarNameRent != null)
                spinnerCarNameRent.setAdapter(arrayAdapterCarNameRent);

        }
    }

    private void setModelRent(JSONArray jsonArray) {
        Type type = new TypeToken<List<ModelName>>() {
        }.getType();
        ArrayList<ModelName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);

        if (tempListNewsFeeds.size() > 0) {
            listModelNameRent.addAll(tempListNewsFeeds);
            arrayAdapterCarModelRent.notifyDataSetChanged();
        }
    }

    private void setVersionRent(JSONArray jsonArray) {
        Type type = new TypeToken<List<VersionName>>() {
        }.getType();
        ArrayList<VersionName> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
        if (tempListNewsFeeds.size() > 0) {
            listVersionNameRent.addAll(tempListNewsFeeds);
            arrayAdapterCarVersionRent.notifyDataSetChanged();
        }
    }

    public FilterResponse getFilter() {
        return filterResponse;
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(getString(R.string.yes))
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show();
    }
    /* [END] - User define function */

    /* [START] - All method related to start new fragment */
    public void startHomeFragment() {
        // --- remove all fragment
        if (lastFragment.trim().length() > 0) {
            FragmentManager fragments = getSupportFragmentManager();
            fragments.popBackStack(lastFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        // Pop off everything up to and including the current tab
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BuyFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        lastFragment = BuyFragment.TAG;
        // Add the new tab fragment
        /*fragmentManager.beginTransaction()
                // .replace(R.id.main_view, BuyFragment.newInstance(), BuyFragment.TAG)
                .replace(R.id.main_view, new BuyFragment(), BuyFragment.TAG)
                .addToBackStack(BuyFragment.TAG)
                .commit();*/


            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.main_view, BuyFragment.newInstance(), BuyFragment.TAG);
            transaction.addToBackStack(BuyFragment.TAG);
            transaction.commit();


    }

    public void startNewFragment(Fragment fragment) {
        hideKeyBoard();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.main_view, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void startNewFragment(Fragment fragment, String tag) {
        // --- remove all fragment
        if (lastFragment.trim().length() > 0) {
            FragmentManager fragments = getSupportFragmentManager();
            fragments.popBackStack(lastFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        lastFragment = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.main_view, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
    /* [END] - All method related to start new fragment */

    /* [START] - Butter knife listener */
    @OnClick({R.id.llBuy, R.id.llRent, R.id.llGarage, R.id.llMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llBuy:
                startHomeFragment();
                break;
            case R.id.llRent:
                startNewFragment(new RentFilterFragment(), RentFilterFragment.TAG);
                break;
            case R.id.llGarage:
                startNewFragment(new GarageFragment(), GarageFragment.TAG);
                break;
            case R.id.llMore:
                startNewFragment(MoreFrg.newInstance(), MoreFrg.TAG);
                break;
        }
    }

    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        onBackPressed();
    }
    /* [END] - Butter knife listener */

    /* [START] - All method for call api */
    // Type 1 = Brand list, 2 = Model, 3 = Version
    private void getList(final int type, String id) {

        if (!Utility.isConnectingToInternet(context)) {
            Utility.showToast(context, getString(R.string.connection));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        String Url = "";

        try {
            if (type == 1) {
                Url = Urls.CAR_NAME_LIST;
//                jsonObject.put("location", id);
            } else if (type == 2) {
                Url = Urls.MODEL_NAME_LIST;
                jsonObject.put("car_name_id", id);

            } else if (type == 3) {
                Url = Urls.VERSION_NAME_LIST;
                jsonObject.put("model_id", id);
            }

            jsonObject.put(Constant.LATITUDE, longitude);
            jsonObject.put(Constant.LONGITUDE, latitude);
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


                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

//                SessionExpireDialog.openDialog(context);
            }

            @Override
            public void onNull(JSONObject js, String msg) {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(JSONObject js, String msg) {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }

    private void getListRent(final int type, String id) {
        getLocation();

        if (!Utility.isConnectingToInternet(context)) {
            Utility.showToast(context, getString(R.string.connection));
            return;
        }

        JSONObject jsonObject = new JSONObject();
        String Url = "";


        try {
            if (type == 1) {
                Url = Urls.CAR_NAME_LIST;

            } else if (type == 2) {
                Url = Urls.MODEL_NAME_LIST;
                jsonObject.put("car_name_id", id);
            } else if (type == 3) {
                Url = Urls.VERSION_NAME_LIST;
                jsonObject.put("model_id", id);
            }
            jsonObject.put(Constant.LATITUDE, latitude);
            jsonObject.put(Constant.LONGITUDE, longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new AqueryCall(this).postWithJsonToken(Url, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                saveDataRent(type, js);
            }

            @Override
            public void onFailed(JSONObject js, String msg) {


                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {

//                SessionExpireDialog.openDialog(context);
            }

            @Override
            public void onNull(JSONObject js, String msg) {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onException(JSONObject js, String msg) {

                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }
    /* [END] - All method for call api */

    /* [START] - Related to get current location */
    private static final int PLACE_PICKER_REQUEST = 999;
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    public String latitude = "";
    public String longitude = "";

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, HomeAct.this);
//                StringBuilder stBuilder = new StringBuilder();
//                String placename = String.format("%s", place.getName());
//                latitude = String.valueOf(place.getLatLng().latitude);
//                setLog("LAT 2 : " + latitude);
//                longitude = String.valueOf(place.getLatLng().longitude);
//                String address = String.format("%s", place.getAddress());
//                stBuilder.append(placename);
//                stBuilder.append(", ");
//                stBuilder.append(address);
//                // tv_rent_fragment_location.setText(stBuilder.toString());
//                // callMethodEventList(0);
//            }
//        }
//    }

    // get user current location
    private void getLocation() {
        // get location
        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
            PermissionUtility.requestPermission(this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
                    PermissionUtility.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CONSTANT);
        } else {
            setLog("PERMISSION grant");
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
                setLog("Lat : " + bestLocation.getLatitude() + " - Long : " + bestLocation.getLongitude());
                latitude = String.valueOf(bestLocation.getLatitude());
                setLog("LAT 1 : " + latitude);
                longitude = String.valueOf(bestLocation.getLongitude());
                setLog("Lat : " + getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
                //tv_rent_fragment_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

            } else {
                setLog("Location is null");
            }
        }
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
    /* [END] - Related to get current location */

    // get user current location
//    private void getLocation() {
//        // get location
//        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
//                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
//            PermissionUtility.requestPermission(this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
//                    PermissionUtility.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CONSTANT);
//        } else {
//            Utility.setLog("PERMISSION grant");
//            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            List<String> providers = locationManager.getProviders(true);
//            Location bestLocation = null;
//            for (String provider : providers) {
//                Location l = locationManager.getLastKnownLocation(provider);
//                if (l == null) {
//                    continue;
//                }
//                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                    bestLocation = l; // Found best last known location;
//                }
//            }
//            if (bestLocation != null) {
//                Utility.setLog("Lat : " + bestLocation.getLatitude() + " - Long : " + bestLocation.getLongitude());
//                latitude = String.valueOf(bestLocation.getLatitude());
//                Utility.setLog("LAT 1 : " + latitude);
//                longitude = String.valueOf(bestLocation.getLongitude());
//                Utility.setLog("Lat : " + getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
////                tv_rent_fragment_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
//
//            } else {
//                Utility.setLog("Location is null");
//            }
//        }
//    }
}

