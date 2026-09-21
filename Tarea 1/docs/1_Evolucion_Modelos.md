# 1. Evolución de los Modelos

## ¿Qué es un Modelo de Lenguaje?
Un modelo de lenguaje es un sistema de inteligencia artificial diseñado para comprender, generar y predecir lenguaje natural basándose en patrones estadísticos aprendidos de grandes volúmenes de texto. En su forma más básica, su función principal es predecir la siguiente palabra (o token) en una secuencia.

Con el tiempo, estos sistemas evolucionaron hacia los **Modelos de Lenguaje Grandes (LLM, por sus siglas en inglés)**. Esta evolución se logró aumentando masivamente la cantidad de parámetros de las redes neuronales y alimentándolas con corpus de datos a escala de internet, lo que les otorgó capacidades emergentes como la traducción, el resumen y la generación de código.

## Modelos con Razonamiento Explícito
Recientemente, la industria ha introducido modelos con capacidades de **razonamiento explícito** (capaces de "pensar" antes de responder). Es crucial aclarar que **esta capacidad no aparece mágicamente solo por aumentar el tamaño del modelo** (más parámetros). 

Este razonamiento avanzado proviene de dos factores fundamentales:
1. **Técnicas de entrenamiento específicas:** Como el Aprendizaje por Refuerzo (RL) para incentivar la exploración de cadenas de pensamiento lógicas.
2. **Cómputo adicional al momento de la inferencia:** El modelo gasta tiempo y poder de procesamiento extra evaluando múltiples rutas lógicas, detectando errores propios y refinando su respuesta antes de entregar el texto final al usuario.