# Pipeline conectado

El flujo persistente es:

1. El recolector normaliza una partida.
2. La partida se guarda en la base de datos.
3. El agregador consulta la base de datos.
4. Calcula estadísticas globales o filtradas por mapa y modo.
5. Se crea un snapshot versionado.
6. La APK consume los endpoints de analytics y meta.

La siguiente integración conecta el recolector directamente al repositorio persistente para que las muestras sobrevivan a reinicios.
