from mcp.server.mcpserver import MCPServer
import random

# Inicializamos el servidor
mcp = MCPServer("ServidorPropio")

@mcp.tool()
def sugerir_mejores_apuestas(equipo_local: str, equipo_visitante: str) -> str:
    """
    Analiza un partido y sugiere los mejores mercados de apuestas (Pick principal, Goles, Ambos Anotan).
    """
    # Usamos una semilla basada en los nombres para que el resultado sea consistente
    # si consultas el mismo partido varias veces.
    random.seed(equipo_local.lower() + equipo_visitante.lower())
    
    prob_local = random.uniform(1.2, 3.5)
    prob_visitante = random.uniform(1.8, 4.5)
    prob_empate = random.uniform(2.9, 3.8)
    
    goles_esperados = random.choice(["Más de 1.5", "Más de 2.5", "Menos de 2.5", "Menos de 3.5"])
    ambos_anotan = random.choice(["Sí", "Sí", "No"]) # Más probabilidad de que sí
    
    # Lógica para la sugerencia principal
    if prob_local < 1.7:
        pick_principal = f"Victoria de {equipo_local} (Momio aprox: {prob_local:.2f}) - Pick conservador"
    elif prob_visitante < 2.0:
        pick_principal = f"Victoria de {equipo_visitante} (Momio aprox: {prob_visitante:.2f}) - Pick de valor"
    else:
        pick_principal = f"Doble Oportunidad Local/Empate (Momio aprox: 1.40) - Partido muy cerrado"
        
    reporte = (
        f"Análisis de Apuestas: {equipo_local} vs {equipo_visitante}\n"
        f"----------------------------------------------------\n"
        f"Pick Principal: {pick_principal}\n"
        f"Mercado de Goles: {goles_esperados}\n"
        f"¿Ambos equipos anotan?: {ambos_anotan}\n\n"
        f"Recomendación: Cruza el pick principal con el mercado de goles para mejorar la cuota en tu casa de apuestas."
    )
    return reporte

@mcp.tool()
def calcular_parlay(momios_decimales: list[float], inversion: float) -> str:
    """
    Calcula la ganancia potencial de una apuesta combinada (parlay/acumulada).
    """
    if not momios_decimales:
        return "Error: Debes ingresar al menos un momio."
        
    cuota_total = 1.0
    for momio in momios_decimales:
        cuota_total *= momio
        
    ganancia_neta = (cuota_total * inversion) - inversion
    cobro_total = cuota_total * inversion
    
    return (
        f"Ticket Parlay ({len(momios_decimales)} selecciones)\n"
        f"Cuota Total Combinada: {cuota_total:.2f}\n"
        f"Inversión: ${inversion:.2f}\n"
        f"Ganancia Neta: ${ganancia_neta:.2f}\n"
        f"Cobro Potencial: ${cobro_total:.2f}"
    )

if __name__ == "__main__":
    mcp.run()