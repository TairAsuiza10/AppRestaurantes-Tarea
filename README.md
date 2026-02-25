#  App de Gestión de Restaurantes y Menús

Aplicación Android desarrollada como tarea práctica, tomando como base el proyecto de Contactos de clase. Esta versión demuestra el uso avanzado de **Jetpack Compose**, **Room**, **Navigation Compose** y la arquitectura **MVVM** mediante la gestión de **dos entidades relacionadas**.

## Requisitos de la Tarea Cumplidos

- [x] **Dos entidades con Room (con relación):** Se crearon las entidades `Restaurant` y `Dish`. Están relacionadas de Uno a Muchos (1:N) utilizando una llave foránea (`restaurantId`) con eliminación en cascada (`CASCADE`).
- [x] **CRUD completo para ambas entidades:** Se implementaron las funciones para Crear, Leer, Actualizar (Editar) y Eliminar tanto los restaurantes como los platos de cada menú.
- [x] **Navegación entre pantallas:** Se configuró `NavHost` para navegar desde la lista principal de restaurantes hacia el menú específico del restaurante seleccionado, pasando el ID como argumento.
- [x] **Arquitectura MVVM:** Se estructuró el código separando las vistas (`Screens`), la lógica de negocio y estado (`RestaurantViewModel`) y el acceso a datos (`RestaurantDao` y Room).

##  Interfaz y Experiencia de Usuario (UI/UX)

Se implementaron mejoras visuales utilizando componentes de **Material Design 3**:
* Uso de `ElevatedCard` para resaltar los restaurantes en la pantalla principal.
* Uso de `OutlinedCard` para diferenciar visualmente los platos del menú.
* Implementación de **Alert Dialogs** para los formularios de creación y edición, evitando saltos innecesarios entre pantallas.
* Manejo de "Empty States" (pantallas vacías) con mensajes amigables cuando no hay datos registrados.

##  Base de Datos SQLite

La base de datos ha sido configurada bajo el nombre `restaurant_database`.

En el dispositivo/emulador, Room guarda el archivo SQLite en la siguiente ruta:
`/data/data/com.carevalojesus.contactsapp/databases/restaurant_database`

**Para inspeccionarla desde Android Studio:**
1. Ve a `View` > `Tool Windows` > `App Inspection`
2. Selecciona la pestaña **Database Inspector**
3. Podrás ver las tablas `restaurants` y `dishes` actualizándose en tiempo real.

##  Cómo ejecutar el proyecto

1. Clonar este repositorio en tu máquina local.
2. Abrir el proyecto en **Android Studio** (Ladybug o superior).
3. Esperar a que Gradle sincronice las dependencias.
4. Conectar un dispositivo físico o iniciar un emulador (SDK mínimo: API 26).
5. Presionar **Run** (botón verde) o `Shift + F10`.
   
##