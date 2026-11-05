# Actualización automática

## Catálogo del juego

BrawlAPI se utiliza para detectar cambios en el catálogo. Sus datos se reconstruyen a partir de los archivos del juego cuando Brawl Stars recibe una actualización.

El backend calcula una huella usando IDs y versiones de brawlers y mapas. Si la huella cambia, el sistema sabe que debe sincronizar el catálogo y marcar una nueva versión de datos.

GitHub Actions ejecuta una comprobación cada 6 horas y permite una ejecución manual.

## Datos competitivos

El catálogo del juego y las estadísticas competitivas son problemas distintos.

La API estática utilizada para el catálogo no proporciona rotación de eventos en vivo ni estadísticas competitivas observadas. Por eso el meta, pick rate, win rate y counters deberán alimentarse mediante una fuente de partidas/estadísticas autorizada y una base de datos propia.

Nunca se debe rellenar el meta con datos inventados.

## Flujo final

1. Detectar cambio de catálogo.
2. Sincronizar nuevos brawlers, mapas y recursos.
3. Versionar el snapshot.
4. Ingerir estadísticas competitivas reales.
5. Recalcular meta y counters.
6. Publicar el nuevo snapshot.
7. La APK consulta la versión y actualiza su caché.
