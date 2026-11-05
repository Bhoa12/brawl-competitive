# Persistencia

La aplicación incorpora una capa SQLAlchemy para separar la lógica de negocio de la base de datos.

## Desarrollo
SQLite permite ejecutar el proyecto localmente sin infraestructura adicional.

## Producción
La variable DATABASE_URL permite utilizar PostgreSQL. La migración a un proveedor gestionado debe acompañarse de:
- copias de seguridad;
- migraciones versionadas;
- cifrado de secretos;
- límites de retención para muestras;
- monitorización.

Las credenciales nunca deben incluirse en la APK ni en el repositorio.
