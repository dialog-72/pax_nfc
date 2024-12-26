package com.dialog.pax_nfc;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import android.content.Context;
import com.pax.dal.IDAL;
import com.pax.dal.IPicc;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EM1KeyType;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.PiccCardInfo;
import com.pax.dal.exceptions.PiccDevException;
import com.pax.neptunelite.api.NeptuneLiteUser;


/** PaxNfcPlugin */
public class PaxNfcPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  public static Context appContext;

  public DetectMThread detectMThread;
  public DetectABThread detectABThread;

  public static  String detectionResponse;

  private EventChannel eventChannel;

  private EventChannel.EventSink eventSink;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "pax_nfc");
    channel.setMethodCallHandler(this);
    appContext = flutterPluginBinding.getApplicationContext();
    Detection.setUp(appContext);

    BinaryMessenger binaryMessenger = flutterPluginBinding.getBinaryMessenger();
    NfcCardInfoHandler nfcCardInfoHandler = new NfcCardInfoHandler();
    new EventChannel(binaryMessenger, "nfc_event_channel").setStreamHandler(nfcCardInfoHandler);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method){
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "startNfcDetectionThreads":
        Detection.open();
        detectMThread = new DetectMThread(NfcCardInfoHandler.handler);
        detectABThread = new DetectABThread(NfcCardInfoHandler.handler);
        detectMThread.start();
        detectABThread.start();
        break;

      case "stopNfcDetectionThreads":
        detectMThread.interrupt();
        detectABThread.interrupt();
        Detection.close();
        break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    if (detectMThread != null){
      detectMThread.interrupt();
      detectMThread = null;
    }

    if (detectABThread != null){
      detectABThread.interrupt();
      detectABThread = null;
    }

//    Detection.tearDown();

  }


}
