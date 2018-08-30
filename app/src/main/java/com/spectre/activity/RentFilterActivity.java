package com.spectre.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.spectre.R;
import com.spectre.customView.CustomTextView;
import com.spectre.customView.RangeSeekBar;
import com.spectre.fragment.RentFragment;
import com.spectre.helper.Common;
import com.spectre.other.Constant;
import com.spectre.utility.PermissionUtility;
import com.spectre.utility.Utility;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.spectre.utility.Utility.getTextViewString;

public class RentFilterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    CustomTextView toolbarTitle;
    @BindView(R.id.toolbar_actionbar)
    Toolbar toolbarActionbar;
    @BindView(R.id.cardView2)
    CardView cardView2;
    @BindView(R.id.txtLocation)
    TextView txtLocation;
    @BindView(R.id.imgLocation)
    ImageView imgLocation;
    @BindView(R.id.input_location)
    EditText inputLocation;
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
    @BindView(R.id.rel_pickUp)
    RelativeLayout relPickUp;
    @BindView(R.id.txt_date_dropUp)
    TextView txtDateDropUp;
    @BindView(R.id.txt_month_dropUp)
    TextView txtMonthDropUp;
    @BindView(R.id.txt_year_dropUp)
    TextView txtYearDropUp;
    @BindView(R.id.txt_day_dropUp)
    TextView txtDayDropUp;
    @BindView(R.id.rel_dropUp)
    RelativeLayout relDropUp;
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
    Context context;
    ActionBar actionBar;
    private static final int LOCATION_PERMISSION_CONSTANT = 101;
    private static final int PLACE_PICKER_REQUEST = 999;
    private String latitude = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setContentView(context, R.layout.activity_rent_filter);
        ButterKnife.bind(this);
        context = this;
        actionBar = Utility.setUpToolbar_(context, "<font color='#ffffff'>Search</font>", true);
        initView();
    }

    private void initView() {
        rangPrice.setRangeValues(100, 10000);
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
            PermissionUtility.requestPermission(RentFilterActivity.this, new String[]{PermissionUtility.ACCESS_FINE_LOCATION,
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
                inputLocation.setText(getFullAddress(Double.valueOf(latitude), Double.valueOf(longitude)));

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

    void setcurrentDate() {
        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat monthname = new SimpleDateFormat("MMM");
        String monthName = monthname.format(mcurrentDate.getTime());

        SimpleDateFormat weekname = new SimpleDateFormat("EEEE");
        Date date = new Date(mYear, mMonth, mDay - 1);
        String weekNameStr = weekname.format(date);

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


    private void showDialog(final int i, final TextView txtDatePickUp, final TextView txtDateDropUp, final TextView txtDayPickUp, final TextView txtDayDropUp, final TextView txtMonthPickUp, final TextView txtMonthDropUp, final TextView txtYearPickUp, final TextView txtYearDropUp) {

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        int dayofweek = mcurrentDate.get(Calendar.DAY_OF_WEEK);

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
                String monthName = monthname.format(myCalendar.getTime());

                SimpleDateFormat weekname = new SimpleDateFormat("EEEE");
                Date date = new Date(selectedyear, selectedmonth, selectedday - 1);
                String weekNameStr = weekname.format(date);

                int mDay = selectedday;
                int mMonth = selectedmonth;
                int mYear = selectedyear;
                if (i == 0) {
                    txtDatePickUp.setText("" + mDay);
                    txtMonthPickUp.setText("" + monthName);
                    txtYearPickUp.setText("" + mYear);
                    txtDayPickUp.setText(weekNameStr);
                } else {
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

    @OnClick({R.id.rel_pickUp, R.id.rel_dropUp, R.id.btnSearch,R.id.llLocation})
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
                Common.location=inputLocation.getText().toString();

                buySearchFragment.setArguments(bundle);

                break;
            case R.id.llLocation:
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(RentFilterActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
