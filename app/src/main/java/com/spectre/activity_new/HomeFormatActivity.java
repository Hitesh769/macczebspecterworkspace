package com.spectre.activity_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.spectre.R;
import com.spectre.activity.AddWorkActivity;
import com.spectre.activity.EditProfileActivity;
import com.spectre.activity.LoginActivity;
import com.spectre.activity.ManageAdActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.ManageWorkActivity;
import com.spectre.activity.NotificationActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.activity.SettingsActivity;
import com.spectre.activity.SignupActivity;
import com.spectre.beans.AdPost;
import com.spectre.beans.CarName;
import com.spectre.beans.FilterResponse;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.fragment.BuyFragment;
import com.spectre.fragment.GarageFragment;
import com.spectre.fragment.RentFragment;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFormatActivity extends MasterAppCompactActivity implements View.OnClickListener, LocationListener {
/*Drawer Option Garage ----> My Profile,Add Your Work,Manage Your Work,Post Your Work,
Post Your Ad,Manage Your Ad,Give for Rent,Manage Rented,Settings*/

    /*Drawer Option User ---> My Profile,Post Your Ad,Manage Your Ad,Give for Rent,Manage Rented,Settings*/

    private Context context;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private BuyFragment buyFragment;
    private RentFragment rentFragment;
    private GarageFragment garageFragment;
    private TextView tvName, tvLocation;
    private CircleImageView ivProfile;
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
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private String latitude = "";
    private String longitude = "";
    private int status;
    private ArrayList<AdPost> arraylist;

    /* All bundle key */
    private static final String KEY_TYPE = "type";

    /* All bundle value */
    private String type = "0";

    /**
     * Get start intent for Activity
     *
     * @param context
     * @return
     */
    public static Intent getStartIntent(Context context, String type) {
        Intent intent = new Intent(context, HomeFormatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_format);
        // Utility.setContentView(context, R.layout.activity_home_format);

        // Init screen context
        context = HomeFormatActivity.this;

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
                                        PermissionUtility.requestPermission(HomeFormatActivity.this,
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

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(getString(R.string.yes))
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show();
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

    /* [START] - User define function */
    // get bundle data
    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString(KEY_TYPE) != null)
                type = bundle.getString(KEY_TYPE, "");
        }
    }

    TextView tvUserRegi, tvLogin, tvGarageRegi;

    // init all controls
    private void initControls() {
        setUpToolbar(context, "Home");

        filterResponse = new FilterResponse();
        filterResponseRent = new FilterResponse();

        drawerLayout = findViewById(R.id.drawerlayout);
        tvName = findViewById(R.id.tv_name);
        tvLocation = findViewById(R.id.tv_location);
        tvLogin = findViewById(R.id.tv_login);
        tvUserRegi = findViewById(R.id.tv_user_regi);
        tvGarageRegi = findViewById(R.id.tv_garage_regi);

        ivProfile = findViewById(R.id.iv_profile);

        viewPager = findViewById(R.id.viewPager);

        tabLayout = findViewById(R.id.tabLayout);

        if (!type.equalsIgnoreCase(AppConstants.TYPE_GUEST)) {
            tvGarageRegi.setVisibility(View.GONE);
            tvUserRegi.setVisibility(View.GONE);
            tvLogin.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.GONE);
        }
        if (type.equalsIgnoreCase(AppConstants.TYPE_BUYER)) {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(getString(R.string.as_buyer));
        }
        if (type.equalsIgnoreCase(AppConstants.TYPE_SELLER)) {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(getString(R.string.as_seller));
        }

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        // set horizontal line with tab view
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.GRAY);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);

        setCarNames();
        setCarNamesRent();
    }

    // set all listener
    private void setListener() {
        ((TextView) findViewById(R.id.tv_profile)).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_post_ad)).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_manage_ad)).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_setting)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.layout_logout)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.profile_edit)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.btn_cross)).setOnClickListener(this);

        tvLogin.setOnClickListener(this);
        tvUserRegi.setOnClickListener(this);
        tvGarageRegi.setOnClickListener(this);
    }

    // get user current location
    private void getLocation() {
        // get location
        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
            PermissionUtility.requestPermission(this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
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
                Utility.setLog("LAT 1 : " + latitude);
                longitude = String.valueOf(bestLocation.getLongitude());
                Utility.setLog("Lat : " + getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
//                tv_rent_fragment_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

            } else {
                Utility.setLog("Location is null");
            }
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
    /* [END] - User define function */


    private void setCarModel(String id, boolean isCall) {
        listModelName.clear();
        listModelName.add(new ModelName("0", getString(R.string.model)));

        if (isCall)
            getList(2, id);
        else {
            if (arrayAdapterCarModel == null) {
                arrayAdapterCarModel = new ArrayAdapter<ModelName>(context, android.R.layout.simple_list_item_single_choice, listModelName);
                spinnerCarModel.setAdapter(arrayAdapterCarModel);
            } else {
                arrayAdapterCarModel.notifyDataSetChanged();
            }
        }
    }

    private void setCarVersion(String id, boolean isCall) {
        listVersionName.clear();
        listVersionName.add(new VersionName("0", getString(R.string.version)));

        if (isCall)
            getList(3, id);
        else {
            if (arrayAdapterCarVersion == null) {
                arrayAdapterCarVersion = new ArrayAdapter<VersionName>(context, android.R.layout.simple_list_item_single_choice, listVersionName);
                spinnerCarVersion.setAdapter(arrayAdapterCarVersion);
            } else {
            }
            arrayAdapterCarVersion.notifyDataSetChanged();
        }
    }

    private void setCarModelRent(String id, boolean isCall) {
        listModelNameRent.clear();
        listModelNameRent.add(new ModelName("0", getString(R.string.model)));

        if (isCall)
            getListRent(2, id);
        else {
            if (arrayAdapterCarModelRent == null) {
                arrayAdapterCarModelRent = new ArrayAdapter<ModelName>(context, android.R.layout.simple_list_item_single_choice, listModelNameRent);
                spinnerCarModelRent.setAdapter(arrayAdapterCarModelRent);
            } else {
                arrayAdapterCarModelRent.notifyDataSetChanged();
            }
        }
    }

    private void setCarVersionRent(String id, boolean isCall) {
        listVersionNameRent.clear();
        listVersionNameRent.add(new VersionName("0", getString(R.string.version)));

        if (isCall)
            getListRent(3, id);
        else {
            if (arrayAdapterCarVersionRent == null) {
                arrayAdapterCarVersionRent = new ArrayAdapter<VersionName>(context, android.R.layout.simple_list_item_single_choice, listVersionNameRent);
                spinnerCarVersionRent.setAdapter(arrayAdapterCarVersionRent);
            } else {
            }
            arrayAdapterCarVersionRent.notifyDataSetChanged();
        }
    }

    public Toolbar setUpToolbar(final Context context, String title) {
        final AppCompatActivity activity = (AppCompatActivity) context;
        Toolbar actionBarToolbar = (Toolbar) activity.findViewById(R.id.toolbar_actionbar);
        activity.setSupportActionBar(actionBarToolbar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarToolbar.setNavigationIcon(R.mipmap.drawer);
        actionBarToolbar.setNavigationContentDescription(title);
        actionBarToolbar.setTitleTextColor(Color.WHITE);
        actionBarToolbar.setTitle(title);
        actionBarToolbar.setSubtitle("");
        // actionBarToolbar.inflateMenu(R.menu.home_menu);

        actionBarToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:

                        break;
                    case R.id.filter:
                        if (viewPager.getCurrentItem() == 2) {
                            iPhone1();
                        } else if (viewPager.getCurrentItem() == 0) {
                            iPhone();
                        } else {
                            iPhoneRent();
                        }
                        break;
                    case R.id.notification:
                        startActivity(new Intent(context, NotificationActivity.class));
                        break;
                }
                return false;
            }
        });
        actionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeft();
            }
        });
        return actionBarToolbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.home_menu, menu);
        item = menu.findItem(R.id.filter);
        boolean setVisibility = isLogin();
        menu.getItem(2).setVisible(setVisibility);

        menu.getItem(0).setVisible(false);
        // menu.getItem(2).setVisible(setVisibility);
        return super.onCreateOptionsMenu(menu);
    }

    private void openLeft() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else
            drawerLayout.closeDrawer(Gravity.LEFT);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        } else if (viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0, false);
            return;
        } else
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (type.equalsIgnoreCase("0")) {
            switch (v.getId()) {
                case R.id.btn_cross:
                    break;
                case R.id.tv_login:
                    startActivity(new Intent(context, LoginActivity.class));
                    break;
                case R.id.tv_user_regi:
                    startActivity(new Intent(context, SignupActivity.class).putExtra(Constant.TYPE, 1));
                    break;
                case R.id.tv_garage_regi:
                    startActivity(new Intent(context, SignupActivity.class).putExtra(Constant.TYPE, 2));
                    break;
                case R.id.tv_profile:
                case R.id.tv_post_ad:
//                case R.id.tv_manage_rent:
                case R.id.tv_manage_ad:
//                case R.id.tv_manage_work:
//                case R.id.tv_post_work:
//                case R.id.tv_give_rent:
                case R.id.tv_setting:
                case R.id.layout_logout:
                    openDialogToLogin();
                    break;
                default:
                    dialogClick(v);
                    return;
            }
            onBackPressed();
        } else {
            switch (v.getId()) {
                case R.id.btn_cross:
                    onBackPressed();
                    break;
                case R.id.tv_profile:
                case R.id.profile_edit:
                    startActivity(new Intent(context, EditProfileActivity.class));
                    onBackPressed();
                    break;
                case R.id.tv_post_ad:
                    //startActivity(new Intent(context, PostAdActivity.class));
                    choosePostAd();
                    onBackPressed();
                    break;
                case R.id.tv_manage_ad:
                    //startActivity(new Intent(context, ManageAdActivity.class));
                    chooseManageAd();
                    onBackPressed();
                    break;
//                case R.id.tv_manage_rent:
//                    startActivity(new Intent(context, ManageRentedActivity.class));
//                    onBackPressed();
//                    break;
//                case R.id.tv_manage_work:
//                    startActivity(new Intent(context, ManageWorkActivity.class));
//                    onBackPressed();
//                    break;
//                case R.id.tv_post_work:
//                    startActivityForResult(new Intent(context, AddWorkSectionActivity.class), 404);
//                    // startActivityForResult(new Intent(context, AddWorkActivity.class), 404);
//                    onBackPressed();
//                    break;
//                case R.id.tv_give_rent:
//                    startActivity(new Intent(context, RentCarActivity.class));
//                    onBackPressed();
//                    break;
                case R.id.tv_setting:
                    startActivity(new Intent(context, SettingsActivity.class));
                    onBackPressed();
                    break;
                case R.id.layout_logout:
                    Utility.openDialogToLogout(context);
                    onBackPressed();
                    break;
                default:
                    dialogClick(v);
                    break;
            }

        }
    }

    private void dialogClick(View v) {
        switch (v.getId()) {
            case R.id.txt_header:
                if (ddFilter != null)
                    ddFilter.dismiss();
                break;
            case R.id.spinner_name:
                if (ddFilter != null && spinnerCarName != null) {
                    spinnerCarName.performClick();
                }
                break;
            case R.id.spinner_model:
                if (ddFilter != null && spinnerCarModel != null)
                    spinnerCarModel.performClick();
                break;
            case R.id.spinner_version:
                if (ddFilter != null && spinnerCarVersion != null)
                    spinnerCarVersion.performClick();
                break;
            case R.id.spinner_car_type:
                if (ddFilter != null && spinnerCarType != null)
                    spinnerCarType.performClick();
                break;
            case R.id.spinner_year_to:
                if (ddFilter != null && spinnerYearTo != null)
                    spinnerYearTo.performClick();
                break;
            case R.id.spinner_year_from:
                if (ddFilter != null && spinnerYearFrom != null)
                    spinnerYearFrom.performClick();
                break;
            case R.id.spinner_price:
                if (ddFilter != null && spinnerPrice != null)
                    spinnerPrice.performClick();
                break;
            case R.id.spinner_color:
                if (ddFilter != null && spinnerColor != null)
                    spinnerColor.performClick();
                break;
            case R.id.btn_buy_done:
                setFilter();
                break;
            case R.id.txt_header_rent:
                if (ddFilterRent != null)
                    ddFilterRent.dismiss();
                break;
            case R.id.spinner_name_rent:
                if (ddFilterRent != null && spinnerCarNameRent != null) {
                    spinnerCarNameRent.performClick();
                }
                break;
            case R.id.spinner_model_rent:
                if (ddFilterRent != null && spinnerCarModelRent != null)
                    spinnerCarModelRent.performClick();
                break;
            case R.id.spinner_version_rent:
                if (ddFilterRent != null && spinnerCarVersionRent != null)
                    spinnerCarVersionRent.performClick();
                break;

            case R.id.spinner_price_rent:
                if (ddFilterRent != null && spinnerPriceRent != null)
                    spinnerPriceRent.performClick();
                break;
            case R.id.btn_rent_done:
                setFilterRent();
                break;
            case R.id.spinner_from:
                Utility.openCalendarDialog(context, spinner_from_rent);
                break;
            case R.id.spinner_to:
                Utility.openCalendarDialog(context, spinner_to_rent);
                break;
            case R.id.btn_rent_reset:
                ddFilterRent.dismiss();
                filterResponseRent = new FilterResponse();
                spinnerPriceRent.setSelection(0);
                spinner_price_rent.setText(getString(R.string.price_upto));
                if (rentFragment != null) {
                    rentFragment.resetData(filterResponseRent);
                }
                break;
            case R.id.btn_buy_reset:
                ddFilter.dismiss();
                spinnerColor.setSelection(0);
                spinnerCarType.setSelection(0);
                spinnerPrice.setSelection(0);
                spinnerYearFrom.setSelection(0);
                spinnerYearTo.setSelection(0);
                spinner_price.setText(getString(R.string.price_upto));
                spinner_car_type.setText(getString(R.string.car_type));
                spinner_color.setText(getString(R.string.color));
                spinner_year_to.setText(getString(R.string.year_to));
                spinner_year_from.setText(getString(R.string.year_from));
                filterResponse = new FilterResponse();
                if (buyFragment != null) {
                    // buyFragment.resetData(filterResponse);
                }
                break;
        }
    }

    private void setFilter() {
        if (spinnerCarType.getSelectedItemPosition() != 0)
            filterResponse.setCar_type(spinnerCarType.getSelectedItem().toString());
        else
            filterResponse.setCar_type("");

        if (spinnerColor.getSelectedItemPosition() != 0)
            filterResponse.setColour(spinnerColor.getSelectedItem().toString());
        else
            filterResponse.setColour("");

        if (spinnerPrice.getSelectedItemPosition() != 0) {
            String numberOnly = spinnerPrice.getSelectedItem().toString().replaceAll("[^0-9]", "");
            filterResponse.setShow_price_max_range(spinnerPrice.getSelectedItem().toString());
            filterResponse.setPrice_max_range(numberOnly);
        } else {
            filterResponse.setShow_price_max_range("");
            filterResponse.setPrice_max_range("0");
        }

        if (spinnerYearTo.getSelectedItemPosition() != 0)
            filterResponse.setYear_to(spinnerYearTo.getSelectedItem().toString());
        else
            filterResponse.setYear_to("");

        if (spinnerYearFrom.getSelectedItemPosition() != 0)
            filterResponse.setYear_from(spinnerYearFrom.getSelectedItem().toString());
        else
            filterResponse.setYear_from("");


        if (spinnerCarName.getSelectedItemPosition() != 0) {
            if (listBrandName.size() > 0) {
                filterResponse.setCar_name_id(listBrandName.get(spinnerCarName.getSelectedItemPosition()).getId());
                filterResponse.setCarName(spinner_name.getText().toString());
            } else {
                filterResponse.setCar_name_id("0");
                filterResponse.setCarName("");
            }
        } else {
            filterResponse.setCar_name_id("0");
            filterResponse.setCarName("");
        }


        if (spinnerCarModel.getSelectedItemPosition() != 0) {
            if (listModelName.size() > 0) {
                filterResponse.setModel_id(listModelName.get(spinnerCarModel.getSelectedItemPosition()).getId());
                filterResponse.setCarModel(spinner_model.getText().toString());
            } else {
                filterResponse.setModel_id("0");
                filterResponse.setCarModel("");
            }
        } else {
            filterResponse.setModel_id("0");
            filterResponse.setCarModel("");
        }


        if (spinnerCarVersion.getSelectedItemPosition() != 0) {
            if (listVersionName.size() > 0) {
                filterResponse.setVersion_id(listVersionName.get(spinnerCarVersion.getSelectedItemPosition()).getId());
                filterResponse.setCarVersion(spinner_version.getText().toString());
            } else {
                filterResponse.setVersion_id("0");
                filterResponse.setCarVersion("");
            }
        } else {
            filterResponse.setVersion_id("0");
            filterResponse.setCarVersion("");
        }

        listModelName1 = listModelName;
        listVersionName1 = listVersionName;

        ddFilter.dismiss();

        if (buyFragment != null) {
            // buyFragment.resetData(filterResponse);
        }
    }

    /*Rent*/

    private void setFilterRent() {
        if (spinnerPriceRent.getSelectedItemPosition() != 0) {
            String numberOnly = spinnerPriceRent.getSelectedItem().toString().replaceAll("[^0-9]", "");
            filterResponseRent.setShow_price_max_range(spinnerPriceRent.getSelectedItem().toString());
            filterResponseRent.setPrice_max_range(numberOnly);
        } else {
            filterResponseRent.setShow_price_max_range("");
            filterResponseRent.setPrice_max_range("0");
        }

        if (spinnerCarNameRent.getSelectedItemPosition() != 0) {
            if (listBrandNameRent.size() > 0) {
                filterResponseRent.setCar_name_id(listBrandNameRent.get(spinnerCarNameRent.getSelectedItemPosition()).getId());
                filterResponseRent.setCarName(spinner_name_rent.getText().toString());
            } else {
                filterResponseRent.setCar_name_id("0");
                filterResponseRent.setCarName("");
            }
        } else {
            filterResponseRent.setCar_name_id("0");
            filterResponseRent.setCarName("");
        }

        if (spinnerCarModelRent.getSelectedItemPosition() != 0) {
            if (listModelNameRent.size() > 0) {
                filterResponseRent.setModel_id(listModelNameRent.get(spinnerCarModelRent.getSelectedItemPosition()).getId());
                filterResponseRent.setCarModel(spinner_model_rent.getText().toString());
            } else {
                filterResponseRent.setModel_id("0");
                filterResponseRent.setCarModel("");
            }
        } else {
            filterResponseRent.setModel_id("0");
            filterResponseRent.setCarModel("");
        }

        if (spinnerCarVersionRent.getSelectedItemPosition() != 0) {
            if (listVersionNameRent.size() > 0) {
                filterResponseRent.setVersion_id(listVersionNameRent.get(spinnerCarVersionRent.getSelectedItemPosition()).getId());
                filterResponseRent.setCarVersion(spinner_version_rent.getText().toString());
            } else {
                filterResponseRent.setVersion_id("0");
                filterResponseRent.setCarVersion("");
            }
        } else {
            filterResponseRent.setVersion_id("0");
            filterResponseRent.setCarVersion("");
        }

        if (spinner_to_rent.getText().toString() != null && !spinner_to_rent.getText().toString().isEmpty()) {
            filterResponseRent.setYear_to(spinner_to_rent.getText().toString());
        } else {
            filterResponseRent.setYear_to("");
        }

        if (spinner_from_rent.getText().toString() != null && !spinner_from_rent.getText().toString().isEmpty()) {
            filterResponseRent.setYear_from(spinner_from_rent.getText().toString());
        } else {
            filterResponseRent.setYear_from("");
        }

        listModelName1Rent = listModelNameRent;
        listVersionName1Rent = listVersionNameRent;

        ddFilterRent.dismiss();

        if (rentFragment != null) {
            rentFragment.resetData(filterResponseRent);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        buyFragment = new BuyFragment();
        adapter.addFrag(buyFragment, "Buy");


        rentFragment = new RentFragment();
        adapter.addFrag(rentFragment, "Rent");


        garageFragment = new GarageFragment();
        adapter.addFrag(garageFragment, "Garage");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            if (position == 0) {
                buyFragment = (BuyFragment) createdFragment;
                return buyFragment;
            } else if (position == 1) {
                rentFragment = (RentFragment) createdFragment;
                return rentFragment;
            } else if (position == 2) {
                garageFragment = (GarageFragment) createdFragment;
                return garageFragment;
            }
            return null;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!type.equalsIgnoreCase("0") || !SharedPrefUtils.getPreference(context, Constant.USER_NAME, "").isEmpty())
            tvName.setText(SharedPrefUtils.getPreference(context, Constant.USER_NAME, ""));
        else
            tvName.setText("Guest User");

        if (!type.equalsIgnoreCase("0") || !SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, "").isEmpty())
            new AQuery(context).id(ivProfile).image(SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, ""), true, true, 0, R.mipmap.gestuser);
        else
            ivProfile.setImageResource(R.mipmap.gestuser);

        try {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openDialogToLogin() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setMessage("You need to login to perform this action.");
        builder.setTitle("Login");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Utils.logout(context);
            }
        });
        builder.create().show();
    }


    public void iPhone() {
        try {
            if (ddFilter == null) {
                ddFilter = new Dialog(context);
                ddFilter.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                ddFilter.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ddFilter.setContentView(R.layout.dialog_filter);
                ddFilter.setCancelable(false);
                ddFilter.getWindow().setLayout(-1, -2);
                ddFilter.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                setDialog(ddFilter);
            } else {
                updateDialog();
            }

            ddFilter.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDialog() {
        if (filterResponse != null) {
            spinnerColor.setSelection(arrayAdapterCarColor.getPosition(filterResponse.getColour()));
            spinnerCarType.setSelection(arrayAdapterCarType.getPosition(filterResponse.getCar_type()));
            spinnerPrice.setSelection(arrayAdapterPrice.getPosition(filterResponse.getShow_price_max_range()));

            if (!filterResponse.getYear_from().isEmpty())
                spinnerYearFrom.setSelection(arrayAdapterYearFrom.getPosition(filterResponse.getYear_from()));
            else
                spinnerYearFrom.setSelection(0);


            if (!filterResponse.getYear_to().isEmpty())
                spinnerYearTo.setSelection(arrayAdapterYearTo.getPosition(filterResponse.getYear_to()));
            else
                spinnerYearTo.setSelection(0);

            if (listBrandName.size() == 1) {
                setCarNames();
            }

            if (filterResponse.getCarName() != null && !filterResponse.getCarName().isEmpty())
                spinner_name.setText(filterResponse.getCarName());

            spinnerCarName.setSelection(arrayAdapterCarName.getPosition(new CarName(filterResponse.getCar_name_id(), "")), false);


            if (filterResponse.getCarModel() != null && !filterResponse.getCarModel().isEmpty())
                spinner_model.setText(filterResponse.getCarModel());

            if (listModelName1 != null) {
                listModelName = listModelName1;
                arrayAdapterCarModel.notifyDataSetChanged();

                if (!filterResponse.getModel_id().isEmpty()) {
                    spinnerCarModel.setSelection(arrayAdapterCarModel.getPosition(new ModelName(filterResponse.getCar_name_id(), "")), false);

                }
            } else {
                spinnerCarModel.setSelection(0);
            }

            if (filterResponse.getCarVersion() != null && !filterResponse.getCarVersion().isEmpty())
                spinner_version.setText(filterResponse.getCarVersion());

            if (listVersionName1 != null) {
                listVersionName = listVersionName1;
                arrayAdapterCarVersion.notifyDataSetChanged();

                if (!filterResponse.getVersion_id().isEmpty()) {
                    spinnerCarVersion.setSelection(arrayAdapterCarVersion.getPosition(new VersionName(filterResponse.getVersion_id(), "")), false);

                }
            } else {
                spinnerCarVersion.setSelection(0);
            }

        }

    }

    private void setDialog(Dialog dd) {
        ((TextView) dd.findViewById(R.id.txt_header)).setOnClickListener(this);
        dd.findViewById(R.id.btn_buy_reset).setOnClickListener(this);
        spinner_name = (TextView) dd.findViewById(R.id.spinner_name);
        spinner_name.setOnClickListener(this);
        spinner_model = (TextView) dd.findViewById(R.id.spinner_model);
        spinner_model.setOnClickListener(this);
        spinner_version = (TextView) dd.findViewById(R.id.spinner_version);
        spinner_version.setOnClickListener(this);
        spinner_car_type = (TextView) dd.findViewById(R.id.spinner_car_type);
        spinner_car_type.setOnClickListener(this);
        spinner_color = (TextView) dd.findViewById(R.id.spinner_color);
        spinner_color.setOnClickListener(this);
        spinner_price = (TextView) dd.findViewById(R.id.spinner_price);
        spinner_price.setOnClickListener(this);
        spinner_year_from = (TextView) dd.findViewById(R.id.spinner_year_from);
        spinner_year_from.setOnClickListener(this);
        spinner_year_to = (TextView) dd.findViewById(R.id.spinner_year_to);
        spinner_year_to.setOnClickListener(this);
        ((CustomRayMaterialTextView) dd.findViewById(R.id.btn_buy_done)).setOnClickListener(this);

        String arrayCarType[] = getResources().getStringArray(R.array.car_type);
        arrayCarType[0] = getString(R.string.car_type);
        List<String> carType = Arrays.asList(arrayCarType);
        // carType.remove(0);
        //carType.add(0,getString(R.string.any_type));

        String arraycarColor[] = getResources().getStringArray(R.array.car_color);
        arraycarColor[0] = getString(R.string.color);
        List<String> carColor = Arrays.asList(arraycarColor);
        // carColor.remove(0);

        String arraycarPrice[] = getResources().getStringArray(R.array.car_price);
        List<String> carPrice = Arrays.asList(arraycarPrice);
        //carColor.add(0,getString(R.string.any_color));

        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        ArrayList<String> years = new ArrayList<String>();
        ArrayList<String> years1 = new ArrayList<String>();
        years.add(0, getString(R.string.year_from));
        for (int i = thisYear; i >= 1950; i--) {
            years.add(Integer.toString(i));
        }

        years1.addAll(years);
        years1.set(0, getString(R.string.year_to));

        spinnerCarName = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerCarModel = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerCarVersion = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerCarType = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerColor = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerYearTo = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerYearFrom = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerPrice = new Spinner(context, Spinner.MODE_DIALOG);
        LinearLayout linearLayout = (LinearLayout) dd.findViewById(R.id.ll_spinno);
        linearLayout.addView(spinnerCarName);
        linearLayout.addView(spinnerCarModel);
        linearLayout.addView(spinnerCarVersion);
        linearLayout.addView(spinnerCarType);
        linearLayout.addView(spinnerColor);
        linearLayout.addView(spinnerYearFrom);
        linearLayout.addView(spinnerYearTo);
        linearLayout.addView(spinnerPrice);

        arrayAdapterCarType = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, carType);
        // arrayAdapterCarType.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerCarType.setAdapter(arrayAdapterCarType);

        arrayAdapterCarColor = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, carColor);
        // arrayAdapterCarColor.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerColor.setAdapter(arrayAdapterCarColor);

        arrayAdapterYearTo = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, years1);
        // arrayAdapterYearTo.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerYearTo.setAdapter(arrayAdapterYearTo);

        arrayAdapterYearFrom = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, years);
        // arrayAdapterYearFrom.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerYearFrom.setAdapter(arrayAdapterYearFrom);


        arrayAdapterPrice = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, carPrice);
        // arrayAdapterYearFrom.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerPrice.setAdapter(arrayAdapterPrice);

       /* spinnerCarType.setSelection(0,false);
        spinnerColor.setSelection(0,false);
        spinnerYearTo.setSelection(0,false);
        spinnerYearFrom.setSelection(0,false);
        spinnerPrice.setSelection(0,false);*/

        if (listBrandName.size() == 1) {
            setCarNames();
        }

        arrayAdapterCarName = new ArrayAdapter<CarName>(context, android.R.layout.simple_list_item_single_choice, listBrandName);
        spinnerCarName.setAdapter(arrayAdapterCarName);

        setCarModel("", false);


        spinnerCarName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_name.setText(spinnerCarName.getSelectedItem().toString());
                if (i != 0) {
                    spinnerCarVersion.setSelection(0);
                    spinnerCarModel.setSelection(0);
                    setCarModel(listBrandName.get(i).getId(), true);
                } else {
                    spinnerCarVersion.setSelection(0);
                    spinnerCarModel.setSelection(0);
                    setCarModel("", false);
                    setCarVersion("", false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_model.setText(spinnerCarModel.getSelectedItem().toString());
                if (i != 0) {
                    spinnerCarVersion.setSelection(0);
                    setCarVersion(listModelName.get(i).getId(), true);

                } else {
                    spinnerCarVersion.setSelection(0);
                    setCarVersion("", false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_version.setText(spinnerCarVersion.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_car_type.setText(spinnerCarType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_color.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerYearTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_year_to.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerYearFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_year_from.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPrice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_price.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*Rent*/

    public void iPhoneRent() {
        try {
            if (ddFilterRent == null) {
                ddFilterRent = new Dialog(context);
                ddFilterRent.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                ddFilterRent.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ddFilterRent.setContentView(R.layout.dialog_filter_rent);
                ddFilterRent.setCancelable(false);
                ddFilterRent.getWindow().setLayout(-1, -2);
                ddFilterRent.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                setDialogRent(ddFilterRent);
            } else {
                updateDialogRent();
            }

            ddFilterRent.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDialogRent() {
        if (filterResponseRent != null) {
            spinnerPriceRent.setSelection(arrayAdapterPriceRent.getPosition(filterResponseRent.getShow_price_max_range()));

            if (listBrandNameRent.size() == 1) {
                setCarNamesRent();
            }

            if (filterResponseRent.getCarName() != null && !filterResponseRent.getCarName().isEmpty())
                spinner_name_rent.setText(filterResponseRent.getCarName());


            spinnerCarNameRent.setSelection(arrayAdapterCarNameRent.getPosition(new CarName(filterResponseRent.getCar_name_id(), "")), false);


            if (filterResponseRent.getCarModel() != null && !filterResponseRent.getCarModel().isEmpty())
                spinner_model_rent.setText(filterResponseRent.getCarModel());

            if (listModelName1Rent != null) {
                listModelNameRent = listModelName1Rent;
                arrayAdapterCarModelRent.notifyDataSetChanged();

                if (!filterResponseRent.getModel_id().isEmpty()) {
                    spinnerCarModelRent.setSelection(arrayAdapterCarModelRent.getPosition(new ModelName(filterResponseRent.getCar_name_id(), "")), false);
                }
            } else {
                spinnerCarModelRent.setSelection(0);
            }

            if (filterResponseRent.getCarVersion() != null && !filterResponse.getCarVersion().isEmpty())
                spinner_version_rent.setText(filterResponse.getCarVersion());

            if (listVersionName1Rent != null) {
                listVersionNameRent = listVersionName1Rent;
                arrayAdapterCarVersionRent.notifyDataSetChanged();

                if (!filterResponseRent.getVersion_id().isEmpty()) {
                    spinnerCarVersionRent.setSelection(arrayAdapterCarVersionRent.getPosition(new VersionName(filterResponseRent.getVersion_id(), "")), false);

                }
            } else {
                spinnerCarVersionRent.setSelection(0);
            }

            if (filterResponseRent.getYear_from() != null && !filterResponseRent.getYear_from().isEmpty())
                spinner_from_rent.setText(filterResponseRent.getYear_from());
            else
                spinner_from_rent.setText(getString(R.string.from));


            if (filterResponseRent.getYear_to() != null && !filterResponseRent.getYear_to().isEmpty())
                spinner_to_rent.setText(filterResponseRent.getYear_to());
            else
                spinner_to_rent.setText(getString(R.string.to));
        }
    }

    private void setDialogRent(Dialog dd) {

        ((TextView) dd.findViewById(R.id.txt_header_rent)).setOnClickListener(this);
        spinner_name_rent = (TextView) dd.findViewById(R.id.spinner_name_rent);
        spinner_name_rent.setOnClickListener(this);
        spinner_model_rent = (TextView) dd.findViewById(R.id.spinner_model_rent);
        spinner_model_rent.setOnClickListener(this);
        spinner_version_rent = (TextView) dd.findViewById(R.id.spinner_version_rent);
        spinner_version_rent.setOnClickListener(this);
        spinner_price_rent = (TextView) dd.findViewById(R.id.spinner_price_rent);
        spinner_price_rent.setOnClickListener(this);
        spinner_from_rent = (TextView) dd.findViewById(R.id.spinner_from);
        spinner_to_rent = (TextView) dd.findViewById(R.id.spinner_to);
        spinner_from_rent.setOnClickListener(this);
        spinner_to_rent.setOnClickListener(this);

        ((CustomRayMaterialTextView) dd.findViewById(R.id.btn_rent_done)).setOnClickListener(this);
        ((CustomRayMaterialTextView) dd.findViewById(R.id.btn_rent_reset)).setOnClickListener(this);


        String arraycarPrice[] = getResources().getStringArray(R.array.car_price);
        List<String> carPrice = Arrays.asList(arraycarPrice);
        //carColor.add(0,getString(R.string.any_color));

        spinnerCarNameRent = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerCarModelRent = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerCarVersionRent = new Spinner(context, Spinner.MODE_DIALOG);
        spinnerPriceRent = new Spinner(context, Spinner.MODE_DIALOG);
        LinearLayout linearLayout = (LinearLayout) dd.findViewById(R.id.ll_spinno);
        linearLayout.addView(spinnerCarNameRent);
        linearLayout.addView(spinnerCarModelRent);
        linearLayout.addView(spinnerCarVersionRent);
        linearLayout.addView(spinnerPriceRent);

        arrayAdapterPriceRent = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice, carPrice);
        // arrayAdapterYearFrom.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinnerPriceRent.setAdapter(arrayAdapterPriceRent);

       /* spinnerCarType.setSelection(0,false);
        spinnerColor.setSelection(0,false);
        spinnerYearTo.setSelection(0,false);
        spinnerYearFrom.setSelection(0,false);
        spinnerPrice.setSelection(0,false);*/

        if (listBrandNameRent.size() == 1) {
            setCarNamesRent();
        }

        arrayAdapterCarNameRent = new ArrayAdapter<CarName>(context, android.R.layout.simple_list_item_single_choice, listBrandNameRent);
        spinnerCarNameRent.setAdapter(arrayAdapterCarNameRent);


        arrayAdapterCarModelRent = new ArrayAdapter<ModelName>(context, android.R.layout.simple_list_item_single_choice, listModelNameRent);
        spinnerCarModelRent.setAdapter(arrayAdapterCarModelRent);

        arrayAdapterCarVersionRent = new ArrayAdapter<VersionName>(context, android.R.layout.simple_list_item_single_choice, listVersionNameRent);
        spinnerCarVersionRent.setAdapter(arrayAdapterCarVersionRent);

        setCarModelRent("", false);

        spinnerCarNameRent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_name_rent.setText(spinnerCarNameRent.getSelectedItem().toString());
                if (i != 0) {
                    spinnerCarVersionRent.setSelection(0);
                    spinnerCarModelRent.setSelection(0);
                    setCarModelRent(listBrandNameRent.get(i).getId(), true);
                } else {
                    spinnerCarVersionRent.setSelection(0);
                    spinnerCarModelRent.setSelection(0);
                    setCarModelRent("", false);
                    setCarVersionRent("", false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarModelRent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_model_rent.setText(spinnerCarModelRent.getSelectedItem().toString());
                if (i != 0) {
                    spinnerCarVersionRent.setSelection(0);
                    setCarVersionRent(listModelNameRent.get(i).getId(), true);

                } else {
                    spinnerCarVersionRent.setSelection(0);
                    setCarVersionRent("", false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarVersionRent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_version_rent.setText(spinnerCarVersionRent.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerPriceRent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_price_rent.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean isLogin() {
        // String type = SharedPrefUtils.getPreference(context, Constant.TYPE);
        if (type.equalsIgnoreCase("") || type.equalsIgnoreCase("0"))
            return false;

        return true;
    }

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

    /*RENT*/
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

    public FilterResponse getFilterRent() {
        return filterResponseRent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == 405) {
                    if (data != null) {
                        garageFragment.setData(data);
                    }
                } else if (requestCode == 404) {
                    int type = data.getIntExtra(Constant.TYPE, -1);
                    if (type != -1) {
                        if (type == 0) {
                            buyFragment.setService(data);
                        } else {
                            rentFragment.setService(data);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //start of set post ad dialog
    public void choosePostAd() {

        ddPostAd = new Dialog(context);

        ddPostAd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ddPostAd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ddPostAd.setContentView(R.layout.dialog_post_ad_buyer);

        btn_post_buy = ddPostAd.findViewById(R.id.btn_post_buy);
        btn_post_garage = ddPostAd.findViewById(R.id.btn_post_garage);
        btn_post_rent = ddPostAd.findViewById(R.id.btn_post_rent);
        txt_post_ad_header = ddPostAd.findViewById(R.id.txt_post_ad_header);

        if (!SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").isEmpty() && SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").equalsIgnoreCase("1")) {
            btn_post_garage.setVisibility(View.GONE);
        } else
            btn_post_garage.setVisibility(View.VISIBLE);
        ddPostAd.setCancelable(false);
        ddPostAd.getWindow().setLayout(-1, -2);
        ddPostAd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ddPostAd.show();

        btn_post_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PostAdActivity.class));
                ddPostAd.dismiss();
            }
        });

        btn_post_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RentCarActivity.class));
                ddPostAd.dismiss();
            }
        });

        btn_post_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddWorkActivity.class));
//                Garage adPost = new Garage();
//                if (context instanceof HomeFormatActivity && status == 1) {
//                    Intent intent = new Intent(context, GarageDetailActivity.class);
//                    intent.putExtra(Constant.DATA, adPost);
//                    intent.putExtra(Constant.POSITION, arraylist.get(0));
//                    ((HomeFormatActivity) context).startActivityForResult(intent, 404);
//                }

//                Intent intent = new Intent(context, GarageDetailActivity.class);
//                intent.putExtra(Constant.DATA, adPost);
//                startActivity(intent);
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
    //End of set post ad dialog

    //start of set manage ad dialog
    public void chooseManageAd() {

        ddManageAd = new Dialog(context);

        ddManageAd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ddManageAd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ddManageAd.setContentView(R.layout.dialog_mange_ad_seller);
        btn_manage_buy = ddManageAd.findViewById(R.id.btn_manage_buy);
        btn_manage_rent = ddManageAd.findViewById(R.id.btn_manage_rent);
        btn_manage_garage = ddManageAd.findViewById(R.id.btn_manage_garage);
        txt_manage_ad_header = ddManageAd.findViewById(R.id.txt_manage_ad_header);
        btn_manage_garage.setVisibility(View.VISIBLE);
        if (!SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").isEmpty() && SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").equalsIgnoreCase("1")) {
            btn_manage_garage.setVisibility(View.GONE);
        }
        ddManageAd.setCancelable(false);
        ddManageAd.getWindow().setLayout(-1, -2);
        ddManageAd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ddManageAd.show();

        btn_manage_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ManageAdActivity.class));
                ddManageAd.dismiss();
            }
        });

        btn_manage_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ManageRentedActivity.class));
                ddManageAd.dismiss();
            }
        });

        btn_manage_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ManageWorkActivity.class));
                ddManageAd.dismiss();
            }
        });

        txt_manage_ad_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddManageAd.dismiss();
            }
        });
    }
//End of set manage ad dialog

    public void iPhone1() {
        try {
            if (ddGarageFilter == null) {
                ddGarageFilter = new Dialog(context);
                ddGarageFilter.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                ddGarageFilter.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                ddGarageFilter.setContentView(R.layout.dialog_garage_filter);
                ddGarageFilter.setCancelable(false);
                ddGarageFilter.getWindow().setLayout(-1, -2);
                ddGarageFilter.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                setDialog1(ddGarageFilter);
            } else {
                updateDialog1();
            }

            ddGarageFilter.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDialog1() {
        if (garageType.equalsIgnoreCase("0")) {
            radio_both.setChecked(true);
        } else if (garageType.equalsIgnoreCase("1")) {
            radio_main.setChecked(true);
        } else if (garageType.equalsIgnoreCase("2")) {
            radio_repair.setChecked(true);
        } else if (garageType.equalsIgnoreCase("3")) {
            radio_repair_service.setChecked(true);
        }
    }

    private void setDialog1(Dialog ddFilter) {
        radio_main = ddFilter.findViewById(R.id.radio_main);
        radio_repair = ddFilter.findViewById(R.id.radio_repair);
        radio_both = ddFilter.findViewById(R.id.radio_both);
        radio_repair_service = ddFilter.findViewById(R.id.radio_repair_service);
        txt_header = ddFilter.findViewById(R.id.txt_header);
        btn_garage_done = ddFilter.findViewById(R.id.btn_garage_done);
        btn_garage_reset = ddFilter.findViewById(R.id.btn_garage_reset);

        if (garageType.equalsIgnoreCase("0")) {
            radio_both.setChecked(true);
        } else if (garageType.equalsIgnoreCase("1")) {
            radio_main.setChecked(true);
        } else if (garageType.equalsIgnoreCase("2")) {
            radio_repair.setChecked(true);
        } else if (garageType.equalsIgnoreCase("3")) {
            radio_repair_service.setChecked(true);
        }

        txt_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddGarageFilter.dismiss();
            }
        });

        btn_garage_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radio_both.isChecked()) {
                    garageType = "0";
                } else if (radio_main.isChecked()) {
                    garageType = "1";
                } else if (radio_repair.isChecked()) {
                    garageType = "2";
                } else if (radio_repair_service.isChecked()) {
                    garageType = "3";
                }
                garageFragment.resetData(garageType);
                ddGarageFilter.dismiss();
            }
        });

        btn_garage_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_both.setChecked(true);
                garageType = "0";
                garageFragment.resetData(garageType);
                ddGarageFilter.dismiss();
            }
        });

        AppCompatRadioButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radio_main.setChecked(radio_main == buttonView);
                    radio_repair.setChecked(radio_repair == buttonView);
                    radio_both.setChecked(radio_both == buttonView);
                    radio_repair_service.setChecked(radio_repair_service == buttonView);

                    /*if (radio_both.isChecked()) {
                        garageType = "0";
                    } else if (radio_main.isChecked()) {
                        garageType = "1";
                    } else if (radio_repair.isChecked()) {
                        garageType = "2";
                    } else if (radio_repair_service.isChecked()) {
                        garageType = "3";
                    }*/
                }
            }
        };
        radio_main.setOnCheckedChangeListener(listener);
        radio_repair.setOnCheckedChangeListener(listener);
        radio_both.setOnCheckedChangeListener(listener);
        radio_repair_service.setOnCheckedChangeListener(listener);
    }
}
