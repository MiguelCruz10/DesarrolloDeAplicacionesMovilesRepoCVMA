# 5. El Servidor de Sistema de Archivos (FS)

**Aclaración importante:** El servidor "FS" (File System) **no es** el protocolo MCP en sí mismo. Es únicamente uno de los múltiples servidores de referencia creados para demostrar las capacidades del protocolo.

## Herramientas Expuestas
El servidor FS publica un catálogo de herramientas (Tools) que el cliente descubre dinámicamente. Las operaciones expuestas son:
* **Listar:** Ver el contenido de un directorio.
* **Leer:** Extraer el contenido de un archivo de texto.
* **Escribir:** Sobrescribir o añadir contenido a un archivo.
* **Crear:** Generar un archivo nuevo.
* **Mover:** Cambiar la ubicación de un archivo.
* **Buscar:** Localizar archivos por nombre o patrones de contenido dentro del directorio.

## Delimitación del Alcance
El alcance del servidor no es absoluto; se delimita estrictamente mediante los **directorios permitidos** (Roots). Al iniciar el servidor, se le pasa como argumento exclusivamente la ruta de la carpeta del proyecto.

**¿Por qué existe este límite y qué pasaría si se omite?**
El límite existe como mecanismo de contención (sandboxing). Si no existiera este límite o si se configurara la carpeta raíz del sistema (`C:\` o `/`), el modelo, ya sea por una alucinación (error) o por un ataque de inyección de instrucciones en el código, podría listar, leer o incluso sobrescribir archivos críticos del sistema operativo, o extraer credenciales SSH y bases de datos personales almacenadas en la computadora del usuario.