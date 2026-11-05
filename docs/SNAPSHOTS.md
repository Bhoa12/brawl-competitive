# Snapshots históricos del meta

Un snapshot congela el estado calculado del meta en un momento concreto.

Cada snapshot conserva:
- parche;
- ámbito del análisis;
- fecha de creación;
- tamaño de muestra;
- métricas agregadas.

## Comparación

El sistema compara el snapshot actual con el anterior para detectar cambios en pick rate y win rate.

## Producción

La implementación actual usa memoria como frontera de desarrollo. La interfaz del repositorio está separada para migrar a PostgreSQL sin cambiar las rutas ni la lógica de comparación.
