# Contexto competitivo

Todas las recomendaciones del sistema pueden asociarse a un contexto:

- mapa;
- modo;
- rango;
- subrango;
- parche.

El contexto se convierte en una clave de ámbito reproducible. Esto evita mezclar estadísticas de contextos diferentes y permite que el frontend solicite exactamente el meta que necesita.

## Próximo paso

La ingesta debe aportar rango, subrango y parche cuando la fuente de datos los permita. El sistema no debe inventar esos valores.
