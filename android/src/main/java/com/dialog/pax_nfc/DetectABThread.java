package com.dialog.pax_nfc;

import android.os.Handler;

import com.pax.dal.entity.EDetectMode;

class DetectABThread extends Thread {

    public static Handler handler;

    public DetectABThread(Handler h){
        handler = h;
    }

    @Override
    public void run() {
        super.run();

        while (!Thread.interrupted()) {
            int i = Detection.detectType(handler, EDetectMode.EMV_AB);
            if (i == 1) {
                break;
            }
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                // Log.i("ss", "exit thread" + getId());
                break;
            }
        }
    }
}