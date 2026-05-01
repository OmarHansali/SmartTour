class ARAnalysis {
  final List<DetectedObject> detectedObjects;
  final NearestPlace? nearestPlace;
  final Direction? direction;
  final String description;

  ARAnalysis({
    required this.detectedObjects,
    this.nearestPlace,
    this.direction,
    required this.description,
  });

  factory ARAnalysis.fromJson(Map<String, dynamic> json) {
    return ARAnalysis(
      detectedObjects: (json['detectedObjects'] as List?)
          ?.map((e) => DetectedObject.fromJson(e))
          .toList() ?? [],
      nearestPlace: json['nearestPlace'] != null
          ? NearestPlace.fromJson(json['nearestPlace'])
          : null,
      direction: json['direction'] != null
          ? Direction.fromJson(json['direction'])
          : null,
      description: json['description'] ?? 'No analysis available',
    );
  }
}

class DetectedObject {
  final String name;
  final double confidence;
  final String description;

  DetectedObject({
    required this.name,
    required this.confidence,
    required this.description,
  });

  factory DetectedObject.fromJson(Map<String, dynamic> json) {
    return DetectedObject(
      name: json['name'] ?? 'Unknown',
      confidence: (json['confidence'] as num?)?.toDouble() ?? 0.0,
      description: json['description'] ?? '',
    );
  }
}

class NearestPlace {
  final String name;
  final double distanceMeters;
  final double bearing;
  final String? category;
  final String? description;

  NearestPlace({
    required this.name,
    required this.distanceMeters,
    required this.bearing,
    this.category,
    this.description,
  });

  factory NearestPlace.fromJson(Map<String, dynamic> json) {
    return NearestPlace(
      name: json['name'] ?? 'Unknown',
      distanceMeters: (json['distanceMeters'] as num).toDouble(),
      bearing: (json['bearing'] as num).toDouble(),
      category: json['category'],
      description: json['description'],
    );
  }
}

class Direction {
  final double heading;
  final String instruction;
  final double arrowRotation;

  Direction({
    required this.heading,
    required this.instruction,
    required this.arrowRotation,
  });

  factory Direction.fromJson(Map<String, dynamic> json) {
    return Direction(
      heading: (json['heading'] as num).toDouble(),
      instruction: json['instruction'] ?? '',
      arrowRotation: (json['arrowRotation'] as num).toDouble(),
    );
  }
}
