import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import '../models/place.dart';
import '../models/ar_analysis.dart';
import '../models/trip.dart';
import '../models/user.dart';

class ApiService {
  // For USB tethering
  // static const String baseUrl = 'http://192.168.243.1:8081/api';

  // For PC Hotspot
  static const String baseUrl = 'http://192.168.137.1:8081/api';

  // For emulator
  // static const String baseUrl = 'http://10.0.2.2:8081/api';

  static const String demoUserId = '550e8400-e29b-41d4-a716-446655440000';
  static const Duration _timeout = Duration(seconds: 15);

  // ─── helpers ────────────────────────────────────────────────────────────────

  static Future<http.Response> _get(String path) async {
    try {
      final response = await http
          .get(Uri.parse('$baseUrl$path'))
          .timeout(_timeout);
      return response;
    } on SocketException {
      throw Exception('No connection to server. Make sure the backend is running.');
    } on HttpException {
      throw Exception('Server error. Please try again.');
    }
  }

  static Future<http.Response> _post(String path, Map<String, dynamic> body) async {
    try {
      final response = await http
          .post(
            Uri.parse('$baseUrl$path'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(body),
          )
          .timeout(_timeout);
      return response;
    } on SocketException {
      throw Exception('No connection to server. Make sure the backend is running.');
    } on HttpException {
      throw Exception('Server error. Please try again.');
    }
  }

  static Future<http.Response> _delete(String path) async {
    try {
      final response = await http
          .delete(Uri.parse('$baseUrl$path'))
          .timeout(_timeout);
      return response;
    } on SocketException {
      throw Exception('No connection to server. Make sure the backend is running.');
    } on HttpException {
      throw Exception('Server error. Please try again.');
    }
  }

  // ─── Maps ────────────────────────────────────────────────────────────────────

  static Future<NearbyPlacesResponse> getNearbyPlaces(
    double latitude,
    double longitude, {
    int radiusMeters = 5000,
  }) async {
    final response = await _post('/maps/nearby', {
      'latitude': latitude,
      'longitude': longitude,
      'radiusMeters': radiusMeters,
    });

    if (response.statusCode == 200) {
      return NearbyPlacesResponse.fromJson(jsonDecode(response.body));
    }
    throw Exception('Failed to load nearby places (${response.statusCode})');
  }

  // ─── AR ──────────────────────────────────────────────────────────────────────

  static Future<ARAnalysis> analyzeARScene({
    required double latitude,
    required double longitude,
    String? imageBase64,
    List<String>? detectedObjects,
    double? compassHeading,
  }) async {
    final response = await _post('/ar/analyze', {
      'latitude': latitude,
      'longitude': longitude,
      if (imageBase64 != null) 'imageBase64': imageBase64,
      if (detectedObjects != null) 'detectedObjects': detectedObjects,
      if (compassHeading != null) 'compassHeading': compassHeading,
    });

    if (response.statusCode == 200) {
      return ARAnalysis.fromJson(jsonDecode(response.body));
    }
    throw Exception('Failed to analyze AR scene (${response.statusCode})');
  }

  // ─── Trips ───────────────────────────────────────────────────────────────────

  static Future<Trip> generateTrip({
    required double latitude,
    required double longitude,
    int radiusMeters = 5000,
    int maxPlaces = 4,
    List<String>? categories,
  }) async {
    final response = await _post('/trips', {
      'userId': demoUserId,
      'latitude': latitude,
      'longitude': longitude,
      'radiusMeters': radiusMeters,
      'maxPlaces': maxPlaces,
      if (categories != null && categories.isNotEmpty) 'categories': categories,
    });

    if (response.statusCode == 200 || response.statusCode == 201) {
      return Trip.fromJson(jsonDecode(response.body));
    }
    throw Exception('Failed to generate trip (${response.statusCode})');
  }

  static Future<List<Trip>> getUserTrips() async {
    final response = await _get('/trips/user/$demoUserId');

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((e) => Trip.fromJson(e)).toList();
    }
    throw Exception('Failed to load trips (${response.statusCode})');
  }

  // ─── Profile ─────────────────────────────────────────────────────────────────

  static Future<User> getProfile() async {
    final response = await _get('/profile/$demoUserId');

    if (response.statusCode == 200) {
      return User.fromJson(jsonDecode(response.body));
    }
    throw Exception('Failed to load profile (${response.statusCode})');
  }

  static Future<List<Favorite>> getFavorites() async {
    final response = await _get('/profile/$demoUserId/favorites');

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((e) => Favorite.fromJson(e)).toList();
    }
    throw Exception('Failed to load favorites (${response.statusCode})');
  }

  static Future<void> addFavorite(String placeId) async {
    if (placeId.isEmpty) throw Exception('Invalid place ID');

    final response = await _post('/profile/favorites', {
      'userId': demoUserId,
      'placeId': placeId,
    });

    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception('Failed to add favorite (${response.statusCode})');
    }
  }

  static Future<void> removeFavorite(String placeId) async {
    if (placeId.isEmpty) throw Exception('Invalid place ID');

    final response = await _delete('/profile/$demoUserId/favorites/$placeId');

    if (response.statusCode != 200 && response.statusCode != 204) {
      throw Exception('Failed to remove favorite (${response.statusCode})');
    }
  }

  static Future<List<VisitHistory>> getVisitHistory() async {
    final response = await _get('/profile/$demoUserId/history');

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((e) => VisitHistory.fromJson(e)).toList();
    }
    throw Exception('Failed to load visit history (${response.statusCode})');
  }

  static Future<void> recordVisit(
    String placeId, {
    double? rating,
    String? notes,
  }) async {
    if (placeId.isEmpty) throw Exception('Invalid place ID');

    final response = await _post('/profile/history', {
      'userId': demoUserId,
      'placeId': placeId,
      if (rating != null) 'rating': rating,
      if (notes != null) 'notes': notes,
    });

    if (response.statusCode != 200 && response.statusCode != 201) {
      throw Exception('Failed to record visit (${response.statusCode})');
    }
  }
}