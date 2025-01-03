package com.dialog.pax_nfc;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import dalvik.system.PathClassLoader;
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

  public boolean isPaxLibraryAvailable(){
      // Chemin du fichier de la bibliothèque native
      String libraryPath = System.getProperty("java.library.path");; // Chemin valide pour Android
      String libraryName = "paxapijni"; // Nom sans préfixe 'lib' ni extension

      // Créer un PathClassLoader
      PathClassLoader classLoader = new PathClassLoader(libraryPath, ClassLoader.getSystemClassLoader());

      String c = classLoader.findLibrary(libraryName);

      if (c != null){
        Log.d("com.dialog.pax_nfc", "libpaxapijni.so loaded successfully !");
        return true;
      } else {
        Log.e("pax_nfc", "Error while loading the libpaxapijni.so library. This can mean you are not on a PAX device.");
        return false;
      }
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

    if (isPaxLibraryAvailable()) {
      channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "pax_nfc");
      channel.setMethodCallHandler(this);
      appContext = flutterPluginBinding.getApplicationContext();
      Detection.setUp(appContext);
      Detection.open();
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

        detectMThread = new DetectMThread(NfcCardInfoHandler.handler);
        detectABThread = new DetectABThread(NfcCardInfoHandler.handler);
        detectMThread.start();
        detectABThread.start();
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

  }


}
