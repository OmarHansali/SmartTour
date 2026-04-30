import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

import 'app_container.dart';
import 'ui/map/map_screen.dart';
import 'ui/map/map_view_model.dart';

void main() {
  runApp(const SmartTourApp());
}

/// Root widget.  Initialises the DI container once and makes it available
/// to the entire widget tree.
class SmartTourApp extends StatelessWidget {
  const SmartTourApp({super.key});

  @override
  Widget build(BuildContext context) {
    final container = AppContainer();

    return MaterialApp(
      title: 'SmartTour',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: const Color(0xFF1976D2), // matches Kotlin primary
        brightness: Brightness.light,
        appBarTheme: const AppBarTheme(
          centerTitle: false,
          elevation: 0,
        ),
        cardTheme: CardTheme(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(12),
          ),
          elevation: 2,
        ),
        filledButtonTheme: FilledButtonThemeData(
          style: FilledButton.styleFrom(
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8),
            ),
          ),
        ),
      ),
      // ── Demo: navigate straight to the map page ─────────────────────────
      // In the full 5-page app, replace this with a proper Navigator/Router
      // that hands a real tourId from the home/tour-list screen.
      home: ChangeNotifierProvider(
        create: (_) => container.makeMapViewModel(),
        child: const MapScreen(tourId: 'demo-tour-1'),
      ),
    );
  }
}
