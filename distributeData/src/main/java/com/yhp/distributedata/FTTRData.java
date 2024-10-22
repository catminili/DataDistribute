package com.yhp.distributedata;

import com.alibaba.fastjson.JSONObject;


public class FTTRData {
    private String CmdType;
    private String IotCmdType;
    private JSONObject IotData;


    @Override
    public String toString() {
        return "FTTRData{" +
                "CmdType='" + CmdType + '\'' +
                ", IotCmdType='" + IotCmdType + '\'' +
                ", IotData=" + IotData +
                '}';
    }
}
