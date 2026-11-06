import 'package:flutter/material.dart';

class Section6ContainersScreen extends StatelessWidget {
  const Section6ContainersScreen({super.key});

  Widget _buildCard({
    required BuildContext context,
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
        title: const Text('Sección 6: Contenedores y Estructura'),
        actions: [
          IconButton(
            icon: const Icon(Icons.info_outline),
            tooltip: 'Información de estructura',
            onPressed: () {
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(content: Text('Acción de AppBar superior ejecutada.')),
              );
            },
          ),
        ],
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            // 1. Distribución en Fila (Row) y Columna (Column)
            _buildCard(
              context: context,
              title: '1. Distribución en Fila (Row) y Columna (Column)',
              explanation:
                  'Row organiza sus hijos de manera horizontal secuencial; Column los organiza verticalmente.',
              child: Column(
                children: [
                  const Text('Ejemplo de Fila (Row):', style: TextStyle(fontWeight: FontWeight.bold)),
                  const SizedBox(height: 8),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Theme.of(context).colorScheme.primaryContainer,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: const Text('Caja A (Row)'),
                      ),
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Theme.of(context).colorScheme.secondaryContainer,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: const Text('Caja B (Row)'),
                      ),
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Theme.of(context).colorScheme.tertiaryContainer,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: const Text('Caja C (Row)'),
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // 2. Vista superpuesta (Stack)
            _buildCard(
              context: context,
              title: '2. Vista Superpuesta (Stack)',
              explanation:
                  'Permite superponer elementos en capas según el orden del árbol (eje Z), ideal para insignias, portadas con texto flotante o fondos.',
              child: Center(
                child: SizedBox(
                  width: 260,
                  height: 140,
                  child: Stack(
                    children: [
                      // Capa inferior: Fondo
                      Container(
                        decoration: BoxDecoration(
                          borderRadius: BorderRadius.circular(16),
                          gradient: LinearGradient(
                            colors: [
                              Theme.of(context).colorScheme.primary,
                              Theme.of(context).colorScheme.tertiary,
                            ],
                          ),
                        ),
                      ),
                      // Capa intermedia: Texto central
                      const Center(
                        child: Text(
                          'Capa de Fondo (Stack)',
                          style: TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 16,
                          ),
                        ),
                      ),
                      // Capa superior: Etiqueta posicionada
                      Positioned(
                        top: 12,
                        right: 12,
                        child: Container(
                          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                          decoration: BoxDecoration(
                            color: Colors.black.withOpacity(0.6),
                            borderRadius: BorderRadius.circular(12),
                          ),
                          child: const Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              Icon(Icons.layers, color: Colors.white, size: 14),
                              SizedBox(width: 4),
                              Text('Superpuesto', style: TextStyle(color: Colors.white, fontSize: 11)),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),

            // 3. Distribución con restricciones o pesos proporcionales (Expanded / Flexible)
            _buildCard(
              context: context,
              title: '3. Distribución Proporcional con Pesos (Flex / Expanded)',
              explanation:
                  'Divide el espacio disponible con proporciones exactas mediante el parámetro flex (1x, 2x y 1x sumando 4 partes totales).',
              child: Column(
                children: [
                  Row(
                    children: [
                      Expanded(
                        flex: 1,
                        child: Container(
                          height: 48,
                          color: Colors.teal.shade300,
                          alignment: Alignment.center,
                          child: const Text('Flex: 1 (25%)', style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                      ),
                      const SizedBox(width: 4),
                      Expanded(
                        flex: 2,
                        child: Container(
                          height: 48,
                          color: Colors.teal.shade500,
                          alignment: Alignment.center,
                          child: const Text('Flex: 2 (50%)', style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold)),
                        ),
                      ),
                      const SizedBox(width: 4),
                      Expanded(
                        flex: 1,
                        child: Container(
                          height: 48,
                          color: Colors.teal.shade300,
                          alignment: Alignment.center,
                          child: const Text('Flex: 1 (25%)', style: TextStyle(fontWeight: FontWeight.bold)),
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),

            // 4. Contenedor con desplazamiento vertical (SingleChildScrollView)
            _buildCard(
              context: context,
              title: '4. Contenedor Desplazable (Scroll Vertical)',
              explanation:
                  'Toda esta pantalla está contenida en un SingleChildScrollView, lo que previene desbordamientos de pantalla (RenderFlex overflow) en cualquier tamaño de pantalla.',
              child: const Row(
                children: [
                  Icon(Icons.unfold_more, size: 30),
                  SizedBox(width: 12),
                  Expanded(
                    child: Text(
                      'Prueba deslizando verticalmente hacia arriba y hacia abajo para navegar por todos los componentes.',
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
