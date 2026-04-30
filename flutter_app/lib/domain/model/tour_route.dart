import 'tour_stop.dart';

/// Represents the full route for an active tour, including all stops and
/// progress information.
class TourRoute {
  final String id;
  final String tourName;
  final double totalDistanceKm;
  final int estimatedDurationMin;
  final List<TourStop> stops;
  final int currentStopIndex;

  const TourRoute({
    required this.id,
    required this.tourName,
    required this.totalDistanceKm,
    required this.estimatedDurationMin,
    required this.stops,
    required this.currentStopIndex,
  });

  /// The stop the user is currently at, or null when the route has no stops.
  TourStop? get currentStop => stops.isEmpty ? null : stops[currentStopIndex.clamp(0, stops.length - 1)];

  /// The next unvisited stop after the current position.
  TourStop? get nextStop {
    final nextIndex = currentStopIndex + 1;
    return nextIndex < stops.length ? stops[nextIndex] : null;
  }

  /// Fraction [0..1] of stops already visited.
  double get progressFraction =>
      stops.isEmpty ? 0.0 : stops.where((s) => s.isVisited).length / stops.length;

  TourRoute copyWith({
    String? id,
    String? tourName,
    double? totalDistanceKm,
    int? estimatedDurationMin,
    List<TourStop>? stops,
    int? currentStopIndex,
  }) {
    return TourRoute(
      id: id ?? this.id,
      tourName: tourName ?? this.tourName,
      totalDistanceKm: totalDistanceKm ?? this.totalDistanceKm,
      estimatedDurationMin: estimatedDurationMin ?? this.estimatedDurationMin,
      stops: stops ?? this.stops,
      currentStopIndex: currentStopIndex ?? this.currentStopIndex,
    );
  }
}
