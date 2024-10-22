package com.yhp.distributedata;


public interface IGetDataCallBack {

    void registerGetDataCallBack(String cmdType,GetDataCallBack callBack);
    interface GetDataCallBack {
        void dataCallBack(FTTRData data);

        void sessionTokenCallBack(String sessionToken);

    }
}
