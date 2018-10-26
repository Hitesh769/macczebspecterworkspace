package com.spectre.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spectre.R;
import com.spectre.activity.NotificationDetailActivity;
import com.spectre.beans.NotificationListUser;
import com.spectre.customView.CustomTextView;
import com.spectre.model.Chat.UserList;
import com.spectre.other.Constant;
import com.spectre.utility.SharedPrefUtils;

import java.util.ArrayList;
/**
 * Created by Sumit on 9/25/2016.
 */
public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ViewHolder> {

    private final ArrayList<UserList.Datum> arraylist;
    private final Context appContext;
    private String TAG = "";
    private Dialog dialog;
    private static ClickListener clickListener;
    public ChatUserListAdapter(Context appContext, ArrayList<UserList.Datum> arraylist) {
        this.arraylist = arraylist;
        this.appContext = appContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserList.Datum adPost = arraylist.get(position);
        if (!adPost.getUserId().equals(SharedPrefUtils.getPreference(appContext, Constant.USER_ID, ""))) {
            holder.txt_name.setText(adPost.getFullName());
        }
    }


    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CustomTextView txt_name;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (CustomTextView) itemView.findViewById(R.id.txt_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }
    public void setOnItemClickListener(ClickListener clickListener) {
        ChatUserListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
