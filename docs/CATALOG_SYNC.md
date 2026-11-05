# Sincronización APK ↔ catálogo

La APK consulta GET /catalog y conserva una caché de la última versión obtenida.

Cuando el servidor publica una nueva versión:
- aparecen nuevos brawlers sin actualizar la APK;
- aparecen mapas nuevos;
- cambian nombres o URLs de recursos;
- el cliente puede invalidar su caché mediante el campo version.

La persistencia local de la caché y la sincronización programada del servidor son los siguientes pasos.
