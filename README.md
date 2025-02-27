## Ejecución de la Aplicación

La aplicación está diseñada para gestionar las reservas de hoteles y vuelos, permitiendo realizar las siguientes acciones:

- **Crear hoteles.**
- **Editar hoteles.**
- **Eliminar hoteles.**
- **Buscar hoteles.**
- **Crear vuelos.**
- **Editar vuelos.**
- **Eliminar vuelos.**
- **Buscar vuelos.**
- **Hacer reservas de vuelos.**
- **Hacer reservas de hoteles.**
- **Buscar usuarios vuelos.**

## Manejo de Excepciones

La aplicación incluye manejo de errores en las siguientes situaciones:

- Si se intenta agregar un ciudadano o turno con datos incompletos, se mostrarán mensajes de error en la página.
- Si no hay datos disponibles (como ciudadanos o turnos), la interfaz lo indicará.
- Se incluyen validaciones en el formulario para evitar el envío de datos vacíos.

## Supuestos

- Se asume que la base de datos está configurada correctamente y es accesible desde la aplicación.
- La base de datos tiene las siguientes tablas:
  - **ciudadanos**: contiene columnas como `id`, `nombre`, `apellido` y `dni`.
  - **turnos**: contiene columnas como `id`, `numero`, `fecha`, `descripcion`, `estado` y `ciudadano_id` (clave foránea).
- Los métodos del controlador manejan correctamente las operaciones CRUD y están conectados al modelo de datos.
- La aplicación maneja correctamente las transacciones en la base de datos, asegurando que se realicen commits o rollbacks según corresponda.

## Pruebas

Para probar la aplicación:

1. **Ejecutar la aplicación en el servidor:**
   - Copia los archivos del proyecto en el directorio `webapps` del proyecto.
   - Asegúrate de que Tomcat 10 esté configurado y en ejecución.
   - Accede a la aplicación en tu navegador web utilizando la URL correspondiente.

2. **Prueba de ciudadanos:**
   - Accede a la sección de ciudadanos.
   - Haz clic en el botón "Agregar Nuevo Ciudadano", para probar la funcionalidad de registro.
   - Verifica que el nuevo ciudadano aparezca en la tabla de ciudadanos.
   - Intenta agregar un ciudadano sin completar todos los campos para comprobar los mensajes de error.

3. **Prueba de turnos:**
   - Accede a la sección de turnos.
   - Haz clic en el botón "Agregar Nuevo Turno", para probar el registro de turnos.
   - Intenta agregar un turno sin completar todos los campos para comprobar los mensajes de error.
   - Intenta asociar el nuevo turno a un ciudadano que no exista para comprobar los mensajes de error.
   - Completa los filtros (fecha, estado, ciudadano ID) y verifica que la tabla de turnos responda correctamente.

## Requisitos

Para probar la aplicación necesitas:

### 1. Herramientas necesarias:
   - Tener IntelliJ IDEA Community Edition instalado (la versión usada en el proyecto es la 2024.3.1).
   - Tener XAMPP instalado.
   - Tener Apache Tomcat 10 instalado.
   - Tener MySQL Workbench instalado (la versión usada en el proyecto es la 8.0 CE).

### 2. Configuración de la base de datos:
   - Crea una base de datos llamada `turnos_db`.
   - Crear la tabla `ciudadano` y `turno`
   - Crear el índice en las columnas `fecha` y `estado` de la tabla `turno`.
   - Crear datos de prueba.
   - Comprueba que el archivo `persistence.xml` esté configurado con las credenciales correctas para conectarse a la base de datos.


> **Nota:** Todos los comandos SQL necesarios se encuentran en el archivo `turnero.sql`.
