package com.spectre.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.adapter.SearchBuyListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.beans.FilterResponse;
import com.spectre.customView.CustomTextView;
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

import static com.spectre.utility.Utility.setLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuySearchFragment extends Fragment {

    // set tag name for manage fragment
    public static final String TAG = BuySearchFragment.class.getSimpleName();

    private View view;
    private Context context;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ProgressView progressDialog1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private ArrayList<AdPost> Arraylist = new ArrayList<>();
    CustomTextView txtConnection;
    private FilterResponse filterResponse;
    String type = "0";
    private String latitude = "";
    private String longitude = "",maxrange="",minrang="";
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    // Get main activity to access public variables and methods
    private HomeAct mainActivity() {
        return ((HomeAct) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_garage, container, false);
        view = inflater.inflate(R.layout.fragment_buy, container, false);
        context = getActivity();
        initView();
        return view;
    }

    private void initView() {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            latitude = bundle.getString(Constant.LATITUDE);
            longitude=bundle.getString(Constant.LONGITUDE);
            maxrange = bundle.getString(Constant.MAXRANGE);
            minrang=bundle.getString(Constant.MINRANGE);
        }

        // set visibility for menu and back icon
        mainActivity().changeBottomMenuColor(HomeAct.MENU_BUY);

        // set screen title
        mainActivity().txtAppBarTitle.setText(getString(R.string.searchresult));

        // hide back arrow
        mainActivity().imgBack.setVisibility(View.VISIBLE);

        // hide or show app bar
        mainActivity().rlAppBarMain.setVisibility(View.VISIBLE);
        setUpRecycler();
        setUpRecyclerListener();
        //getLocation();
        callMethodEventList(0);

      /*  setUpRecyclerListener();
        callMethodEventList(0);*/
    }

    public void setUpRecyclerListener() {
        loading = true;
        loaddingDone = true;
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

//                visibleItemCount = mLayoutManager.getChildCount();
//                totalItemCount = mLayoutManager.getItemCount();
//                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
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
    private void setUpRecycler() {
        /*for (int i = 0; i <= 9; i++) {
            EventDetail eventDetail = new EventDetail();
            Arraylist.add(eventDetail);
        }*/
        progressDialog1 = (ProgressView) view.findViewById(R.id.progress_pv_linear_colors);
        txtConnection = ((CustomTextView) view.findViewById(R.id.no_data));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchBuyListAdapter(context, Arraylist, 1);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void callMethodEventList(final int i) {
        // XML : item_car_detail
        loading = false;
        if (filterResponse == null) {
            filterResponse = new FilterResponse();
        }
        // Upcoming_event
        try {
            if (i == 0) {
                if (Arraylist.size() == 0) {
                    //  MyDialogProgress.open(context);
                    // params.put(Constant.CREATE_AT, "0");
                    filterResponse.setCreate_at("0");
                } else {
                    //params.put(Constant.CREATE_AT, Arraylist.get(Arraylist.size() - 1).getCreate_at());
                    filterResponse.setCreate_at(Arraylist.get(Arraylist.size() - 1).getCreate_at() + "");
                }
                //  params.put(Constant.LIST_TYPE, "0");
                filterResponse.setList_type("0");
                (progressDialog1).start();
            } else {
                filterResponse.setCreate_at(Arraylist.get(0).getCreate_at() + "");
                filterResponse.setList_type("1");
                //  params.put(Constant.CREATE_AT, Arraylist.get(0).getCreate_at());
                //   params.put(Constant.LIST_TYPE, "1");
            }
            filterResponse.setType("1");
            JSONObject params = new JSONObject(new Gson().toJson(filterResponse));

            params.put(Constant.LATITUDE, latitude);
            params.put(Constant.LONGITUDE, longitude);
            params.put(Constant.MAXPRICE,maxrange);
            params.put(Constant.MINPRICE,minrang);

            AqueryCall request = new AqueryCall(getActivity());
            setLog("TOKEN : " + SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""));
            request.postWithJsonToken(Urls.FILTER, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject js, String success) {
                    txtConnection.setVisibility(View.GONE);
                    saveAndForward(js, i);
                    setLog("----Buy fragment----");

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

                    setLog("Buy Fragment FAIL");
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

    public void closeProgressDialog(int i) {
        try {
            //  MyDialogProgress.close(context);

            if (progressDialog1.isShown())
                progressDialog1.stop();

          //  if (i == 1)
                //onItemsLoadComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}