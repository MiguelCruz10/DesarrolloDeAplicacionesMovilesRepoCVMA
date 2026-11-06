import 'package:flutter/material.dart';
import '../models/app_state.dart';
import 'section1_text_screen.dart';
import 'section2_buttons_screen.dart';
import 'section3_selection_screen.dart';
import 'section4_lists_screen.dart';
import 'section5_feedback_screen.dart';
import 'section6_containers_screen.dart';

class HomeScreen extends StatefulWidget {
  final VoidCallback onToggleTheme;
  final bool isDarkMode;

  const HomeScreen({
    super.key,
    required this.onToggleTheme,
    required this.isDarkMode,
  });

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _selectedIndex = 0;

  final List<Widget> _pantallas = const [
    Section1TextScreen(),
    Section2ButtonsScreen(),
    Section3SelectionScreen(),
    Section4ListsScreen(),
    Section5FeedbackScreen(),
    Section6ContainersScreen(),
  ];

  @override
  void initState() {
    super.initState();
    appState.addListener(_alCambiarEstado);
  }

  @override
  void dispose() {
    appState.removeListener(_alCambiarEstado);
    super.dispose();
  }

  void _alCambiarEstado() {
    if (mounted) setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            UserAccountsDrawerHeader(
              currentAccountPicture: const CircleAvatar(
                backgroundColor: Colors.white,
                child: Icon(Icons.person, size: 40, color: Colors.blueAccent),
              ),
              accountName: Text(
                appState.nombreUsuario,
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
              accountEmail: const Text('2024630153 - Grupo 7CV4'),
            ),
            ListTile(
              leading: const Icon(Icons.text_fields),
              title: const Text('1. Entradas de Texto'),
              selected: _selectedIndex == 0,
              onTap: () {
                setState(() => _selectedIndex = 0);
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.smart_button),
              title: const Text('2. Botones y Acciones'),
              selected: _selectedIndex == 1,
              onTap: () {
                setState(() => _selectedIndex = 1);
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.check_box_outlined),
              title: const Text('3. Elementos de Selección'),
              selected: _selectedIndex == 2,
              onTap: () {
                setState(() => _selectedIndex = 2);
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.format_list_bulleted),
              title: const Text('4. Listas y Colecciones'),
              selected: _selectedIndex == 3,
              onTap: () {
                setState(() => _selectedIndex = 3);
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.feedback_outlined),
              title: const Text('5. Feedback y Visuales'),
              selected: _selectedIndex == 4,
              onTap: () {
                setState(() => _selectedIndex = 4);
                Navigator.pop(context);
              },
            ),
            ListTile(
              leading: const Icon(Icons.dashboard_customize_outlined),
              title: const Text('6. Contenedores y Estructura'),
              selected: _selectedIndex == 5,
              onTap: () {
                setState(() => _selectedIndex = 5);
                Navigator.pop(context);
              },
            ),
            const Divider(),
            ListTile(
              leading: Icon(widget.isDarkMode ? Icons.light_mode : Icons.dark_mode),
              title: Text(widget.isDarkMode ? 'Cambiar a Modo Claro' : 'Cambiar a Modo Oscuro'),
              onTap: () {
                widget.onToggleTheme();
                Navigator.pop(context);
              },
            ),
          ],
        ),
      ),
      appBar: AppBar(
        title: const Text('Catálogo de UI - Flutter'),
        actions: [
          IconButton(
            icon: Icon(widget.isDarkMode ? Icons.light_mode : Icons.dark_mode),
            tooltip: 'Cambiar Modo Claro/Oscuro',
            onPressed: widget.onToggleTheme,
          ),
        ],
      ),
      body: IndexedStack(
        index: _selectedIndex,
        children: _pantallas,
      ),
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        onDestinationSelected: (int index) {
          setState(() => _selectedIndex = index);
        },
        destinations: const [
          NavigationDestination(
            icon: Icon(Icons.text_fields),
            label: 'Textos',
          ),
          NavigationDestination(
            icon: Icon(Icons.smart_button),
            label: 'Botones',
          ),
          NavigationDestination(
            icon: Icon(Icons.check_box_outlined),
            label: 'Selección',
          ),
          NavigationDestination(
            icon: Icon(Icons.format_list_bulleted),
            label: 'Listas',
          ),
          NavigationDestination(
            icon: Icon(Icons.feedback_outlined),
            label: 'Feedback',
          ),
          NavigationDestination(
            icon: Icon(Icons.dashboard_customize_outlined),
            label: 'Estructura',
          ),
        ],
      ),
    );
  }
}
