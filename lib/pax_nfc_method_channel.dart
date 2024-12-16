import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'pax_nfc_platform_interface.dart';

/// An implementation of [PaxNfcPlatform] that uses method channels.
class MethodChannelPaxNfc extends PaxNfcPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('pax_nfc');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> startNfcDetectionThreads() async {
    await methodChannel.invokeMethod<String>('startNfcDetectionThreads');
  }

  @override
  Future<void> stopNfcDetectionThreads() async {
    await methodChannel.invokeMethod<String>('stopNfcDetectionThreads');
  }

  @override
  Stream<String> listenNfcStream() {
    startNfcDetectionThreads();
    const eventChannel = EventChannel('nfc_event_channel');
    final eventStream = eventChannel.receiveBroadcastStream();
    return eventStream.map((event) {
      stopNfcDetectionThreads();
      return event.toString();
    });
  }
}