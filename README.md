# Parcial2
 
Link al repositorio: https://github.com/siraglez/Parcial2.git

Este programa incluye 3 aplicaciones: un horario de clases, un listado de eventos y un listado de farmacias de zaragoza. Tiene una pantalla principal de bienvenida donde encontramos 3 botones para ir a cada una de las aplicaciones.

## 1. Horario
### Descripción:
Esta aplicación permite gestionar un horario de clases.
### Funcionalidades:
- **Agregar Asignaturas**: El usuario puede introducir manualmente el nombre de la asignatura, el día, la fecha, y la hora. Estos datos se guardan en Firebase.
- **Ver Horario**: Muestra las asignaturas guardadas según el día seleccionado desde un `Spinner`.
- **Clase Actual**: Determina si el usuario tiene una clase en el horario actual, mostrando el nombre de la clase o indicando que no hay clases.

-------------------

## 2. Eventos
### Descripción: 
Esta aplicación permite gestionar un listado de eventos con soporte multilingüe (español e inglés).
### Funcionalidades:
- **Agregar Evento**: Permite al usuario registrar un evento con nombre, descripción, dirección, fecha, precio, y aforo, almacenados en Firebase.
- **Listar Eventos**: Los eventos almacenados se presentan en un `ListView`, mostrando nombre, descripción y precio.
- **Cambio de Idioma**: Desde la lista de eventos, el usuario puede alternar entre español e inglés. La configuración se aplica tanto a la lista como a la pantalla de agregar eventos.

-------------------

## 3. Farmacia
### Descripción:
Esta aplicación muestra una lista de farmacias de Zaragoza. Al seleccionar una farmacia, se abre una pantalla con un mapa y un marcador en su ubicación.
### Funcionalidades:
- **Carga de Datos desde JSON**: La aplicación carga datos de farmacias de un archivo JSON local y los almacena en Firebase.
- **Lista de Farmacias**: Muestra nombre, teléfono, y un ícono en un `ListView`.
- **Mapa de Farmacias**: Al seleccionar una farmacia, se muestra su ubicación en un mapa (imagen fija), con un marcador posicionado según las coordenadas.

-------------------
### Notas Técnicas:
1. **Firebase**:
   - Utilizado para almacenar datos de eventos, asignaturas, y farmacias.
   - Los datos de farmacias se cargan desde un archivo JSON y luego se suben a Firebase.
3. **Mapas con Imagen estática**:
   - Para la aplicación de farmacias, se utiliza una imagen fija con un marcador que se posiciona dinámicamente.
5. **Multilenguaje**:
   - La aplicación de eventos soporta cambios dinámicos de idioma.

### Instalación:
- Se ejecuta cada aplicación desde la `MainActivity` principal, donde se encuentran los 3 botones: `Horario`, `Listado Eventos` y `Farmacias Zaragoza`.
