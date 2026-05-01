import 'place.dart';

class Trip {
  final String? id;
  final String name;
  final List<PlaceInTrip> places;
  final List<RoutePoint> route;
  final int? estimatedDurationMinutes;
  final int? estimatedDistanceMeters;
  final int totalStops;

  Trip({
    this.id,
    required this.name,
    required this.places,
    required this.route,
    this.estimatedDurationMinutes,
    this.estimatedDistanceMeters,
    required this.totalStops,
  });

  factory Trip.fromJson(Map<String, dynamic> json) {
    return Trip(
      id: json['id'],
      name: json['name'] ?? 'Untitled Trip',
      places: (json['places'] as List).map((e) => PlaceInTrip.fromJson(e)).toList(),
      route: (json['route'] as List).map((e) => RoutePoint.fromJson(e)).toList(),
      estimatedDurationMinutes: json['estimatedDurationMinutes'],
      estimatedDistanceMeters: json['estimatedDistanceMeters'],
      totalStops: json['totalStops'] ?? 0,
    );
  }
}

class PlaceInTrip {
  final String? id;
  final String name;
  final String? description;
  final double latitude;
  final double longitude;
  final String? category;
  final double? rating;
  final String? imageUrl;
  final String? address;
  final int order;
  final int estimatedVisitDurationMinutes;

  PlaceInTrip({
    this.id,
    required this.name,
    this.description,
    required this.latitude,
    required this.longitude,
    this.category,
    this.rating,
    this.imageUrl,
    this.address,
    required this.order,
    this.estimatedVisitDurationMinutes = 60,
  });

  factory PlaceInTrip.fromJson(Map<String, dynamic> json) {
    return PlaceInTrip(
      id: json['id'],
      name: json['name'] ?? 'Unknown',
      description: json['description'],
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      category: json['category'],
      rating: json['rating'] != null ? (json['rating'] as num).toDouble() : null,
      imageUrl: json['imageUrl'],
      address: json['address'],
      order: json['order'] ?? 0,
      estimatedVisitDurationMinutes: json['estimatedVisitDurationMinutes'] ?? 60,
    );
  }
}

class RoutePoint {
  final double latitude;
  final double longitude;
  final int order;
  final String? placeName;

  RoutePoint({
    required this.latitude,
    required this.longitude,
    required this.order,
    this.placeName,
  });

  factory RoutePoint.fromJson(Map<String, dynamic> json) {
    return RoutePoint(
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      order: json['order'] ?? 0,
      placeName: json['placeName'],
    );
  }
}
