import 'package:flutter/material.dart';
import '../models/app_state.dart';

class Section1TextScreen extends StatefulWidget {
  const Section1TextScreen({super.key});

  @override
  State<Section1TextScreen> createState() => _Section1TextScreenState();
}

class _Section1TextScreenState extends State<Section1TextScreen> {
  final _formKey = GlobalKey<FormState>();
  final _textSimpleCtrl = TextEditingController();
  final _textValidacionCtrl = TextEditingController();
  final _passwordCtrl = TextEditingController();
  final _multilineCtrl = TextEditingController();
  final _searchCtrl = TextEditingController();

  bool _obscurePassword = true;
  String _resultadoBusqueda = '';

  final List<String> _sugerencias = [
    'Android',
    'Flutter',
    'Jetpack Compose',
    'Kotlin',
    'Dart',
    'Material 3',
    'Widgets',
    'Views XML'
  ];

  @override
  void dispose() {
    _textSimpleCtrl.dispose();
    _textValidacionCtrl.dispose();
    _passwordCtrl.dispose();
    _multilineCtrl.dispose();
    _searchCtrl.dispose();
    super.dispose();
  }

  Widget _buildCard({
    required String title,
    required String explanation,
    required Widget child,
  }) {
    return Card(
      margin: const EdgeInsets.symmetric(vertical: 8),
      elevation: 1,
      child: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              title,
              style: Theme.of(context).textTheme.titleMedium?.copyWith(
                    fontWeight: FontWeight.bold,
                  ),
            ),
            const SizedBox(height: 4),
            Text(
              explanation,
              style: Theme.of(context).textTheme.bodySmall?.copyWith(
                    color: Theme.of(context).colorScheme.outline,
                  ),
            ),
            const Divider(height: 20),
            child,
          ],
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sección 1: Entrada de Texto'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              // 1. Campo de texto simple
              _buildCard(
                title: '1. Campo de texto simple',
                explanation:
                    'Permite capturar cadenas de texto estándar del usuario. En este caso actualiza el nombre global de la aplicación (vinculado con Sección 4 y 5).',
                child: TextField(
                  controller: _textSimpleCtrl,
                  decoration: InputDecoration(
                    labelText: 'Nombre de usuario',
                    hintText: 'Ej. Miguel Ángel',
                    border: const OutlineInputBorder(),
                    prefixIcon: const Icon(Icons.person),
                    suffixIcon: IconButton(
                      icon: const Icon(Icons.save),
                      tooltip: 'Guardar globalmente',
                      onPressed: () {
                        appState.actualizarNombre(_textSimpleCtrl.text);
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text(
                              'Nombre global actualizado a: ${appState.nombreUsuario}',
                            ),
                            duration: const Duration(seconds: 2),
                          ),
                        );
                      },
                    ),
                  ),
                ),
              ),

              // 2. Campo con validación y mensaje de error visible
              _buildCard(
                title: '2. Campo con validación y error visible',
                explanation:
                    'Evalúa en tiempo real si el valor cumple con una regla (mínimo 5 caracteres y no vacío). Si falla, muestra un mensaje de error.',
                child: Column(
                  children: [
                    TextFormField(
                      controller: _textValidacionCtrl,
                      decoration: const InputDecoration(
                        labelText: 'Correo electrónico o matrícula',
                        hintText: 'Mínimo 5 caracteres',
                        border: OutlineInputBorder(),
                        prefixIcon: Icon(Icons.email),
                      ),
                      validator: (value) {
                        if (value == null || value.trim().isEmpty) {
                          return 'Este campo no puede estar vacío';
                        }
                        if (value.trim().length < 5) {
                          return 'Debe contener al menos 5 caracteres';
                        }
                        return null;
                      },
                    ),
                    const SizedBox(height: 10),
                    ElevatedButton.icon(
                      onPressed: () {
                        _formKey.currentState?.validate();
                      },
                      icon: const Icon(Icons.check_circle_outline),
                      label: const Text('Comprobar Validación'),
                    ),
                  ],
                ),
              ),

              // 3. Campo de contraseña con opción para mostrar u ocultar
              _buildCard(
                title: '3. Contraseña con toggle de visibilidad',
                explanation:
                    'Oculta los caracteres ingresados por privacidad pero permite al usuario conmutar la visibilidad mediante un icono de ojo.',
                child: TextField(
                  controller: _passwordCtrl,
                  obscureText: _obscurePassword,
                  decoration: InputDecoration(
                    labelText: 'Contraseña de acceso',
                    border: const OutlineInputBorder(),
                    prefixIcon: const Icon(Icons.lock),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscurePassword
                            ? Icons.visibility_off
                            : Icons.visibility,
                      ),
                      onPressed: () {
                        setState(() {
                          _obscurePassword = !_obscurePassword;
                        });
                      },
                    ),
                  ),
                ),
              ),

              // 4. Campos con distintos tipos de teclado
              _buildCard(
                title: '4. Teclados numérico y telefónico',
                explanation:
                    'Modifican el software de teclado mostrado al usuario para facilitar la entrada de datos numéricos o teléfonos.',
                child: Column(
                  children: [
                    const TextField(
                      keyboardType: TextInputType.number,
                      decoration: InputDecoration(
                        labelText: 'Teclado Numérico (Ej. Boleta/PIN)',
                        border: OutlineInputBorder(),
                        prefixIcon: Icon(Icons.numbers),
                      ),
                    ),
                    const SizedBox(height: 12),
                    const TextField(
                      keyboardType: TextInputType.phone,
                      decoration: InputDecoration(
                        labelText: 'Teclado Telefónico (Ej. 55-1234-5678)',
                        border: OutlineInputBorder(),
                        prefixIcon: Icon(Icons.phone),
                      ),
                    ),
                  ],
                ),
              ),

              // 5. Campo multilínea
              _buildCard(
                title: '5. Campo multilínea',
                explanation:
                    'Permite la captura de bloques de texto largos como comentarios o notas, expandiéndose dinámicamente con múltiples renglones.',
                child: TextField(
                  controller: _multilineCtrl,
                  maxLines: 3,
                  keyboardType: TextInputType.multiline,
                  decoration: const InputDecoration(
                    labelText: 'Comentarios o Descripción',
                    hintText: 'Escribe varias líneas de texto...',
                    border: OutlineInputBorder(),
                    alignLabelWithHint: true,
                  ),
                ),
              ),

              // 6. Campo con sugerencias automáticas o desplegable
              _buildCard(
                title: '6. Autocompletado / Sugerencias',
                explanation:
                    'Ofrece una lista de sugerencias predictivas filtradas conforme el usuario escribe texto (ej: Android, Flutter, Kotlin).',
                child: Autocomplete<String>(
                  optionsBuilder: (TextEditingValue textEditingValue) {
                    if (textEditingValue.text.isEmpty) {
                      return const Iterable<String>.empty();
                    }
                    return _sugerencias.where((String option) {
                      return option
                          .toLowerCase()
                          .contains(textEditingValue.text.toLowerCase());
                    });
                  },
                  onSelected: (String seleccion) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Seleccionaste: $seleccion')),
                    );
                  },
                  fieldViewBuilder:
                      (context, controller, focusNode, onEditingComplete) {
                    return TextField(
                      controller: controller,
                      focusNode: focusNode,
                      decoration: const InputDecoration(
                        labelText: 'Escribe una tecnología...',
                        border: OutlineInputBorder(),
                        prefixIcon: Icon(Icons.auto_awesome),
                      ),
                    );
                  },
                ),
              ),

              // 7. Barra de búsqueda
              _buildCard(
                title: '7. Barra de búsqueda',
                explanation:
                    'Facilita la consulta y filtrado de contenido en la aplicación, con botón para borrar el término o ejecutar la búsqueda.',
                child: Column(
                  children: [
                    SearchBar(
                      controller: _searchCtrl,
                      hintText: 'Buscar en el catálogo...',
                      leading: const Icon(Icons.search),
                      trailing: [
                        IconButton(
                          icon: const Icon(Icons.clear),
                          onPressed: () {
                            setState(() {
                              _searchCtrl.clear();
                              _resultadoBusqueda = '';
                            });
                          },
                        ),
                      ],
                      onSubmitted: (query) {
                        setState(() {
                          _resultadoBusqueda =
                              query.isEmpty ? '' : 'Buscando: "$query"';
                        });
                      },
                    ),
                    if (_resultadoBusqueda.isNotEmpty) ...[
                      const SizedBox(height: 8),
                      Text(
                        _resultadoBusqueda,
                        style: TextStyle(
                          color: Theme.of(context).colorScheme.primary,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ]
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
