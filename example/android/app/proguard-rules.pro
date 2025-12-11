# Flutter's default rules are included automatically.

# Rule for your Flutter plugin. This prevents the removal of the
# native classes that communicate with your Dart code.
# IMPORTANT: Replace "com.dialog.pax_nfc" with the actual package name of your plugin's Java/Kotlin code if it's different.
-keep class com.dialog.pax_nfc.** { *; }

# Rules for the PAX Neptune Lite API. This is crucial as the build
# process might see this code as "unused" and remove it,
# causing crashes in release mode.
-keep class com.pax.** { *; }

# Keep any dynamically instantiated classes, like your MainActivity.
-keep class com.dialog.pax_nfc_example.MainActivity { *; }

# Keep classes that are used in native methods (JNI).
# This is often necessary for payment SDKs.
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep class members that are accessed from native code.
-keepclassmembers class * {
    native <methods>;
}
