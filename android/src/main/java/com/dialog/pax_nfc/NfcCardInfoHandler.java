package com.dialog.pax_nfc;

import android.os.Handler;

import io.flutter.plugin.common.EventChannel;

public class NfcCardInfoHandler implements EventChannel.StreamHandler {

    private static EventChannel.EventSink sink = null;

    public static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    if(sink != null){
                        sink.success(msg.obj);
                    }
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        sink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        sink = null;
    }
}
