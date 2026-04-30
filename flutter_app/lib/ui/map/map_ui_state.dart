import '../../domain/model/tour_route.dart';

/// Sealed union representing all possible states of the map screen.
sealed class MapUiState {
  const MapUiState();
}

/// Initial / data-fetch-in-progress state.
class MapLoadingState extends MapUiState {
  const MapLoadingState();
}

/// Data successfully loaded.
class MapSuccessState extends MapUiState {
  const MapSuccessState(this.route);

  final TourRoute route;
}

/// An error occurred while loading or updating data.
class MapErrorState extends MapUiState {
  const MapErrorState(this.message);

  final String message;
}
