package com.yhp.distributedata;


import java.util.Locale;

public interface ILinkHomeCommonDataCallBack {

    //发送指令
    void sendRequest(String jsonObject);

    //获取当前语言
    Locale getCurrentLocale();

    //获取当前账号信息（手机号 || 邮箱）
    String getUserInfo();

}
