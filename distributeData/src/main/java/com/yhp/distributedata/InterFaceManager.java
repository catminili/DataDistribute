package com.yhp.distributedata;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterFaceManager {


    private String SESSION_TOKEN;

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

    public synchronized void registerInterface(String cmdType, IGetDataCallBack.GetDataCallBack callBack){
        List<FTTRData> dataList = new ArrayList<>();
        mData.put(cmdType,dataList);
        mListen.put(cmdType,callBack);
        mListen.get(cmdType).sessionTokenCallBack(SESSION_TOKEN);
    }

    public synchronized void sendData(String cmdType,FTTRData fttrData){
        if (!mData.keySet().contains(cmdType)){
            Log.d("InterFaceManager", "getData:  "+ cmdType +"UnRegister");
            return;
        }
        if (!mListen.keySet().contains(cmdType)){
            Log.d("InterFaceManager", "getData:  "+ cmdType +"UnRegister");
            return;
        }
        mListen.get(cmdType).dataCallBack(fttrData);
    }


}
