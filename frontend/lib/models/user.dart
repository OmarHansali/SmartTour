class User {
  final String? id;
  final String name;
  final String email;
  final String? avatarUrl;
  final Set<String> preferences;
  final int totalVisits;
  final int totalFavorites;

  User({
    this.id,
    required this.name,
    required this.email,
    this.avatarUrl,
    required this.preferences,
    required this.totalVisits,
    required this.totalFavorites,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'],
      name: json['name'] ?? 'User',
      email: json['email'] ?? '',
      avatarUrl: json['avatarUrl'],
      preferences: (json['preferences'] as List?)?.map((e) => e.toString()).toSet() ?? {},
      totalVisits: (json['totalVisits'] as num?)?.toInt() ?? 0,
      totalFavorites: (json['totalFavorites'] as num?)?.toInt() ?? 0,
    );
  }
}

class Favorite {
  final String? id;
  final PlaceSummary place;
  final DateTime createdAt;

  Favorite({
    this.id,
    required this.place,
    required this.createdAt,
  });

  factory Favorite.fromJson(Map<String, dynamic> json) {
    return Favorite(
      id: json['id'],
      place: PlaceSummary.fromJson(json['place']),
      createdAt: DateTime.parse(json['createdAt']),
    );
  }
}

class VisitHistory {
  final String? id;
  final PlaceSummary place;
  final DateTime visitedAt;
  final double? userRating;
  final String? notes;

  VisitHistory({
    this.id,
    required this.place,
    required this.visitedAt,
    this.userRating,
    this.notes,
  });

  factory VisitHistory.fromJson(Map<String, dynamic> json) {
    return VisitHistory(
      id: json['id'],
      place: PlaceSummary.fromJson(json['place']),
      visitedAt: DateTime.parse(json['visitedAt']),
      userRating: json['userRating'] != null ? (json['userRating'] as num).toDouble() : null,
      notes: json['notes'],
    );
  }
}

class PlaceSummary {
  final String? id;
  final String name;
  final String? category;
  final String? imageUrl;
  final String? address;
  final double? rating;

  PlaceSummary({
    this.id,
    required this.name,
    this.category,
    this.imageUrl,
    this.address,
    this.rating,
  });

  factory PlaceSummary.fromJson(Map<String, dynamic> json) {
    return PlaceSummary(
      id: json['id'],
      name: json['name'] ?? 'Unknown',
      category: json['category'],
      imageUrl: json['imageUrl'],
      address: json['address'],
      rating: json['rating'] != null ? (json['rating'] as num).toDouble() : null,
    );
  }
}
