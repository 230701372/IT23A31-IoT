import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _usernameController = TextEditingController();
  final _passwordController = TextEditingController();
  final _truckIdController = TextEditingController();

  String _message = '';

  Future<void> _register() async {
    // Attempt to create the truck
    final truckResponse = await http.post(
      Uri.parse('http://192.168.193.253:8080/api/truck/create'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'truckId': _truckIdController.text.trim(),
      }),
    );

    // Handle truck creation response
    if (truckResponse.statusCode == 200) {
      // If truck creation is successful, proceed with driver registration
      final driverResponse = await http.post(
        Uri.parse('http://192.168.193.253:8080/driver/register'),
        headers: {'Content-Type': 'application/json'},
        body: json.encode({
          'username': _usernameController.text.trim(),
          'password': _passwordController.text.trim(),
          'truckId': _truckIdController.text.trim(),
        }),
      );

      if (driverResponse.statusCode == 200) {
        setState(() => _message = 'Registered successfully. Go to login.');
      } else if (driverResponse.statusCode == 409) {
        setState(() => _message = 'Username already exists.');
      } else {
        setState(() => _message = 'Driver registration failed.');
      }
    } else if (truckResponse.statusCode == 400) {
      setState(() => _message = 'Truck ID already exists.');
    } else {
      setState(() => _message = 'Truck creation failed.');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.green.shade50,
      body: Center(
        child: Card(
          margin: const EdgeInsets.symmetric(horizontal: 24),
          shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(16)),
          elevation: 8,
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Text("Driver Registration", style: TextStyle(fontSize: 24, fontWeight: FontWeight.bold)),
                const SizedBox(height: 20),
                TextField(
                  controller: _usernameController,
                  decoration: const InputDecoration(labelText: 'Username', prefixIcon: Icon(Icons.person)),
                ),
                TextField(
                  controller: _passwordController,
                  obscureText: true,
                  decoration: const InputDecoration(labelText: 'Password', prefixIcon: Icon(Icons.lock)),
                ),
                TextField(
                  controller: _truckIdController,
                  decoration: const InputDecoration(
                    labelText: 'Truck ID',
                    prefixIcon: Icon(Icons.local_shipping),
                  ),
                ),
                const SizedBox(height: 20),
                ElevatedButton(
                  onPressed: _register,
                  style: ElevatedButton.styleFrom(minimumSize: const Size.fromHeight(50)),
                  child: const Text("Register"),
                ),
                if (_message.isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.only(top: 12),
                    child: Text(_message, style: const TextStyle(color: Colors.black87)),
                  ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}