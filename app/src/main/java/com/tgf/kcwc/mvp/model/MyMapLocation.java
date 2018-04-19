package com.tgf.kcwc.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.location.AMapLocation;

/**
 * Author:Jenny
 * Date:2017/7/24
 * E-mail:fishloveqin@gmail.com
 */

public class MyMapLocation implements Parcelable {

    public AMapLocation mapLocation;

    public double       mTotalMile    = 0; //总里程
    public int          mTimeCounter  = 0; //时间
    public double       mCucrentMile;
    public double       mTempMaxSpeed = 0;

    public int          mRideId;
    public double       speed;
    public RideData mRideData;
    public MyMapLocation(AMapLocation mapLocation, double mTotalMile, int mTimeCounter,
                         double mCucrentMile, double mTempMaxSpeed, double speed, int rideId) {
        this.mapLocation = mapLocation;
        this.mTotalMile = mTotalMile;
        this.mTimeCounter = mTimeCounter;
        this.mCucrentMile = mCucrentMile;
        this.mTempMaxSpeed = mTempMaxSpeed;
        this.speed = speed;
        this.mRideId = rideId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mapLocation, flags);
        dest.writeDouble(this.mTotalMile);
        dest.writeInt(this.mTimeCounter);
        dest.writeDouble(this.mCucrentMile);
        dest.writeDouble(this.mTempMaxSpeed);
        dest.writeInt(this.mRideId);
        dest.writeDouble(this.speed);
    }

    protected MyMapLocation(Parcel in) {
        this.mapLocation = in.readParcelable(AMapLocation.class.getClassLoader());
        this.mTotalMile = in.readDouble();
        this.mTimeCounter = in.readInt();
        this.mCucrentMile = in.readDouble();
        this.mTempMaxSpeed = in.readDouble();
        this.mRideId = in.readInt();
        this.speed = in.readDouble();
    }

    public static final Creator<MyMapLocation> CREATOR = new Creator<MyMapLocation>() {
        @Override
        public MyMapLocation createFromParcel(Parcel source) {
            return new MyMapLocation(source);
        }

        @Override
        public MyMapLocation[] newArray(int size) {
            return new MyMapLocation[size];
        }
    };
}
