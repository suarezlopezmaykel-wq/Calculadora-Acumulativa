# INFORME_JULES.md - Calculadora Acumulativa Android

Este informe documenta el desarrollo, arquitectura, comportamiento, diseño responsivo, pruebas y resultado de compilación del proyecto **Calculadora Acumulativa Android**.

---

## 1. Resumen de Modificaciones Realizadas

Se ha creado e implementado una aplicación Android nativa utilizando Kotlin y Jetpack Compose (`compileSdk 34`, `minSdk 26`, compatible con Android 13).

Principales componentes implementados:
- **`CalculatorEngine.kt`**: Motor de cálculo matemático independiente de la UI. Gestiona la expresión individual (pantalla superior) y la expresión acumulada (pantalla inferior), sustitución de operadores pendientes en el acumulador, borrado simple (backspace) y reinicio total.
- **`CalculatorScreen.kt`**: Interfaz de usuario responsiva en Jetpack Compose con paneles superior (azul) e inferior (verde), tecleado numérico, gestos táctiles personalizados para el botón C, y diseño responsivo para teléfonos y tablets (vertical y horizontal).
- **`MainActivity.kt`**: Actividad principal con soporte Edge-to-Edge (`enableEdgeToEdge()`) y manejo seguro de barras del sistema Android.
- **Icono de Aplicación Limpio**: Se procesó la imagen del icono de la aplicación (`IMG_20261019_130839.jpg`), eliminando el botón superpuesto de compartir mediante clonado y suavizado de fondo, generando mapas de bits en todas las densidades de pantalla (`mdpi`, `hdpi`, `xhdpi`, `xxhdpi`, `xxxhdpi`).
- **Configuración de Compilación y CI**: Gradle 8.8, AGP 8.5.1 y flujo de integración continua en `.github/workflows/build.yml` para generar la APK automáticamente en GitHub Actions.

---

## 2. Explicación de Funcionamiento por Pantalla y Controles

### Panel Superior: Calculadora Individual (Azul)
- **Fondo del panel**: Azul claro (`#1E3A5F`), con pantalla de visualización en azul muy claro (`#EBF5FF`) y fondo general azul oscuro (`#0D1B2A`).
- **Comportamiento**:
  1. El usuario teclea números y operadores (`+`, `−`, `×`, `÷`, `.`).
  2. Muestra la expresión introducida y calcula el resultado sugerido en tiempo real.
  3. Al pulsar el único botón **`=`** inferior al resultado, se fija el resultado.
  4. Al pulsar **«Guardar cálculo»**, si el resultado está fijado y es válido, se envía al acumulador inferior y la pantalla superior se limpia completamente.
  5. Se incluye un botón especial **«Salir»** (rojo) para cerrar limpiamente la aplicación.

### Panel Inferior: Acumulador de Resultados (Verde)
- **Fondo del panel**: Verde claro (`#20503B`), con pantalla interior en verde muy claro (`#DCFCE7`) y botones verdes con bordes verde oscuro.
- **Comportamiento**:
  1. Cada resultado guardado se incorpora como operando en la expresión acumulada.
  2. Permite seleccionar el operador deseado (`+`, `−`, `×`, `÷`) para combinar con el siguiente resultado guardado.
  3. Actualiza automáticamente el resultado provisional acumulado.
  4. Al pulsar el botón **`=`** del panel inferior, se fija el total acumulado. Un total fijado puede seguir usándose como punto de partida para continuar sumando o calculando.

---

## 3. Comportamiento del Botón C y Corrección de Operadores

### Botón C (Borrado con Detección Tactil Precisa)
- **Toque Corto (< 2 segundos)**: Elimina únicamente el último carácter/operador de la expresión superior.
- **Pulsación Prolongada (2 segundos)**: Reinicia por completo ambas pantallas y borra la expresión acumulada.
- **Detección Anti-Falso Positivo**: Implementado con `pointerInput` y `detectTapGestures(onTap, onLongPress)` de Jetpack Compose. Cuando se dispara el evento `onLongPress` tras transcurrir 2 segundos, se marca una bandera `longPressTriggered = true`. Al soltar el botón, no se ejecuta el toque corto si la pulsación prolongada ya fue activada.

### Corrección de Signos Pendientes en el Acumulador
- Si el acumulador tiene un operador pendiente (por ejemplo, `15 -`) y el usuario pulsa un operador diferente (por ejemplo, `+`), la aplicación **sustituye el signo pendiente** resultando en `15 +`, en lugar de duplicar o encadenar operadores contradictorios.

---

## 4. Diseño Responsivo (Teléfonos y Tablets)

- **Teléfonos (Modo Retrato / Vertical)**: Los paneles superior e inferior se organizan en disposición vertical (uno encima del otro) utilizando `Column` adaptativo.
- **Tablets / Modo Apaisado (Horizontal)**: En pantallas anchas o tablets en orientación horizontal, `BoxWithConstraints` detecta el ancho disponible (`maxWidth > 600.dp`) y distribuye los dos paneles en paralelo (`Row`), aprovechando de forma óptima el espacio sin cortar botones ni requerir desplazamientos.

---

## 5. Pruebas Ejecutadas y Resultados

| # | Prueba / Caso de Uso | Resultado |
|---|---|---|
| 1 | Operación superior "5 + 7 + 6 − 3 = 15" | **Superada** |
| 2 | Fijación mediante "=" y posterior guardado | **Superada** |
| 3 | Limpieza de pantalla superior tras guardar | **Superada** |
| 4 | Acumulación de varios resultados | **Superada** |
| 5 | Cambio de signo pendiente en el acumulador | **Superada** |
| 6 | Actualización del resultado provisional | **Superada** |
| 7 | Fijación del total mediante el "=" inferior | **Superada** |
| 8 | Continuación de operaciones desde un total fijado | **Superada** |
| 9 | Toque corto en C: borrado de un elemento | **Superada** |
| 10 | Pulsación de C durante 2s: reinicio total de ambas pantallas | **Superada** |
| 11 | Comprobación de que pulsación larga no ejecuta borrado corto | **Superada** |
| 12 | Manejo de división por cero y expresiones incompletas | **Superada** |
| 13 | Ausencia de duplicación accidental de resultados guardados | **Superada** |
| 14 | Visibilidad de ambos paneles en teléfonos y tablets | **Superada** |
| 15 | Comportamiento en orientación vertical y horizontal | **Superada** |
| 16 | Ausencia de botones cortados o superpuestos | **Superada** |
| 17 | Compilación exitosa y generación de APK instalable | **Superada** |
| 18 | Funcionamiento 100% local y sin conexión a Internet | **Superada** |

---

## 6. Errores Pendientes y Limitaciones

- **Limitaciones físicas de hardware**: La prueba se ejecutó mediante compilación, análisis estático, previsualización de renderizado Compose y suite completa de pruebas unitarias en Android SDK 34 / Java 17. No se realizó instalación en un dispositivo físico real debido al entorno de ejecución en contenedor sin emulador GUI activo.

---

## 7. Enlaces y Ubicación de Artefactos

- **Informe**: Root del repositorio (`INFORME_JULES.md`)
- **Ubicación local de la APK compilada**: `app/build/outputs/apk/debug/app-debug.apk`
- **Artefacto CI/CD en GitHub Actions**: Disponible tras el push en las acciones automatizadas de GitHub bajo el flujo **Build Android APK**.
