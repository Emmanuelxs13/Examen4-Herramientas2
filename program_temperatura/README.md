## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

# Examen 4 - Programación Funcional: Temperaturas por Ciudad

## Descripción General

Esta aplicación Java permite analizar y visualizar registros de temperaturas diarias de distintas ciudades a partir de un archivo CSV. Utiliza programación funcional (Streams, lambdas, colecciones inmutables) y una interfaz gráfica moderna basada en Swing, con un menú inferior siempre visible para una experiencia fluida.

## Estructura del Proyecto

- `src/` Código fuente Java organizado en paquetes:
  - `model/TemperatureRecord.java`: Modelo inmutable para cada registro.
  - `service/CsvService.java`: Carga y parseo funcional del CSV.
  - `service/TemperatureService.java`: Procesamiento funcional de datos (promedios, máximos, mínimos).
  - `service/ChartService.java`: Generación de gráficas de barras (JFreeChart).
  - `utils/DateUtils.java`: Utilidades para fechas.
  - `Main.java`: Orquestador e interfaz gráfica.
- `lib/` Dependencias externas (JARs de JFreeChart y JCommon).
- `bin/` Archivos compilados.
- `ejemplo_temperaturas.csv`: Archivo de ejemplo listo para usar.

## Requisitos para Compilar y Ejecutar

### Opción 1: Usar archivos JAR

1. Descarga JFreeChart y JCommon desde:
   - https://sourceforge.net/projects/jfreechart/
2. Coloca los archivos `.jar` en la carpeta `lib/`.
3. Compila el proyecto:
   ```powershell
   javac -cp ".;lib/*" -d bin src/**/*.java
   ```
4. Ejecuta la aplicación:
   ```powershell
   java -cp ".;lib/*;bin" Main
   ```

### Opción 2: Usar Maven o Gradle

Agrega la dependencia de JFreeChart en tu archivo de configuración.

## Formato y Criterios del Archivo CSV

- El archivo debe tener el siguiente encabezado y formato:
  ```
  Ciudad,Fecha,Temperatura
  CiudadA,2023-01-01,25.3
  CiudadB,2023-01-01,22.1
  ...
  ```
- La fecha debe estar en formato `yyyy-MM-dd`.
- La temperatura debe ser un número (puede tener decimales).
- No debe haber columnas extra ni faltar ninguna columna.
- **El rango de fechas que ingreses en la aplicación debe estar presente en el archivo CSV** para evitar errores o resultados vacíos.
- Un archivo de ejemplo (`ejemplo_temperaturas.csv`) se encuentra en la raíz del proyecto.

## Funcionalidades Principales

- **Carga de datos:** Selecciona el archivo CSV desde la interfaz.
- **Menú inferior fijo:** Siempre visible, permite navegar entre funciones sin perder contexto.
- **Gráfica de barras:** Selecciona un rango de fechas y visualiza el promedio de temperatura por ciudad.
- **Consulta por fecha:** Ingresa una fecha y obtén la ciudad más y menos calurosa ese día.
- **Transiciones suaves:** El contenido central cambia sin cerrar la ventana ni perder el menú.
- **Manejo de errores:** Mensajes claros si el archivo es inválido, faltan datos o el rango de fechas no existe.

## Métodos y Clases Clave

- `Main.java`:
  - `main(String[] args)`: Inicializa la interfaz y muestra los criterios del CSV.
  - `initUI()`: Configura la ventana principal y el menú inferior.
  - `crearMenuInferior()`: Crea el panel de botones fijo e interactivo.
  - `handleRangeChartUI()`: Solicita fechas y muestra la gráfica en el panel central.
  - `handleHottestColdestUI()`: Solicita fecha y muestra resultados en el panel central.
  - `setContent(JPanel)`: Cambia el contenido central con transición suave.
- `CsvService.loadFromCsv(String path)`: Carga y valida los registros del archivo.
- `TemperatureService.averageTemperatureByCity(...)`: Calcula promedios funcionalmente.
- `TemperatureService.hottestAndColdestCity(...)`: Obtiene ciudad más y menos calurosa.
- `ChartService.ChartPanelWrapper`: Panel reutilizable para integrar gráficas en la UI.

## Recomendaciones

- Usa el archivo de ejemplo para probar la aplicación.
- Si creas tu propio CSV, asegúrate de que el rango de fechas que vayas a consultar esté presente en el archivo.
- Si tienes dudas, revisa los comentarios en el código fuente, que explican cada sección clave.

---

**Autor:** Examen 4 - Programación Funcional, Java Swing, 2025
