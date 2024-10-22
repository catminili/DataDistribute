package com.yhp.distributedata;


public interface IGetDataCallBack {

    void registerGetDataCallBack(String cmdType,GetDataCallBack callBack);
    interface GetDataCallBack {
        void getDataCallBack(FTTRData data);

        void getSessionToken(String sessionToken);

    }
}
