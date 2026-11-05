# Motor de meta

## Objetivo

Calcular y servir el meta por contexto, no una tier list universal.

Un resultado puede filtrarse por:

- mapa;
- modo;
- rango;
- subrango;
- parche.

## Métricas

Cada entrada está preparada para incluir:

- tier;
- meta score;
- pick rate;
- win rate;
- tendencia;
- tamaño de muestra;
- fecha de actualización;
- versión/parche.

## Meta Score

La fórmula inicial pondera:

- rendimiento (50 %);
- popularidad observada (20 %);
- tendencia (15 %);
- matchups (15 %).

Los pesos se versionarán y podrán modificarse cuando tengamos datos reales suficientes.

## Integridad

El motor no inventa estadísticas actuales. Hasta conectar un colector de datos, los snapshots se devuelven vacíos con tamaño de muestra cero.
