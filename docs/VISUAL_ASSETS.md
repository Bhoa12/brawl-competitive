# Política visual de Brawl Competitive

La aplicación será visual por defecto: no usará el nombre como sustituto de un recurso gráfico cuando exista una imagen disponible.

## Recursos mostrados

- Brawlers: retrato y/o modelo.
- Mapas: imagen renderizada del mapa.
- Gadgets: icono.
- Habilidades estelares: icono.
- Refuerzos: icono.
- Hipercargas y otras mecánicas: icono cuando la fuente proporcione un identificador/recurso.
- Rangos competitivos: emblema gráfico.
- Modos de juego: icono.

## Fuente de activos

BrawlAPI proporciona `imageUrl` para brawlers, gadgets, habilidades estelares, mapas y modos. El CDN público de Brawlify contiene rutas para mapas, brawlers, gadgets, habilidades estelares, refuerzos y emblemas competitivos.

La interfaz mostrará siempre:
1. Imagen principal.
2. Nombre como etiqueta accesible y texto de apoyo.
3. Datos competitivos asociados.

Los recursos se cargarán remotamente y se almacenarán en caché para reducir tráfico y permitir una experiencia rápida.
