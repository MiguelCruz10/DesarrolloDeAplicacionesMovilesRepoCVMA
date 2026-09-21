# 8. Servidor MCP Propio (Puntos Extra)

Para cumplir con el requerimiento opcional, se desarrolló e implementó un servidor MCP personalizado utilizando el SDK oficial de Python (`mcp`). 

## Caso de Uso
El servidor está diseñado para asistir en el análisis y cálculo de apuestas deportivas. Al conectar este servidor, el modelo de IA delega las predicciones estructuradas y la matemática financiera a scripts locales, evitando alucinaciones en el cálculo de momios.

## Herramientas Implementadas
El servidor expone dos herramientas principales:

1. **`sugerir_mejores_apuestas`**: Recibe el nombre de un equipo local y uno visitante. Utiliza una función determinista basada en los nombres para calcular probabilidades estimadas y devuelve un reporte estructurado con el pick principal, mercado de goles y la probabilidad de que ambos equipos anoten.
2. **`calcular_parlay`**: Recibe una lista de momios en formato decimal y un monto de inversión. Calcula la cuota total combinada, la ganancia neta y el cobro potencial exacto.

## Evidencia de Funcionamiento
El servidor fue conectado exitosamente a Google Antigravity. Al solicitarle un análisis para un partido específico (ej. Cruz Azul vs Toluca), el cliente descubrió la herramienta, delegó la petición al script de Python y formateó la respuesta devuelta por el servidor local. 

*(La captura de pantalla de esta ejecución se encuentra en el archivo principal README.md).*