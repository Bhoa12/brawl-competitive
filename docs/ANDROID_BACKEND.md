# Conectar Android con el backend

La APK consulta el endpoint GET /analytics/summary.

La URL del backend se configura mediante la propiedad Gradle COMPETITIVE_API_BASE_URL. No se deben incluir tokens secretos en la aplicación.

Para desarrollo local se puede usar una URL de un backend accesible desde el dispositivo o emulador. En producción debe utilizarse HTTPS.

La respuesta incluye tamaño de muestra, fecha de cálculo y las filas de rendimiento por brawler.
