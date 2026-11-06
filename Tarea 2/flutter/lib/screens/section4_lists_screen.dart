import 'package:flutter/material.dart';
import '../models/app_state.dart';

class Section4ListsScreen extends StatefulWidget {
  const Section4ListsScreen({super.key});

  @override
  State<Section4ListsScreen> createState() => _Section4ListsScreenState();
}

class _Section4ListsScreenState extends State<Section4ListsScreen>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 2, vsync: this);
    appState.addListener(_actualizar);
  }

  @override
  void dispose() {
    appState.removeListener(_actualizar);
    _tabController.dispose();
    super.dispose();
  }

  void _actualizar() {
    if (mounted) setState(() {});
  }

  void _mostrarDetalle(ItemElemento item) {
    showModalBottomSheet(
      context: context,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      builder: (context) {
        return Padding(
          padding: const EdgeInsets.all(24.0),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                children: [
                  CircleAvatar(
                    backgroundColor: Theme.of(context).colorScheme.primaryContainer,
                    child: Icon(item.icono, color: Theme.of(context).colorScheme.primary),
                  ),
                  const SizedBox(width: 16),
                  Expanded(
                    child: Text(
                      item.titulo,
                      style: Theme.of(context).textTheme.titleLarge?.copyWith(
                            fontWeight: FontWeight.bold,
                          ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              Chip(label: Text('Categoría: ${item.categoria}')),
              const SizedBox(height: 12),
              Text(
                item.descripcion,
                style: Theme.of(context).textTheme.bodyMedium,
              ),
              const SizedBox(height: 20),
              Align(
                alignment: Alignment.centerRight,
                child: FilledButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text('Cerrar'),
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
    final items = appState.elementos;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Sección 4: Listas y Colecciones'),
        bottom: TabBar(
          controller: _tabController,
          tabs: const [
            Tab(icon: Icon(Icons.view_list), text: 'Lista Vertical'),
            Tab(icon: Icon(Icons.grid_view), text: 'Cuadrícula (Grid)'),
          ],
        ),
      ),
      body: Column(
        children: [
          // Banner de estado vinculado
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            color: Theme.of(context).colorScheme.surfaceContainerHighest,
            child: Row(
              children: [
                const Icon(Icons.filter_list, size: 20),
                const SizedBox(width: 8),
                Expanded(
                  child: Text(
                    'Filtro actual: ${appState.categoriaFavorita} (cambia en Sección 3) | Total: ${items.length}',
                    style: const TextStyle(fontSize: 12, fontWeight: FontWeight.bold),
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.refresh, size: 20),
                  tooltip: 'Reiniciar lista completa',
                  onPressed: () => appState.recargarElementos(),
                )
              ],
            ),
          ),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: [
                // Vista 1: Lista Vertical con Pull-to-refresh, Swipe to dismiss y Empty State
                items.isEmpty
                    ? _buildEmptyState()
                    : RefreshIndicator(
                        onRefresh: () async {
                          await Future.delayed(const Duration(seconds: 1));
                          appState.recargarElementos();
                        },
                        child: ListView.separated(
                          padding: const EdgeInsets.all(12),
                          itemCount: items.length,
                          separatorBuilder: (context, index) => const Divider(height: 1),
                          itemBuilder: (context, index) {
                            final item = items[index];
                            return Dismissible(
                              key: ValueKey(item.id),
                              direction: DismissDirection.endToStart,
                              background: Container(
                                alignment: Alignment.centerRight,
                                padding: const EdgeInsets.only(right: 20),
                                color: Theme.of(context).colorScheme.error,
                                child: const Icon(Icons.delete, color: Colors.white),
                              ),
                              onDismissed: (direction) {
                                appState.eliminarElemento(item.id);
                                ScaffoldMessenger.of(context).showSnackBar(
                                  SnackBar(
                                    content: Text('Se eliminó "${item.titulo}"'),
                                    action: SnackBarAction(
                                      label: 'Deshacer',
                                      onPressed: () {
                                        appState.agregarElemento(item.titulo, item.categoria);
                                      },
                                    ),
                                  ),
                                );
                              },
                              child: ListTile(
                                leading: CircleAvatar(
                                  child: Icon(item.icono, size: 20),
                                ),
                                title: Text(item.titulo, style: const TextStyle(fontWeight: FontWeight.bold)),
                                subtitle: Text('${item.categoria} • Desliza a la izquierda para borrar'),
                                trailing: const Icon(Icons.chevron_right),
                                onTap: () => _mostrarDetalle(item),
                              ),
                            );
                          },
                        ),
                      ),

                // Vista 2: Cuadrícula (GridView)
                items.isEmpty
                    ? _buildEmptyState()
                    : GridView.builder(
                        padding: const EdgeInsets.all(12),
                        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                          crossAxisCount: 2,
                          crossAxisSpacing: 10,
                          mainAxisSpacing: 10,
                          childAspectRatio: 1.1,
                        ),
                        itemCount: items.length,
                        itemBuilder: (context, index) {
                          final item = items[index];
                          return Card(
                            elevation: 2,
                            child: InkWell(
                              borderRadius: BorderRadius.circular(12),
                              onTap: () => _mostrarDetalle(item),
                              child: Padding(
                                padding: const EdgeInsets.all(12),
                                child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  children: [
                                    Icon(item.icono, size: 36, color: Theme.of(context).colorScheme.primary),
                                    const SizedBox(height: 8),
                                    Text(
                                      item.titulo,
                                      textAlign: TextAlign.center,
                                      maxLines: 1,
                                      overflow: TextOverflow.ellipsis,
                                      style: const TextStyle(fontWeight: FontWeight.bold),
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      item.categoria,
                                      style: TextStyle(
                                        fontSize: 12,
                                        color: Theme.of(context).colorScheme.outline,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          );
                        },
                      ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildEmptyState() {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.inbox_outlined, size: 64, color: Theme.of(context).colorScheme.outline),
            const SizedBox(height: 16),
            Text(
              'No hay elementos disponibles',
              style: Theme.of(context).textTheme.titleMedium?.copyWith(fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              'Prueba seleccionando "Todos" en la Sección 3 o presiona el botón para recargar la lista.',
              textAlign: TextAlign.center,
              style: TextStyle(color: Theme.of(context).colorScheme.outline),
            ),
            const SizedBox(height: 16),
            FilledButton.icon(
              onPressed: () => appState.recargarElementos(),
              icon: const Icon(Icons.refresh),
              label: const Text('Recargar Lista'),
            ),
          ],
        ),
      ),
    );
  }
}
