import 'package:flutter/foundation.dart';

import '../../domain/use_case/get_active_tour_map_use_case.dart';
import '../../domain/use_case/mark_stop_visited_use_case.dart';
import 'map_ui_state.dart';

/// ViewModel for [MapScreen].  Mirrors the logic of the Kotlin [MapViewModel].
///
/// Exposes a single [state] notifier consumed by the UI.  Optimistic updates
/// are applied immediately on "mark visited"; the previous state is restored if
/// the backend call fails.
class MapViewModel extends ChangeNotifier {
  MapViewModel({
    required GetActiveTourMapUseCase getActiveTourMapUseCase,
    required MarkStopVisitedUseCase markStopVisitedUseCase,
  })  : _getActiveTourMapUseCase = getActiveTourMapUseCase,
        _markStopVisitedUseCase = markStopVisitedUseCase;

  final GetActiveTourMapUseCase _getActiveTourMapUseCase;
  final MarkStopVisitedUseCase _markStopVisitedUseCase;

  MapUiState _state = const MapLoadingState();
  MapUiState get state => _state;

  void _setState(MapUiState newState) {
    _state = newState;
    notifyListeners();
  }

  /// Loads the route for [tourId] from the backend.
  Future<void> loadRoute(String tourId) async {
    _setState(const MapLoadingState());
    try {
      final route = await _getActiveTourMapUseCase(tourId);
      _setState(MapSuccessState(route));
    } catch (e) {
      _setState(MapErrorState(e.toString().replaceFirst('Exception: ', '')));
    }
  }

  /// Marks [stopId] as visited in tour [tourId].
  ///
  /// Applies an optimistic UI update immediately, then syncs with the backend.
  /// On failure, the previous state is restored and an error is shown.
  Future<void> markCurrentStopVisited(String tourId, String stopId) async {
    final previousState = _state;

    // --- Optimistic update -----------------------------------------------
    if (previousState is MapSuccessState) {
      final updatedStops = previousState.route.stops.map((stop) {
        return stop.id == stopId ? stop.copyWith(isVisited: true) : stop;
      }).toList();

      final nextIndex = (previousState.route.currentStopIndex + 1)
          .clamp(0, updatedStops.length - 1);

      _setState(MapSuccessState(
        previousState.route.copyWith(
          stops: updatedStops,
          currentStopIndex: nextIndex,
        ),
      ));
    }

    // --- Persist to backend -----------------------------------------------
    try {
      await _markStopVisitedUseCase(tourId, stopId);
    } catch (e) {
      // Revert to previous state on failure
      _setState(previousState);
      _setState(MapErrorState(e.toString().replaceFirst('Exception: ', '')));
    }
  }
}
