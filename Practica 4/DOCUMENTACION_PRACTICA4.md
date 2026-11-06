# Práctica 4: Gestión de Archivos Multiplataforma (Android e iOS)
**Unidad de Aprendizaje:** Desarrollo de Aplicaciones Móviles Nativas  
**Programa Académico:** Ingeniería en Sistemas Computacionales (Plan 2020)  
**Institución:** Instituto Politécnico Nacional — Escuela Superior de Cómputo (ESCOM)  
**Semestre:** 2027-1  

---

## 1. Introducción y Resumen Ejecutivo

Esta práctica implementa la solución completa a los dos requerimientos estipulados en la Práctica 4 utilizando **Kotlin Multiplatform (KMP)** con **Compose Multiplatform** para compartir el 100% de la lógica de negocio y la interfaz de usuario declarativa entre **Android** e **iOS**, respetando los modelos de seguridad de almacenamiento de cada sistema operativo (Scoped Storage / SAF en Android y Sandbox / Documents Directory en iOS).

Se desarrollaron dos proyectos independientes y funcionales:
1. **Parte 1 — `GestorArchivosKMP`**: Explorador y visualizador de archivos interno y del sistema, con soporte de metadatos, temas dinámicos (Guinda IPN y Azul ESCOM), historial reciente y favoritos persistentes.
2. **Parte 2 — `JuegoKMP`**: Juego original de lógica y puzzle numérico (*2048 KMP*) con 2 niveles/modos (4×4 y 5×5), persistencia completa de sesión, e importación/exportación en tres formatos independientes: **Texto Plano (.txt)**, **XML (.xml)** y **JSON (.json)** sin librerías externas pesadas.

---

## 2. Tecnologías y Entorno de Desarrollo

- **Lenguaje:** Kotlin 2.3.21
- **UI Compartida:** JetBrains Compose Multiplatform 1.11.1
- **Design System:** Material 3 Multiplatform 1.9.0
- **Android Gradle Plugin (AGP):** 9.3.0
- **Compatibilidad Android:** `minSdk = 24` (Android 7.0 Nougat), `compileSdk = 36`, `targetSdk = 36`
- **Compatibilidad iOS:** iOS 15.0 o superior (Framework estático `Shared.framework` consumido desde Xcode con Apple Silicon / Swift UI hosting `MainViewController`).
- **Offline First:** Ambas aplicaciones operan al 100% sin conexión a Internet y no requieren dependencias remotas durante la ejecución.

---

## 3. Parte 1: Gestor de Archivos Multiplataforma (`GestorArchivosKMP`)

### 3.1. Arquitectura y Componentes
- **Espacio y Modelo de Datos (`FileItem.kt`):** Modela archivos y directorios con atributos de `name`, `path`, `isDirectory`, `size` (bytes), `lastModified` (epoch millis) y `mimeType`.
- **Abstracción Multiplataforma (`FileRepository.kt`):**
  - Declaración `expect class FileRepository` en `commonMain`.
  - Implementación Android (`FileRepository.android.kt`): Utiliza el almacenamiento interno privado de la aplicación (`Context.filesDir`) respetando Scoped Storage, con detección MIME mediante `android.webkit.MimeTypeMap`.
  - Implementación iOS (`FileRepository.ios.kt`): Utiliza la API nativa de Apple Foundation (`NSFileManager`, `NSDocumentDirectory`, `NSUserDomainMask`) respetando las directivas del sandbox de iOS.
- **Visores Especializados:**
  - **Texto / Markdown (.txt, .md):** Renderizado con tipografía monoespaciada y numeración de líneas.
  - **JSON & XML (.json, .xml):** Resaltado de sintaxis sintáctico en tiempo real (diferenciación de llaves, strings, tags y valores mediante tokens de color de la paleta Material).
  - **Imágenes (.jpg, .png, .webp):** Detección de buffers de imagen e interfaz dedicada.
  - **Formatos no soportados:** Cuadro de diálogo controlado informando las limitaciones y canalizando la apertura segura.
- **Persistencia de Recientes y Favoritos (`PreferencesRepository` & `RecentFilesManager`):**
  - Android: `SharedPreferences` en modo privado (`MODE_PRIVATE`).
  - iOS: `NSUserDefaults.standardUserDefaults`.
  - Capacidad de almacenar hasta 20 elementos recientes con rotación FIFO y gestión persistente de favoritos.
- **Sistema de Temas Institucionales (`Theme.kt`):**
  - **Tema Guinda (IPN):** Primario `#722F37`, Secundario `#9B4D55`, Superficie `#FFFFFF` (Claro) / `#2D2020` (Oscuro).
  - **Tema Azul (ESCOM):** Primario `#003F87`, Secundario `#1565C0`, Superficie `#FFFFFF` (Claro) / `#1A2030` (Oscuro).
  - Soporte automático para cambio entre Modo Claro y Modo Oscuro del sistema operativo (`isSystemInDarkTheme()`).

---

## 4. Parte 2: Juego con Gestión de Archivos (`JuegoKMP`)

### 4.1. Mecánica del Juego (*2048 KMP*)
- Tablero matricial interactivo con dos niveles de dificultad seleccionables:
  - **Nivel 1:** Tablero clásico 4×4.
  - **Nivel 2:** Tablero expandido 5×5 (mayor combinatoria y desafío).
- Motor de juego puramente funcional (`GameEngine.kt`): Manejo inmutable de estados, adición estocástica de nuevas fichas (90% probabilidad de 2, 10% de 4), detección de fusiones por deslizamiento en las 4 direcciones (`UP`, `DOWN`, `LEFT`, `RIGHT`), detección de victoria (ficha 2048) y Game Over (tablero lleno sin movimientos posibles).
- Control dual: Controles visuales direccionales integrados y barra de puntuación y cronómetro dinámico en tiempo real.

### 4.2. Persistencia y Serialización en 3 Formatos (`Serialization.kt` y `SaveManager`)
El usuario puede alternar libremente el formato de guardado preferido en la configuración o al momento de guardar:
1. **Texto Plano (.txt):** Delimitado por secciones (`=== PARTIDA ===`, `--- TABLERO ---`, `--- HISTORIAL ---`).
2. **JSON (.json):** Estructura jerárquica con arreglos bidimensionales para las filas del tablero y metadatos de sesión.
3. **XML (.xml):** Estructura XML bien formada con declaración `<?xml version="1.0" encoding="UTF-8"?>`, nodo raíz `<partida>` y elementos `<fila>` y `<celda>`.

### 4.3. Gestión de Partidas Guardadas
- **Carga de partidas:** Reconstruye exactamente el estado del tablero, nivel, puntos acumulados y tiempo transcurrido desde cualquiera de los tres formatos.
- **Visualizador integrado:** Diálogo modal que permite inspeccionar directamente el texto crudo del archivo guardado sin salir del juego.
- **Etiquetado y Categorización:** Campo de etiquetas opcional al guardar (ej. *"Récord"*, *"Nivel Difícil"*).
- **Eliminación y Exportación:** Diálogo de confirmación para borrado y función de exportación como texto legible.

---

## 5. Matriz de Pruebas Realizadas

| ID | Caso de Prueba | Plataforma / SO | Pasos Realizados | Resultado Esperado | Resultado Obtenido | Estatus |
|---|---|---|---|---|---|---|
| **CP01** | Operación 100% Offline | Android 14 / iOS 17 | Desactivar Wi-Fi y datos móviles. Abrir ambas aplicaciones. | Carga completa sin solicitudes de red ni caídas. | Funcionalidad completa verificada sin conexión. | **Aprobado** |
| **CP02** | Creación y Lectura de Archivos Iniciales | Android 14 | Abrir `GestorArchivosKMP` por primera vez. | Generación automática de `bienvenida.txt`, `datos.json`, `configuracion.xml` y carpeta `documentos/`. | Archivos visibles en el explorador interno. | **Aprobado** |
| **CP03** | Visualización de TXT y MD | Android / iOS | Seleccionar `bienvenida.txt` y `notas.md`. | Despliegue de texto con numeración de líneas y visor monoespaciado. | Formato legible y fluido con desplazamiento vertical. | **Aprobado** |
| **CP04** | Resaltado de Sintaxis JSON/XML | Android / iOS | Abrir `datos.json` y `configuracion.xml`. | Coloreado distintivo de claves, strings y etiquetas XML. | Diferenciación visual clara de elementos sintácticos. | **Aprobado** |
| **CP05** | Cambio de Temas Institucionales | Android / iOS | Alternar entre Guinda (IPN) y Azul (ESCOM). | Actualización de paleta primaria/secundaria y persistencia del tema tras reinicio. | Colores `#722F37` y `#003F87` aplicados correctamente. | **Aprobado** |
| **CP06** | Favoritos y Recientes | Android / iOS | Marcar archivos con estrella y abrir varios ficheros. | Listado en pantallas de Favoritos y Recientes. | Persistencia confirmada tras cerrar y reabrir la app. | **Aprobado** |
| **CP07** | Guardado de Partida en TXT | Android / iOS | Jugar 2048, presionar Guardar, formato TXT, etiqueta "Prueba". | Generación de archivo `.txt` en directorio `saves/`. | Archivo generado con formato delimitado correcto. | **Aprobado** |
| **CP08** | Guardado de Partida en JSON | Android / iOS | Configurar formato JSON, guardar partida en curso. | Generación de archivo `.json` sintácticamente válido. | JSON bien formado con matriz de celdas. | **Aprobado** |
| **CP09** | Guardado de Partida en XML | Android / iOS | Configurar formato XML, guardar partida. | Generación de archivo `.xml` con tags `<partida>`, `<tablero>`. | Estructura XML íntegra y legible. | **Aprobado** |
| **CP10** | Carga y Recuperación de Sesión | Android / iOS | Salir al menú, abrir "Partidas guardadas" y cargar partida guardada. | Restauración idéntica del tablero, nivel, puntos y cronómetro. | Estado recuperado sin alteraciones ni pérdidas. | **Aprobado** |
| **CP11** | Eliminación Segura | Android / iOS | Seleccionar eliminar archivo/partida con diálogo de confirmación. | Confirmación previa; borrado del fichero en disco. | Eliminación exitosa sin congelamientos. | **Aprobado** |

---

## 6. Instrucciones de Compilación y Ejecución

### 6.1. Android (desde Windows, macOS o Linux)
1. Abrir **Android Studio** (versión Ladybug o superior).
2. Abrir el proyecto deseado:
   - Para la Parte 1: Abrir la carpeta `GestorArchivosKMP`.
   - Para la Parte 2: Abrir la carpeta `JuegoKMP`.
3. Sincronizar Gradle (`File -> Sync Project with Gradle Files`).
4. Seleccionar la configuración de ejecución `androidApp` en la barra superior.
5. Seleccionar un emulador Android (API 24 o superior) o dispositivo físico con depuración USB habilitada.
6. Presionar **Run (`Shift + F10`)**.
7. Alternativamente por terminal en la raíz de cada proyecto:
   ```powershell
   .\gradlew.bat :androidApp:installDebug
   ```

### 6.2. iOS (desde entorno macOS con Xcode)
1. Abrir la carpeta `iosApp` en **Xcode**:
   - `GestorArchivosKMP/iosApp` o `JuegoKMP/iosApp`.
2. Esperar a que Xcode indexe y resuelva el esquema de compilación.
3. El proyecto ejecutará la tarea de Gradle de KMP (`embedAndSignAppleFrameworkForXcode`) para compilar el módulo `:shared` en el framework nativo para el simulador/dispositivo seleccionado.
4. Seleccionar un simulador (ej. *iPhone 15*, *iPhone 16*) con iOS 15 o superior.
5. Presionar **Run (`Cmd + R`)**.

---

## 7. Conclusiones

El uso de **Kotlin Multiplatform** junto con **Compose Multiplatform** demostró ser una solución de alta eficiencia para el desarrollo móvil moderno. Permitió unificar completamente la lógica de parseo, serialización, estados de interfaz y reglas de negocio, manteniendo al mismo tiempo el respeto riguroso por las APIs de almacenamiento seguro de cada plataforma a través de la arquitectura `expect`/`actual`. Ambas aplicaciones cumplen la totalidad de requerimientos técnicos, visuales y de persistencia estipulados para la Práctica 4.

---

## 8. Bibliografía (Formato APA)

- JetBrains. (2024). *Kotlin Multiplatform overview*. JetBrains Documentation. https://kotlinlang.org/docs/multiplatform.html
- JetBrains. (2024). *Compose Multiplatform by JetBrains*. JetBrains Developer Portal. https://www.jetbrains.com/lp/compose-multiplatform/
- Android Developers. (2024). *Data and file storage overview*. Google Developers. https://developer.android.com/training/data-storage
- Apple Inc. (2024). *File System Basics*. Apple Developer Documentation. https://developer.apple.com/library/archive/documentation/FileManagement/Conceptual/FileSystemProgrammingGuide/FileSystemOverview/FileSystemOverview.html
