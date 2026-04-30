import '../repository/map_repository.dart';

/// Marks a tour stop as visited on the backend.
class MarkStopVisitedUseCase {
  const MarkStopVisitedUseCase(this._repository);

  final MapRepository _repository;

  Future<void> call(String tourId, String stopId) =>
      _repository.markStopVisited(tourId, stopId);
}
