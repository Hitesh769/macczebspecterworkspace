package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.spectre.R;
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

public class ChatActivity extends AppCompatActivity {
    Context context;
    @BindView(R.id.txtAppBarTitle)
    TextView txtAppBarTitle;
    @BindView(R.id.imgBack)
    AppCompatImageView imgBack;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    RelativeLayout img_send;
    EditText edittext_chatbox;
    private List<ChatModel.Datum> mMessageList;
    private List<ChatModel.Datum> oldmMessageList;
    static  String userid = "",userName;
    ChatModel.Datum cDatum;
    boolean isResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_chat);
        ButterKnife.bind(this);
        init();
        userid = getIntent().getStringExtra(Constant.USER_ID);
        String name=getIntent().getStringExtra(Constant.USER_NAME);
        userName = name.substring(0, 1).toUpperCase() + name.substring(1);
        getChatList(true);

    }

    private void init() {
        edittext_chatbox = (EditText) findViewById(R.id.edittext_chatbox);
        img_send = (RelativeLayout) findViewById(R.id.img_send);
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageList = new ArrayList<>();

        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edittext_chatbox.getText().toString();
                if (!msg.isEmpty()) {
                    sendMessage(msg);
                } else {
                    Toast.makeText(context, "Please Type message", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }



    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        return intent;
    }

    private void getChatList(final boolean isrefresh) {
        MyDialogProgress.close(context);
        txtAppBarTitle.setText(userName);
        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constant.TO_ID, SharedPrefUtils.getPreference(context, Constant.USER_ID, ""));
            jsonObject.put(Constant.FROM_ID, userid);
            jsonObject.put(Constant.LANGUAGE, "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Urls.GETCHATROOM, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                try {
                    if (js.getString("status").equals("success")) {
                        isResponse=true;

                        if (oldmMessageList != null) {
                            oldmMessageList.clear();
                        }
                        oldmMessageList = new ArrayList<ChatModel.Datum>(mMessageList);
                        mMessageList.clear();
                        JSONArray array = js.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject1 = array.getJSONObject(i);
                            cDatum = new ChatModel.Datum();
                            cDatum.setId(jsonObject1.get("id").toString());
                            cDatum.setToId(jsonObject1.get("to_id").toString());
                            cDatum.setToChat(jsonObject1.get("to_chat").toString());
                            cDatum.setFromId(jsonObject1.get("from_id").toString());
                            cDatum.setCreated(jsonObject1.get("created").toString());
                            mMessageList.add(cDatum);
                        }
                        if (isrefresh) {
                           setAdapter();
                        }else {
                            if (mMessageList.size() > oldmMessageList.size()){
                               setAdapter();
                            }
                            else {
                                getChatList(false);
                            }
                        }
                        MyDialogProgress.close(context);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    MyDialogProgress.close(context);
                }
            }

            @Override
            public void onFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
               // Utility.showToast(context, msg);
            }

            @Override
            public void onAuthFailed(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                SessionExpireDialog.openDialog(context, 0, "");
            }

            @Override
            public void onNull(JSONObject js, String msg) {
                MyDialogProgress.close(context);
                if (isResponse) {
                    getChatList(false);
                }
                else{
                    getChatList(true);
                }
                Utility.showToast(context, msg);
            }

            @Override
            public void onException(JSONObject js, String msg) {
                MyDialogProgress.close(context);
               // Utility.showToast(context, msg);
            }

            @Override
            public void onInactive(JSONObject js, String inactive, String status) {

            }
        });
    }

    private void sendMessage(String message) {
        // MyDialogProgress.open(context);
        JSONObject jsonObject = new JSONObject();
      //  mMessageAdapter.add
   /*   cDatum = new ChatModel.Datum();
        cDatum.setId("");
        cDatum.setToId(SharedPrefUtils.getPreference(context, Constant.USER_ID,""));
        cDatum.setToChat(edittext_chatbox.getText().toString());
        cDatum.setFromId("");
        cDatum.setCreated("now");
        mMessageList.add(cDatum);
        //setAdapter();
        mMessageAdapter.notifyDataSetChanged();*/

        edittext_chatbox.setText("");
        try {
            jsonObject.put(Constant.TO_ID, SharedPrefUtils.getPreference(context, Constant.USER_ID, ""));
            jsonObject.put(Constant.FROM_ID, userid);
            jsonObject.put(Constant.TO_CHAT, message);
            jsonObject.put(Constant.LANGUAGE, "en");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new AqueryCall(this).postWithJsonToken(Urls.CHATROOM, SharedPrefUtils.getPreference(context, Constant.USER_TOKEN, ""), jsonObject, new RequestCallback() {
            @Override
            public void onSuccess(JSONObject js, String msg) {
                try {
                    if (js.getString("status").equals("success")) {
                        edittext_chatbox.setText("");
                        mMessageAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    void setAdapter(){
        mMessageAdapter = new MessageListAdapter(context, mMessageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(context));
        mMessageRecycler.scrollToPosition(mMessageList.size() - 1);
        mMessageRecycler.setAdapter(mMessageAdapter);
        getChatList(false);
    }
    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        onBackPressed();
    }



}
