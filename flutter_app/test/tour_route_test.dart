import 'package:flutter_test/flutter_test.dart';
import 'package:smarttour_flutter/domain/model/tour_route.dart';
import 'package:smarttour_flutter/domain/model/tour_stop.dart';

void main() {
  group('TourRoute', () {
    final stops = [
      const TourStop(
        id: '1',
        name: 'Stop A',
        description: 'Desc A',
        latitude: 33.0,
        longitude: -7.0,
        order: 0,
        isVisited: true,
      ),
      const TourStop(
        id: '2',
        name: 'Stop B',
        description: 'Desc B',
        latitude: 33.1,
        longitude: -7.1,
        order: 1,
        isVisited: false,
      ),
      const TourStop(
        id: '3',
        name: 'Stop C',
        description: 'Desc C',
        latitude: 33.2,
        longitude: -7.2,
        order: 2,
        isVisited: false,
      ),
    ];

    final route = TourRoute(
      id: 'r1',
      tourName: 'Test Tour',
      totalDistanceKm: 5.0,
      estimatedDurationMin: 60,
      stops: stops,
      currentStopIndex: 1,
    );

    test('currentStop returns stop at currentStopIndex', () {
      expect(route.currentStop?.id, '2');
    });

    test('nextStop returns stop after currentStopIndex', () {
      expect(route.nextStop?.id, '3');
    });

    test('progressFraction reflects visited stops', () {
      expect(route.progressFraction, closeTo(1 / 3, 0.001));
    });

    test('nextStop is null when at last stop', () {
      final lastRoute = route.copyWith(currentStopIndex: 2);
      expect(lastRoute.nextStop, isNull);
    });
  });

  group('TourStop.copyWith', () {
    const stop = TourStop(
      id: 'x',
      name: 'X',
      description: '',
      latitude: 0,
      longitude: 0,
      order: 0,
      isVisited: false,
    );

    test('updates isVisited', () {
      final updated = stop.copyWith(isVisited: true);
      expect(updated.isVisited, isTrue);
      expect(updated.id, 'x');
    });
  });
}
