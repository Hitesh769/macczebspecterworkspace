package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.adapter.ReviewListAdapter;
import com.spectre.beans.Review;
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

public class ReviewListActivity extends AppCompatActivity {

    private Context context;
    private ActionBar actionBar;

    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    ArrayList<Review> arraylist = new ArrayList<>();
    CustomTextView txtConnection;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    ProgressView progressDialog1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG = ReviewListActivity.class.getName();
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_review_list);
        context = this;
        Utility.setContentView(context, R.layout.activity_manage_rented);
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Reviews</font>", true);
        initView();
    }

    private void initView() {
        callMethodEventList(0);
        setUpRecycler();
        setSwipeLayout();
        setUpRecyclerListener();
        //userId= (String) getIntent().getExtras().get(Constant.DATA);
        userId= SharedPrefUtils.getPreference(context, Constant.USER_ID,"");
       // callMethodEventList(0);
    }

    public void setSwipeLayout() {
        swipeRefreshLayout = ((SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout));
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setEnabled(false);
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
            if (arraylist.size() == 0) {
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
            arraylist.add(eventDetail);
        }*/
        progressDialog1 = (ProgressView) findViewById(R.id.progress_pv_linear_colors);
        txtConnection = ((CustomTextView) findViewById(R.id.no_data));
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReviewListAdapter(context, arraylist, 1);
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
                if (arraylist.size() == 0) {
                    MyDialogProgress.open(context);
                    params.put(Constant.CREATE_AT, "0");

                } else {
                    (progressDialog1).start();
                    params.put(Constant.CREATE_AT, arraylist.get(arraylist.size() - 1).getCreate_at());
                }
                params.put(Constant.LIST_TYPE, "0");
            } else {
                params.put(Constant.CREATE_AT, arraylist.get(0).getCreate_at());
                params.put(Constant.LIST_TYPE, "1");
            }

            params.put(Constant.SECOND_USER_ID,userId);

            AqueryCall request = new AqueryCall(this);
            request.postWithJsonToken(Urls.REVIEW_N_RATING_LIST, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject js, String success) {
                    txtConnection.setVisibility(View.GONE);
                    saveAndForward(js, i);
                }

                @Override
                public void onFailed(JSONObject js, String failed) {

                    loading = true;
                    loaddingDone = false;
                    if (arraylist.size() == 0) {
                        txtConnection.setVisibility(View.VISIBLE);
                        txtConnection.setText(failed);
                    //   txtConnection.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
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
                    if (arraylist.size() == 0) {
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
                    if (arraylist.size() == 0) {
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

                Type type = new TypeToken<List<Review>>() {
                }.getType();
                List<Review> tempListNewsFeeds = new Gson().fromJson(jsonArray.toString(), type);
                if (i == 0) {
                    arraylist.addAll(arraylist.size(), tempListNewsFeeds);
                } else {
                    arraylist.addAll(0, tempListNewsFeeds);
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
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_review, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                Intent intent=new Intent(ReviewListActivity.this,AddReviewActivity.class);
             //   intent.putExtra(Constant.DATA, adPost);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
