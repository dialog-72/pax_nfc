package com.dialog.pax_nfc;

import android.os.Handler;
import android.util.Log;

import com.pax.dal.entity.EDetectMode;

class DetectABThread extends Thread {

    public static Handler handler;

    public DetectABThread(Handler h){
        handler = h;
    }

    @Override
    public void run() {
        Log.e("NFC_THREAD", "super run");
        super.run();

        try {
            while (!Thread.interrupted()) {
                Log.e("NFC_THREAD", "detect");

                int i = Detection.detectType(handler, EDetectMode.EMV_AB);
                if (i == 1) {
                    Log.e("NFC_THREAD", "break");

                    break;
                }
                try {
                    Log.e("NFC_THREAD", "sleep");

                    sleep(2000);
                } catch (InterruptedException e) {
                    // Log.i("ss", "exit thread" + getId());
                    break;
                }
            }
        } catch (Exception e) {
            Log.e("NFC_THREAD", "Thread AB failed", e);
        }
    }
}