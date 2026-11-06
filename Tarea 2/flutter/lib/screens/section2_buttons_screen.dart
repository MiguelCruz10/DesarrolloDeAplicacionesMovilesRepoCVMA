import 'package:flutter/material.dart';

class Section2ButtonsScreen extends StatefulWidget {
  const Section2ButtonsScreen({super.key});

  @override
  State<Section2ButtonsScreen> createState() => _Section2ButtonsScreenState();
}

class _Section2ButtonsScreenState extends State<Section2ButtonsScreen> {
  String _ultimoMensaje = 'Ningún botón pulsado aún.';
  bool _estaCargando = false;
  int _indiceSegmentado = 0;

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

  void _notificar(String boton) {
    setState(() {
      _ultimoMensaje = 'Acción ejecutada: $boton';
    });
    ScaffoldMessenger.of(context).hideCurrentSnackBar();
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text('Pulsaste: $boton'),
        duration: const Duration(seconds: 1),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sección 2: Botones y Acciones'),
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () => _notificar('Botón de Acción Flotante Extendido (FAB)'),
        icon: const Icon(Icons.add),
        label: const Text('FAB Extendido'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // Banner de respuesta interactiva
            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Theme.of(context).colorScheme.primaryContainer,
                borderRadius: BorderRadius.circular(12),
              ),
              child: Row(
                children: [
                  Icon(
                    Icons.info_outline,
                    color: Theme.of(context).colorScheme.onPrimaryContainer,
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Text(
                      _ultimoMensaje,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        color: Theme.of(context).colorScheme.onPrimaryContainer,
                      ),
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 8),

            // 1. Botones primarios, contorno y texto
            _buildCard(
              title: '1. Botones: Relleno, Contorno y Texto',
              explanation:
                  'Representan la jerarquía visual de acciones: alta prioridad (Filled/Elevated), media prioridad (Outlined) y baja prioridad (TextButton).',
              child: Wrap(
                spacing: 12,
                runSpacing: 12,
                children: [
                  FilledButton(
                    onPressed: () => _notificar('Botón Relleno (FilledButton)'),
                    child: const Text('Relleno'),
                  ),
                  OutlinedButton(
                    onPressed: () => _notificar('Botón con Contorno (Outlined)'),
                    child: const Text('Contorno'),
                  ),
                  TextButton(
                    onPressed: () => _notificar('Botón de Texto (TextButton)'),
                    child: const Text('Solo Texto'),
                  ),
                ],
              ),
            ),

            // 2. Botón de icono y botón con icono más texto
            _buildCard(
              title: '2. Botón de Icono y Botón Mixto (Icono + Texto)',
              explanation:
                  'El botón de icono ahorra espacio para acciones directas (como favoritos o compartir). El mixto aporta claridad contextual.',
              child: Wrap(
                spacing: 16,
                crossAxisAlignment: WrapCrossAlignment.center,
                children: [
                  IconButton.filled(
                    onPressed: () => _notificar('Botón de Icono Favorito'),
                    icon: const Icon(Icons.favorite),
                    tooltip: 'Marcar Favorito',
                  ),
                  IconButton.outlined(
                    onPressed: () => _notificar('Botón de Icono Compartir'),
                    icon: const Icon(Icons.share),
                    tooltip: 'Compartir',
                  ),
                  ElevatedButton.icon(
                    onPressed: () => _notificar('Botón Descargar con Icono'),
                    icon: const Icon(Icons.download),
                    label: const Text('Descargar'),
                  ),
                ],
              ),
            ),

            // 3. Botón de alternancia (Segmented Button)
            _buildCard(
              title: '3. Botón de Alternancia / Segmentado',
              explanation:
                  'Permite elegir una opción entre varias posibilidades mutuamente excluyentes agrupadas en un solo componente estilizado.',
              child: SegmentedButton<int>(
                segments: const [
                  ButtonSegment<int>(
                    value: 0,
                    label: Text('Día'),
                    icon: Icon(Icons.wb_sunny_outlined),
                  ),
                  ButtonSegment<int>(
                    value: 1,
                    label: Text('Semana'),
                    icon: Icon(Icons.calendar_view_week),
                  ),
                  ButtonSegment<int>(
                    value: 2,
                    label: Text('Mes'),
                    icon: Icon(Icons.calendar_month),
                  ),
                ],
                selected: {_indiceSegmentado},
                onSelectionChanged: (Set<int> nuevoIndice) {
                  setState(() {
                    _indiceSegmentado = nuevoIndice.first;
                    final nombres = ['Día', 'Semana', 'Mes'];
                    _notificar('Segmento seleccionado: ${nombres[_indiceSegmentado]}');
                  });
                },
              ),
            ),

            // 4. Botón deshabilitado y botón en estado de carga
            _buildCard(
              title: '4. Botón Deshabilitado y en Estado de Carga',
              explanation:
                  'Indican que una acción no está disponible temporalmente o que está ejecutando un proceso en segundo plano de manera asíncrona.',
              child: Row(
                children: [
                  const ElevatedButton(
                    onPressed: null, // Deshabilitado pasando null
                    child: Text('Deshabilitado'),
                  ),
                  const SizedBox(width: 16),
                  ElevatedButton(
                    onPressed: _estaCargando
                        ? null
                        : () {
                            setState(() => _estaCargando = true);
                            _notificar('Procesando solicitud de carga...');
                            Future.delayed(const Duration(seconds: 3), () {
                              if (mounted) {
                                setState(() => _estaCargando = false);
                                _notificar('¡Carga completada con éxito!');
                              }
                            });
                          },
                    child: _estaCargando
                        ? const SizedBox(
                            width: 20,
                            height: 20,
                            child: CircularProgressIndicator(strokeWidth: 2),
                          )
                        : const Text('Simular Carga (3s)'),
                  ),
                ],
              ),
            ),

            // 5. Demostración de FAB normal en el cuerpo
            _buildCard(
              title: '5. Botón de Acción Flotante (FAB Normal)',
              explanation:
                  'Acción primaria en pantalla que flota sobre el contenido. Observa también el FAB extendido anclado en la parte inferior derecha.',
              child: Row(
                children: [
                  FloatingActionButton(
                    mini: true,
                    onPressed: () => _notificar('Mini FAB normal'),
                    child: const Icon(Icons.edit),
                  ),
                  const SizedBox(width: 16),
                  FloatingActionButton(
                    onPressed: () => _notificar('FAB estándar circular'),
                    child: const Icon(Icons.navigation),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 60), // Margen para el FAB anclado
          ],
        ),
      ),
    );
  }
}
