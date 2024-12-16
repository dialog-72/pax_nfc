import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'pax_nfc_method_channel.dart';

abstract class PaxNfcPlatform extends PlatformInterface {
  /// Constructs a PaxNfcPlatform.
  PaxNfcPlatform() : super(token: _token);

  static final Object _token = Object();

  static PaxNfcPlatform _instance = MethodChannelPaxNfc();

  /// The default instance of [PaxNfcPlatform] to use.
  ///
  /// Defaults to [MethodChannelPaxNfc].
  static PaxNfcPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PaxNfcPlatform] when
  /// they register themselves.
  static set instance(PaxNfcPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> startNfcDetectionThreads() {
    throw UnimplementedError(
        'startNfcDetectionThreads() has not been implemented.');
  }

  Future<void> stopNfcDetectionThreads() {
    throw UnimplementedError(
        'stopNfcDetectionThreads() has not been implemented.');
  }

  Stream<String> listenNfcStream() {
    throw UnimplementedError('listenNfcStream() has not been implemented.');
  }
}
