package com.spectre.fragment_new;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spectre.R;
import com.spectre.activity.AddWorkActivity;
import com.spectre.activity.ChatActivity;
import com.spectre.activity.LoginActivity;
import com.spectre.activity.ManageAdActivity;
import com.spectre.activity.ManageRentedActivity;
import com.spectre.activity.ManageWorkActivity;
import com.spectre.activity.PostAdActivity;
import com.spectre.activity.RentCarActivity;
import com.spectre.activity_new.HomeAct;
import com.spectre.activity_new.SignUpAct;
import com.spectre.customView.CustomRayMaterialTextView;
import com.spectre.customView.CustomTextView;
import com.spectre.other.Constant;
import com.spectre.utility.SharedPrefUtils;

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
    @BindView(R.id.llLogout)
    LinearLayout llLogout;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.llLogin)
    LinearLayout llLogin;
    @BindView(R.id.llSignUp)
    LinearLayout llSignUp;
    @BindView(R.id.llMyAccount)
    LinearLayout llMyAccount;
    @BindView(R.id.llPostAd)
    LinearLayout llPostAd;
    @BindView(R.id.llManageAd)
    LinearLayout llManageAd;
    @BindView(R.id.llSettings)
    LinearLayout llSettings;
    @BindView(R.id.llAboutUs)
    LinearLayout llAboutUs;
    @BindView(R.id.llPrivacyPolicy)
    LinearLayout llPrivacyPolicy;
    @BindView(R.id.llSocial)
    LinearLayout llSocial;
    @BindView(R.id.llFeedback)
    LinearLayout llFeedback;
    @BindView(R.id.cardView1)
    CardView cardView1;
    @BindView(R.id.linlogin)
    LinearLayout linlogin;
    @BindView(R.id.linRegister)
    LinearLayout linRegister;
    @BindView(R.id.llChangeLanguage)
    LinearLayout llChangeLanguage;
    @BindView(R.id.linChangeLanguage)
    LinearLayout linChangeLanguage;
    @BindView(R.id.linAboutUs)
    LinearLayout linAboutUs;
    @BindView(R.id.linPrivacyPolicy)
    LinearLayout linPrivacyPolicy;
    @BindView(R.id.linMyProfile)
    LinearLayout linMyProfile;
    @BindView(R.id.linPostAd)
    LinearLayout linPostAd;
    @BindView(R.id.linManageAds)
    LinearLayout linManageAds;
    @BindView(R.id.linSettings)
    LinearLayout linSettings;
    @BindView(R.id.linMessage)
    LinearLayout linMessage;


    // screen context
    private Context context;

    private boolean isEmployee;
    private Dialog ddPostAd = null;
    private CustomRayMaterialTextView btn_post_buy, btn_post_rent, btn_post_garage;
    private CustomTextView txt_post_ad_header;
    private Dialog ddManageAd = null;
    private CustomRayMaterialTextView btn_manage_buy, btn_manage_rent, btn_manage_garage;
    private CustomTextView txt_header, txt_manage_ad_header;
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

        boolean isLogin = SharedPrefUtils.getPreference(context, Constant.ISLOGIN, false);

        if (isLogin == true) {
            llLogout.setVisibility(View.VISIBLE);
            linlogin.setVisibility(View.GONE);
            linRegister.setVisibility(View.GONE);
            linChangeLanguage.setVisibility(View.GONE);
            linMyProfile.setVisibility(View.VISIBLE);
            linPostAd.setVisibility(View.VISIBLE);
            linManageAds.setVisibility(View.VISIBLE);
            linSettings.setVisibility(View.VISIBLE);
            linMessage.setVisibility(View.VISIBLE);
            llSocial.setVisibility(View.VISIBLE);

        } else {
            llLogout.setVisibility(View.GONE);
        }


    }

    private void setListener() {

    }
    /* [END] - User define function */

    /* [START] - Butter knife listener */
    @OnClick({R.id.linMessage,R.id.llLogout, R.id.llLogin, R.id.llSignUp, R.id.llMyAccount, R.id.llPostAd, R.id.llManageAd, R.id.llSettings, R.id.llAboutUs, R.id.llPrivacyPolicy, R.id.llSocial, R.id.llFeedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llLogout:
                SharedPrefUtils.setPreference(context, Constant.ISLOGIN, false);
                SharedPrefUtils.clearAllPreference(context);
                SharedPrefUtils.setPreference(context, Constant.USER_TOKEN, "ef73781effc5774100f87fe2f437a435");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(MoreFrg.this).attach(MoreFrg.this).commit();
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
                choosePostAd();
                break;
            case R.id.llManageAd:
                chooseManageAd();
                break;
            case R.id.linMessage:
                startAct(mainActivity(), ChatActivity.getStartIntent(context));
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
    public void choosePostAd() {

        ddPostAd = new Dialog(context);

        ddPostAd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ddPostAd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ddPostAd.setContentView(R.layout.dialog_post_ad_buyer);

        btn_post_buy = ddPostAd.findViewById(R.id.btn_post_buy);
        btn_post_garage = ddPostAd.findViewById(R.id.btn_post_garage);
        btn_post_rent = ddPostAd.findViewById(R.id.btn_post_rent);
        txt_post_ad_header = ddPostAd.findViewById(R.id.txt_post_ad_header);

        if (!SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").isEmpty() && SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").equalsIgnoreCase("1")) {
            btn_post_garage.setVisibility(View.GONE);
        } else
            btn_post_garage.setVisibility(View.VISIBLE);
        ddPostAd.setCancelable(false);
        ddPostAd.getWindow().setLayout(-1, -2);
        ddPostAd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ddPostAd.show();

        btn_post_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, PostAdActivity.class));
                ddPostAd.dismiss();
            }
        });

        btn_post_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, RentCarActivity.class));
                ddPostAd.dismiss();
            }
        });

        btn_post_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddWorkActivity.class));
//                Garage adPost = new Garage();
//                if (context instanceof HomeFormatActivity && status == 1) {
//                    Intent intent = new Intent(context, GarageDetailActivity.class);
//                    intent.putExtra(Constant.DATA, adPost);
//                    intent.putExtra(Constant.POSITION, arraylist.get(0));
//                    ((HomeFormatActivity) context).startActivityForResult(intent, 404);
//                }

//                Intent intent = new Intent(context, GarageDetailActivity.class);
//                intent.putExtra(Constant.DATA, adPost);
//                startActivity(intent);
                ddPostAd.dismiss();
            }
        });

        txt_post_ad_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddPostAd.dismiss();
            }
        });
    }
    public void chooseManageAd() {

        ddManageAd = new Dialog(context);

        ddManageAd.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        ddManageAd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ddManageAd.setContentView(R.layout.dialog_mange_ad_seller);
        btn_manage_buy = ddManageAd.findViewById(R.id.btn_manage_buy);
        btn_manage_rent = ddManageAd.findViewById(R.id.btn_manage_rent);
        btn_manage_garage = ddManageAd.findViewById(R.id.btn_manage_garage);
        txt_manage_ad_header = ddManageAd.findViewById(R.id.txt_manage_ad_header);
        btn_manage_garage.setVisibility(View.VISIBLE);
        if (!SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").isEmpty() && SharedPrefUtils.getPreference(context, Constant.USER_TYPE, "").equalsIgnoreCase("1")) {
            btn_manage_garage.setVisibility(View.GONE);
        }
        ddManageAd.setCancelable(false);
        ddManageAd.getWindow().setLayout(-1, -2);
        ddManageAd.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        ddManageAd.show();

        btn_manage_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ManageAdActivity.class));
                ddManageAd.dismiss();
            }
        });

        btn_manage_rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ManageRentedActivity.class));
                ddManageAd.dismiss();
            }
        });

        btn_manage_garage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, ManageWorkActivity.class));
                ddManageAd.dismiss();
            }
        });

        txt_manage_ad_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ddManageAd.dismiss();
            }
        });
    }
}
