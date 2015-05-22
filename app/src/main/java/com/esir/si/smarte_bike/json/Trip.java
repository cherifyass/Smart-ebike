package com.esir.si.smarte_bike.json;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {
    private String origin;
    private String destination;
    private String date;
    private String distance;

    public Trip(String origin, String destination, String date, String distance) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.distance = distance;
    }

    public Trip(Parcel source){
        origin = source.readString();
        destination = source.readString();
        date = source.readString();
        distance = source.readString();
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(origin);
        dest.writeString(destination);
        dest.writeString(date);
        dest.writeString(distance);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

}

