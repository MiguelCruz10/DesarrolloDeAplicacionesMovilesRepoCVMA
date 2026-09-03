import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Práctica 1 - Flutter'),
          backgroundColor: Colors.blue,
        ),
        body: const Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Text(
                'Hola Mundo',
                style: TextStyle(fontSize: 28, fontWeight: FontWeight.bold, color: Colors.green),
              ),
              SizedBox(height: 12),
              Text('Cruz Villa Miguel Ángel', style: TextStyle(fontSize: 20)),
              SizedBox(height: 8),
              Text('Boleta: 2024630153', style: TextStyle(fontSize: 18)),
              SizedBox(height: 8),
              Text('Grupo: 7CV4', style: TextStyle(fontSize: 18)),
            ],
          ),
        ),
      ),
    );
  }
}