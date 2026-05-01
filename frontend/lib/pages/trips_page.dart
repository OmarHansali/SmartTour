import 'package:flutter/material.dart';
import 'package:geolocator/geolocator.dart';
import '../models/trip.dart';
import '../services/api_service.dart';

class TripsPage extends StatefulWidget {
  const TripsPage({super.key});

  @override
  State<TripsPage> createState() => _TripsPageState();
}

class _TripsPageState extends State<TripsPage> {
  List<Trip> _trips = [];
  bool _isLoading = true;
  String? _error;
  Position? _currentPosition;

  final List<String> _categories = [
    'MUSEUM', 'PARK', 'CAFE', 'RESTAURANT',
    'SHOPPING', 'LANDMARK', 'NATURE', 'ENTERTAINMENT'
  ];
  List<String> _selectedCategories = [];
  double _radiusMeters = 5000;
  int _maxPlaces = 4;

  @override
  void initState() {
    super.initState();
    _initializeAndLoad();
  }

  Future<void> _initializeAndLoad() async {
    await _getLocation();
    await _loadTrips();
  }

  Future<void> _getLocation() async {
    try {
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) return;

      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
      }

      if (permission != LocationPermission.denied &&
          permission != LocationPermission.deniedForever) {
        _currentPosition = await Geolocator.getCurrentPosition();
      }
    } catch (e) {
      debugPrint('Location error: $e');
    }
  }

  Future<void> _loadTrips() async {
    try {
      final trips = await ApiService.getUserTrips();
      setState(() {
        _trips = trips;
        _isLoading = false;
      });
    } catch (e) {
      setState(() {
        _error = 'Failed to load trips: $e';
        _isLoading = false;
      });
    }
  }

  Future<void> _generateNewTrip() async {
    if (_currentPosition == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Waiting for location...')),
      );
      return;
    }

    setState(() {
      _isLoading = true;
    });

    try {
      final trip = await ApiService.generateTrip(
        latitude: _currentPosition!.latitude,
        longitude: _currentPosition!.longitude,
        radiusMeters: _radiusMeters.toInt(),
        maxPlaces: _maxPlaces,
        categories: _selectedCategories.isEmpty ? null : _selectedCategories,
      );

      setState(() {
        _trips.insert(0, trip);
        _isLoading = false;
      });

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Generated trip: ${trip.name}')),
      );
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to generate trip: $e')),
      );
    }
  }

  void _showGenerateDialog() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (context) => StatefulBuilder(
        builder: (context, setModalState) => Container(
          padding: const EdgeInsets.all(16),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Generate New Trip',
                style: Theme.of(context).textTheme.headlineSmall,
              ),
              const SizedBox(height: 16),
              Text(
                'Categories',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: _categories.map((category) {
                  final isSelected = _selectedCategories.contains(category);
                  return FilterChip(
                    label: Text(category),
                    selected: isSelected,
                    onSelected: (selected) {
                      setModalState(() {
                        if (selected) {
                          _selectedCategories.add(category);
                        } else {
                          _selectedCategories.remove(category);
                        }
                      });
                    },
                  );
                }).toList(),
              ),
              const SizedBox(height: 16),
              Text(
                'Search Radius: ${_radiusMeters.toInt()} meters',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              Slider(
                value: _radiusMeters,
                min: 1000,
                max: 20000,
                divisions: 19,
                label: '${_radiusMeters.toInt()}m',
                onChanged: (value) {
                  setModalState(() {
                    _radiusMeters = value;
                  });
                },
              ),
              const SizedBox(height: 16),
              Text(
                'Max Places: $_maxPlaces',
                style: Theme.of(context).textTheme.titleMedium,
              ),
              Slider(
                value: _maxPlaces.toDouble(),
                min: 2,
                max: 6,
                divisions: 4,
                label: '$_maxPlaces',
                onChanged: (value) {
                  setModalState(() {
                    _maxPlaces = value.toInt();
                  });
                },
              ),
              const SizedBox(height: 16),
              SizedBox(
                width: double.infinity,
                child: FilledButton.icon(
                  onPressed: () {
                    Navigator.pop(context);
                    _generateNewTrip();
                  },
                  icon: const Icon(Icons.auto_awesome),
                  label: const Text('Generate Optimized Trip'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Trip Plans'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _isLoading ? null : _loadTrips,
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _error != null && _trips.isEmpty
              ? Center(
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Icon(Icons.error_outline, size: 48, color: Colors.grey),
                      const SizedBox(height: 16),
                      Text(_error!),
                      const SizedBox(height: 16),
                      FilledButton(
                        onPressed: _loadTrips,
                        child: const Text('Retry'),
                      ),
                    ],
                  ),
                )
              : ListView.builder(
                  padding: const EdgeInsets.all(16),
                  itemCount: _trips.length + 1,
                  itemBuilder: (context, index) {
                    if (index == 0) {
                      return Padding(
                        padding: const EdgeInsets.only(bottom: 16),
                        child: Card(
                          child: InkWell(
                            onTap: _showGenerateDialog,
                            borderRadius: BorderRadius.circular(12),
                            child: Container(
                              padding: const EdgeInsets.all(24),
                              child: Row(
                                children: [
                                  Container(
                                    padding: const EdgeInsets.all(12),
                                    decoration: BoxDecoration(
                                      color: Theme.of(context).colorScheme.primaryContainer,
                                      shape: BoxShape.circle,
                                    ),
                                    child: Icon(
                                      Icons.add_location_alt,
                                      color: Theme.of(context).colorScheme.primary,
                                    ),
                                  ),
                                  const SizedBox(width: 16),
                                  Expanded(
                                    child: Column(
                                      crossAxisAlignment: CrossAxisAlignment.start,
                                      children: [
                                        Text(
                                          'Create New Trip',
                                          style: Theme.of(context).textTheme.titleMedium,
                                        ),
                                        const SizedBox(height: 4),
                                        Text(
                                          'Get an optimized route for nearby places',
                                          style: Theme.of(context).textTheme.bodySmall,
                                        ),
                                      ],
                                    ),
                                  ),
                                  const Icon(Icons.arrow_forward_ios, size: 16),
                                ],
                              ),
                            ),
                          ),
                        ),
                      );
                    }

                    final trip = _trips[index - 1];
                    return _buildTripCard(trip);
                  },
                ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: _showGenerateDialog,
        icon: const Icon(Icons.add),
        label: const Text('New Trip'),
      ),
    );
  }

  Widget _buildTripCard(Trip trip) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      child: ExpansionTile(
        title: Text(
          trip.name,
          style: const TextStyle(fontWeight: FontWeight.bold),
        ),
        subtitle: Row(
          children: [
            Icon(Icons.place, size: 16, color: Colors.grey[600]),
            const SizedBox(width: 4),
            Text('${trip.totalStops} stops'),
            const SizedBox(width: 16),
            Icon(Icons.schedule, size: 16, color: Colors.grey[600]),
            const SizedBox(width: 4),
            Text('${trip.estimatedDurationMinutes ?? 0} min'),
            const SizedBox(width: 16),
            Icon(Icons.directions_walk, size: 16, color: Colors.grey[600]),
            const SizedBox(width: 4),
            Text('${((trip.estimatedDistanceMeters ?? 0) / 1000).toStringAsFixed(1)} km'),
          ],
        ),
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Divider(),
                Text(
                  'Route',
                  style: Theme.of(context).textTheme.titleSmall,
                ),
                const SizedBox(height: 8),
                ...trip.places.asMap().entries.map((entry) {
                  final i = entry.key;
                  final place = entry.value;
                  return _buildRouteStep(i, place, i == trip.places.length - 1);
                }),
                const SizedBox(height: 16),
                SizedBox(
                  width: double.infinity,
                  child: OutlinedButton.icon(
                    onPressed: () {
                      // Would open maps with route
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Opening navigation...')),
                      );
                    },
                    icon: const Icon(Icons.navigation),
                    label: const Text('Start Navigation'),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildRouteStep(int index, PlaceInTrip place, bool isLast) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Column(
          children: [
            Container(
              width: 32,
              height: 32,
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.primary,
                shape: BoxShape.circle,
              ),
              child: Center(
                child: Text(
                  '${index + 1}',
                  style: const TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
            ),
            if (!isLast)
              Container(
                width: 2,
                height: 40,
                color: Colors.grey[300],
              ),
          ],
        ),
        const SizedBox(width: 12),
        Expanded(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                place.name,
                style: const TextStyle(fontWeight: FontWeight.w500),
              ),
              const SizedBox(height: 2),
              Text(
                '${place.category ?? 'Place'} • ${place.estimatedVisitDurationMinutes} min visit',
                style: TextStyle(
                  fontSize: 12,
                  color: Colors.grey[600],
                ),
              ),
              if (place.address != null)
                Text(
                  place.address!,
                  style: TextStyle(
                    fontSize: 12,
                    color: Colors.grey[500],
                  ),
                ),
              const SizedBox(height: 8),
            ],
          ),
        ),
      ],
    );
  }
}
