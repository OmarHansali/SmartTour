import 'dart:async';
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:geolocator/geolocator.dart';
import '../models/place.dart';
import '../services/api_service.dart';

class MapsPage extends StatefulWidget {
  const MapsPage({super.key});

  @override
  State<MapsPage> createState() => _MapsPageState();
}

class _MapsPageState extends State<MapsPage> {
  GoogleMapController? _mapController;
  Position? _currentPosition;
  List<Place> _nearbyPlaces = [];
  bool _isLoading = true;
  String? _error;
  Set<Marker> _markers = {};
  double _radiusMeters = 5000;

  static const CameraPosition _defaultPosition = CameraPosition(
    target: LatLng(31.6295, -7.9811), // Marrakech default
    zoom: 14,
  );

  @override
  void initState() {
    super.initState();
    _initializeLocation();
  }

  Future<void> _initializeLocation() async {
    setState(() {
      _isLoading = true;
      _error = null;
    });

    try {
      bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
      if (!serviceEnabled) {
        setState(() {
          _error = 'Location services are disabled. Please enable GPS.';
          _isLoading = false;
        });
        return;
      }

      LocationPermission permission = await Geolocator.checkPermission();
      if (permission == LocationPermission.denied) {
        permission = await Geolocator.requestPermission();
        if (permission == LocationPermission.denied) {
          setState(() {
            _error = 'Location permission denied.';
            _isLoading = false;
          });
          return;
        }
      }

      if (permission == LocationPermission.deniedForever) {
        setState(() {
          _error = 'Location permission permanently denied. Please enable it in Settings.';
          _isLoading = false;
        });
        return;
      }

      _currentPosition = await Geolocator.getCurrentPosition(
        desiredAccuracy: LocationAccuracy.high,
      );
      await _loadNearbyPlaces();
    } catch (e) {
      setState(() {
        _error = 'Location error: $e';
        _isLoading = false;
      });
    }
  }

  Future<void> _loadNearbyPlaces() async {
    if (_currentPosition == null) return;

    setState(() {
      _isLoading = true;
      _error = null;
    });

    try {
      final response = await ApiService.getNearbyPlaces(
        _currentPosition!.latitude,
        _currentPosition!.longitude,
        radiusMeters: _radiusMeters.toInt(),
      );

      setState(() {
        _nearbyPlaces = response.places;
        _updateMarkers();
        _isLoading = false;
      });

      _animateToPosition(
        LatLng(_currentPosition!.latitude, _currentPosition!.longitude),
      );
    } catch (e) {
      setState(() {
        _error = e.toString().replaceFirst('Exception: ', '');
        _isLoading = false;
      });
    }
  }

  void _updateMarkers() {
    _markers = {};

    if (_currentPosition != null) {
      _markers.add(
        Marker(
          markerId: const MarkerId('current_location'),
          position: LatLng(_currentPosition!.latitude, _currentPosition!.longitude),
          icon: BitmapDescriptor.defaultMarkerWithHue(BitmapDescriptor.hueAzure),
          infoWindow: const InfoWindow(title: 'You are here'),
        ),
      );
    }

    for (final place in _nearbyPlaces) {
      _markers.add(
        Marker(
          markerId: MarkerId(place.id ?? place.name),
          position: LatLng(place.latitude, place.longitude),
          infoWindow: InfoWindow(
            title: place.name,
            snippet:
                '${place.category ?? 'Place'} • ${place.distanceMeters?.toStringAsFixed(0) ?? '?'}m',
          ),
          onTap: () => _showPlaceDetails(place),
        ),
      );
    }
  }

  void _animateToPosition(LatLng position) {
    _mapController?.animateCamera(
      CameraUpdate.newCameraPosition(
        CameraPosition(target: position, zoom: 15),
      ),
    );
  }

  void _showPlaceDetails(Place place) {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) => Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Handle
            Center(
              child: Container(
                width: 40,
                height: 4,
                decoration: BoxDecoration(
                  color: Colors.grey[300],
                  borderRadius: BorderRadius.circular(2),
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Name + rating
            Row(
              children: [
                Expanded(
                  child: Text(
                    place.name,
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                  ),
                ),
                if (place.rating != null)
                  Container(
                    padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                    decoration: BoxDecoration(
                      color: Colors.amber[100],
                      borderRadius: BorderRadius.circular(20),
                    ),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(Icons.star, size: 16, color: Colors.amber),
                        const SizedBox(width: 4),
                        Text(
                          place.rating!.toStringAsFixed(1),
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                  ),
              ],
            ),
            const SizedBox(height: 8),

            // Category chip
            if (place.category != null)
              Chip(
                label: Text(place.category!),
                backgroundColor: Theme.of(context).colorScheme.primaryContainer,
                labelStyle: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimaryContainer,
                ),
              ),

            const SizedBox(height: 8),

            // Address
            if (place.address != null)
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Icon(Icons.location_on, size: 16, color: Colors.grey),
                  const SizedBox(width: 4),
                  Expanded(
                    child: Text(
                      place.address!,
                      style: TextStyle(color: Colors.grey[700]),
                    ),
                  ),
                ],
              ),

            // Distance
            if (place.distanceMeters != null) ...[
              const SizedBox(height: 4),
              Row(
                children: [
                  const Icon(Icons.directions_walk, size: 16, color: Colors.grey),
                  const SizedBox(width: 4),
                  Text(
                    '${place.distanceMeters!.toStringAsFixed(0)} meters away',
                    style: TextStyle(color: Colors.grey[700]),
                  ),
                ],
              ),
            ],

            // Description
            if (place.description != null) ...[
              const SizedBox(height: 12),
              Text(
                place.description!,
                style: Theme.of(context).textTheme.bodyMedium,
              ),
            ],

            const SizedBox(height: 20),

            // Actions
            Row(
              children: [
                Expanded(
                  child: FilledButton.icon(
                    onPressed: place.id != null
                        ? () {
                            Navigator.pop(context);
                            _showVisitDialog(place);
                          }
                        : null,
                    icon: const Icon(Icons.check_circle),
                    label: const Text('Mark Visited'),
                  ),
                ),
                const SizedBox(width: 8),
                Expanded(
                  child: OutlinedButton.icon(
                    onPressed: place.id != null
                        ? () {
                            Navigator.pop(context);
                            _addToFavorites(place);
                          }
                        : null,
                    icon: const Icon(Icons.favorite_border),
                    label: const Text('Favorite'),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 8),
          ],
        ),
      ),
    );
  }

  void _showVisitDialog(Place place) {
    double rating = 4.0;
    final notesController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => StatefulBuilder(
        builder: (context, setDialogState) => AlertDialog(
          title: Text('Rate your visit to\n${place.name}'),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              // Star rating
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: List.generate(5, (i) {
                  return IconButton(
                    icon: Icon(
                      i < rating ? Icons.star : Icons.star_border,
                      color: Colors.amber,
                      size: 32,
                    ),
                    onPressed: () => setDialogState(() => rating = i + 1.0),
                  );
                }),
              ),
              const SizedBox(height: 12),
              TextField(
                controller: notesController,
                decoration: const InputDecoration(
                  labelText: 'Notes (optional)',
                  border: OutlineInputBorder(),
                ),
                maxLines: 2,
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () {
                Navigator.pop(context);
                _recordVisit(place, rating: rating, notes: notesController.text);
              },
              child: const Text('Save'),
            ),
          ],
        ),
      ),
    );
  }

  Future<void> _recordVisit(Place place, {double? rating, String? notes}) async {
    if (place.id == null) {
      _showSnack('Cannot record visit: place ID missing');
      return;
    }
    try {
      await ApiService.recordVisit(
        place.id!,
        rating: rating,
        notes: notes?.isNotEmpty == true ? notes : null,
      );
      _showSnack('Visit recorded!');
    } catch (e) {
      _showSnack('Error: ${e.toString().replaceFirst('Exception: ', '')}');
    }
  }

  Future<void> _addToFavorites(Place place) async {
    if (place.id == null) {
      _showSnack('Cannot add to favorites: place ID missing');
      return;
    }
    try {
      await ApiService.addFavorite(place.id!);
      _showSnack('Added to favorites!');
    } catch (e) {
      _showSnack('Error: ${e.toString().replaceFirst('Exception: ', '')}');
    }
  }

  void _showSnack(String msg) {
    if (!mounted) return;
    ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('SmartTour Map'),
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: _isLoading ? null : _loadNearbyPlaces,
            tooltip: 'Refresh',
          ),
        ],
      ),
      body: Stack(
        children: [
          // Map
          GoogleMap(
            mapType: MapType.normal,
            initialCameraPosition: _defaultPosition,
            markers: _markers,
            myLocationEnabled: true,
            myLocationButtonEnabled: false,
            zoomControlsEnabled: false,
            onMapCreated: (controller) {
              _mapController = controller;
              if (_currentPosition != null) {
                _animateToPosition(
                  LatLng(_currentPosition!.latitude, _currentPosition!.longitude),
                );
              }
            },
          ),

          // Loading overlay
          if (_isLoading)
            Container(
              color: Colors.black45,
              child: const Center(child: CircularProgressIndicator()),
            ),

          // Error overlay
          if (_error != null && !_isLoading)
            Container(
              color: Colors.black45,
              child: Center(
                child: Card(
                  margin: const EdgeInsets.all(32),
                  child: Padding(
                    padding: const EdgeInsets.all(20),
                    child: Column(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        const Icon(Icons.error_outline, color: Colors.red, size: 48),
                        const SizedBox(height: 12),
                        Text(
                          _error!,
                          textAlign: TextAlign.center,
                          style: const TextStyle(fontSize: 14),
                        ),
                        const SizedBox(height: 16),
                        FilledButton.icon(
                          onPressed: _initializeLocation,
                          icon: const Icon(Icons.refresh),
                          label: const Text('Retry'),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ),

          // Top info bar
          if (!_isLoading && _error == null)
            Positioned(
              top: 12,
              left: 12,
              right: 12,
              child: Card(
                child: Padding(
                  padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
                  child: Row(
                    children: [
                      Icon(
                        Icons.place,
                        size: 18,
                        color: Theme.of(context).colorScheme.primary,
                      ),
                      const SizedBox(width: 8),
                      Expanded(
                        child: Text(
                          _nearbyPlaces.isEmpty
                              ? 'No places found nearby'
                              : '${_nearbyPlaces.length} places within ${_radiusMeters.toInt()}m',
                          style: Theme.of(context).textTheme.bodyMedium,
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),

          // My location FAB
          Positioned(
            bottom: 24,
            right: 16,
            child: FloatingActionButton(
              onPressed: () {
                if (_currentPosition != null) {
                  _animateToPosition(
                    LatLng(_currentPosition!.latitude, _currentPosition!.longitude),
                  );
                } else {
                  _initializeLocation();
                }
              },
              tooltip: 'My location',
              child: const Icon(Icons.my_location),
            ),
          ),
        ],
      ),
    );
  }

  @override
  void dispose() {
    _mapController?.dispose();
    super.dispose();
  }
}