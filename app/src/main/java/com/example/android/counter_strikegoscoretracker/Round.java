package com.example.android.counter_strikegoscoretracker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dts on 25/03/17.
 */

/*public class Round {

    private String mRoundWinner;
    private String mWinType;

    public Round(String roundWinner, String winType) {
        mRoundWinner = roundWinner;
        mWinType = winType;
    }

    public String getRoundWinner() {
        return mRoundWinner;
    }

    public String getWinType() {
        return mWinType;
    }
} */

public class Round implements Parcelable {
    String mRoundWinner;
    String mWinType;

    public Round(String roundWinner, String winType) {
        this.mRoundWinner = roundWinner;
        this.mWinType = winType;
    }

    private Round(Parcel in) {
        mRoundWinner = in.readString();
        mWinType = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return mWinType + ": " + mRoundWinner;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mRoundWinner);
        out.writeString(mWinType);
    }

    public static final Parcelable.Creator<Round> CREATOR = new Parcelable.Creator<Round>() {
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        public Round[] newArray(int size) {
            return new Round[size];
        }
    };

    public String getRoundWinner() {
        return mRoundWinner;
    }

    public String getWinType() {
        return mWinType;
    }
}