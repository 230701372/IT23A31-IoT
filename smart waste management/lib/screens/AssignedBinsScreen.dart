import 'package:flutter/material.dart';
import 'package:flutter_application_1/service/bin_service.dart';

class AssignedBinsScreen extends StatefulWidget {
  final String driverId;

  const AssignedBinsScreen({super.key, required this.driverId});

  @override
  State<AssignedBinsScreen> createState() => _AssignedBinsScreenState();
}

class _AssignedBinsScreenState extends State<AssignedBinsScreen> {
  List<dynamic> _bins = [];
  bool _isLoading = true;
  bool _hasError = false;

  @override
  void initState() {
    super.initState();
    _fetchAssignedBins();
  }

  Future<void> _fetchAssignedBins() async {
    try {
      final bins = await BinService.getAssignedBins(widget.driverId);
      setState(() {
        _bins = bins;
        _isLoading = false;
      });
    } catch (e) {
      print('Error fetching assigned bins: $e');
      setState(() {
        _hasError = true;
        _isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Assigned Bins")),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _hasError
              ? const Center(child: Text('Error loading assigned bins'))
              : _bins.isEmpty
                  ? const Center(child: Text('No assigned bins'))
                  : ListView.builder(
                      itemCount: _bins.length,
                      itemBuilder: (context, index) {
                        final bin = _bins[index];
                        return ListTile(
                          title: Text("Bin ID: ${bin['id']}"),
                          subtitle: Text("Location: ${bin['latitude']}, ${bin['longitude']}"),
                        );
                      },
                    ),
    );
  }
}
