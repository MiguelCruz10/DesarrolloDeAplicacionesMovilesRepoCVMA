# 2. El Problema del Aislamiento

## Limitaciones Inherentes de un LLM
Por su propia naturaleza, un modelo de lenguaje grande (LLM) es una función matemática compleja: **recibe texto y devuelve texto**. Por sí mismo, un LLM no tiene la capacidad de ver el entorno del usuario, no puede modificar archivos y no ejecuta llamadas al sistema operativo (syscalls). Es un ente completamente aislado.

Este aislamiento existe por dos categorías de razones fundamentales:

### Razones de Arquitectura
El modelo, debido a sus gigantescos requerimientos de hardware, generalmente se ejecuta en un clúster de servidores remotos con múltiples GPUs. Físicamente, no existe un canal directo de comunicación entre ese servidor remoto y el disco duro local de la computadora del usuario. El puente tradicional ha sido únicamente la ventana de chat del navegador.

### Razones de Seguridad
Incluso si el modelo se ejecutara localmente, el aislamiento es una característica deseada:
* **Consentimiento del usuario:** Un programa de IA no debe tener autonomía para leer o borrar archivos personales sin permiso explícito.
* **Aislamiento del sistema (Sandboxing):** Previene que un error del modelo corrompa el sistema operativo.
* **Riesgo de Inyección de Instrucciones (Prompt Injection):** Si un modelo lee automáticamente un archivo malicioso descargado de internet, las instrucciones ocultas en ese texto podrían "secuestrar" al modelo y ordenarle que extraiga y envíe información privada del usuario.