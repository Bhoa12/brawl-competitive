# Catálogo dinámico

La APK debe resolver nombres e imágenes desde el backend, no desde una lista fija incorporada en cada versión.

## Endpoints
- GET /catalog
- GET /catalog/brawlers
- GET /catalog/maps

## Flujo de actualización
1. Un adaptador consulta una fuente autorizada.
2. Normaliza brawlers, mapas y recursos visuales.
3. Valida los datos.
4. Publica una nueva versión del catálogo.
5. La APK detecta la versión y refresca su caché.

La sincronización automática debe respetar los términos de las fuentes utilizadas y no incluir credenciales privadas en el cliente.
