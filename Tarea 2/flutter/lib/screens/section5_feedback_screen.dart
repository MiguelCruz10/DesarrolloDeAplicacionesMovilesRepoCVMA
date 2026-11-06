import 'package:flutter/material.dart';
import '../models/app_state.dart';

class Section5FeedbackScreen extends StatefulWidget {
  const Section5FeedbackScreen({super.key});

  @override
  State<Section5FeedbackScreen> createState() => _Section5FeedbackScreenState();
}

class _Section5FeedbackScreenState extends State<Section5FeedbackScreen> {
  final double _progreso = 0.65;
  int _contadorNotificaciones = 3;

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

  void _mostrarDialogoConfirmacion() {
    showDialog(
      context: context,
      builder: (BuildContext ctx) {
        return AlertDialog(
          title: const Text('Confirmar Operación'),
          content: Text(
            '¿Deseas guardar los cambios realizados para el usuario "${appState.nombreUsuario}"?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(ctx).pop(),
              child: const Text('Cancelar'),
            ),
            FilledButton(
              onPressed: () {
                Navigator.of(ctx).pop();
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(content: Text('¡Operación confirmada con éxito!')),
                );
              },
              child: const Text('Confirmar'),
            ),
          ],
        );
      },
    );
  }

  void _mostrarBottomSheet() {
    showModalBottomSheet(
      context: context,
      showDragHandle: true,
      builder: (ctx) {
        return Container(
          width: double.infinity,
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Hoja Inferior Modal (Bottom Sheet)',
                style: Theme.of(context).textTheme.titleLarge?.copyWith(
                      fontWeight: FontWeight.bold,
                    ),
              ),
              const SizedBox(height: 8),
              Text(
                'Usuario actual vinculado: ${appState.nombreUsuario}',
                style: const TextStyle(fontWeight: FontWeight.w500),
              ),
              const SizedBox(height: 12),
              const Text(
                'Las hojas inferiores permiten presentar información contextual, menús de opciones o tareas secundarias sin abandonar la vista actual.',
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: double.infinity,
                child: FilledButton(
                  onPressed: () => Navigator.pop(ctx),
                  child: const Text('Entendido'),
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Sección 5: Feedback y Visuales'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // 1. Textos con distintos estilos, tamaños y énfasis
            _buildCard(
              title: '1. Tipografías, Estilos y Jerarquía Visual',
              explanation:
                  'Presenta información textual variando peso, escala tipográfica de Material 3 y colores de énfasis.',
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Titular Grande (Headline Large)',
                    style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                          fontWeight: FontWeight.bold,
                        ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    'Subtítulo con énfasis secundario para el usuario: ${appState.nombreUsuario}',
                    style: TextStyle(
                      color: Theme.of(context).colorScheme.primary,
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    'Cuerpo de texto estándar (Body Medium). Demuestra legibilidad, interlineado y contraste tanto en modo claro como en modo oscuro.',
                    style: Theme.of(context).textTheme.bodyMedium,
                  ),
                ],
              ),
            ),

            // 2. Modos de escalado de imágenes
            _buildCard(
              title: '2. Carga y Escalado de Imágenes (BoxFit)',
              explanation:
                  'Demuestra cómo encajar imágenes adaptándose a diferentes relaciones de aspecto: BoxFit.cover (recorte proporcional) y BoxFit.contain.',
              child: Row(
                children: [
                  Expanded(
                    child: Column(
                      children: [
                        ClipRRect(
                          borderRadius: BorderRadius.circular(8),
                          child: Container(
                            height: 100,
                            color: Colors.grey.shade300,
                            child: Image.network(
                              'https://picsum.photos/200/200',
                              fit: BoxFit.cover,
                              errorBuilder: (context, error, stackTrace) =>
                                  const Icon(Icons.broken_image, size: 40),
                            ),
                          ),
                        ),
                        const SizedBox(height: 4),
                        const Text('BoxFit.cover', style: TextStyle(fontSize: 12)),
                      ],
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      children: [
                        ClipRRect(
                          borderRadius: BorderRadius.circular(8),
                          child: Container(
                            height: 100,
                            color: Colors.grey.shade300,
                            child: Image.network(
                              'https://picsum.photos/300/200',
                              fit: BoxFit.contain,
                              errorBuilder: (context, error, stackTrace) =>
                                  const Icon(Icons.broken_image, size: 40),
                            ),
                          ),
                        ),
                        const SizedBox(height: 4),
                        const Text('BoxFit.contain', style: TextStyle(fontSize: 12)),
                      ],
                    ),
                  ),
                ],
              ),
            ),

            // 3. Indicadores de progreso (lineal y circular)
            _buildCard(
              title: '3. Barras de Progreso: Lineal y Circular',
              explanation:
                  'Permiten comunicar procesos continuos o tareas con duración conocida (determinado) e indefinida (indeterminado).',
              child: Column(
                children: [
                  LinearProgressIndicator(value: _progreso),
                  const SizedBox(height: 8),
                  Text('Progreso determinado: ${(_progreso * 100).round()}%'),
                  const SizedBox(height: 16),
                  const Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Column(
                        children: [
                          CircularProgressIndicator(),
                          SizedBox(height: 8),
                          Text('Indeterminado', style: TextStyle(fontSize: 12)),
                        ],
                      ),
                      Column(
                        children: [
                          CircularProgressIndicator(value: 0.75),
                          SizedBox(height: 8),
                          Text('Determinado (75%)', style: TextStyle(fontSize: 12)),
                        ],
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // 4. Mensajes emergentes, diálogos y bottom sheet
            _buildCard(
              title: '4. Mensajes y Diálogos de Retroalimentación',
              explanation:
                  'SnackBar con acción interactiva, cuadro de diálogo modal de confirmación y hoja inferior deslizable.',
              child: Wrap(
                spacing: 12,
                runSpacing: 8,
                children: [
                  FilledButton.tonal(
                    onPressed: () {
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: const Text('Acción guardada correctamente'),
                          action: SnackBarAction(
                            label: 'Deshacer',
                            onPressed: () {},
                          ),
                        ),
                      );
                    },
                    child: const Text('Mostrar SnackBar con Acción'),
                  ),
                  OutlinedButton(
                    onPressed: _mostrarDialogoConfirmacion,
                    child: const Text('Diálogo de Confirmación'),
                  ),
                  ElevatedButton(
                    onPressed: _mostrarBottomSheet,
                    child: const Text('Abrir Bottom Sheet'),
                  ),
                ],
              ),
            ),

            // 5. Tarjetas, separadores y distintivos numéricos (Badges)
            _buildCard(
              title: '5. Tarjeta con Separador y Distintivo (Badge)',
              explanation:
                  'Los badges alertan de notificaciones pendientes y los separadores diferencian secciones lógicas en una tarjeta.',
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  Badge(
                    label: Text('$_contadorNotificaciones'),
                    child: IconButton.filledTonal(
                      icon: const Icon(Icons.notifications),
                      onPressed: () {
                        setState(() => _contadorNotificaciones++);
                      },
                    ),
                  ),
                  const VerticalDivider(width: 32),
                  const Text('Toca la campana para sumar badges'),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
