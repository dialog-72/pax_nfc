package com.dialog.pax_nfc;

import android.os.Handler;

import com.pax.dal.entity.EDetectMode;

public class DetectMThread extends Thread {

    public static Handler handler;

    public DetectMThread(Handler h){
        handler = h;
    }

    @Override
    public void run() {
        super.run();

        while (!Thread.interrupted()) {

            int blockNum = 4;
            String password = "FFFFFFFFFFFF";
            int i = Detection.detectType(handler, EDetectMode.ONLY_M);


            if (i == 1) {
                break;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                // e.printStackTrace();
                break;
            }

        }

    }

}