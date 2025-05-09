import 'dart:convert';
import 'package:http/http.dart' as http;

class BinService {
  static const String baseUrl = 'http://192.168.193.253:8080/api/bins';

  /// Fetch assigned bins for a driver
  static Future<List<dynamic>> getAssignedBins(String driverId) async {
    final url = Uri.parse('$baseUrl/driver/assigned-bins?driverId=$driverId');

    final response = await http.get(url);

    if (response.statusCode == 200) {
      return json.decode(response.body);
    }else if (response.statusCode == 404) {
    // No bins assigned, return empty list
    return [];
    } else {
      print('Failed to fetch assigned bins: ${response.statusCode}');
      print('Response body: ${response.body}');
      throw Exception('Failed to fetch assigned bins');
    }
  }
} 