package com.yhp.distributedata;

import android.util.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FttrDataThread extends Thread{

    private ConcurrentLinkedQueue<String> mData;

    private IAARDataCallBack<String> mDataCallBack;

//    private Object lock = new Object();
    public FttrDataThread(IAARDataCallBack<String> mDataCallBack,ConcurrentLinkedQueue<String> mData){
        this.mData = mData;
        this.mDataCallBack = mDataCallBack;
    }

    private boolean isStopped;

    @Override
    public void run() {
        try {
            //监听消息接收
            while (true && !isStopped) {
//                Log.d("test", "run: "+ "aaaaa");
                synchronized (FttrDataThread.this) {
//                    System.out.println("aaaa");
                    while (!mData.isEmpty()) {
                        mDataCallBack.callBackData(mData.poll());
                        sleep(10);
                    }
                    wait();
//                wait();
//                Log.d("test", "run: "+ "bbbbb");
//                    System.out.println("bbbb");
                }
            }
        }catch (Exception e){
            //异常停止
            isStopped = true;
            e.printStackTrace();
        }
    }

    public boolean isThreadWaiting() {
        return getState() == Thread.State.WAITING || getState() == Thread.State.TIMED_WAITING;
    }

    public boolean isStopped(){
        return isStopped;
    }

    public void addData(String data){
        synchronized (this){
            mData.offer(data);
        }
    }

    public void threadNotify(){
        synchronized (this){
            notify();
        }
    }


}
