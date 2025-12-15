import 'package:flutter/material.dart';
import 'package:pax_nfc/pax_nfc.dart';
import 'package:shared_preferences/shared_preferences.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _paxNfcPlugin = PaxNfc();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            StreamBuilder(
              stream: _paxNfcPlugin.listenNfcStream(),
              builder: (context, snapshot) {
                if (snapshot.hasData) {
                  return Text('${snapshot.data}');
                } else {
                  return const CircularProgressIndicator();
                }
              },
            ),
            Row(
              children: [
                ElevatedButton(
                  onPressed: () => _paxNfcPlugin.startNfcDetectionThreads(),
                  child: const Text("démarrer les threads"),
                ),
                ElevatedButton(
                  onPressed: () => _paxNfcPlugin.stopNfcDetectionThreads(),
                  child: const Text("arrêter les threads"),
                )
              ],
            ),
          ],
        ),
      ),
    );
  }
}
