class Place {
  final String? id;
  final String name;
  final String? description;
  final double latitude;
  final double longitude;
  final String? category;
  final double? rating;
  final String? imageUrl;
  final String? address;
  final double? distanceMeters;

  Place({
    this.id,
    required this.name,
    this.description,
    required this.latitude,
    required this.longitude,
    this.category,
    this.rating,
    this.imageUrl,
    this.address,
    this.distanceMeters,
  });

  factory Place.fromJson(Map<String, dynamic> json) {
    return Place(
      id: json['id'],
      name: json['name'] ?? 'Unknown',
      description: json['description'],
      latitude: (json['latitude'] as num).toDouble(),
      longitude: (json['longitude'] as num).toDouble(),
      category: json['category'],
      rating: json['rating'] != null ? (json['rating'] as num).toDouble() : null,
      imageUrl: json['imageUrl'],
      address: json['address'],
      distanceMeters: json['distanceMeters'] != null ? (json['distanceMeters'] as num).toDouble() : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name,
      'description': description,
      'latitude': latitude,
      'longitude': longitude,
      'category': category,
      'rating': rating,
      'imageUrl': imageUrl,
      'address': address,
    };
  }
}

class NearbyPlacesResponse {
  final List<Place> places;
  final double userLatitude;
  final double userLongitude;
  final int totalFound;

  NearbyPlacesResponse({
    required this.places,
    required this.userLatitude,
    required this.userLongitude,
    required this.totalFound,
  });

  factory NearbyPlacesResponse.fromJson(Map<String, dynamic> json) {
    return NearbyPlacesResponse(
      places: (json['places'] as List).map((e) => Place.fromJson(e)).toList(),
      userLatitude: (json['userLatitude'] as num).toDouble(),
      userLongitude: (json['userLongitude'] as num).toDouble(),
      totalFound: json['totalFound'] ?? 0,
    );
  }
}
