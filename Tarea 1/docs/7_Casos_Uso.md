# 7. Casos de Uso Actuales

La integración de MCP y entornos impulsados por IA está revolucionando el desarrollo de software. Actualmente, varias herramientas implementan este ecosistema:

1. **Cursor:** Un entorno de desarrollo (IDE) basado en VS Code que utiliza MCP y otras integraciones profundas para sugerir ediciones en múltiples archivos, indexar el código completo y permitir que el modelo genere refactorizaciones masivas directamente en el editor.
2. **Google Antigravity:** Un entorno de desarrollo auténtico (no confundir con modelos en bruto) que utiliza protocolos contextuales para operar sobre el árbol de trabajo del usuario, gestionando archivos locales y asistiendo en el ciclo completo de vida del software.
3. **Claude Desktop:** La aplicación de escritorio oficial de Anthropic, que mediante la configuración de su archivo `claude_desktop_config.json`, permite al usuario montar servidores MCP (como el de sistema de archivos o conexiones a bases de datos) para que Claude analice repositorios, lea logs de errores locales y escriba scripts directamente en el disco.

*(Nota técnica: Es importante diferenciar estas plataformas de los modelos en sí; por ejemplo, **Qwen** es una familia de modelos de lenguaje desarrollados por Alibaba, no una plataforma de desarrollo ni un cliente MCP).*

**¿Por qué operan sobre repositorios completos sin subidas manuales?**
Gracias a MCP y servidores locales como el de Sistema de Archivos, estas herramientas descubren el árbol del proyecto a través de comandos como "listar" y "buscar". En lugar de requerir que el usuario suba archivos ZIP al navegador o copie y pegue código repetitivamente, el modelo solicita leer únicamente los archivos específicos que necesita en tiempo real para obtener contexto, modificándolos localmente a través de la interfaz del IDE o cliente.