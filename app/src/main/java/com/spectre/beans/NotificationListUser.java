package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ebabu on 1/3/18.
 */

public class NotificationListUser implements Parcelable{
    /*  "notification_id": "1",
              "sender_id": "313",
              "type": "1",
              "msg": "someone showing interest you",
              "id": "20",
              "create_at": "2018-03-01 11:32:36"*/
    String notification_id, sender_id, type, msg, id, create_at;

    protected NotificationListUser(Parcel in) {
        notification_id = in.readString();
        sender_id = in.readString();
        type = in.readString();
        msg = in.readString();
        id = in.readString();
        create_at = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notification_id);
        dest.writeString(sender_id);
        dest.writeString(type);
        dest.writeString(msg);
        dest.writeString(id);
        dest.writeString(create_at);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationListUser> CREATOR = new Creator<NotificationListUser>() {
        @Override
        public NotificationListUser createFromParcel(Parcel in) {
            return new NotificationListUser(in);
        }

        @Override
        public NotificationListUser[] newArray(int size) {
            return new NotificationListUser[size];
        }
    };

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public static Creator<NotificationListUser> getCREATOR() {
        return CREATOR;
    }
}
