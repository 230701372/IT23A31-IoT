import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_map/flutter_map.dart';
import 'package:http/http.dart' as http;
import 'package:latlong2/latlong.dart';

class RouteMapScreen extends StatefulWidget {
  final String driverId;

  const RouteMapScreen({super.key, required this.driverId});

  @override
  State<RouteMapScreen> createState() => _RouteMapScreenState();
}

class _RouteMapScreenState extends State<RouteMapScreen> {
  List<LatLng> _routePoints = [];
  bool _isLoading = true;
  bool _hasError = false;

  @override
  void initState() {
    super.initState();
    _fetchRoute();
  }

  Future<void> _fetchRoute() async {
    try {
      final response = await http.get(
        Uri.parse('http://192.168.193.253:8080/api/route/optimized/driver/${widget.driverId}'),
        headers: {
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final coordinates = data['features'][0]['geometry']['coordinates'];

        setState(() {
          _routePoints = coordinates
              .map<LatLng>((coord) => LatLng(coord[1], coord[0]))
              .toList();
          _isLoading = false;
        });
      } else {
        print("Error response: ${response.body}");
        setState(() {
          _hasError = true;
          _isLoading = false;
        });
      }
    } catch (e) {
      print("Fetch route error: $e");
      setState(() {
        _hasError = true;
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Bin Route Map")),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _hasError
              ? const Center(child: Text("Failed to load route"))
              : FlutterMap(
                  options: MapOptions(
                    center: _routePoints.isNotEmpty ? _routePoints.first : LatLng(0, 0),
                    zoom: 13.0,
                  ),
                  children: [
                    TileLayer(
                      urlTemplate: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
                      subdomains: const ['a', 'b', 'c'],
                    ),
                    PolylineLayer(
                      polylines: [
                        Polyline(
                          points: _routePoints,
                          strokeWidth: 4.0,
                          color: Colors.green,
                        ),
                      ],
                    ),
                    MarkerLayer(
  markers: _routePoints.map((point) {
    return Marker(
      point: point,
      width: 40,
      height: 40,
      child: Icon(Icons.delete, color: Colors.red),
    );
  }).toList(),
),


                  ],
                ),
    );
  }
}
