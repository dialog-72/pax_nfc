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

  /// starts the detection threads
  @override
  Future<void> startNfcDetectionThreads() async {
    await methodChannel.invokeMethod<String>('startNfcDetectionThreads');
  }

  /// stops the detection threads
  @override
  Future<void> stopNfcDetectionThreads() async {
    await methodChannel.invokeMethod<String>('stopNfcDetectionThreads');
  }

  /// starts listening to the nfc stream
  @override
  Stream<String> listenNfcStream() {
    startNfcDetectionThreads();
    const eventChannel = EventChannel('nfc_event_channel');
    final eventStream = eventChannel.receiveBroadcastStream();
    return eventStream.map((event) {
      /// stopping and starting because once a thread has detected a card, it cannot be reused and has to be intialized again
      stopNfcDetectionThreads();
      startNfcDetectionThreads();
      return event.toString();
    });
  }
}
