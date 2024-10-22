package com.yhp.distributedata;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;


public class FTTRData implements Parcelable {
    private String CmdType;
    private String IotCmdType;
    private JSONObject IotData;


    public String getCmdType() {
        return CmdType;
    }

    public void setCmdType(String cmdType) {
        CmdType = cmdType;
    }

    public String getIotCmdType() {
        return IotCmdType;
    }

    public void setIotCmdType(String iotCmdType) {
        IotCmdType = iotCmdType;
    }

    public JSONObject getIotData() {
        return IotData;
    }

    public void setIotData(JSONObject iotData) {
        IotData = iotData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.CmdType);
        dest.writeString(this.IotCmdType);
        dest.writeSerializable(this.IotData);
    }

    public FTTRData() {
    }

    protected FTTRData(Parcel in) {
        this.CmdType = in.readString();
        this.IotCmdType = in.readString();
        this.IotData = (JSONObject) in.readSerializable();
    }

    public static final Parcelable.Creator<FTTRData> CREATOR = new Parcelable.Creator<FTTRData>() {
        @Override
        public FTTRData createFromParcel(Parcel source) {
            return new FTTRData(source);
        }

        @Override
        public FTTRData[] newArray(int size) {
            return new FTTRData[size];
        }
    };
}
