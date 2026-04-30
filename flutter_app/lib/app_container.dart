import 'data/api/map_api_service.dart';
import 'data/repository/map_repository_impl.dart';
import 'domain/use_case/get_active_tour_map_use_case.dart';
import 'domain/use_case/mark_stop_visited_use_case.dart';
import 'ui/map/map_view_model.dart';

/// Manual dependency-injection container – mirrors the Kotlin [AppContainer]
/// singleton pattern.
///
/// A single instance lives for the app's lifetime.  The [MapViewModel] is
/// created lazily per-screen via the [makeMapViewModel] factory so each
/// screen gets its own isolated state.
class AppContainer {
  AppContainer({String? baseUrl}) {
    final apiService = MapApiService(baseUrl: baseUrl);
    final repository = MapRepositoryImpl(apiService);
    _getActiveTourMapUseCase = GetActiveTourMapUseCase(repository);
    _markStopVisitedUseCase = MarkStopVisitedUseCase(repository);
  }

  late final GetActiveTourMapUseCase _getActiveTourMapUseCase;
  late final MarkStopVisitedUseCase _markStopVisitedUseCase;

  /// Creates a fresh [MapViewModel] instance.  Call once per [MapScreen].
  MapViewModel makeMapViewModel() => MapViewModel(
        getActiveTourMapUseCase: _getActiveTourMapUseCase,
        markStopVisitedUseCase: _markStopVisitedUseCase,
      );
}
