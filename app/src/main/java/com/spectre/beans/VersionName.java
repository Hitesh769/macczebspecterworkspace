package com.spectre.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hp on 19/11/2016.
 */
public class VersionName implements Parcelable {
    String id, version_name;

    protected VersionName(Parcel in) {
        id = in.readString();
        version_name = in.readString();
    }


    public VersionName(String id, String version_name) {
        this.id = id;
        this.version_name = version_name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(version_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VersionName> CREATOR = new Creator<VersionName>() {
        @Override
        public VersionName createFromParcel(Parcel in) {
            return new VersionName(in);
        }

        @Override
        public VersionName[] newArray(int size) {
            return new VersionName[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String toString() {
        return version_name;
    }

    @Override
    public boolean equals(Object obj) {
        VersionName carName = (VersionName) obj;
        if (carName.getId().equals(this.getId())) {
            return true;
        }
        return false;
    }
}
