import '../../domain/model/tour_stop.dart';

/// JSON DTO for a single tour stop, matching the Kotlin backend response.
class TourStopDto {
  final String id;
  final String name;
  final String description;
  final double latitude;
  final double longitude;
  final int order;
  final bool isVisited;
  final String? imageUrl;

  const TourStopDto({
    required this.id,
    required this.name,
    required this.description,
    required this.latitude,
    required this.longitude,
    required this.order,
    required this.isVisited,
    this.imageUrl,
  });

  factory TourStopDto.fromJson(Map<String, dynamic> json) {
    return TourStopDto(
      id: json['id'] as String,
      name: json['name'] as String,
      description: json['description'] as String,
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      order: json['order'] as int,
      isVisited: json['isVisited'] as bool,
      imageUrl: json['imageUrl'] as String?,
    );
  }

  TourStop toDomain() => TourStop(
        id: id,
        name: name,
        description: description,
        latitude: latitude,
        longitude: longitude,
        order: order,
        isVisited: isVisited,
        imageUrl: imageUrl,
      );
}
