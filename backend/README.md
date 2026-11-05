# Brawl Competitive Meta Backend

Este backend está diseñado para alimentar la APK sin publicar una nueva versión cuando cambie el meta.

## Arquitectura

1. Un recolector consulta fuentes permitidas de datos.
2. Se normalizan partidas y eventos.
3. Se agregan estadísticas por mapa, modo y ventana temporal.
4. Se publica un JSON versionado consumido por la APK.

## Principio fundamental

Nunca se inventan estadísticas. Cada respuesta competitiva debe incluir:
- ventana temporal
- tamaño de muestra
- fecha de actualización
- fuente/metodología

## Configuración

Las credenciales de la API oficial deben mantenerse exclusivamente en secretos del servidor o del workflow. Nunca se incluyen en la APK.
