import 'pax_nfc_platform_interface.dart';

class PaxNfc {
  Future<void> startNfcDetectionThreads() {
    return PaxNfcPlatform.instance.startNfcDetectionThreads();
  }

  Future<void> stopNfcDetectionThreads() {
    return PaxNfcPlatform.instance.stopNfcDetectionThreads();
  }

  Stream<String> listenNfcStream() {
    return PaxNfcPlatform.instance.listenNfcStream();
  }
}
