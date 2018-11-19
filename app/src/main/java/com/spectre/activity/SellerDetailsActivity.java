package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.adapter.WorkListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.beans.Garage;
import com.spectre.beans.Work;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.PermissionUtility;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SellerDetailsActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.imgProfileImage)
    CircleImageView imgProfileImage;
    @BindView(R.id.reclv_workList)
    RecyclerView reclvWorkList;
    @BindView(R.id.txtWorkList)
    TextView txtWorkList;
    @BindView(R.id.no_data)
    CustomTextView txtConnection;
    @BindView(R.id.progress_pv_linear_colors)
    ProgressView progressDialog1;
    @BindView(R.id.imgBack)
    AppCompatImageView imgBack;
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.txt_email)
    TextView txtEmail;
    private boolean loading = true;
    static String userid = "", userName;
    private boolean loaddingDone = true;
    private String latitude = "";
    private String longitude = "";
    private AdPost adPost;
    private Garage garagePost;
    private RecyclerView.Adapter mAdapter;
    String type = "0";
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    ArrayList<Work> worklist;
    private LinearLayoutManager mLayoutManager;
    private String userId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.seller_profile);
        ButterKnife.bind(this);
        init();
        /*userid = getIntent().getStringExtra(Constant.USER_ID);
        String name = getIntent().getStringExtra(Constant.USER_NAME);
        userName = name.substring(0, 1).toUpperCase() + name.substring(1);*/
    }

    private void init() {
        txtAppBarTitle.setText("Profile");
        worklist = new ArrayList<>();
        getLocation();
        isSeller();
        setUpRecycler();
        callMethodEventList(0);
    }

    private void isSeller() {
        if (getIntent().getStringExtra(Constant.DATA) != null && getIntent().getStringExtra(Constant.DATA).equals("2")) {
            userId = SharedPrefUtils.getPreference(context, Constant.USER_ID, "");
            txtName.setText(SharedPrefUtils.getPreference(context, Constant.USER_NAME, ""));
            txtEmail.setText(SharedPrefUtils.getPreference(context,Constant.USER_EMAIL,""));
            txtPhone.setText(SharedPrefUtils.getPreference(context,Constant.USER_MOBILE,""));
            txtAddress.setText(SharedPrefUtils.getPreference(context, Constant.USER_ADDRESS_, ""));
            if (SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, "") != null && !SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, "").isEmpty())
                new AQuery(context).id(imgProfileImage).image(SharedPrefUtils.getPreference(context, Constant.USER_IMAGE, ""), true, true, 0, R.mipmap.gestuser);
            else
                imgProfileImage.setImageResource(R.mipmap.gestuser);
        } else if (getIntent().getStringExtra(Constant.ISGARAGE).equals("NO")){
            setAllData();
        }
        else{
            setGarageData();
        }
    }

    private void setAllData() {
        adPost = (AdPost) getIntent().getExtras().get(Constant.DATA);
        userId = adPost.getUser_id();
        if (adPost.getFull_name() != null) {
            txtName.setText(adPost.getFull_name().trim());
        }
        if (adPost.getAddress() != null) {
            txtAddress.setText(adPost.getAddress());
        }
       if (adPost.getMobile_no() != null) {
            txtPhone.setText(adPost.getMobile_no());
        }
       if (adPost.getEmail() != null) {
            txtEmail.setText(adPost.getEmail());
        }

        if (adPost.getUser_image() != null && !adPost.getUser_image().isEmpty())
            new AQuery(context).id(imgProfileImage).image(adPost.getUser_image(), true, true, 0, R.mipmap.gestuser);
        else
            imgProfileImage.setImageResource(R.mipmap.gestuser);
    }
    private void setGarageData() {
        garagePost = (Garage) getIntent().getExtras().get(Constant.DATA);
        userId = garagePost.getUser_id();
        if (garagePost.getFull_name() != null) {
            txtName.setText(garagePost.getFull_name().trim());
        }
        if (garagePost.getAddress() != null) {
            txtAddress.setText(garagePost.getAddress());
        }
        if (garagePost.getMobile_no() != null) {
            txtPhone.setText(garagePost.getMobile_no());
        }
        if (garagePost.getEmail() != null) {
            txtEmail.setText(garagePost.getEmail());
        }

        if (garagePost.getUser_image() != null && !garagePost.getUser_image().isEmpty())
            new AQuery(context).id(imgProfileImage).image(garagePost.getUser_image(), true, true, 0, R.mipmap.gestuser);
        else
            imgProfileImage.setImageResource(R.mipmap.gestuser);
    }

    private void callMethodEventList(final int i) {
        JSONObject params = new JSONObject();
        // Upcoming_event
        try {
            if (i == 0) {
                if (worklist.size() == 0) {
                    MyDialogProgress.open(context);
                    params.put(Constant.CREATE_AT, "0");

                } else {
                    (progressDialog1).start();
                    params.put(Constant.CREATE_AT, worklist.get(worklist.size() - 1).getCreate_at());
                }
                params.put(Constant.LIST_TYPE, "0");
            } else {
                params.put(Constant.CREATE_AT, worklist.get(0).getCreate_at());
                params.put(Constant.LIST_TYPE, "1");
            }

            params.put(Constant.SECOND_USER_ID, userId);
            AqueryCall request = new AqueryCall(this);
            request.postWithJsonToken(Urls.GARAGE_WORK_LIST_BYID, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject js, String success) {
                    txtConnection.setVisibility(View.GONE);
                    saveAndForward(js, i);
                }

                @Override
                public void onFailed(JSONObject js, String failed) {

                    loading = true;
                    loaddingDone = false;
                    if (worklist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        reclvWorkList.setVisibility(View.GONE);
                        txtConnection.setText(failed);
                       // txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    closeProgressDialog(i);
                }

                @Override
                public void onAuthFailed(JSONObject js, String failed) {

                    closeProgressDialog(i);
                    SessionExpireDialog.openDialog(context, 0, "");

                }

                @Override
                public void onNull(JSONObject js, String nullp) {
                    loading = true;
                    loaddingDone = false;
                    if (worklist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(nullp);
                        if (nullp.equalsIgnoreCase(getString(R.string.connection)))
                            txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nointernet, 0, 0);
                       /* else
                            txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);*/
                    }
                    closeProgressDialog(i);
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    // MyDialog.dialog_(exception, context);
                    if (worklist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(exception);
                        //   txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nodata, 0, 0);
                    }
                    closeProgressDialog(i);
                }

                @Override
                public void onInactive(JSONObject js, String inactive, String status) {
                    txtConnection.setVisibility(View.VISIBLE);
                   /* if (myInt == 1) {
                        closeProgressDialog();
                        SessionExpire.iPhone(inactive, context);
                    }*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Utility.showToast(context, getString(R.string.something_wrong));
            closeProgressDialog(i);
        }
    }

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SellerDetailsActivity.class);
        return intent;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @OnClick({R.id.imgBack, R.id.imgProfileImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgProfileImage:
                if (getIntent().getStringExtra(Constant.DATA) != null && getIntent().getStringExtra(Constant.DATA).equals("2")) {
                    Intent intent = new Intent(context, EditProfileActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    private void getLocation() {
        // get location
        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
            PermissionUtility.requestPermission(SellerDetailsActivity.this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
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
            } else {
                Utility.setLog("Location is null");
            }
        }
    }


    private void setUpRecycler() {
        reclvWorkList.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(context, 2);
        reclvWorkList.setLayoutManager(mLayoutManager);
        mAdapter = new WorkListAdapter(context, worklist, 1);
        reclvWorkList.setAdapter(mAdapter);
    }

    private void saveAndForward(JSONObject js, int i) {
        try {
            JSONArray jsonArray = js.getJSONArray("data");
            if (jsonArray.length() > 0) {

                Type type = new TypeToken<List<Work>>() {
                }.getType();
                List<Work> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
                if (i == 0) {
                    worklist.addAll(worklist.size(), tempListNewsFeeds);
                } else {
                    worklist.addAll(0, tempListNewsFeeds);
                }
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading = true;
        closeProgressDialog(i);
    }

    public void closeProgressDialog(int i) {
        try {
            MyDialogProgress.close(context);

            if (progressDialog1.isShown())
                progressDialog1.stop();

            if (i == 1)
                onItemsLoadComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onItemsLoadComplete() {
        //re.setRefreshing(false);
    }
}
