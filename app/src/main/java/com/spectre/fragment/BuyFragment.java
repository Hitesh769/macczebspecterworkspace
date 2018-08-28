package com.spectre.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rey.material.widget.ProgressView;
import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.adapter.CarListAdapter;
import com.spectre.beans.AdPost;
import com.spectre.beans.FilterResponse;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.RangeSeekBar;
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.spectre.utility.Utility.getTextViewString;
import static com.spectre.utility.Utility.setLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment {

    // set tag name for manage fragment
    public static final String TAG = BuyFragment.class.getSimpleName();
    @BindView(R.id.rangPrice)
    RangeSeekBar rangPrice;

    Unbinder unbinder;
    @BindView(R.id.txtMinRange)
    TextView txtMinRange;
    @BindView(R.id.txtMaxRange)
    TextView txtMaxRange;
    @BindView(R.id.txtLocation)
    TextView txtLocation;

    private View view;

    private Context context;
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    // private GridLayoutManager gridHorizontal;
    private RecyclerView.Adapter mAdapter;
    ArrayList<AdPost> Arraylist = new ArrayList<>();
    CustomTextView txtConnection;
    private boolean loaddingDone = true;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    ProgressView progressDialog1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FilterResponse filterResponse;
    private String lastFragment = "";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment.
     */
    public static BuyFragment newInstance() {
        BuyFragment fragment = new BuyFragment();
        return fragment;
    }

    // Get main activity to access public variables and methods
    private HomeAct mainActivity() {
        return ((HomeAct) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // view = inflater.inflate(R.layout.fragment_buy, container, false);
        view = inflater.inflate(R.layout.frg_buy, container, false);
        unbinder = ButterKnife.bind(this, view);

        context = getActivity();

        filterResponse = mainActivity().getFilter();

        initView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        getLocation();
        setUpRecycler();
        setSwipeLayout();
        setUpRecyclerListener();
        callMethodEventList(0);

        // set visibility for menu and back icon
        mainActivity().changeBottomMenuColor(HomeAct.MENU_BUY);

        // hide back arrow
        mainActivity().imgBack.setVisibility(View.GONE);

        // set screen title
        mainActivity().txtAppBarTitle.setText(getString(R.string.buy));

        // hide or show app bar
        mainActivity().rlAppBarMain.setVisibility(View.GONE);

        // Set the range
        rangPrice.setRangeValues(0, 10000);
        rangPrice.setNotifyWhileDragging(true);
        rangPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                txtMinRange.setText(String.valueOf(minValue));
                txtMaxRange.setText(String.valueOf(maxValue));
            }
        });
    }

//    private static final int PLACE_PICKER_REQUEST = 999;
//    private static final int LOCATION_PERMISSION_CONSTANT = 101;
//    private String latitude = "";
//    private String longitude = "";

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlacePicker.getPlace(data, getActivity());
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
//                callMethodEventList(0);
//            }
//        }
//
//    }

//    private void getLocation() {
//        // get location
//        if (!PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_FINE_LOCATION) ||
//                !PermissionUtility.checkPermission(context, PermissionUtility.ACCESS_COARSE_LOCATION)) {
//            PermissionUtility.requestPermission(getActivity(), new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
//                    PermissionUtility.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CONSTANT);
//        } else {
//            setLog("PERMISSION grant");
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
//                setLog("Lat : " + bestLocation.getLatitude() + " - Long : " + bestLocation.getLongitude());
//                latitude = String.valueOf(bestLocation.getLatitude());
//                setLog("LAT 1 : " + latitude);
//                longitude = String.valueOf(bestLocation.getLongitude());
//                setLog("Lat : " + getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
//                // tv_rent_fragment_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));
//
//            } else {
//                setLog("Location is null");
//            }
//        }
//    }

//    private Address getAddress(Context context, double lat, double lng) {
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//
//        if (!geocoder.isPresent()) {
//            // showToast(context, R.string.e_service_not_available);
//            return null;
//        }
//
//        Address obj = null;
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//            if (addresses.size() > 0)
//                obj = addresses.get(0);
//            return obj;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private String getFullAddress(double lat, double lng) {
//        Address address = getAddress(context, lat, lng);
//        if (address == null) {
//            return "";
//        }
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(address.getAddressLine(0));
//        buffer.append((address.getAdminArea() == null) ? "" : " ," + address.getLocality());
//        buffer.append((address.getAdminArea() == null) ? "" : " ," + address.getAdminArea());
//        buffer.append((address.getSubLocality() == null) ? "" : " ," + address.getSubLocality());
//        buffer.append((address.getCountryName() == null) ? "" : " ," + address.getCountryName());
//        buffer.append((address.getPostalCode() == null) ? "" : " ," + address.getPostalCode());
//        return String.valueOf(buffer);
//    }

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
        progressDialog1 = view.findViewById(R.id.progress_pv_linear_colors);
        txtConnection = view.findViewById(R.id.no_data);
        mRecyclerView = view.findViewById(R.id.recycler_view2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        gridHorizontal = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(gridHorizontal);
        mRecyclerView.setNestedScrollingEnabled(false);
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
//                visibleItemCount = gridHorizontal.getChildCount();
//                totalItemCount = gridHorizontal.getItemCount();
//                pastVisiblesItems = gridHorizontal.findFirstVisibleItemPosition();

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

            AqueryCall request = new AqueryCall(getActivity());
            setLog("TOKEN : " + SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""));
            request.postWithJsonToken(Urls.FILTER, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""),params, new RequestCallback() {

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


    private void resetData(FilterResponse filterResponse) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSearch)
    public void onViewClicked() {

        BuySearchFragment buySearchFragment =new BuySearchFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.MAXRANGE,getTextViewString(txtMaxRange));
        bundle.putString(Constant.MINRANGE,getTextViewString(txtMinRange));
        bundle.putString(Constant.LATITUDE,latitude);
        bundle.putString(Constant.LONGITUDE,longitude);
        buySearchFragment.setArguments(bundle);
        startNewFragment(buySearchFragment, BuySearchFragment.TAG);
        //setFilterData();
    }
    public void startNewFragment(Fragment fragment, String tag) {
        // --- remove all fragment
        if (lastFragment.trim().length() > 0) {
            FragmentManager fragments = getFragmentManager();
            fragments.popBackStack(lastFragment, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        lastFragment = fragment.getClass().getSimpleName();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_left, R.anim.exit_to_left);
        // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.main_view, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
    private void setFilterData() {
        FilterResponse filterResponse = new FilterResponse();
        filterResponse.setCar_type("");
        filterResponse.setColour("");
        filterResponse.setShow_price_max_range("");
        filterResponse.setPrice_max_range(getTextViewString(txtMaxRange));
        filterResponse.setPrice_min_range(getTextViewString(txtMinRange));
        filterResponse.setYear_to("");
        filterResponse.setYear_from("");
        filterResponse.setCar_name_id("0");
        filterResponse.setCarName("");
        filterResponse.setModel_id("0");
        filterResponse.setCarModel("");
        filterResponse.setVersion_id("0");
        filterResponse.setLocation(getTextViewString(txtLocation));
        filterResponse.setCarVersion("");
        // resetData(filterResponse);
    }

    /* [START] - Location methods */
    @OnClick(R.id.txtLocation)
    public void onLocationClicked() {
        setLog("location text click");
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.imgLocation)
    public void onImgLocationClicked() {
        setLog("location image click");
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private static final int PLACE_PICKER_REQUEST = 999;
    private String latitude = "";
    private String longitude = "";

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
                txtLocation.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

            } else {
                Utility.setLog("Location is null");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setLog("location getting");
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
                setLog("address : " + address);
                txtLocation.setText(stBuilder.toString());
                Arraylist.clear();
                // callMethodEventList(0);
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
                                        PermissionUtility.requestPermission(getActivity(),
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

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(getString(R.string.yes))
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.no), null)
                .create()
                .show();
    }
    /* [END] - Location methods */
}
