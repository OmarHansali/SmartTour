import '../../domain/model/tour_route.dart';
import 'tour_stop_dto.dart';

/// JSON DTO for a tour route, matching the Kotlin backend response.
class TourRouteDto {
  final String id;
  final String tourName;
  final double totalDistanceKm;
  final int estimatedDurationMin;
  final List<TourStopDto> stops;
  final int currentStopIndex;

  const TourRouteDto({
    required this.id,
    required this.tourName,
    required this.totalDistanceKm,
    required this.estimatedDurationMin,
    required this.stops,
    required this.currentStopIndex,
  });

  factory TourRouteDto.fromJson(Map<String, dynamic> json) {
    final stopsList = (json['stops'] as List<dynamic>)
        .map((e) => TourStopDto.fromJson(e as Map<String, dynamic>))
        .toList();

    return TourRouteDto(
      id: json['id'] as String,
      tourName: json['tourName'] as String,
      totalDistanceKm: (json['totalDistanceKm'] as num).toDouble(),
      estimatedDurationMin: json['estimatedDurationMin'] as int,
      stops: stopsList,
      currentStopIndex: json['currentStopIndex'] as int,
    );
  }

  TourRoute toDomain() => TourRoute(
        id: id,
        tourName: tourName,
        totalDistanceKm: totalDistanceKm,
        estimatedDurationMin: estimatedDurationMin,
        stops: stops.map((s) => s.toDomain()).toList(),
        currentStopIndex: currentStopIndex,
      );
}
