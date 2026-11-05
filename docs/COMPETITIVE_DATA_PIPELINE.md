# Pipeline de datos competitivos

## Fuente primaria
La API oficial de Brawl Stars se consulta únicamente desde el backend. El token vive en variables de entorno y nunca dentro de la APK.

## Flujo
1. Consultar battle logs autorizados.
2. Normalizar mapa, modo, brawlers y resultado.
3. Almacenar muestras versionadas.
4. Agregar pick rate, win rate y matchups.
5. Recalcular meta y counters.

Un battle log individual no representa el meta global: se necesita una muestra amplia y diversa.
