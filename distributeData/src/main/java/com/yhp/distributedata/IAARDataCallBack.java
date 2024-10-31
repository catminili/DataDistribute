package com.yhp.distributedata;

public interface IAARDataCallBack<T> {

    void callBackData(T data);

    void login(String userInfo);

    void exception(StarFlashException var1);
}
