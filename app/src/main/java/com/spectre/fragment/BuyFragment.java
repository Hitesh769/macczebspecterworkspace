package com.spectre.fragment;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.activity.HomeActivity;
import com.spectre.adapter.CarListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.beans.FilterResponse;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.PermissionUtility;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment {

    private View view;

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
    private String TAG = BuyFragment.class.getName();
    private FilterResponse filterResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buy, container, false);
        context = getActivity();
        filterResponse = ((HomeActivity) getActivity()).getFilter();
        initView();

        return view;
    }


    private void initView() {
        getLocation();
        setUpRecycler();
        setSwipeLayout();
        setUpRecyclerListener();
        callMethodEventList(0);
    }

    private static final int PLACE_PICKER_REQUEST = 999;
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private String latitude = "";
    private String longitude = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                latitude = String.valueOf(place.getLatLng().latitude);
                Utility.setLog("LAT 2 : " + latitude);
                longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(", ");
                stBuilder.append(address);
                // tv_rent_fragment_location.setText(stBuilder.toString());
                callMethodEventList(0);
            }
        }

    }

    private void getLocation() {
        // get location
        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
            PermissionUtility.requestPermission(getActivity(), new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
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
                // tv_rent_fragment_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

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

    public void setSwipeLayout() {
        swipeRefreshLayout = ((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout));
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
        progressDialog1 = (ProgressView) view.findViewById(R.id.progress_pv_linear_colors);
        txtConnection = ((CustomTextView) view.findViewById(R.id.no_data));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CarListAdapter(context, Arraylist, 0);
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
            //  MyDialogProgress.close(context);

            if (progressDialog1.isShown())
                progressDialog1.stop();

            if (i == 1)
                onItemsLoadComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callMethodEventList(final int i) {
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


            AqueryCall request = new AqueryCall(getActivity());
            request.postWithJsonToken(Urls.FILTER, Utility.getSharedPreferences(context, Constant.USER_TOKEN), params, new RequestCallback() {

                @Override
                public void onSuccess(JSONObject js, String success) {
                    txtConnection.setVisibility(View.GONE);
                    saveAndForward(js, i);
                    Utility.setLog("----Buy fragment----");

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


    public void resetData(FilterResponse filterResponse) {
        if (Arraylist != null) {
            Arraylist.clear();
            mAdapter.notifyDataSetChanged();
        }
        loaddingDone = true;

        this.filterResponse = filterResponse;

        callMethodEventList(0);
    }


    public void setService(Intent data) {
        try {
            int pos = data.getIntExtra(Constant.POSITION, -1);
            AdPost adPost = data.getParcelableExtra(Constant.DATA);
            if (pos != -1 && Arraylist != null && Arraylist.size() > 0) {
                Arraylist.set(pos, adPost);
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
