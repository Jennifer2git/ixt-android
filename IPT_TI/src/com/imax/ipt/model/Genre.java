package com.imax.ipt.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Genre implements Parcelable {
    private int id;
    private String name;

    public Genre(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Genre> CREATOR = new Creator<Genre>() {

        @Override
        public Genre[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Genre[size];
        }

        @Override
        public Genre createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Genre(source);
        }
    };

    private Genre(Parcel source) {
        id = source.readInt();
        name = source.readString();
    }
}
