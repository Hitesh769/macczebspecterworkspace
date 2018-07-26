package com.spectre.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.adapter.GarageListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
// 9993265267 ---> Garage
public class GarageHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private CustomTextView tvName;
    private CircleImageView ivProfile;

    private Context context;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    ArrayList<AdPost> Arraylist = new ArrayList<>();
    CustomTextView txtConnection;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    ProgressView progressDialog1;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Dialog ddFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_garage_home);
        context = this;
        Utility.setContentView(context, R.layout.activity_garage_home);
        initView();
        setUpToolbar(context, getString(R.string.manage_work));
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
                    case R.id.filter:
                    case R.id.notification:
                        startActivity(new Intent(context, NotificationActivity.class));
                       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            circleReveal(R.id.searchtoolbar, 1, true, false);
                        else {
                            searchtollbar.setVisibility(View.VISIBLE);
                            item_search.expandActionView();
                            return true;
                        }*/
                        //   Utility.showToast(context, "Coming Soon");
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


    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        ((CustomTextView) findViewById(R.id.tv_home)).setVisibility(View.GONE);
        ((CustomTextView) findViewById(R.id.tv_login)).setVisibility(View.GONE);
        ((CustomTextView) findViewById(R.id.tv_user_regi)).setVisibility(View.GONE);
        ((CustomTextView) findViewById(R.id.tv_garage_regi)).setVisibility(View.GONE);
        //  ((CustomTextView) findViewById(R.id.tv_manage_rent)).setVisibility(View.GONE);
        ((CustomTextView) findViewById(R.id.tv_profile)).setOnClickListener(this);
//        ((CustomTextView) findViewById(R.id.tv_give_rent)).setOnClickListener(this);
//        ((CustomTextView) findViewById(R.id.tv_manage_rent)).setOnClickListener(this);
        ivProfile = (CircleImageView) findViewById(R.id.iv_profile);

        CustomTextView tvPostAd = ((CustomTextView) findViewById(R.id.tv_post_ad));
        // tvPostAd.setText("Add Work");
        tvPostAd.setOnClickListener(this);

        CustomTextView tvManageAd = ((CustomTextView) findViewById(R.id.tv_manage_ad));
        //   tvManageAd.setText("Manage Work");
        tvManageAd.setOnClickListener(this);
//        ((CustomTextView) findViewById(R.id.tv_manage_work)).setOnClickListener(this);
//        ((CustomTextView) findViewById(R.id.tv_post_work)).setOnClickListener(this);
        (findViewById(R.id.ll_garage)).setVisibility(View.VISIBLE);
        //   tvManageAd.setVisibility(View.GONE);

        // ((CustomTextView) findViewById(R.id.tv_manage_ad)).setOnClickListener(this);
        //  ((CustomTextView) findViewById(R.id.tv_give_rent)).setVisibility(View.GONE);
        ((CustomTextView) findViewById(R.id.tv_setting)).setOnClickListener(this);
        ((RelativeLayout) findViewById(R.id.layout_logout)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.profile_edit)).setOnClickListener(this);
        ((ImageView) findViewById(R.id.btn_cross)).setOnClickListener(this);

        tvName = (CustomTextView) findViewById(R.id.tv_name);

        setUpRecycler();
        setSwipeLayout();
        setUpRecyclerListener();
        callMethodEventList(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


    private void openLeft() {
        if (!drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.openDrawer(Gravity.LEFT);
        } else
            drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cross:
                break;
            case R.id.tv_login:
                startActivity(new Intent(context, LoginActivity.class));
                break;
            case R.id.tv_user_regi:
            case R.id.tv_garage_regi:
                startActivity(new Intent(context, SignupActivity.class));
                break;
            case R.id.tv_profile:
            case R.id.profile_edit:
                startActivity(new Intent(context, EditProfileActivity.class));
                break;
//            case R.id.tv_post_work:
//                startActivityForResult(new Intent(context, AddWorkActivity.class), 404);
//                break;
//            case R.id.tv_manage_work:
//                startActivity(new Intent(context, ManageWorkActivity.class));
//                break;
            case R.id.tv_post_ad:
                startActivity(new Intent(context, PostAdActivity.class));
                break;
            case R.id.tv_manage_ad:
                startActivity(new Intent(context, ManageAdActivity.class));
                break;
//            case R.id.tv_manage_rent:
//                startActivity(new Intent(context, ManageRentedActivity.class));
//                break;
//            case R.id.tv_give_rent:
//                startActivity(new Intent(context, RentCarActivity.class));
//                break;
            case R.id.tv_setting:
                startActivity(new Intent(context, SettingsActivity.class));
                break;
            case R.id.layout_logout:
                Utility.openDialogToLogout(context);
                break;
        }
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        } else
            super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!Utility.getSharedPreferences(context, Constant.USER_NAME).isEmpty())
            tvName.setText(Utility.getSharedPreferences(context, Constant.USER_NAME));
        else
            tvName.setText("");

        if (!Utility.getSharedPreferences(context, Constant.USER_IMAGE).isEmpty())
            new AQuery(context).id(ivProfile).image(Utility.getSharedPreferences(context, Constant.USER_IMAGE), true, true, 0, R.mipmap.gestuser);
        else
            ivProfile.setImageResource(R.mipmap.gestuser);

    }


    public void setSwipeLayout() {
        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
                onItemsLoadComplete();
            }
        });
    }

    private void refreshItems() {
        if (loading) {
            if (Arraylist.size() == 0) {
                loading = false;
                onItemsLoadComplete();
                callMethodEventList(0);
            } else {
                loading = false;
                callMethodEventList(1);
            }
        } else {
            onItemsLoadComplete();
        }
    }

    private void setUpRecycler() {
        /*for (int i = 0; i <= 9; i++) {
            EventDetail eventDetail = new EventDetail();
            Arraylist.add(eventDetail);
        }*/
        progressDialog1 = (ProgressView) findViewById(R.id.progress_pv_linear_colors);
        txtConnection = ((CustomTextView) findViewById(R.id.no_data));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GarageListAdapter(context, Arraylist, 2);
        mRecyclerView.setAdapter(mAdapter);
    }

    void onItemsLoadComplete() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setUpRecyclerListener() {
        loading = true;
        loaddingDone = true;
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (loading && loaddingDone) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        callMethodEventList(0);
                    }
                }
            }
        });
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

    private void callMethodEventList(final int i) {
        JSONObject params = new JSONObject();
        // Upcoming_event
        try {
            if (i == 0) {
                if (Arraylist.size() == 0) {
                    MyDialogProgress.open(context);
                    params.put(Constant.CREATE_AT, "0");

                } else {
                    (progressDialog1).start();
                    params.put(Constant.CREATE_AT, Arraylist.get(Arraylist.size() - 1).getCreate_at());
                }
                params.put(Constant.LIST_TYPE, "0");
            } else {
                params.put(Constant.CREATE_AT, Arraylist.get(0).getCreate_at());
                params.put(Constant.LIST_TYPE, "1");
            }

            AqueryCall request = new AqueryCall(this);
            request.postWithJsonToken(Urls.GARAGE_WORK_LIST, Utility.getSharedPreferences(context, Constant.USER_TOKEN), params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject js, String success) {
                    txtConnection.setVisibility(View.GONE);
                    saveAndForward(js, i);
                }

                @Override
                public void onFailed(JSONObject js, String failed) {

                    loading = true;
                    loaddingDone = false;
                    if (Arraylist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(failed);
                        txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
                    if (Arraylist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(nullp);
                        if (nullp.equalsIgnoreCase(getString(R.string.connection)))
                            txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nointernet, 0, 0);
                        else
                            txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                    closeProgressDialog(i);
                }

                @Override
                public void onException(JSONObject js, String exception) {
                    // MyDialog.dialog_(exception, context);
                    if (Arraylist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(exception);
                        //   txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nodata, 0, 0);
                    }
                    closeProgressDialog(i);
                }

                @Override
                public void onInactive(JSONObject js, String inactive, String status) {
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

    private void saveAndForward(JSONObject js, int i) {
        try {
            JSONArray jsonArray = js.getJSONArray("data");
            if (jsonArray.length() > 0) {

                Type type = new TypeToken<List<AdPost>>() {
                }.getType();
                List<AdPost> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
                if (i == 0) {
                    Arraylist.addAll(Arraylist.size(), tempListNewsFeeds);
                } else {
                    Arraylist.addAll(0, tempListNewsFeeds);
                }
            }

            mAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
        loading = true;
        closeProgressDialog(i);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404) {
            if (resultCode == Activity.RESULT_OK) {
                if (Arraylist != null && Arraylist.size() > 0) {
                    try {
                        if (data.getExtras() != null) {
                            int position = data.getExtras().getInt(Constant.POSITION);
                            AdPost adPost = data.getParcelableExtra(Constant.DATA);
                            Arraylist.set(position, adPost);

                            if (adPost.getDelete_status().equalsIgnoreCase("2")) {
                                Arraylist.remove(position);

                                if (Arraylist.size() == 0) {
                                    txtConnection.setVisibility(View.VISIBLE);
                                    txtConnection.setText(getString(R.string.data_not_avail));
                                    txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    refreshItems();
                }
            }
        }
    }
}
