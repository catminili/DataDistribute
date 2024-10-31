package com.yhp.distributedata;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DistributeDataManager {


    private ILinkHomeCommonDataCallBack linkHomeCommonDataCallBack;

    //全局会话session
    private String SESSION_TOKEN;


    //每个aar自己注册回调
    private Map<String,IAARDataCallBack> mDataCallBackMap;
    //注册的aar get数据的定时任务
    //aar自己定时Get数据，只分发接收到的数据数据
//    private Map<String, Disposable> mGetDataTaskMap;


    private Map<String,FttrDataThread> mReceiveDataThreadMap;

    private Map<String, ConcurrentLinkedQueue<String>> mReceiveDataMap;

    //不存在同时操作不同aar情况
    //防止aar内部加锁阻塞
    private Map<String,Object> mReceiveDataLockMap;

    private Object receiveDataLock;

    private Object sendDataLock;

    private Object loginLock;

    private Object mErrorLock;
    private DistributeDataManager(){
//        mData = new HashMap<>();
        mDataCallBackMap = new HashMap<>();
//        mGetDataTaskMap = new HashMap<>();
        mReceiveDataMap = new HashMap<>();
        mReceiveDataThreadMap = new HashMap<>();
        mReceiveDataLockMap = new HashMap<>();

        receiveDataLock = new Object();
        sendDataLock = new Object();
        loginLock = new Object();
        mErrorLock = new Object();
    }

    private static class InterFaceManagerInstance{
        private static final DistributeDataManager INSTANCE = new DistributeDataManager();
    }

    public static DistributeDataManager getInstance(){
        return InterFaceManagerInstance.INSTANCE;
    }


    //获取本地语言
    public Locale getCurrentLocale () {
        if (linkHomeCommonDataCallBack != null){
          return linkHomeCommonDataCallBack.getCurrentLocale();
        }
        return Locale.SIMPLIFIED_CHINESE;
    }

    //获取用户信息
    public String getUserInfo(){
        if (linkHomeCommonDataCallBack != null){
            return linkHomeCommonDataCallBack.getUserInfo();
        }
        return "";
    }

    //获取会话session
    public String getSessionToken(Context context) {
        initSessionToken(context);
        return SESSION_TOKEN;
    }

    public void initSessionToken(Context context){
        synchronized (this) {
            SESSION_TOKEN = (String) SPUtil.get(context, "sessionToken", "");
            if (SESSION_TOKEN.isEmpty()) {
//            //去外部存储找
//            FileInputStream fis = null;
//            BufferedReader reader = null;
//            FileOutputStream fos = null;
//            OutputStreamWriter osw = null;
//            try {
//                File tokenFile = new File(Environment.getExternalStorageDirectory(),"/Download/jedver/linkhome/sessionToken.txt");
//                //外部存储存在  直接取
//                if (tokenFile.exists()){
//                    fis = new FileInputStream(tokenFile);
//                    reader = new BufferedReader(new InputStreamReader(fis));
//                    String line;
//                    while ((line = reader.readLine()) != null){
//                        String[] parts = line.split("=");
//                        if (parts.length == 2) {
//                            SPUtil.put(application,parts[0], parts[1]);
//                        }
//                    }
//                }
//                //外部存储不存在 重新分配并存入外部存储
//                else {
//                    if (!tokenFile.getParentFile().exists()){
//                        tokenFile.getParentFile().mkdirs();
//                    }
//                    SESSION_TOKEN  = (int) (Math.random() * Integer.MAX_VALUE) + "";
//                    //存入外部存储
//                    //todo
//                    fos = new FileOutputStream(tokenFile);
//                    osw = new OutputStreamWriter(fos);
//                    osw.write("sessionToken" + "=" + SESSION_TOKEN + "\n");
//                }
//                osw.flush();
//            }catch (IOException e){
//                e.printStackTrace();
//            }finally {
//                try {
//                    if (reader != null) {
//                        reader.close();
//                    }
//                    if (fis != null){
//                        fis.close();
//                    }
//                    if (osw != null) {
//                        osw.close();
//                    }
//                    if (fos != null){
//                        fos.close();
//                    }
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }

//            DEVICE_UUID = UUID.randomUUID().toString();
                SESSION_TOKEN = (int) (Math.random() * Integer.MAX_VALUE) + "";
                SPUtil.put(context, "sessionToken", SESSION_TOKEN);
            }
        }
    }

    public  void registerInterface(String iotCmdType, IAARDataCallBack callBack){
        synchronized (this) {
//        List<FTTRData> dataList = new ArrayList<>();
//        mData.put(iotCmdType,dataList);
            mDataCallBackMap.put(iotCmdType, callBack);
//            mCallBack.put(iotCmdType).sessionTokenCallBack(SESSION_TOKEN);
            mReceiveDataMap.put(iotCmdType,new ConcurrentLinkedQueue<>());
            mReceiveDataThreadMap.put(iotCmdType,new FttrDataThread(mDataCallBackMap.get(iotCmdType),mReceiveDataMap.get(iotCmdType)));
            mReceiveDataLockMap.put(iotCmdType,new Object());
//            registerGetDataTask(iotCmdType);
        }
    }

//    protected void registerGetDataTask(String iotCmdType,String getData,String getCmd){
//        Observable.interval(1000, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.from(mTaskExecutor))
//                .observeOn(Schedulers.from(mTaskExecutor))
//                .subscribe(aLong -> {
//                    //
//                    FTTRSocket.getInstance().socketSendData(getData, getCmd);
//                });
//    }

    //数据的回调由linkhome去分发
    //数据接收
    public void receiveData(final String data){
        synchronized (receiveDataLock){
            JSONObject fttrData = JSONObject.parseObject(data);
            String iotCmdType = fttrData.getString(Config.IOT_CMD_TYPE);
            //防止异常数据
            if (!mDataCallBackMap.containsKey(iotCmdType)){
                Log.d("InterFaceManager", "receiveData: "+" aar未注册回调");
                return;
            }
            //加一个线程正常工作检测，防止线程异常挂掉
            if (mReceiveDataThreadMap.get(iotCmdType).isStopped()){
                //线程挂断,重启线程
                FttrDataThread oldFttrDataThread = mReceiveDataThreadMap.get(iotCmdType);
                //清除旧的线程
                oldFttrDataThread = null;
                //重启启动任务
                FttrDataThread fttrDataThread = new FttrDataThread(mDataCallBackMap.get(iotCmdType) ,mReceiveDataMap.get(iotCmdType));
                mReceiveDataThreadMap.put(iotCmdType,fttrDataThread);
                mReceiveDataThreadMap.get(iotCmdType).addData(data);
                fttrDataThread.start();
            }else if (mReceiveDataThreadMap.get(iotCmdType).isThreadWaiting()){
                //线程休眠，唤醒线程。
                mReceiveDataThreadMap.get(iotCmdType).addData(data);
                mReceiveDataThreadMap.get(iotCmdType).threadNotify();
            }
        }
    }

    //数据发送
    public void sendData(final String data){
        synchronized (sendDataLock) {
            if (linkHomeCommonDataCallBack != null) {
                linkHomeCommonDataCallBack.sendRequest(data);
                Log.d("InterFaceManager", "sendData: " + data);
            } else {
                Log.d("InterFaceManager", "sendData: " + "ILinkHomeCommonDataCallBack  接口未实现或不存在");
            }
        }
    }

    //登录
    public void login(String userInfo){
        synchronized (loginLock) {
            for (IAARDataCallBack dataCallBack : mDataCallBackMap.values()) {
                dataCallBack.login(userInfo);
            }
        }
    }


    //异常
    public void error(String iotCmdType,StarFlashException exception){
        synchronized (mErrorLock){
            IAARDataCallBack dataCallBack = mDataCallBackMap.get(iotCmdType);
            if (dataCallBack == null){
                Log.d("InterFaceManager", "error: " + "ILinkHomeCommonDataCallBack  接口未实现或不存在");
                return;
            }
            dataCallBack.exception(exception);
        }
    }
//    public void sendData(final String iotCmdType){
////        if (!mData.keySet().contains(iotCmdType)){
////            Log.d("InterFaceManager", "getData:  "+ iotCmdType +"UnRegister");
////            return;
////        }
//        mTaskExecutor.execute(()->{
//            synchronized (mLock.get(iotCmdType)){
//                if (!mCallBack.keySet().contains(iotCmdType)){
//                    Log.d("InterFaceManager", "getData:  "+ iotCmdType +"UnRegister");
//                    return;
//                }
//                mCallBack.get(iotCmdType).dataCallBack(fttrData);
//            }
//        });
//    }


}
