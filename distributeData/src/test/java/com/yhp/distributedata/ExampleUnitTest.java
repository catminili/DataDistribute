package com.yhp.distributedata;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void test() {
        FttrDataThread fttrDataThread = new FttrDataThread(new IAARDataCallBack<String>() {
            @Override
            public void callBackData(String data) {

            }

            @Override
            public String login() {
                return null;
            }

            @Override
            public void exception(StarFlashException var1) {

            }
        },new ConcurrentLinkedQueue<>());

        fttrDataThread.start();

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fttrDataThread.threadNotify();
        }
    }
}