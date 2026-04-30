import '../model/tour_route.dart';
import '../repository/map_repository.dart';

/// Fetches the active tour route from the backend.
class GetActiveTourMapUseCase {
  const GetActiveTourMapUseCase(this._repository);

  final MapRepository _repository;

  Future<TourRoute> call(String tourId) => _repository.getActiveTourRoute(tourId);
}
