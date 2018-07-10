package com.spectre.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.spectre.R;
import com.spectre.activity.AddWorkSectionActivity;
import com.spectre.customView.AlertBox;
import com.spectre.customView.CustomEditText;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.other.Constant;
import com.spectre.utility.Utility;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class WorkDescriptionFragment extends Fragment implements View.OnClickListener {

    private View view;
    private CustomEditText et_mileage, et_price, et_car_condition, et_problem;
    private AppCompatRadioButton radioyes, radioNo;
    private CustomRayMaterialTextView btn_des_next;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_work_description, container, false);
        context = getActivity();
        initView();
        return view;
    }

    private void initView() {
        et_car_condition = (CustomEditText) view.findViewById(R.id.et_car_condition);
        et_mileage = (CustomEditText) view.findViewById(R.id.et_mileage);
        et_price = (CustomEditText) view.findViewById(R.id.et_price);
        et_problem = (CustomEditText) view.findViewById(R.id.et_problem);
        btn_des_next = (CustomRayMaterialTextView) view.findViewById(R.id.btn_des_next);
        btn_des_next.setOnClickListener(this);

        radioyes = (AppCompatRadioButton) view.findViewById(R.id.radio_yes);
        radioNo = (AppCompatRadioButton) view.findViewById(R.id.radio_no);
        AppCompatRadioButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    radioyes.setChecked(radioyes == buttonView);
                    radioNo.setChecked(radioNo == buttonView);

                }
            }
        };
        radioyes.setOnCheckedChangeListener(listener);
        radioNo.setOnCheckedChangeListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_des_next:
                checkCondition();
                break;
        }
    }

    private void checkCondition() {
        et_mileage.setError(null);
        et_car_condition.setError(null);
        et_price.setError(null);
        et_problem.setError(null);


        if (et_mileage.getText().toString().trim().isEmpty()) {
            et_mileage.setError(getString(R.string.pls_select_cMileage));
            et_mileage.requestFocus();
            return;
        }

        if (et_price.getText().toString().trim().isEmpty()) {
            et_price.setError(getString(R.string.pls_select_cPrice));
            et_price.requestFocus();
            return;
        }

        if (et_car_condition.getText().toString().trim().isEmpty()) {
            et_car_condition.setError(getString(R.string.pls_select_cCondition));
            et_car_condition.requestFocus();
            return;
        }

        if (et_problem.getText().toString().trim().isEmpty()) {
            et_problem.setError(getString(R.string.pls_enter_problem));
            et_problem.requestFocus();
            return;
        }

        ((AddWorkSectionActivity) context).setPosition(2, true);
    }


    public void clearFocus() {
        if (et_car_condition != null)
            et_car_condition.setError(null);
        if (et_price != null)
            et_price.setError(null);
        if (et_mileage != null)
            et_mileage.setError(null);
        if (et_problem != null)
            et_problem.setError(null);
    }


    public void setFocus() {
        if (et_car_condition != null)
            et_car_condition.setFocusable(true);
        if (et_price != null)
            et_price.setFocusable(true);
        if (et_mileage != null)
            et_mileage.setFocusable(true);
        if (et_problem != null)
            et_problem.setFocusable(true);
    }

    public JSONObject getData() {
        JSONObject jsonObject = ((AddWorkSectionActivity)context).jsonObject;
        try {
            jsonObject.put(Constant.MILEAGE, et_mileage.getText().toString().trim());
            jsonObject.put(Constant.PRICE, et_price.getText().toString().trim());
            jsonObject.put(Constant.CAR_CONDITION, et_car_condition.getText().toString().trim());
            jsonObject.put(Constant.PROBLEM, et_problem.getText().toString().trim());
            jsonObject.put(Constant.CAR_MODIFIED, radioNo.isChecked() ? "0" : "1");
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject=null;
        }


        return jsonObject;
    }
}
