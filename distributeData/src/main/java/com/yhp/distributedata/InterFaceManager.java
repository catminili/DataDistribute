package com.yhp.distributedata;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterFaceManager {

    private Map<String, List<FTTRData>> mData;
    private Map<String, IGetDataCallBack.GetDataCallBack> mListen;
    private InterFaceManager(){
        mData = new HashMap<>();
        mListen = new HashMap<>();
    }

    private static class InterFaceManagerInstance{
        private static final InterFaceManager INSTANCE = new InterFaceManager();
    }

    public static InterFaceManager getInstance(){
        return InterFaceManagerInstance.INSTANCE;
    }

    public synchronized void registerInterface(String cmdType, IGetDataCallBack.GetDataCallBack callBack){
        List<FTTRData> dataList = new ArrayList<>();
        mData.put(cmdType,dataList);
        mListen.put(cmdType,callBack);
    }

    public synchronized void callBack(String cmdType,FTTRData fttrData){
        if (!mData.keySet().contains(cmdType)){
            Log.d("InterFaceManager", "getData:  "+ cmdType +"UnRegister");
            return;
        }
        if (!mListen.keySet().contains(cmdType)){
            Log.d("InterFaceManager", "getData:  "+ cmdType +"UnRegister");
            return;
        }
        mListen.get(cmdType).getDataCallBack(fttrData);
    }

}
