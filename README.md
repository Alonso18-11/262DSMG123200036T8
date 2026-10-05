# Unidad 6: Persistencia de datos (Android Basics with Compose)

## Ruta 1 – Introducción a SQL

| Carpeta | Codelab | Contenido |
|---|---|---|
| `ruta1/01-sql-basics` | Cómo usar SQL para leer y escribir en una base de datos | `sql/01_crear_y_poblar.sql` crea la tabla `email` con 40 correos de ejemplo. `sql/02_consultas_codelab.sql` tiene todas las consultas del codelab: SELECT, COUNT/MAX/MIN, DISTINCT, WHERE con AND/OR, LIKE, GROUP BY, ORDER BY, LIMIT/OFFSET, INSERT, UPDATE y DELETE. `email.db` es la base lista para abrir en DB Browser for SQLite. La app Android carga esa misma base con Room (`createFromAsset`) para que ejecutes las consultas en el **Database Inspector**. |

## Ruta 2 – Cómo usar Room para la persistencia de datos

| Carpeta | Codelab | Contenido |
|---|---|---|
| `ruta2/01-inventory` | Cómo conservar datos con Room + Cómo leer y actualizar datos con Room | App *Inventario* completa: entidad `Item`, `ItemDao` (insert/update/delete/Flow), `InventoryDatabase` singleton, repositorio, contenedor, `AppViewModelProvider`, navegación con 4 pantallas (lista, agregar, detalle, editar), vender una unidad, eliminar con diálogo de confirmación y `stateIn(WhileSubscribed)`. Prueba instrumentada `ItemDaoTest` con base de datos en memoria (`androidTest`). |
| `ruta2/02-bus-schedule` | Práctica: Bus Schedule | Base precargada `assets/database/bus_schedule.db` (10 paraderos, 80 horarios). DAO con `Flow`, `createFromAsset`, ViewModel con fábrica, horario completo y horario por paradero con navegación. |

## Ruta 3 – Cómo almacenar datos con DataStore

| Carpeta | Codelab | Contenido |
|---|---|---|
| `ruta3/01-dessert-release` | Cómo guardar preferencias de forma local con DataStore | Botón que alterna entre lista y cuadrícula. La elección se guarda con Preferences DataStore (`booleanPreferencesKey`) y se mantiene al cerrar la app. Incluye prueba local del repositorio con un DataStore temporal. |
| `ruta3/02-flight-search` | Proyecto: Crea una app de búsqueda de vuelos | Base precargada con 21 aeropuertos (Perú, Latinoamérica, EE. UU. y España) y tabla `favorite`. Autocompletado con `LIKE`, lista de vuelos desde el aeropuerto elegido, favoritos guardados en Room y texto de búsqueda guardado en DataStore. Pruebas locales de la lógica de vuelos. |

## Cómo abrirlos
Abre cada carpeta de proyecto por separado en Android Studio (File > Open). Las pruebas locales están en `app/src/test` y las instrumentadas (necesitan emulador) en `app/src/androidTest`.

Para ver la base de datos: ejecuta la app y abre **View > Tool Windows > App Inspection > Database Inspector**.

Versiones: AGP 8.7.3, Kotlin 2.0.21, KSP 2.0.21-1.0.28, Room 2.6.1, DataStore 1.1.1, Navigation 2.8.5, Compose BOM 2024.12.01.
