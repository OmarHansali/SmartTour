import 'dart:convert';
import 'package:http/http.dart' as http;

import '../../domain/model/tour_route.dart';
import '../dto/tour_route_dto.dart';

/// Low-level HTTP client that calls the SmartTour Kotlin backend.
///
/// The [baseUrl] defaults to the Android emulator loopback address
/// (10.0.2.2:8080) so the Flutter app running in an emulator reaches the
/// host machine's Spring Boot / Ktor server – matching the existing
/// [RetrofitInstance] BASE_URL in the Kotlin module.
class MapApiService {
  MapApiService({http.Client? client, String? baseUrl})
      : _client = client ?? http.Client(),
        _baseUrl = baseUrl ?? 'http://10.0.2.2:8080';

  final http.Client _client;
  final String _baseUrl;

  /// GET /api/tours/{tourId}/route
  Future<TourRoute> getActiveTourRoute(String tourId) async {
    final uri = Uri.parse('$_baseUrl/api/tours/$tourId/route');
    final response = await _client.get(uri);

    if (response.statusCode == 200) {
      final json = jsonDecode(response.body) as Map<String, dynamic>;
      return TourRouteDto.fromJson(json).toDomain();
    }

    throw Exception(
      'Failed to load tour route (HTTP ${response.statusCode}): ${response.body}',
    );
  }

  /// POST /api/tours/{tourId}/stops/{stopId}/visit
  Future<void> markStopVisited(String tourId, String stopId) async {
    final uri = Uri.parse('$_baseUrl/api/tours/$tourId/stops/$stopId/visit');
    final response = await _client.post(uri);

    if (response.statusCode != 200 && response.statusCode != 204) {
      throw Exception(
        'Failed to mark stop visited (HTTP ${response.statusCode}): ${response.body}',
      );
    }
  }
}
