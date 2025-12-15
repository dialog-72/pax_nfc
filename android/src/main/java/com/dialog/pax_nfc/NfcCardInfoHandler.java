package com.dialog.pax_nfc;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import io.flutter.plugin.common.EventChannel;

public class NfcCardInfoHandler implements EventChannel.StreamHandler {
    private EventChannel.EventSink eventSink;
    private final Handler handler;

    NfcCardInfoHandler() {
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (eventSink != null) {
                    // Assuming the message object (msg.obj) is a String or another serializable type.
                    // Adjust if you are sending a different data type.
                    eventSink.success(msg.obj);
                }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        // This method is called when the Flutter side starts listening.
        // We save the EventSink to be able to send events to Flutter.
        this.eventSink = events;
    }

    @Override
    public void onCancel(Object arguments) {
        // This method is called when the Flutter side stops listening.
        // We clear the EventSink to prevent memory leaks.
        this.eventSink = null;
    }
}
