import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:latlong2/latlong.dart';
import 'package:provider/provider.dart';

import '../../domain/model/tour_route.dart';
import '../../domain/model/tour_stop.dart';
import 'components/map_top_bar.dart';
import 'components/next_stop_card.dart';
import 'components/tour_map_legend.dart';
import 'map_ui_state.dart';
import 'map_view_model.dart';

/// The full-screen map page for an active SmartTour.
///
/// Responsibilities:
/// - Load the tour route from the Kotlin backend via [MapViewModel].
/// - Render an OpenStreetMap tile layer (no API key needed).
/// - Draw a polyline connecting all stops.
/// - Show colour-coded markers: primary = current, green = visited, grey = upcoming.
/// - Display a legend overlay and a bottom card with stop info + progress.
/// - Allow the user to mark the current stop as visited (optimistic update).
class MapScreen extends StatefulWidget {
  const MapScreen({
    super.key,
    required this.tourId,
    this.onBackClick,
  });

  final String tourId;
  final VoidCallback? onBackClick;

  @override
  State<MapScreen> createState() => _MapScreenState();
}

class _MapScreenState extends State<MapScreen> {
  @override
  void initState() {
    super.initState();
    // Load route after the first frame so the context is fully mounted.
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<MapViewModel>().loadRoute(widget.tourId);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<MapViewModel>(
      builder: (context, viewModel, _) {
        final state = viewModel.state;

        return switch (state) {
          MapLoadingState() => const Scaffold(
              body: Center(child: CircularProgressIndicator()),
            ),
          MapErrorState(:final message) => Scaffold(
              body: Center(
                child: Padding(
                  padding: const EdgeInsets.all(24),
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      Icon(Icons.error_outline,
                          size: 48,
                          color: Theme.of(context).colorScheme.error),
                      const SizedBox(height: 12),
                      Text(
                        message,
                        textAlign: TextAlign.center,
                        style: TextStyle(
                            color: Theme.of(context).colorScheme.error),
                      ),
                      const SizedBox(height: 16),
                      FilledButton.icon(
                        onPressed: () =>
                            viewModel.loadRoute(widget.tourId),
                        icon: const Icon(Icons.refresh),
                        label: const Text('Retry'),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          MapSuccessState(:final route) => _MapContent(
              route: route,
              tourId: widget.tourId,
              onBackClick: widget.onBackClick ?? () => Navigator.maybePop(context),
              onMarkVisited: (stopId) =>
                  viewModel.markCurrentStopVisited(widget.tourId, stopId),
            ),
        };
      },
    );
  }
}

// ---------------------------------------------------------------------------
// Private composable – only rendered when data is available
// ---------------------------------------------------------------------------

class _MapContent extends StatefulWidget {
  const _MapContent({
    required this.route,
    required this.tourId,
    required this.onBackClick,
    required this.onMarkVisited,
  });

  final TourRoute route;
  final String tourId;
  final VoidCallback onBackClick;
  final void Function(String stopId) onMarkVisited;

  @override
  State<_MapContent> createState() => _MapContentState();
}

class _MapContentState extends State<_MapContent> {
  late final MapController _mapController;

  @override
  void initState() {
    super.initState();
    _mapController = MapController();
  }

  @override
  void didUpdateWidget(_MapContent oldWidget) {
    super.didUpdateWidget(oldWidget);
    // Re-centre when the current stop changes (e.g. after marking visited).
    if (oldWidget.route.currentStopIndex != widget.route.currentStopIndex) {
      final center = _centerPoint(widget.route);
      _mapController.move(center, 15);
    }
  }

  @override
  void dispose() {
    _mapController.dispose();
    super.dispose();
  }

  static LatLng _centerPoint(TourRoute route) {
    final stop = route.stops.firstWhere(
      (s) => !s.isVisited,
      orElse: () => route.stops.first,
    );
    return LatLng(stop.latitude, stop.longitude);
  }

  @override
  Widget build(BuildContext context) {
    final route = widget.route;
    final colorScheme = Theme.of(context).colorScheme;

    // Build route polyline points
    final polylinePoints = route.stops
        .map((s) => LatLng(s.latitude, s.longitude))
        .toList();

    // Build markers
    final markers = route.stops.map((stop) {
      return _buildMarker(stop, colorScheme, route);
    }).toList();

    final center = _centerPoint(route);

    return Scaffold(
      appBar: MapTopBar(
        tourName: route.tourName,
        onBackClick: widget.onBackClick,
      ),
      body: Stack(
        children: [
          // ── Map ────────────────────────────────────────────────────────
          FlutterMap(
            mapController: _mapController,
            options: MapOptions(
              initialCenter: center,
              initialZoom: 15,
              interactionOptions: const InteractionOptions(
                flags: InteractiveFlag.all,
              ),
            ),
            children: [
              // OSM tile layer – no API key required
              TileLayer(
                urlTemplate:
                    'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
                userAgentPackageName: 'com.smarttour.flutter',
                maxNativeZoom: 19,
              ),

              // Route polyline
              if (polylinePoints.length > 1)
                PolylineLayer(
                  polylines: [
                    Polyline(
                      points: polylinePoints,
                      strokeWidth: 4,
                      color: colorScheme.primary.withOpacity(0.7),
                    ),
                  ],
                ),

              // Stop markers
              MarkerLayer(markers: markers),
            ],
          ),

          // ── Legend (top-left) ──────────────────────────────────────────
          const Positioned(
            top: 8,
            left: 8,
            child: TourMapLegend(),
          ),

          // ── Bottom info card ───────────────────────────────────────────
          Positioned(
            left: 0,
            right: 0,
            bottom: 0,
            child: NextStopCard(
              currentStop: route.currentStop,
              nextStop: route.nextStop,
              progressFraction: route.progressFraction,
              onMarkVisited: () {
                final stopId = route.currentStop?.id;
                if (stopId != null) widget.onMarkVisited(stopId);
              },
            ),
          ),
        ],
      ),
    );
  }

  /// Builds a [Marker] widget for [stop], using colour to indicate state.
  Marker _buildMarker(
      TourStop stop, ColorScheme colorScheme, TourRoute route) {
    final bool isCurrent = route.currentStop?.id == stop.id;
    final Color markerColor = isCurrent
        ? colorScheme.primary
        : stop.isVisited
            ? const Color(0xFF4CAF50)
            : Colors.grey;

    return Marker(
      point: LatLng(stop.latitude, stop.longitude),
      width: 36,
      height: 36,
      child: GestureDetector(
        onTap: () => _showStopInfo(context, stop, isCurrent, markerColor),
        child: _MarkerIcon(color: markerColor, isCurrent: isCurrent),
      ),
    );
  }

  void _showStopInfo(
      BuildContext context, TourStop stop, bool isCurrent, Color color) {
    showModalBottomSheet<void>(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (_) => _StopInfoSheet(
        stop: stop,
        isCurrent: isCurrent,
        color: color,
        onMarkVisited: isCurrent && !stop.isVisited
            ? () {
                Navigator.pop(context);
                widget.onMarkVisited(stop.id);
              }
            : null,
      ),
    );
  }
}

// ---------------------------------------------------------------------------
// Marker icon widget
// ---------------------------------------------------------------------------

class _MarkerIcon extends StatelessWidget {
  const _MarkerIcon({required this.color, required this.isCurrent});

  final Color color;
  final bool isCurrent;

  @override
  Widget build(BuildContext context) {
    return Stack(
      alignment: Alignment.center,
      children: [
        Icon(Icons.location_on, color: color, size: 36),
        if (isCurrent)
          Positioned(
            top: 6,
            child: Container(
              width: 10,
              height: 10,
              decoration: const BoxDecoration(
                color: Colors.white,
                shape: BoxShape.circle,
              ),
            ),
          ),
      ],
    );
  }
}

// ---------------------------------------------------------------------------
// Stop info bottom sheet
// ---------------------------------------------------------------------------

class _StopInfoSheet extends StatelessWidget {
  const _StopInfoSheet({
    required this.stop,
    required this.isCurrent,
    required this.color,
    this.onMarkVisited,
  });

  final TourStop stop;
  final bool isCurrent;
  final Color color;
  final VoidCallback? onMarkVisited;

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;
    final colorScheme = Theme.of(context).colorScheme;

    return Padding(
      padding: const EdgeInsets.fromLTRB(20, 16, 20, 32),
      child: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Handle
          Center(
            child: Container(
              width: 40,
              height: 4,
              decoration: BoxDecoration(
                color: colorScheme.onSurfaceVariant.withOpacity(0.3),
                borderRadius: BorderRadius.circular(2),
              ),
            ),
          ),
          const SizedBox(height: 16),

          // Stop header
          Row(
            children: [
              Icon(Icons.location_on, color: color, size: 22),
              const SizedBox(width: 8),
              Expanded(
                child: Text(
                  stop.name,
                  style: textTheme.titleMedium
                      ?.copyWith(fontWeight: FontWeight.bold),
                ),
              ),
              if (stop.isVisited)
                Chip(
                  label: const Text('Visited'),
                  backgroundColor: const Color(0xFF4CAF50).withOpacity(0.15),
                  labelStyle:
                      const TextStyle(color: Color(0xFF4CAF50), fontSize: 12),
                  side: BorderSide.none,
                  padding: EdgeInsets.zero,
                ),
            ],
          ),

          if (stop.description.isNotEmpty) ...[
            const SizedBox(height: 8),
            Text(
              stop.description,
              style: textTheme.bodyMedium?.copyWith(
                color: colorScheme.onSurfaceVariant,
              ),
            ),
          ],

          if (onMarkVisited != null) ...[
            const SizedBox(height: 16),
            SizedBox(
              width: double.infinity,
              child: FilledButton.icon(
                onPressed: onMarkVisited,
                icon: const Icon(Icons.check_circle_outline),
                label: const Text('Mark as visited'),
              ),
            ),
          ],
        ],
      ),
    );
  }
}
