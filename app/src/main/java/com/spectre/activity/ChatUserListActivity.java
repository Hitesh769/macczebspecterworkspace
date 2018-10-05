package com.spectre.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.spectre.R;
import com.spectre.Retrofit.APIClient;
import com.spectre.Retrofit.APIInterface;
import com.spectre.adapter.ChatUserListAdapter;
import com.spectre.adapter.MessageListAdapter;
import com.spectre.customView.MyDialogProgress;
import com.spectre.customView.SessionExpireDialog;
import com.spectre.helper.AqueryCall;
import com.spectre.interfaces.RequestCallback;
import com.spectre.model.Chat.ChatModel;
import com.spectre.model.Chat.UserList;
import com.spectre.other.Constant;
import com.spectre.other.Urls;
import com.spectre.utility.SharedPrefUtils;
import com.spectre.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatUserListActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;
    private RecyclerView mMessageRecycler;

    private ArrayList<UserList.Datum> userLists;
    private List<String> usernameLists;
    private ListView listView;
    String userid="";
    ChatUserListAdapter chatUserListAdapter;
    ArrayAdapter<String> arrayAdapterUserList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_chat_userlist);
        ButterKnife.bind(this);
        init();
        txtAppBarTitle.setText("Select User");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        userLists = new ArrayList<>();
        usernameLists = new ArrayList<>();
        getUserList();
   }



    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChatUserListActivity.class);
        return intent;
    }


    private void getUserList() {
        MyDialogProgress.open(context);

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.USER_ID, SharedPrefUtils.getPreference(context, Constant.USER_ID, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Urls.GETCHATLIST, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                try {
                    if (js.getString("status").equals("success")) {
                        UserList.Datum cDatum;
                        userLists.clear();
                        JSONArray array = js.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            if (!jsonObject1.get("user_id").toString().equals(SharedPrefUtils.getPreference(context, Constant.USER_ID, ""))) {
                                cDatum = new UserList.Datum();
                                cDatum.setFullName(jsonObject1.get("full_name").toString());
                                cDatum.setUserId(jsonObject1.get("user_id").toString());
                                userLists.add(cDatum);
                            }
                        }
                        setAdapter();

                        MyDialogProgress.close(context);
                        //userListDailog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyDialogProgress.close(context);
                }
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                Utility.showToast(context, msg);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    void userListDailog(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dilog_listsearch);
        TextView texttitle = (TextView)dialog.findViewById(R.id.title);
        texttitle.setText("User List");
        listView = (ListView) dialog.findViewById(R.id.List);
        for (int i=0;i<userLists.size();i++){
            if (!userLists.get(i).getUserId().equals(SharedPrefUtils.getPreference(context, Constant.USER_ID, "")))
            usernameLists.add(userLists.get(i).getFullName());
        }
        arrayAdapterUserList=new ArrayAdapter<String>(ChatUserListActivity.this, R.layout.dialoglistitem,R.id.tvName,usernameLists);
        listView.setAdapter(arrayAdapterUserList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userid=userLists.get(position).getUserId();
                //getChatList(true);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    void setAdapter(){
        chatUserListAdapter = new ChatUserListAdapter(context, userLists);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(context));
        mMessageRecycler.setAdapter(chatUserListAdapter);
        chatUserListAdapter.setOnItemClickListener(new ChatUserListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                userid=userLists.get(position).getUserId();
                Intent intent=new Intent(ChatUserListActivity.this,ChatActivity.class);
                intent.putExtra(Constant.USER_ID,userLists.get(position).getUserId());
                intent.putExtra(Constant.USER_NAME,userLists.get(position).getFullName());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }
    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        onBackPressed();
    }
}
