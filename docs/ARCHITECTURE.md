# Arquitectura inicial

## Flujo de datos

Fuentes de datos -> Backend -> Normalización -> Motor estadístico -> API -> APK.

## Principios

1. Las claves privadas nunca se incluyen en la APK.
2. Los datos estáticos y competitivos están separados.
3. Cada métrica competitiva debe almacenar fecha y tamaño de muestra.
4. Las recomendaciones tácticas se distinguen de las estadísticas observadas.
5. La APK puede recibir nuevos datos sin publicar una nueva versión.

## Módulos previstos

- Catálogo de brawlers y habilidades.
- Eventos y mapas.
- Recolector de partidas.
- Meta engine.
- Matchups y counters.
- Builds recomendadas.
- Draft y sinergias.
- Perfil de jugador.
