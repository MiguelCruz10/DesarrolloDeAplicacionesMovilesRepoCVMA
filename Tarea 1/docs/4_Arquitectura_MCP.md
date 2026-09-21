# 4. Arquitectura de MCP

El Model Context Protocol (MCP) utiliza una arquitectura Host/Cliente/Servidor para conectar los modelos de IA con fuentes de datos y herramientas externas.

## Roles en la Arquitectura
* **Host:** Es el modelo de lenguaje subyacente que genera las respuestas e invoca las herramientas (por ejemplo, Claude 3.5 Sonnet).
* **Cliente MCP:** Es la aplicación donde interactúa el usuario (por ejemplo, Claude Desktop o Google Antigravity). Gestiona la conexión con el servidor y actúa como intermediario.
* **Servidor MCP:** Es el programa ligero (en este caso, el servidor de sistema de archivos local) que expone capacidades específicas al cliente a través del protocolo estandarizado.

## Primitivas del Servidor
Un servidor MCP expone tres primitivas principales para extender las capacidades del modelo:
1. **Herramientas (Tools):** Funciones ejecutables que el modelo puede invocar para realizar acciones (ej. ejecutar una búsqueda, escribir un archivo).
2. **Recursos (Resources):** Datos estáticos o dinámicos expuestos al cliente en formato de solo lectura (ej. logs de sistema, esquemas de bases de datos).
3. **Plantillas de Prompt (Prompts):** Estructuras predefinidas de instrucciones que ayudan a estandarizar la interacción del usuario con el modelo.

## Primitivas del Cliente
El cliente MCP también expone primitivas hacia el servidor:
* **Roots (Raíces):** Define los límites del entorno local (como qué carpetas específicas están autorizadas para que el servidor las explore).
* **Sampling (Muestreo/Elicitation):** Permite al servidor solicitar al modelo que genere texto basado en ciertos prompts, útil para que el servidor delegue tareas de razonamiento.

## Protocolos de Transporte (Transports)
MCP soporta dos mecanismos de transporte principales basados en JSON-RPC 2.0:
1. **stdio (Standard Input/Output):** Utilizado para servidores locales que corren como un subproceso directamente en la máquina del usuario (ideal para el servidor de archivos).
2. **SSE (Server-Sent Events) sobre HTTP:** Utilizado para conectar clientes con servidores MCP remotos a través de la red.

> **Nota de Especificación:** Esta investigación está basada en la especificación de MCP versión `2024-11-05`.