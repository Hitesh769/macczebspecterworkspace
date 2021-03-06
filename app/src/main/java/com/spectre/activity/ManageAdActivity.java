package com.spectre.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.adapter.CarListAdapter;
import com.spectre.adapter.ManageAdsListAdapter;
import com.spectre.adapter.SearchBuyListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.customView.CustomTextView;
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
import java.util.List;

public class ManageAdActivity extends AppCompatActivity {

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
    private String TAG = ManageAdActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_manage_ad);
        context = this;
        Utility.setContentView(context, R.layout.activity_manage_ad);
        //  Utility.setUpToolbarWithColor(context, "<font color='#ffffff'>Change Password</font>", true);
        Utility.setUpToolbar_(context, "<font color='#ffffff'>"+getString(R.string.manage_ad)+"</font>", true);
        initView();
    }

    private void initView() {
        setUpRecycler();
        setSwipeLayout();
        setUpRecyclerListener();
        callMethodEventList(0);
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
        mAdapter = new SearchBuyListAdapter(context, Arraylist, 0);
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
            request.postWithJsonToken(Urls.ADD_POST_LIST, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), params, new RequestCallback() {

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
                    if (Arraylist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(nullp);
                        if (nullp.equalsIgnoreCase(getString(R.string.connection)))
                            txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.nointernet, 0, 0);
                     /*   else
                            txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);*/
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
                try {
                    int position = data.getExtras().getInt(Constant.POSITION);
                    AdPost adPost = data.getParcelableExtra(Constant.DATA);
                    Arraylist.set(position, adPost);
                    if (adPost.getDelete_status().equalsIgnoreCase("2")) {
                        Arraylist.remove(position);

                        if (Arraylist.size() == 0) {
                            txtConnection.setVisibility(View.VISIBLE);
                            txtConnection.setText(getString(R.string.data_not_avail));
                           // txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  Arraylist.clear();
                //  callMethodEventList(0);
            }
        }
    }
}
