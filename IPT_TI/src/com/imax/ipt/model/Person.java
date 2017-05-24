package com.imax.ipt.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Person extends Media implements Parcelable {
    private String lastName;
    private String firstName;

    private String[] coverArtPaths;

    public Person() {
        setTitle(firstName + " " + lastName);
    }

    public String fullName() {
        if (firstName == null || lastName == null) {
            if (firstName == null && lastName != null) {
                return lastName;
            } else if (firstName != null && lastName == null) {
                return firstName;
            } else
                return "";
        }
        return this.firstName + " " + this.lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String[] getCoverArtPaths() {
        return coverArtPaths;
    }

    public void setCoverArtPaths(String[] coverArtPaths) {
        this.coverArtPaths = coverArtPaths;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(lastName);
        dest.writeString(firstName);
        dest.writeInt(coverArtPaths.length);
        dest.writeStringArray(coverArtPaths);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Creator<Person>() {

        @Override
        public Person[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Person[size];
        }

        @Override
        public Person createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Person(source);
        }
    };

    private Person(Parcel source) {
        id = source.readString();
        lastName = source.readString();
        firstName = source.readString();
        int length = source.readInt();
        coverArtPaths = new String[length];
        source.readStringArray(coverArtPaths);
    }

    public void setNameByFullName(String fullname) {
        String[] tokens = fullname.trim().split(" ");

        if (tokens.length > 1) {
            lastName = tokens[tokens.length - 1];

            firstName = fullname.substring(0, fullname.length() - lastName.length() - 1);
        } else if (tokens.length == 1) {
            firstName = tokens[0];
            lastName = "";
        }
    }
}
