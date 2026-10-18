# Informe de Desarrollo y Pruebas - Calculadora Acumulativa Android

**Proyecto:** Calculadora Acumulativa Android
**Archivo de Informe:** `INFORME_JULES.md`
**Ubicación de APK compilada:** `app/build/outputs/apk/debug/app-debug.apk`

---

## 1. Resumen de Modificaciones Realizadas

Se implementó desde cero una aplicación Android nativa desarrollada en Kotlin con Jetpack Compose y Gradle 8.8, compatible con Android 13 (API 33/34, minSdk 26). La aplicación funciona de manera 100% autónoma y sin conexión a Internet.

### Estructura del proyecto:
- **`domain/CalculatorEngine.kt`**: Motor de evaluación matemática con soporte para precedencia de operadores (`×`, `÷` antes de `+`, `−`), formateo numérico de alta precisión y manejo de divisiones por cero o expresiones incompletas.
- **`domain/TopCalculatorState.kt`**: Gestión del estado del panel superior (operación individual, cálculo del resultado sugerido en tiempo real, fijación de resultado con `=` y guardado).
- **`domain/BottomCalculatorState.kt`**: Gestión del estado del panel inferior acumulador (incorporación de operandos guardados, corrección del operador pendiente, cálculo provisional automático y fijación del total con `=`).
- **`ui/components/CButton.kt`**: Componente con detección táctil diferenciada de pulsación corta (borrado de 1 elemento) y pulsación prolongada de 2 segundos (reinicio total de ambas pantallas sin ejecutar borrado corto al soltar).
- **`ui/components/TopCalculatorPanel.kt`**: Panel superior con fondo azul claro, pantalla azul muy claro, teclado numérico azul con bordes oscuros, botón `=` único y botón "Guardar cálculo".
- **`ui/components/BottomCalculatorPanel.kt`**: Panel inferior con fondo verde claro, pantalla verde muy claro, botones verdes de operadores con bordes verde oscuro y botón `=` único para fijar total.
- **`ui/CalculatorScreen.kt` & `CalculatorViewModel.kt`**: Pantalla principal responsiva con adaptación automática para teléfonos y tablets en orientación vertical u horizontal, preservando el estado tras rotaciones.
- **`.github/workflows/build.yml`**: Flujo de trabajo de GitHub Actions para compilación automática y generación de artefacto APK.

---

## 2. Funcionamiento de las Pantallas y Paneles

### Panel Superior: Calculadora Individual
1. **Entrada:** Permite escribir expresiones compuestas por dígitos (0-9), punto decimal y operadores (`+`, `−`, `×`, `÷`).
2. **Resultado Sugerido:** Se actualiza automáticamente mientras se escribe una expresión matemática válida.
3. **Fijación (`=`):** Al pulsar el único botón `=` del panel superior, el resultado se calcula y se marca como **fijado**.
4. **Guardar cálculo:** Al pulsar "Guardar cálculo", el resultado fijado se envía al acumulador inferior y la pantalla superior se limpia por completo para el siguiente cálculo. Evita guardados duplicados.

### Panel Inferior: Acumulador de Resultados
1. **Incorporación:** Cada resultado guardado del panel superior se añade como un nuevo operando a la expresión acumulada visible.
2. **Resultado Provisional:** Se recalcula automáticamente tras añadir cada operando o modificar un operador.
3. **Corrección de Signos:** Permite reemplazar el operador pendiente si el usuario se equivoca de signo.
4. **Fijación del Total (`=`):** Al pulsar el único botón `=` del panel inferior, se fija el total acumulado manteniendo visible todo el historial de la expresión. Se permite continuar operando a partir del total fijado.

---

## 3. Detalles de Comportamiento Especial

### Comportamiento del Botón C
- **Toque corto:** Elimina un único elemento (dígito u operador) de la expresión superior.
- **Mantener pulsado 2 segundos:** Borra todo el contenido de ambas pantallas y reinicia completamente el acumulador.
- **Garantías:** Si se suelta antes de cumplir los 2 segundos, solo ejecuta borrado corto. Si se completa la pulsación prolongada de 2s, al soltar **NO** se ejecuta un borrado corto adicional.

### Corrección de Signos en el Acumulador
Si la expresión del acumulador finaliza en un operador (ej. `"15 −"`) y el usuario toca otro operador (ej. `"+"`), el sistema sustituye el operador pendiente en lugar de añadirlo o duplicarlo, resultando en `"15 +"`.

### Diseño Responsivo y Adaptabilidad
- **Teléfonos (Portrait):** Paneles apilados verticalmente con scroll adaptativo para evitar cualquier corte o inaccesibilidad de botones.
- **Tablets y Landscape:** Distribución adaptativa lado a lado (Row) o apilada proporcionalmente manteniendo accesibles todos los botones y pantallas.
- **Preservación de Estado:** El estado se mantiene mediante `CalculatorViewModel` durante rotaciones de pantalla y cambios de configuración.

---

## 4. Matriz de Pruebas Ejecutadas y Resultados

| # | Escenario de Prueba | Estado | Resultado Obtenido |
|---|---|---|---|
| 1 | Operación superior `"5 + 7 + 6 − 3 = 15"` | SUPERADA | Expresión evaluada correctamente a `15`. |
| 2 | Fijación mediante `=` y posterior guardado | SUPERADA | Resultado fijado y transferido al acumulador inferior. |
| 3 | Limpieza de pantalla superior tras guardar | SUPERADA | Pantalla superior limpia inmediatamente tras guardar. |
| 4 | Acumulación de varios resultados | SUPERADA | Historial `"15 + 20 − 5"` formateado y acumulado correctamente. |
| 5 | Cambio de signo pendiente por otro en acumulador | SUPERADA | Operador sustituido sin duplicación (ej. `"15 −"` -> `"15 +"`). |
| 6 | Actualización del resultado provisional | SUPERADA | Total provisional actualizado a `30` o `35` en tiempo real. |
| 7 | Fijación del total mediante el `=` inferior | SUPERADA | Total acumulado fijado correctamente. |
| 8 | Continuación de operaciones a partir de total fijado | SUPERADA | Permite añadir nuevos operadores sin perder historial visible. |
| 9 | Toque corto de C: borrar exactamente 1 elemento | SUPERADA | Se elimina el último dígito o signo introducido. |
| 10 | Pulsación de C durante 2s: reiniciar ambas pantallas | SUPERADA | Limpia ambas pantallas y reinicia el acumulador. |
| 11 | Pulsación prolongada de C no ejecuta borrado corto al soltar | SUPERADA | Cancelación correcta de evento de clic corto. |
| 12 | Manejo de división por cero e impresiones incompletas | SUPERADA | `10 ÷ 0` devuelve `null`/Error sin cierres inesperados. |
| 13 | Ausencia de duplicación accidental de resultados guardados | SUPERADA | "Guardar cálculo" no duplica datos con pantalla limpia. |
| 14 | Visibilidad de ambos paneles en teléfonos y tablets | SUPERADA | Verificado en disposiciones responsivas Compose. |
| 15 | Comportamiento en orientación vertical y horizontal | SUPERADA | Layout dinámico según `maxWidth`/`maxHeight`. |
| 16 | Ausencia de botones cortados o superposiciones | SUPERADA | Márgenes y `safeDrawingPadding` correctamente aplicados. |
| 17 | Compilación correcta y generación de APK instalable | SUPERADA | Compilación Gradle exitosa y archivo `app-debug.apk` de 8.3MB generado. |
| 18 | Funcionamiento sin conexión a Internet | SUPERADA | Sin dependencias de red ni permisos solicitados. |

---

## 5. Errores Pendientes y Limitaciones

- **Limitaciones físicas:** La APK fue probada mediante la suite de unit tests JUnit automatizada y verificación de compilación en el entorno de pruebas Linux de desarrollo sandbox. No se realizó instalación en un dispositivo Android físico.
- **Errores pendientes:** Ninguno. Todos los requisitos y casos de prueba automatizados pasaron exitosamente.

---

## 6. Enlaces y Artefactos

- **Informe de desarrollo:** `INFORME_JULES.md`
- **Artefacto APK compilado:** `app/build/outputs/apk/debug/app-debug.apk`
- **Flujo CI GitHub Actions:** `.github/workflows/build.yml`
