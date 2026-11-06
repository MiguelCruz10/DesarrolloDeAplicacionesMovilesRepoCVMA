import 'package:flutter/material.dart';
import 'screens/home_screen.dart';

void main() {
  runApp(const CatalogoApp());
}

class CatalogoApp extends StatefulWidget {
  const CatalogoApp({super.key});

  @override
  State<CatalogoApp> createState() => _CatalogoAppState();
}

class _CatalogoAppState extends State<CatalogoApp> {
  // Manejo del tema claro y oscuro (cumple el requisito de tema del sistema o toggle interactivo)
  ThemeMode _themeMode = ThemeMode.system;

  void _toggleTheme() {
    setState(() {
      if (_themeMode == ThemeMode.light) {
        _themeMode = ThemeMode.dark;
      } else {
        _themeMode = ThemeMode.light;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final bool isDark = _themeMode == ThemeMode.dark ||
        (_themeMode == ThemeMode.system &&
            MediaQuery.platformBrightnessOf(context) == Brightness.dark);

    return MaterialApp(
      title: 'Catálogo de UI - Flutter',
      debugShowCheckedModeBanner: false,
      themeMode: _themeMode,
      // Tema Claro con Material 3
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.indigo,
          brightness: Brightness.light,
        ),
        cardTheme: const CardThemeData(
          elevation: 1,
        ),
      ),
      // Tema Oscuro con Material 3
      darkTheme: ThemeData(
        useMaterial3: true,
        colorScheme: ColorScheme.fromSeed(
          seedColor: Colors.indigo,
          brightness: Brightness.dark,
        ),
        cardTheme: const CardThemeData(
          elevation: 1,
        ),
      ),
      home: HomeScreen(
        onToggleTheme: _toggleTheme,
        isDarkMode: isDark,
      ),
    );
  }
}
