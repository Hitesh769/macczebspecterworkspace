package com.spectre.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spectre.R;
import com.spectre.adapter.MessageListAdapter;
import com.spectre.model.Message;
import com.spectre.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChatActivity extends AppCompatActivity {
Context context;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<Message> mMessageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Utility.setContentView(context, R.layout.activity_chat);
        //txtAppBarTitle.setText("Chating");
        mMessageList=new ArrayList<>();
        mMessageList.clear();
        Message message;
        message=new Message();
        message.setMessage("sender message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("12:00 Am");
        message.setSender("123");
        mMessageList.add(message);
        message=new Message();
        message.setMessage("receiver message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("1:00 Am");
        message.setSender("12");
        mMessageList.add(message);
        message=new Message();
        message.setMessage("sender message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("11:00 Am");
        message.setSender("123");
        mMessageList.add(message);
        message=new Message();
        message.setMessage("sender message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("10:00 Am");
        message.setSender("123");
        mMessageList.add(message);
        message=new Message();
        message.setMessage("receiver message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("1:00 Am");
        message.setSender("12");
        mMessageList.add(message);
        message=new Message();
        message.setMessage("receiver message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("2:00 Am");
        message.setSender("12");
        mMessageList.add(message);
        message=new Message();
        message.setMessage("sender message sdfsadfjaklsjdfkasjdflasj");
        message.setCreatedAt("just now");
        message.setSender("123");
        mMessageList.add(message);
        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, mMessageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.smoothScrollToPosition(mMessageList.size()-1);
        mMessageRecycler.setAdapter(mMessageAdapter);

    }
    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        return intent;
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
