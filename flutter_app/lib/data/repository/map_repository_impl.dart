import '../../domain/model/tour_route.dart';
import '../../domain/repository/map_repository.dart';
import '../api/map_api_service.dart';

/// Concrete implementation of [MapRepository] backed by [MapApiService].
class MapRepositoryImpl implements MapRepository {
  const MapRepositoryImpl(this._apiService);

  final MapApiService _apiService;

  @override
  Future<TourRoute> getActiveTourRoute(String tourId) =>
      _apiService.getActiveTourRoute(tourId);

  @override
  Future<void> markStopVisited(String tourId, String stopId) =>
      _apiService.markStopVisited(tourId, stopId);
}
