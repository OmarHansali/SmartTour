/// Represents a single stop on a tour route.
class TourStop {
  final String id;
  final String name;
  final String description;
  final double latitude;
  final double longitude;
  final int order;
  final bool isVisited;
  final String? imageUrl;

  const TourStop({
    required this.id,
    required this.name,
    required this.description,
    required this.latitude,
    required this.longitude,
    required this.order,
    required this.isVisited,
    this.imageUrl,
  });

  TourStop copyWith({
    String? id,
    String? name,
    String? description,
    double? latitude,
    double? longitude,
    int? order,
    bool? isVisited,
    String? imageUrl,
  }) {
    return TourStop(
      id: id ?? this.id,
      name: name ?? this.name,
      description: description ?? this.description,
      latitude: latitude ?? this.latitude,
      longitude: longitude ?? this.longitude,
      order: order ?? this.order,
      isVisited: isVisited ?? this.isVisited,
      imageUrl: imageUrl ?? this.imageUrl,
    );
  }
}
