package com.example.project_test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Information implements Parcelable {
    String name;
    String des;
    String phn;
    String email;
    Double latitude;
    Double longitude;
    String addressline;
    Float dist;
    String image;
    int year;
    int month;
    int day;

    public Information() {
    }

    public Information(String name, String des, String phn, String email, Double latitude, Double longitude, String addressline, Float dist, String image, int year, int month, int day) {
        this.name = name;
        this.des = des;
        this.phn = phn;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressline = addressline;
        this.dist = dist;
        this.image = image;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    protected Information(Parcel in) {
        name = in.readString();
        des = in.readString();
        phn = in.readString();
        email = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        addressline = in.readString();
        if (in.readByte() == 0) {
            dist = null;
        } else {
            dist = in.readFloat();
        }
        image = in.readString();
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
    }

    public static final Creator<Information> CREATOR = new Creator<Information>() {
        @Override
        public Information createFromParcel(Parcel in) {
            return new Information(in);
        }

        @Override
        public Information[] newArray(int size) {
            return new Information[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddressline() {
        return addressline;
    }

    public void setAddressline(String addressline) {
        this.addressline = addressline;
    }

    public Float getDist() {
        return dist;
    }

    public void setDist(Float dist) {
        this.dist = dist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(des);
        parcel.writeString(phn);
        parcel.writeString(email);
        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
        parcel.writeString(addressline);
        if (dist == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeFloat(dist);
        }
        parcel.writeString(image);
        parcel.writeInt(year);
        parcel.writeInt(month);
        parcel.writeInt(day);
    }
}
