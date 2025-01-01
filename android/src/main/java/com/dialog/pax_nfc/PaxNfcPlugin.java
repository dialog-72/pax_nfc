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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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

  private static final String CHANNEL = "com.dialog.pax_nfc";
  // Vérifie si la bibliothèque est disponible sur l'appareil
  private boolean isPaxLibraryAvailable() {
    try {
      // Tentative de chargement pour vérifier si elle existe
      System.loadLibrary("libpaxapijni.so");
      return true;
    } catch (UnsatisfiedLinkError e) {
      return false; // Bibliothèque introuvable
    }
  }
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
    channel.setMethodCallHandler(this);
    appContext = flutterPluginBinding.getApplicationContext();
    if(isPaxLibraryAvailable()){
      new Thread(new Runnable() {
        @Override
        public void run() {
          // Offload NFC setup to background thread
          Detection.setUp(appContext);
          Detection.open();

        }
      }).start();

      BinaryMessenger binaryMessenger = flutterPluginBinding.getBinaryMessenger();
      NfcCardInfoHandler nfcCardInfoHandler = new NfcCardInfoHandler();
      new EventChannel(binaryMessenger, "nfc_event_channel").setStreamHandler(nfcCardInfoHandler);
    }

  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method){
      case "getPlatformVersion":
        result.success("Android " + android.os.Build.VERSION.RELEASE);
        break;
      case "startNfcDetectionThreads":

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
          @Override
          public void run() {
            Log.d("NFC", "Starting detection threads...");
            try {

            detectMThread = new DetectMThread(NfcCardInfoHandler.handler);
            detectABThread = new DetectABThread(NfcCardInfoHandler.handler);
            detectMThread.start();
            detectABThread.start();
            } catch (Exception e){
              Log.e("NFC", "Error starting threads", e);
            } finally {
              executorService.shutdown();
            }
          }
        });
        break;

      case "stopNfcDetectionThreads":
        detectMThread.interrupt();
        detectABThread.interrupt();
        break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    Detection.close();
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
    if (appContext != null) {
      appContext = null;
    }
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

  }


}
