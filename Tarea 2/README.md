# Tarea 2: Catálogo de Elementos Básicos de Interfaz de Usuario

## Datos de Identificación
* **Nombre:** Miguel Ángel Cruz Villa
* **Boleta:** 2024630153
* **Grupo:** 7CV4
* **Materia:** Desarrollo de Aplicaciones Móviles
* **Profesor:** Gabriel Hurtado Avilés

---

## 📱 Descripción de la Aplicación y Tecnologías

Esta práctica consiste en el desarrollo de un **Catálogo Interactivo de Elementos de Interfaz de Usuario (UI)** implementado de manera completa y funcional en tres tecnologías diferentes:

1. **Android Nativo con Views y XML:** Implementación tradicional imperativa en Android mediante vistas infladas desde archivos XML y lógica en Kotlin (`Activity`, `Fragment`, `findViewById` / ViewBinding).
2. **Android Nativo con Jetpack Compose:** Paradigma moderno y declarativo en Kotlin utilizando funciones composables (`@Composable`) y gestión de estado con Compose Runtime.
3. **Flutter:** Framework multiplataforma de Google basado en el lenguaje Dart, utilizando un árbol de widgets declarativo (`StatelessWidget`, `StatefulWidget`).

Cada aplicación cuenta con una pantalla principal para navegar fluidamente entre **6 secciones temáticas**, cada una conteniendo componentes interactivos, soporte para **Modo Claro / Modo Oscuro**, explicaciones breves de uso y vinculación entre secciones.

---

## 📁 Estructura del Directorio

```text
Tarea 2/
├── android-views/         # Proyecto Android con Views + XML (Kotlin)
├── android-compose/       # Proyecto Android con Jetpack Compose (Kotlin)
├── flutter/               # Proyecto Flutter (Dart)
├── docs/                  # Capturas de pantalla organizadas por tecnología y sección
├── apks/                  # Archivos APK compilados listos para instalación
│   ├── app-views.apk
│   ├── app-compose.apk
│   └── app-flutter.apk
├── InstruccionesTarea2.pdf # Archivo de especificación original
└── README.md              # Documentación principal del proyecto
```

---

## 🛠️ Instrucciones de Compilación y Ejecución

### 1. Android Nativo con Views (`android-views/`)
* **Requisitos:** Android Studio Ladybug / Meerkat o superior, JDK 17+, Android SDK (API 34+).
* **Compilación por terminal:**
  ```bash
  cd android-views
  ./gradlew assembleDebug
  ```
* **Ejecución en emulador/dispositivo:**
  ```bash
  ./gradlew installDebug
  ```

### 2. Android Nativo con Jetpack Compose (`android-compose/`)
* **Requisitos:** Android Studio con soporte Compose Compiler compatible con Kotlin 1.9+/2.0+, JDK 17+.
* **Compilación por terminal:**
  ```bash
  cd android-compose
  ./gradlew assembleDebug
  ```
* **Ejecución en emulador/dispositivo:**
  ```bash
  ./gradlew installDebug
  ```

### 3. Flutter (`flutter/`)
* **Requisitos:** Flutter SDK 3.x+, Dart SDK 3.x+, Android toolchain configurado.
* **Obtención de dependencias:**
  ```bash
  cd flutter
  flutter pub get
  ```
* **Compilación de APK:**
  ```bash
  flutter build apk --debug
  ```
* **Ejecución en vivo:**
  ```bash
  flutter run
  ```

---

## 📊 Tabla de Equivalencias entre Tecnologías

A continuación se muestra la correspondencia técnica de los elementos de interfaz solicitados en las 6 secciones entre **Android Views (XML)**, **Jetpack Compose** y **Flutter**:

| Categoría | Elemento Requerido | Android Views (XML + Kotlin) | Jetpack Compose (Kotlin) | Flutter (Dart) |
| :--- | :--- | :--- | :--- | :--- |
| **Sección 1: Texto** | Campo de texto simple | `EditText` | `TextField` / `OutlinedTextField` | `TextField` / `TextFormField` |
| | Validación y error visible | `TextInputLayout` con `error` | `TextField(isError = ..., supportingText = ...)` | `TextFormField(validator: ..., decoration: ...)` |
| | Contraseña con toggle | `TextInputLayout(passwordToggleEnabled)` | `TextField(visualTransformation = ...)` | `TextField(obscureText: ..., suffixIcon: ...)` |
| | Tipos de teclado | `android:inputType="..."` | `KeyboardOptions(keyboardType = ...)` | `TextInputType.*` |
| | Campo multilínea | `EditText(inputType="textMultiLine")` | `TextField(singleLine = false, minLines = ...)` | `TextField(maxLines: null, keyboardType: multiline)` |
| | Autocompletado / Opciones | `AutoCompleteTextView` | `ExposedDropdownMenuBox` | `Autocomplete<T>` / `DropdownMenu` |
| | Barra de búsqueda | `SearchView` / `SearchBar` | `SearchBar` / `DockedSearchBar` | `SearchBar` / `SearchAnchor` |
| **Sección 2: Botones** | Botón relleno | `MaterialButton(style="...FilledButton")` | `Button` | `ElevatedButton` / `FilledButton` |
| | Botón con contorno | `MaterialButton(style="...OutlinedButton")` | `OutlinedButton` | `OutlinedButton` |
| | Botón de sólo texto | `MaterialButton(style="...TextButton")` | `TextButton` | `TextButton` |
| | Botón de icono | `IconButton` / `MaterialButton` | `IconButton` | `IconButton` |
| | Botón de acción flotante (FAB) | `FloatingActionButton` / `ExtendedFAB` | `FloatingActionButton` / `ExtendedFloatingActionButton` | `FloatingActionButton` / `FloatingActionButton.extended` |
| | Botón de alternancia / Segmentado | `MaterialButtonToggleGroup` | `SingleChoiceSegmentedButtonRow` | `SegmentedButton` / `ToggleButtons` |
| | Botón deshabilitado | `button.isEnabled = false` | `Button(enabled = false)` | `ElevatedButton(onPressed: null)` |
| | Botón en estado de carga | `MaterialButton` + `ProgressBar` | `Button { CircularProgressIndicator(...) }` | `ElevatedButton(child: CircularProgressIndicator(...))` |
| **Sección 3: Selección** | Casillas de verificación (Checkbox) | `CheckBox` | `Checkbox` / `TriStateCheckbox` | `Checkbox` (tristate: true) |
| | Grupo de botones mutuamente excluyentes | `RadioGroup` + `RadioButton` | `RadioButton` dentro de `Row/Column` | `Radio<T>` / `RadioListTile<T>` |
| | Interruptor (Switch) | `MaterialSwitch` / `SwitchCompat` | `Switch` | `Switch` / `SwitchListTile` |
| | Deslizador (Slider único y rango) | `Slider` / `RangeSlider` | `Slider` / `RangeSlider` | `Slider` / `RangeSlider` |
| | Lista desplegable de selección | `Spinner` | `DropdownMenu` | `DropdownButton<T>` / `DropdownMenu` |
| | Selector de fecha y hora | `MaterialDatePicker` / `MaterialTimePicker` | `DatePickerDialog` / `TimePickerDialog` | `showDatePicker()` / `showTimePicker()` |
| **Sección 4: Listas** | Lista vertical | `RecyclerView` con `LinearLayoutManager` | `LazyColumn` | `ListView.builder` |
| | Cuadrícula de elementos | `RecyclerView` con `GridLayoutManager` | `LazyVerticalGrid` | `GridView.builder` |
| | Lista con encabezados de sección | `RecyclerView` (múltiples `viewType`) | `LazyColumn { stickyHeader { ... } }` | `CustomScrollView` + `SliverStickyHeader` / `ListView` con separators |
| | Selección de item a detalle | `RecyclerView.ViewHolder.setOnClickListener` | `Modifier.clickable { ... }` | `ListTile(onTap: ...)` |
| | Eliminar deslizando (Swipe to dismiss) | `ItemTouchHelper` | `SwipeToDismissBox` | `Dismissible` |
| | Actualizar arrastrando (Pull to refresh) | `SwipeRefreshLayout` | `PullToRefreshBox` / `rememberPullToRefreshState` | `RefreshIndicator` |
| | Estado vacío | `TextView/ImageView` con visibilidad `GONE/VISIBLE` | `if (items.isEmpty()) EmptyView() else ...` | `items.isEmpty ? EmptyView() : ...` |
| **Sección 5: Feedback** | Tipografías / estilos | `TextView(textAppearance="...")` | `Text(style = MaterialTheme.typography...)` | `Text(style: Theme.of(context).textTheme...)` |
| | Imágenes y modos de escalado | `ImageView(scaleType="...")` + Coil/Glide | `AsyncImage(contentScale = ...)` | `Image.asset` / `Image.network(fit: ...)` |
| | Barras de progreso | `LinearProgressIndicator` / `CircularProgressIndicator` | `LinearProgressIndicator` / `CircularProgressIndicator` | `LinearProgressIndicator` / `CircularProgressIndicator` |
| | Mensaje emergente breve (Toast) | `Toast.makeText(...)` | `Toast.makeText(...)` / Toast nativo | `Fluttertoast` / `ScaffoldMessenger.showSnackBar` rápido |
| | Mensaje con acción (Snackbar) | `Snackbar.make(...).setAction(...)` | `SnackbarHostState.showSnackbar(actionLabel = ...)` | `ScaffoldMessenger.of(context).showSnackBar(SnackBar(action: ...))` |
| | Diálogo de confirmación | `MaterialAlertDialogBuilder` | `AlertDialog` | `showDialog(builder: (_) => AlertDialog(...))` |
| | Hoja inferior (Bottom sheet) | `BottomSheetDialog` / `BottomSheetDialogFragment` | `ModalBottomSheet` | `showModalBottomSheet(...)` |
| | Tarjetas, separador y badges | `MaterialCardView`, `Divider`, `BadgeDrawable` | `Card`, `HorizontalDivider`, `BadgedBox` | `Card`, `Divider`, `Badge` |
| **Sección 6: Estructura** | Fila, columna y superpuesta | `LinearLayout (H/V)`, `FrameLayout` / `ConstraintLayout` | `Row`, `Column`, `Box` | `Row`, `Column`, `Stack` |
| | Contenedor con scroll vertical | `ScrollView` / `NestedScrollView` | `Column(modifier = Modifier.verticalScroll(...))` | `SingleChildScrollView` |
| | Barra superior con título y acciones | `MaterialToolbar` | `TopAppBar` / `CenterAlignedTopAppBar` | `AppBar` |
| | Menú lateral o navegación inferior | `DrawerLayout` / `BottomNavigationView` | `ModalNavigationDrawer` / `NavigationBar` | `Drawer` / `NavigationBar` |
| | Distribución proporcional / pesos | `LinearLayout(layout_weight="...")` | `Row/Column(Modifier.weight(...))` | `Expanded(flex: ...)` / `Flexible` |

---

## 📸 Registro de Capturas de Pantalla

Las capturas tomadas se encuentran alojadas en la carpeta [`docs/`](docs/).

### Android Nativo con Views (XML)
| Elemento / Sección | Modo Claro | Modo Oscuro |
| :--- | :---: | :---: |
| Pantalla Principal (Menú / Navegación) | ![Views Claro](docs/views_00_home_claro.png) | ![Views Oscuro](docs/views_00_home_oscuro.png) |
| Sección 1: Entrada de Texto | ![Views S1](docs/views_01_textos.png) | — |
| Sección 2: Botones y Acciones | ![Views S2](docs/views_02_botones.png) | — |
| Sección 3: Elementos de Selección | ![Views S3](docs/views_03_seleccion.png) | — |
| Sección 4: Listas y Colecciones | ![Views S4](docs/views_04_listas.png) | — |
| Sección 5: Información y Retroalimentación | ![Views S5](docs/views_05_feedback.png) | — |
| Sección 6: Contenedores y Estructura | ![Views S6](docs/views_06_contenedores.png) | — |

### Android Nativo con Jetpack Compose
| Elemento / Sección | Modo Claro | Modo Oscuro |
| :--- | :---: | :---: |
| Pantalla Principal (Menú / Navegación) | ![Compose Claro](docs/compose_00_home_claro.png) | ![Compose Oscuro](docs/compose_00_home_oscuro.png) |
| Sección 1: Entrada de Texto | ![Compose S1](docs/compose_01_textos.png) | — |
| Sección 2: Botones y Acciones | ![Compose S2](docs/compose_02_botones.png) | — |
| Sección 3: Elementos de Selección | ![Compose S3](docs/compose_03_seleccion.png) | — |
| Sección 4: Listas y Colecciones | ![Compose S4](docs/compose_04_listas.png) | — |
| Sección 5: Información y Retroalimentación | ![Compose S5](docs/compose_05_feedback.png) | — |
| Sección 6: Contenedores y Estructura | ![Compose S6](docs/compose_06_contenedores.png) | — |

### Flutter (Dart)
| Elemento / Sección | Modo Claro | Modo Oscuro |
| :--- | :---: | :---: |
| Pantalla Principal (Menú / Navegación) | ![Flutter Claro](docs/flutter_00_home_claro.png) | ![Flutter Oscuro](docs/flutter_00_home_oscuro.png) |
| Sección 1: Entrada de Texto | ![Flutter S1](docs/flutter_01_textos.png) | — |
| Sección 2: Botones y Acciones | ![Flutter S2](docs/flutter_02_botones.png) | — |
| Sección 3: Elementos de Selección | ![Flutter S3](docs/flutter_03_seleccion.png) | — |
| Sección 4: Listas y Colecciones | ![Flutter S4](docs/flutter_04_listas.png) | — |
| Sección 5: Información y Retroalimentación | ![Flutter S5](docs/flutter_05_feedback.png) | — |
| Sección 6: Contenedores y Estructura | ![Flutter S6](docs/flutter_06_contenedores.png) | — |

---

## 🧠 Reflexión Comparativa entre Tecnologías

*(Se complementará detalladamente conforme se construya y valide cada aplicación)*:
1. **Velocidad de construcción de la interfaz:** Comparación de tiempos entre crear layouts XML con view-binding vs la agilidad de los composables y widgets.
2. **Legibilidad y mantenibilidad del código:** Análisis de la separación entre diseño (XML) y lógica (Kotlin) frente al enfoque puramente declarativo con código unificado en Compose y Flutter.
3. **Dificultades encontradas:** Retos específicos en la gestión del estado, listas complejas, diálogos modales o adaptación automática del tema oscuro en cada stack.
4. **Preferencia y conclusión técnica:** Elección fundamentada de la tecnología predilecta para el desarrollo móvil moderno.

---

## 📦 Enlace a Binarios (APKs)
Los paquetes compilados listos para prueba en dispositivos físicos o emuladores se encuentran en la carpeta [`apks/`](apks/):
* [Descargar APK Android Views](apks/app-views.apk)
* [Descargar APK Jetpack Compose](apks/app-compose.apk)
* [Descargar APK Flutter](apks/app-flutter.apk)

---

## 📚 Referencias (Formato APA)

* Android Developers. (2024). *Build a Material Design app with Jetpack Compose*. Google. https://developer.android.com/jetpack/compose
* Android Developers. (2024). *Layouts in Android Views*. Google. https://developer.android.com/guide/topics/ui/declaring-layout
* Flutter Documentation. (2024). *Flutter widget catalog*. Google. https://docs.flutter.dev/ui/widgets
* Material Design. (2024). *Material Design 3 Guidelines*. Google. https://m3.material.io/
