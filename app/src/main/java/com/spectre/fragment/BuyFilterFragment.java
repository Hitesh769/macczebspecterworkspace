package com.spectre.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.beans.CarName;
import com.spectre.beans.ModelName;
import com.spectre.beans.VersionName;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.spectre.utility.Utility.setLog;


public class BuyFilterFragment extends Fragment {
    public static final String TAG = BuyFilterFragment.class.getSimpleName();
    @BindView(R.id.cardView2)
    CardView cardView2;
    @BindView(R.id.input_brand_name)
    EditText inputBrandName;
    @BindView(R.id.input_layout_brandname)
    TextInputLayout inputLayoutBrandname;
    @BindView(R.id.input_model_name)
    EditText inputModelName;
    @BindView(R.id.input_layout_modle)
    TextInputLayout inputLayoutModle;
    @BindView(R.id.input_transactionType)
    EditText inputTransactionType;
    @BindView(R.id.input_layout_trasactionTIme)
    TextInputLayout inputLayoutTrasactionTIme;
    @BindView(R.id.input_fromYear)
    EditText inputFromYear;
    @BindView(R.id.input_layout_fromYear)
    TextInputLayout inputLayoutFromYear;
    @BindView(R.id.input_toYear)
    EditText inputToYear;
    @BindView(R.id.input_layout_toYear)
    TextInputLayout inputLayoutToYear;
    @BindView(R.id.input_SellerType)
    EditText inputSellerType;
    @BindView(R.id.input_layout_sellertype)
    TextInputLayout inputLayoutSellertype;
    @BindView(R.id.input_color)
    EditText inputColor;
    @BindView(R.id.input_layout_color)
    TextInputLayout inputLayoutColor;
    @BindView(R.id.llLocation)
    LinearLayout llLocation;
    @BindView(R.id.cardView1)
    CardView cardView1;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    Unbinder unbinder;
    @BindView(R.id.edtLocation)
    EditText edtLocation;
    @BindView(R.id.imgLocation)
    ImageView imgLocation;

    private View view;
    private Context context;
    private String lastFragment = "";
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private static final int PLACE_PICKER_REQUEST = 999;
    private String latitude = "";
    private String longitude = "";
    private ArrayList<CarName> listBrandName = new ArrayList<>();
    private ArrayList<ModelName> listModelName = new ArrayList<>();
    private ArrayList<ModelName> listModelName1 = new ArrayList<>();
    private ArrayList<VersionName> listVersionName = new ArrayList<>();
    private ArrayList<VersionName> listVersionName1 = new ArrayList<>();

    private ArrayAdapter<CarName> arrayAdapterCarName;
    private ArrayAdapter<ModelName> arrayAdapterCarModel;
    private ArrayAdapter<VersionName> arrayAdapterCarVersion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.buy_filter_fragment, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private HomeAct mainActivity() {
        return ((HomeAct) getActivity());
    }

    private void initView() {

        // set visibility for menu and back icon
        mainActivity().changeBottomMenuColor(HomeAct.MENU_BUY);

        // hide back arrow
        mainActivity().imgBack.setVisibility(View.GONE);

        // set screen title
        mainActivity().txtAppBarTitle.setText(getString(R.string.buy));

        // hide or show app bar
        mainActivity().rlAppBarMain.setVisibility(View.GONE);

        setCarNames();
        getLocation();
        //setCarModel("", false);
        //setCarVersion("", false);

    }

    public static BuyFilterFragment newInstance() {
        BuyFilterFragment fragment = new BuyFilterFragment();
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.edtLocation, R.id.imgLocation,R.id.btnSearch, R.id.input_brand_name, R.id.input_model_name, R.id.input_transactionType, R.id.input_fromYear, R.id.input_toYear, R.id.input_color, R.id.input_SellerType})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                BuySearchFragment buySearchFragment = new BuySearchFragment();
              /*  Bundle bundle=new Bundle();
                bundle.putString(Constant.MAXRANGE,getTextViewString(txtMaxRange));
                bundle.putString(Constant.MINRANGE,getTextViewString(txtMinRange));
                bundle.putString(Constant.LATITUDE,latitude);
                bundle.putString(Constant.LONGITUDE,longitude);

                Common.strDatePickUp=txtDatePickUp.getText().toString();
                Common.strMonthPickUp=txtMonthPickUp.getText().toString();
                Common.strDayPickUp=txtDayPickUp.getText().toString();
                Common.strYearPickUp=txtYearPickUp.getText().toString();
                Common.strDateDropoff=txtDateDropUp.getText().toString();
                Common.strMonthDropoff=txtMonthDropUp.getText().toString();
                Common.strDayDropoff=txtDayDropUp.getText().toString();
                Common.strYearDropoff=txtYearDropUp.getText().toString();
                Common.location=input_location.getText().toString();
                buySearchFragment.setArguments(bundle);*/
                mainActivity().startNewFragment(buySearchFragment);
                break;
            case R.id.input_brand_name:
                arrayAdapterCarName = new ArrayAdapter<CarName>(context, android.R.layout.simple_list_item_1, listBrandName);
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdapterCarName)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                inputBrandName.setText(listBrandName.get(position).getCar_name());
                                setCarModel(listBrandName.get(position).getId(), true);
                                //  setCarVersion("", false);
                                dialog.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog.show();
                break;
            case R.id.input_model_name:
                arrayAdapterCarModel = new ArrayAdapter<ModelName>(context, android.R.layout.simple_list_item_1, listModelName);

                DialogPlus dialog1 = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdapterCarModel)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog1, Object item, View view, int position) {
                                inputModelName.setText(listModelName.get(position).getModel_name());
                                setCarVersion(listModelName.get(position).getId(), true);
                                //  setCarVersion("", false);
                                dialog1.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog1.show();
                break;
            case R.id.input_transactionType:
                final ArrayList<String> listtype = new ArrayList<>();
                listtype.add("Any Type");
                listtype.add("Automatic");
                listtype.add("Manual");
                ArrayAdapter<String> arrayAdapterTransmissiontype = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listtype);

                DialogPlus dialog2 = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdapterTransmissiontype)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog1, Object item, View view, int position) {
                                inputTransactionType.setText(listtype.get(position));
                                //  setCarVersion(listModelName.get(position).getId(), true);
                                //  setCarVersion("", false);
                                dialog1.dismiss();
                            }
                        })
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog2.show();
                break;
            case R.id.input_fromYear:
                String arraycarYear[] = getResources().getStringArray(R.array.year);
                final List<String> year = Arrays.asList(arraycarYear);
                ArrayAdapter<String> arrayAdapterfromyear = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, year);
                DialogPlus dialog3 = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdapterfromyear)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog1, Object item, View view, int position) {
                                inputFromYear.setText(year.get(position));
                                //  setCarVersion(listModelName.get(position).getId(), true);
                                //  setCarVersion("", false);
                                dialog1.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog3.show();
                break;
            case R.id.input_toYear:
                String arraycarYear1[] = getResources().getStringArray(R.array.year);
                final List<String> year1 = Arrays.asList(arraycarYear1);
                ArrayAdapter<String> arrayAdapterfromyear1 = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, year1);
                DialogPlus dialog4 = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdapterfromyear1)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog1, Object item, View view, int position) {
                                inputToYear.setText(year1.get(position));
                                //  setCarVersion(listModelName.get(position).getId(), true);
                                //  setCarVersion("", false);
                                dialog1.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog4.show();
                break;
            case R.id.input_SellerType:
                String arraycarSellerType[] = getResources().getStringArray(R.array.seller_type);
                final List<String> sellerList = Arrays.asList(arraycarSellerType);
                ArrayAdapter<String> arrayAdaptercarSellerType = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, sellerList);
                DialogPlus dialog5 = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdaptercarSellerType)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog1, Object item, View view, int position) {
                                inputSellerType.setText(sellerList.get(position));
                                dialog1.dismiss();
                            }
                        })
                        .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog5.show();
                break;
            case R.id.input_color:
                String arrayCarColor[] = getResources().getStringArray(R.array.car_color);
                final List<String> carColor = Arrays.asList(arrayCarColor);

                ArrayAdapter<String> arrayAdapterColor = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, carColor);
                DialogPlus dialog6 = DialogPlus.newDialog(context)
                        .setAdapter(arrayAdapterColor)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog1, Object item, View view, int position) {
                                inputColor.setText(carColor.get(position));
                                //  setCarVersion(listModelName.get(position).getId(), true);
                                //  setCarVersion("", false);
                                dialog1.dismiss();
                            }
                        })
                        .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                        .create();
                dialog6.show();
                break;
            case R.id.edtLocation:
                setLog("location text click");
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.imgLocation:
                    getLocation();
                break;
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
                edtLocation.setText(stBuilder.toString());
               // Arraylist.clear();
                // callMethodEventList(0);
            }
        }
    }


    void displayDialog(ArrayAdapter<Object> adapter) {
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, android.R.id.text1, values);
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    }
                })
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
    }

    private void setCarNames() {
        listBrandName.clear();
        listBrandName.add(new CarName("0", "Any Type"));
        getList(1, "");
    }

    private void setCarModel(String id, boolean isCall) {
        listModelName.clear();
        listModelName.add(new ModelName("0", "Any Type"));

        if (isCall)
            getList(2, id);
        else {
            if (arrayAdapterCarModel == null) {
                arrayAdapterCarModel = new ArrayAdapter<ModelName>(context, android.R.layout.simple_list_item_single_choice, listModelName);
                // spinnerCarModel.setAdapter(arrayAdapterCarModel);
            } else {
                arrayAdapterCarModel.notifyDataSetChanged();
            }
        }
    }

    private void setCarVersion(String id, boolean isCall) {
        listVersionName.clear();
        listVersionName.add(new VersionName("0", "Any Type"));

        if (isCall)
            getList(3, id);
        else {
            if (arrayAdapterCarVersion == null) {
                arrayAdapterCarVersion = new ArrayAdapter<VersionName>(context, android.R.layout.simple_list_item_single_choice, listVersionName);
                // spinnerCarVersion.setAdapter(arrayAdapterCarVersion);
            } else {
            }
            arrayAdapterCarVersion.notifyDataSetChanged();
        }
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

        new AqueryCall(getActivity()).postWithJsonToken(Url, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
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
           /* if (spinnerCarName != null)
                spinnerCarName.setAdapter(arrayAdapterCarName);*/
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
                edtLocation.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

            } else {
                Utility.setLog("Location is null");
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
}
