package com.spectre.model.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("to_id")
    @Expose
    private String toId;
    @SerializedName("from_id")
    @Expose
    private String fromId;
    @SerializedName("to_chat")
    @Expose
    private String toChat;
    @SerializedName("from_chat")
    @Expose
    private String fromChat;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToChat() {
        return toChat;
    }

    public void setToChat(String toChat) {
        this.toChat = toChat;
    }

    public String getFromChat() {
        return fromChat;
    }

    public void setFromChat(String fromChat) {
        this.fromChat = fromChat;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}