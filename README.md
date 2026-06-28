# Registro Contabilidad (Comanda Móvil)

Prototipo académico Android **Java** (2022). El nombre del repo es genérico; la app se llama **Comanda Móvil** (`com.example.calculadoraprecios`).

> **Estado:** archivado — no mantenido. Código de referencia histórica.

## Qué hace

- Carrito de productos con cálculo de totales
- Inventario local (archivos en almacenamiento del dispositivo)
- Estados de orden: pendiente, finalizado, pagado, cancelado
- Exportación de datos a CSV
- Pantalla de ajustes (Preferences)

## Stack

- Java 8 · Android SDK 25–32
- AppCompat, Material, Preference
- Sin SQLite (quedó planeado en comentarios del código)

## Compilar

```bash
./gradlew assembleDebug
```

Abrir en Android Studio si el wrapper falla por versión de Gradle.

## Notas

- `MainActivity` concentra la lógica (prototipo monolítico).
- Proyecto universitario; no representa el stack actual (Kotlin, Compose, MVVM).
