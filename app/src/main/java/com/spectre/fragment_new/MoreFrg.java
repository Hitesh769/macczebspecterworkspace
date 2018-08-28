package com.spectre.fragment_new;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spectre.R;
import com.spectre.activity.LoginActivity;
import com.spectre.activity_new.HomeAct;
import com.spectre.activity_new.LoginAct;
import com.spectre.activity_new.SignUpAct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MoreFrg extends MasterFragment {
    // set tag name for manage fragment
    public static final String TAG = MoreFrg.class.getSimpleName();

    // binder object for bind and unbind butter knife view
    Unbinder unbinder;

    @BindView(R.id.imgProfileImage)
    CircleImageView imgProfileImage;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;

    // screen context
    private Context context;

    private boolean isEmployee;

    // A new instance of fragment.
    public static MoreFrg newInstance() {
        MoreFrg fragment = new MoreFrg();
        return fragment;
    }

    // Get main activity to access public variables and methods
    private HomeAct mainActivity() {
        return ((HomeAct) getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg_more, container, false);

        // bind view using butter knife
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialise all controls
        initControls();

        // Set all listeners
        setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /* [START] - User define function */
    private void initControls() {
        // set up screen context
        context = MoreFrg.this.getActivity();

        // set visibility for menu and back icon
        mainActivity().changeBottomMenuColor(HomeAct.MENU_MORE);

        // set screen title
        mainActivity().txtAppBarTitle.setText(getString(R.string.more));

        // hide back arrow
        mainActivity().imgBack.setVisibility(View.VISIBLE);

        // hide or show app bar
        mainActivity().rlAppBarMain.setVisibility(View.VISIBLE);
    }

    private void setListener() {

    }
    /* [END] - User define function */

    /* [START] - Butter knife listener */
    @OnClick({R.id.llLogout, R.id.llLogin, R.id.llSignUp, R.id.llMyAccount, R.id.llPostAd, R.id.llManageAd, R.id.llSettings, R.id.llAboutUs, R.id.llPrivacyPolicy, R.id.llSocial, R.id.llFeedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llLogout:
                break;
            case R.id.llLogin:
                // startAct(mainActivity(), LoginAct.getStartIntent(context));
                startAct(mainActivity(), LoginActivity.getStartIntent(context));
                break;
            case R.id.llSignUp:
                startAct(mainActivity(), SignUpAct.getStartIntent(context));
                break;
            case R.id.llMyAccount:
                break;
            case R.id.llPostAd:
                break;
            case R.id.llManageAd:
                break;
            case R.id.llSettings:
                break;
            case R.id.llAboutUs:
                break;
            case R.id.llPrivacyPolicy:
                break;
            case R.id.llSocial:
                break;
            case R.id.llFeedback:
                break;
        }
    }
    /* [END] - Butter knife listener */
}
