package com.spectre.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.spectre.R;
import com.spectre.activity_new.HomeAct;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.RangeSeekBar;
import com.spectre.helper.Common;
import com.spectre.other.Constant;
import com.spectre.utility.PermissionUtility;
import com.spectre.utility.Utility;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.spectre.utility.Utility.getTextViewString;
import static com.spectre.utility.Utility.setLog;


public class RentFilterFragment extends Fragment {
    public static final String TAG = RentFilterFragment.class.getSimpleName();
    @BindView(R.id.txtLocation)
    TextView txtLocation;
    @BindView(R.id.imgLocation)
    ImageView imgLocation;
    @BindView(R.id.input_location)
    EditText input_location;
    @BindView(R.id.input_layout_search)
    TextInputLayout inputLayoutSearch;
    @BindView(R.id.llLocation)
    RelativeLayout llLocation;
    @BindView(R.id.txt_date_pickUp)
    TextView txtDatePickUp;
    @BindView(R.id.txt_month_pickUp)
    TextView txtMonthPickUp;
    @BindView(R.id.txt_year_pickUp)
    TextView txtYearPickUp;
    @BindView(R.id.txt_day_pickUp)
    TextView txtDayPickUp;
    @BindView(R.id.txt_date_dropUp)
    TextView txtDateDropUp;
    @BindView(R.id.txt_month_dropUp)
    TextView txtMonthDropUp;
    @BindView(R.id.txt_year_dropUp)
    TextView txtYearDropUp;
    @BindView(R.id.txt_day_dropUp)
    TextView txtDayDropUp;
    @BindView(R.id.rangPrice)
    RangeSeekBar rangPrice;
    @BindView(R.id.txtMinRange)
    TextView txtMinRange;
    @BindView(R.id.txtMaxRange)
    TextView txtMaxRange;
    @BindView(R.id.cardView1)
    CardView cardView1;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    Unbinder unbinder;
    @BindView(R.id.rel_pickUp)
    RelativeLayout relPickUp;
    @BindView(R.id.rel_dropUp)
    RelativeLayout relDropUp;
    private View view;
    private Context context;
    private String lastFragment="";
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private static final int PLACE_PICKER_REQUEST = 999;
    private String latitude = "";
    private String longitude = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.rent_filter_fragment, container, false);
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
        mainActivity().changeBottomMenuColor(HomeAct.MENU_RENT);

        // set screen title
        mainActivity().txtAppBarTitle.setText(getString(R.string.rent));

        // hide back arrow
        mainActivity().imgBack.setVisibility(View.VISIBLE);

        // hide or show app bar
        mainActivity().rlAppBarMain.setVisibility(View.GONE);

        rangPrice.setRangeValues(0, 10000);
        rangPrice.setNotifyWhileDragging(true);
        rangPrice.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                txtMinRange.setText(String.valueOf(minValue));
                txtMaxRange.setText(String.valueOf(maxValue));
            }
        });

        setcurrentDate();
        getLocation();

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
                input_location.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rel_pickUp, R.id.rel_dropUp, R.id.btnSearch,R.id.input_location,R.id.imgLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rel_pickUp:
                 showDialog(0,txtDatePickUp,txtDateDropUp,txtDayPickUp,txtDayDropUp,txtMonthPickUp,txtMonthDropUp,txtYearPickUp,txtYearDropUp);
                break;
            case R.id.rel_dropUp:
                 showDialog(1,txtDatePickUp,txtDateDropUp,txtDayPickUp,txtDayDropUp,txtMonthPickUp,txtMonthDropUp,txtYearPickUp,txtYearDropUp);
                break;
            case R.id.btnSearch:
                RentFragment buySearchFragment =new RentFragment();
                Bundle bundle=new Bundle();
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

                buySearchFragment.setArguments(bundle);
                mainActivity().startNewFragment(buySearchFragment);
                break;
            case R.id.input_location:
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
                input_location.setText(stBuilder.toString());
                //Arraylist.clear();
                // callMethodEventList(0);
            }
        }
    }
    private void showDialog(final int i, final TextView txtDatePickUp, final TextView txtDateDropUp, final TextView txtDayPickUp, final TextView txtDayDropUp, final TextView txtMonthPickUp, final TextView txtMonthDropUp, final TextView txtYearPickUp, final TextView txtYearDropUp) {

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        int dayofweek=mcurrentDate.get(Calendar.DAY_OF_WEEK);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
              //  myCalendar.set(Calendar.DAY_OF_WEEK,);
                //String myFormat = "dd/MM/yy"; //Change as you need
               //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
                SimpleDateFormat monthname = new SimpleDateFormat("MMM");
                String monthName=monthname.format(myCalendar.getTime());

                SimpleDateFormat weekname = new SimpleDateFormat("EEEE");
                Date date = new Date(selectedyear, selectedmonth, selectedday-1);
                String weekNameStr=weekname.format(date);

                int mDay = selectedday;
                int mMonth = selectedmonth;
                int mYear = selectedyear;
                if (i==0) {
                    txtDatePickUp.setText("" + mDay);
                    txtMonthPickUp.setText("" + monthName);
                    txtYearPickUp.setText("" + mYear);
                    txtDayPickUp.setText(weekNameStr);
                }
                else{
                    txtDateDropUp.setText("" + mDay);
                    txtMonthDropUp.setText("" + monthName);
                    txtYearDropUp.setText("" + mYear);
                    txtDayDropUp.setText(weekNameStr);
                }
            }
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    /*public void startNewFragment(Fragment fragment, String tag) {
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
    }*/

    void setcurrentDate(){
         Calendar mcurrentDate = Calendar.getInstance();
         int mYear = mcurrentDate.get(Calendar.YEAR);
         int mMonth = mcurrentDate.get(Calendar.MONTH);
         int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

         SimpleDateFormat monthname = new SimpleDateFormat("MMM");
         String monthName=monthname.format(mcurrentDate.getTime());

         SimpleDateFormat weekname = new SimpleDateFormat("EEEE");
         Date date = new Date(mYear, mMonth, mDay-1);
         String weekNameStr=weekname.format(date);

         //set current date default
         txtDatePickUp.setText("" + mDay);
         txtMonthPickUp.setText("" + monthName);
         txtYearPickUp.setText("" + mYear);
         txtDayPickUp.setText(weekNameStr);
         txtDateDropUp.setText("" + mDay);
         txtMonthDropUp.setText("" + monthName);
         txtYearDropUp.setText("" + mYear);
         txtDayDropUp.setText(weekNameStr);
     }
}
