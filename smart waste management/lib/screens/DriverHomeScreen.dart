import 'package:flutter/material.dart';
import 'package:flutter_application_1/screens/AssignedBinsScreen.dart';
import 'package:flutter_application_1/screens/RouteMapScreen.dart'; // Import the RouteMapScreen

class DriverHomeScreen extends StatelessWidget {
  final String driverId;

  const DriverHomeScreen({super.key, required this.driverId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Driver Home")),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => AssignedBinsScreen(driverId: driverId),
                  ),
                );
              },
              child: const Text("Assigned Bins"),
            ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) => RouteMapScreen(driverId: driverId),
                  ),
                );
              },
              child: const Text("Route Screen"),
            ),
          ],
        ),
      ),
    );
  }
}
