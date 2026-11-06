import 'package:flutter/material.dart';
import '../models/app_state.dart';

class Section3SelectionScreen extends StatefulWidget {
  const Section3SelectionScreen({super.key});

  @override
  State<Section3SelectionScreen> createState() => _Section3SelectionScreenState();
}

class _Section3SelectionScreenState extends State<Section3SelectionScreen> {
  // Checkboxes
  bool _chkOpcion1 = true;
  bool _chkOpcion2 = false;
  bool? _chkIndeterminado; // Tristate (null = indeterminado)

  // Radio Buttons
  int _radioSeleccionado = 1;

  // Switch
  bool _switchActivo = true;

  // Sliders
  double _sliderSimple = 45.0;
  RangeValues _sliderRango = const RangeValues(20.0, 75.0);

  // Dropdown
  final List<String> _categorias = ['Todos', 'Fotografía', 'Cómputo', 'Audio', 'Móviles', 'Videojuegos'];

  // Fecha y Hora seleccionadas
  DateTime? _fechaSeleccionada;
  TimeOfDay? _horaSeleccionada;

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
        title: const Text('Sección 3: Elementos de Selección'),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            // 1. Casillas de verificación (Checkbox) con caso indeterminado
            _buildCard(
              title: '1. Casillas de Verificación (Checkboxes)',
              explanation:
                  'Permiten seleccionar múltiples opciones independientes. Incluye una casilla tri-estado (activado, desactivado, indeterminado).',
              child: Column(
                children: [
                  CheckboxListTile(
                    title: const Text('Recibir notificaciones por correo'),
                    value: _chkOpcion1,
                    onChanged: (val) => setState(() => _chkOpcion1 = val ?? false),
                  ),
                  CheckboxListTile(
                    title: const Text('Aceptar términos y condiciones'),
                    value: _chkOpcion2,
                    onChanged: (val) => setState(() => _chkOpcion2 = val ?? false),
                  ),
                  CheckboxListTile(
                    tristate: true,
                    title: const Text('Estado global del proyecto (Tri-estado)'),
                    subtitle: Text(_chkIndeterminado == null
                        ? 'Estado: Indeterminado'
                        : (_chkIndeterminado! ? 'Estado: Completado' : 'Estado: Pendiente')),
                    value: _chkIndeterminado,
                    onChanged: (val) => setState(() => _chkIndeterminado = val),
                  ),
                ],
              ),
            ),

            // 2. Grupo de botones de opción mutuamente excluyentes (Radio)
            _buildCard(
              title: '2. Grupo de Botones de Opción (Radio)',
              explanation:
                  'Obligan al usuario a elegir exactamente una opción dentro de un conjunto cerrado de posibilidades.',
              child: Column(
                children: [
                  RadioListTile<int>(
                    title: const Text('Nivel de Acceso: Estándar (Lectura)'),
                    value: 1,
                    groupValue: _radioSeleccionado,
                    onChanged: (val) => setState(() => _radioSeleccionado = val!),
                  ),
                  RadioListTile<int>(
                    title: const Text('Nivel de Acceso: Moderador (Edición)'),
                    value: 2,
                    groupValue: _radioSeleccionado,
                    onChanged: (val) => setState(() => _radioSeleccionado = val!),
                  ),
                  RadioListTile<int>(
                    title: const Text('Nivel de Acceso: Administrador (Total)'),
                    value: 3,
                    groupValue: _radioSeleccionado,
                    onChanged: (val) => setState(() => _radioSeleccionado = val!),
                  ),
                ],
              ),
            ),

            // 3. Interruptor (Switch)
            _buildCard(
              title: '3. Interruptor de Estado (Switch)',
              explanation:
                  'Control binario instantáneo (encendido/apagado). Adecuado para activar o desactivar configuraciones de forma directa.',
              child: SwitchListTile(
                title: const Text('Sincronización en la nube'),
                subtitle: Text(_switchActivo ? 'Activado: Sincronizando datos' : 'Desactivado'),
                value: _switchActivo,
                onChanged: (val) => setState(() => _switchActivo = val),
              ),
            ),

            // 4. Deslizador único y deslizador de rango (Slider & RangeSlider)
            _buildCard(
              title: '4. Deslizador Único y Deslizador de Rango',
              explanation:
                  'Permiten seleccionar valores dentro de un intervalo continuo o discreto, de forma visual e intuitiva.',
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Nivel de Brillo / Volumen: ${_sliderSimple.round()}%'),
                  Slider(
                    value: _sliderSimple,
                    min: 0,
                    max: 100,
                    divisions: 20,
                    label: '${_sliderSimple.round()}%',
                    onChanged: (val) => setState(() => _sliderSimple = val),
                  ),
                  const SizedBox(height: 12),
                  Text(
                    'Rango de Precios: \$${_sliderRango.start.round()} - \$${_sliderRango.end.round()}',
                  ),
                  RangeSlider(
                    values: _sliderRango,
                    min: 0,
                    max: 100,
                    divisions: 10,
                    labels: RangeLabels(
                      '\$${_sliderRango.start.round()}',
                      '\$${_sliderRango.end.round()}',
                    ),
                    onChanged: (val) => setState(() => _sliderRango = val),
                  ),
                ],
              ),
            ),

            // 5. Lista desplegable (Dropdown) con vinculación transversal
            _buildCard(
              title: '5. Menú Desplegable (Dropdown)',
              explanation:
                  'Muestra una lista compacta de elementos para elegir uno. NOTA: Al cambiar la categoría aquí, se filtra la lista de la Sección 4 (requisito transversal).',
              child: Row(
                children: [
                  const Text('Filtrar catálogo: ', style: TextStyle(fontWeight: FontWeight.bold)),
                  const SizedBox(width: 16),
                  DropdownButton<String>(
                    value: appState.categoriaFavorita,
                    items: _categorias.map((cat) {
                      return DropdownMenuItem<String>(
                        value: cat,
                        child: Text(cat),
                      );
                    }).toList(),
                    onChanged: (nuevaCat) {
                      if (nuevaCat != null) {
                        setState(() {
                          appState.actualizarCategoria(nuevaCat);
                        });
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                            content: Text('Categoría vinculada con Sección 4: $nuevaCat'),
                            duration: const Duration(seconds: 2),
                          ),
                        );
                      }
                    },
                  ),
                ],
              ),
            ),

            // 6. Selectores de Fecha y Hora (DatePicker & TimePicker)
            _buildCard(
              title: '6. Selectores Modales de Fecha y Hora',
              explanation:
                  'Facilitan la captura estandarizada de fechas del calendario y horas del reloj respetando el formato local del dispositivo.',
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  OutlinedButton.icon(
                    icon: const Icon(Icons.calendar_month),
                    label: Text(
                      _fechaSeleccionada == null
                          ? 'Elegir Fecha'
                          : '${_fechaSeleccionada!.day}/${_fechaSeleccionada!.month}/${_fechaSeleccionada!.year}',
                    ),
                    onPressed: () async {
                      final seleccion = await showDatePicker(
                        context: context,
                        initialDate: DateTime.now(),
                        firstDate: DateTime(2020),
                        lastDate: DateTime(2030),
                      );
                      if (seleccion != null) {
                        setState(() => _fechaSeleccionada = seleccion);
                      }
                    },
                  ),
                  OutlinedButton.icon(
                    icon: const Icon(Icons.access_time),
                    label: Text(
                      _horaSeleccionada == null
                          ? 'Elegir Hora'
                          : _horaSeleccionada!.format(context),
                    ),
                    onPressed: () async {
                      final seleccion = await showTimePicker(
                        context: context,
                        initialTime: TimeOfDay.now(),
                      );
                      if (seleccion != null) {
                        setState(() => _horaSeleccionada = seleccion);
                      }
                    },
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
