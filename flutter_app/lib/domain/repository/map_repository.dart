import '../model/tour_route.dart';

/// Abstract contract for the map repository.
/// Implemented in the data layer; consumed by use-cases.
abstract interface class MapRepository {
  /// Fetches the full route for [tourId] from the backend.
  Future<TourRoute> getActiveTourRoute(String tourId);

  /// Persists a visited state for [stopId] in tour [tourId].
  Future<void> markStopVisited(String tourId, String stopId);
}
