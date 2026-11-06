import 'package:flutter/material.dart';

/// Modelo de datos para las listas de la Sección 4
class ItemElemento {
  final int id;
  final String titulo;
  final String categoria;
  final String descripcion;
  final IconData icono;

  ItemElemento({
    required this.id,
    required this.titulo,
    required this.categoria,
    required this.descripcion,
    required this.icono,
  });
}

/// Estado global sencillo mediante ChangeNotifier para vincular datos entre secciones
class AppState extends ChangeNotifier {
  // Dato vinculado de la Sección 1 (Texto ingresado por el usuario)
  String _nombreUsuario = 'Estudiante';
  String get nombreUsuario => _nombreUsuario;

  void actualizarNombre(String nuevoNombre) {
    if (nuevoNombre.trim().isNotEmpty) {
      _nombreUsuario = nuevoNombre.trim();
      notifyListeners();
    }
  }

  // Dato vinculado de la Sección 3 (Preferencia de categoría seleccionada)
  String _categoriaFavorita = 'Todos';
  String get categoriaFavorita => _categoriaFavorita;

  void actualizarCategoria(String nuevaCategoria) {
    _categoriaFavorita = nuevaCategoria;
    notifyListeners();
  }

  // Lista de elementos de la Sección 4 (interactiva: eliminar, recargar, filtrar)
  List<ItemElemento> _elementos = [];
  List<ItemElemento> get elementos => _categoriaFavorita == 'Todos'
      ? _elementos
      : _elementos.where((item) => item.categoria == _categoriaFavorita).toList();

  AppState() {
    recargarElementos();
  }

  void eliminarElemento(int id) {
    _elementos.removeWhere((item) => item.id == id);
    notifyListeners();
  }

  void agregarElemento(String titulo, String categoria) {
    final nuevoId = DateTime.now().millisecondsSinceEpoch;
    _elementos.insert(
      0,
      ItemElemento(
        id: nuevoId,
        titulo: titulo,
        categoria: categoria,
        descripcion: 'Elemento agregado dinámicamente desde otra sección.',
        icono: Icons.star_border,
      ),
    );
    notifyListeners();
  }

  void recargarElementos() {
    _elementos = [
      ItemElemento(id: 1, titulo: 'Cámara Réflex', categoria: 'Fotografía', descripcion: 'Sensor Full Frame para alta resolución.', icono: Icons.camera_alt),
      ItemElemento(id: 2, titulo: 'Lente 50mm f/1.8', categoria: 'Fotografía', descripcion: 'Lente fijo para retratos y bokeh pronunciado.', icono: Icons.camera),
      ItemElemento(id: 3, titulo: 'Trípode de Aluminio', categoria: 'Fotografía', descripcion: 'Estabilidad garantizada en tomas nocturnas.', icono: Icons.filter_hdr),
      ItemElemento(id: 4, titulo: 'Laptop Gamer Pro', categoria: 'Cómputo', descripcion: 'Procesador de 16 núcleos y 32GB de RAM.', icono: Icons.laptop),
      ItemElemento(id: 5, titulo: 'Teclado Mecánico', categoria: 'Cómputo', descripcion: 'Switches táctiles e iluminación RGB personalizable.', icono: Icons.keyboard),
      ItemElemento(id: 6, titulo: 'Ratón Ergonómico', categoria: 'Cómputo', descripcion: 'Sensor óptico de alta precisión y agarre vertical.', icono: Icons.mouse),
      ItemElemento(id: 7, titulo: 'Monitor 4K UltraWide', categoria: 'Cómputo', descripcion: 'Panel IPS para diseño gráfico y programación.', icono: Icons.desktop_windows),
      ItemElemento(id: 8, titulo: 'Audífonos Bluetooth', categoria: 'Audio', descripcion: 'Cancelación activa de ruido y 30h de batería.', icono: Icons.headphones),
      ItemElemento(id: 9, titulo: 'Micrófono Condensador', categoria: 'Audio', descripcion: 'Patrón cardioide ideal para podcasts y streaming.', icono: Icons.mic),
      ItemElemento(id: 10, titulo: 'Bocina Portátil', categoria: 'Audio', descripcion: 'Resistente al agua IPX7 con graves profundos.', icono: Icons.speaker),
      ItemElemento(id: 11, titulo: 'Smartphone 5G', categoria: 'Móviles', descripcion: 'Pantalla OLED 120Hz con triple sensor fotográfico.', icono: Icons.phone_android),
      ItemElemento(id: 12, titulo: 'Smartwatch Deportivo', categoria: 'Móviles', descripcion: 'Monitoreo de frecuencia cardíaca y GPS integrado.', icono: Icons.watch),
      ItemElemento(id: 13, titulo: 'Tablet para Dibujo', categoria: 'Móviles', descripcion: 'Stylus sensible a 4096 niveles de presión.', icono: Icons.tablet),
      ItemElemento(id: 14, titulo: 'Consola Portátil', categoria: 'Videojuegos', descripcion: 'Pantalla HD y controles desmontables.', icono: Icons.gamepad),
      ItemElemento(id: 15, titulo: 'Control Inalámbrico', categoria: 'Videojuegos', descripcion: 'Gatillos adaptativos y respuesta háptica.', icono: Icons.sports_esports),
      ItemElemento(id: 16, titulo: 'Disco SSD 1TB', categoria: 'Cómputo', descripcion: 'Velocidades de lectura de hasta 7000 MB/s.', icono: Icons.storage),
    ];
    notifyListeners();
  }
}

// Instancia global accesible fácilmente
final appState = AppState();
