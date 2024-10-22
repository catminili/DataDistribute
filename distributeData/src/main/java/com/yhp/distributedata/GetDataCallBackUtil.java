package com.yhp.distributedata;


public class GetDataCallBackUtil {
    private GetDataCallBackUtil(){
        mGetDataCallBack = new GetDataCallBackImp();
    }

    private static class GetDataCallBackUtilInstance{
        private static final GetDataCallBackUtil INSTANCE = new GetDataCallBackUtil();
    }

    public static GetDataCallBackUtil getInstance(){
        return GetDataCallBackUtilInstance.INSTANCE;
    }

    private IGetDataCallBack mGetDataCallBack;


    public  IGetDataCallBack getGetDataCallBack(){
        return mGetDataCallBack;
    }
}
