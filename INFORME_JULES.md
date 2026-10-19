# Informe de Desarrollo y Pruebas - Calculadora Acumulativa Android

**Proyecto:** Calculadora Acumulativa Android
**Archivo de Informe:** `INFORME_JULES.md`
**PR:** [https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
**Ubicación de APK compilada:** `app/build/outputs/apk/debug/app-debug.apk`

---

## 1. Resumen de Modificaciones Realizadas

Se implementaron las correcciones de diseño, distribución y alineación requeridas sobre el repositorio existente `Calculadora-Acumulativa`:

### Archivos Modificados:
1. **`ui/components/TopCalculatorPanel.kt`**:
   - **Reordenamiento de números de arriba a abajo:** Se reestructuró la cuadrícula numérica en secuencia ascendente vertical:
     - Fila 1: `1`, `2`, `3`, `C`, `÷`
     - Fila 2: `4`, `5`, `6`, `×`
     - Fila 3: `7`, `8`, `9`, `−`
     - Fila 4: `0`, `.`, `+`
     - Fila 5: `=`, `Guardar cálculo`
   - **Intercambio de posiciones entre C y ÷:** Se intercambiaron físicamente las celdas del botón `C` y del botón `÷`. `C` ocupa la cuarta columna y `÷` la quinta columna de la fila 1. La lógica funcional permanece 100% intacta (`C` realiza borrado corto con 1 toque o borrado total al mantener 2s; `÷` realiza la división matemática).
2. **`ui/components/CalcButton.kt` & `ui/components/CButton.kt`**:
   - Se estableció una altura mínima uniforme de `48.dp`, tamaño de letra de `22.sp` y texto centrado para garantizar proporciones legibles y alineación perfecta en columnas.
3. **`ui/components/BottomCalculatorPanel.kt`**:
   - Se ampliaron las pantallas y filas de botones con rellenos y tamaños proporcionales para aprovechar el ancho útil de la pantalla en teléfonos y tablets.
4. **`CalculatorEngineTest.kt`**:
   - Se incorporaron pruebas unitarias (`test15` y `test16`) para validar la secuencia de números de arriba a abajo y el funcionamiento de las funciones intercambiadas de `C` y `÷`.

---

## 2. Alineación, Proporciones y Aprovechamiento de Pantalla

- **Alineación Vertical en Columnas:** Los números ocupan limpiamente las columnas 1, 2 y 3. Los operadores y botones de acción se alinean en las columnas contiguas.
- **Aprovechamiento Útil del Teléfono:** Las pantallas de cálculo y filas de botones utilizan `fillMaxWidth()` y distribuciones con pesos (`weight`) para maximizar el área táctil sin generar desbordamientos ni recortes.
- **Preservación de Funciones:** Se conservaron intactas la lógica matemática, la sustitución de signos en el acumulador, el botón `=` en cada panel, la función "Guardar cálculo", el botón rojo de salida con borrado completo y el icono adaptativo Android.

---

## 3. Matriz de Pruebas Ejecutadas y Resultados

| # | Escenario de Prueba | Estado | Resultado Obtenido |
|---|---|---|---|
| 1 | Operación superior `"5 + 7 + 6 − 3 = 15"` | SUPERADA | Expresión evaluada correctamente a `15`. |
| 2 | Fijación mediante `=` y posterior guardado | SUPERADA | Resultado fijado y transferido al acumulador inferior. |
| 3 | Limpieza de pantalla superior tras guardar | SUPERADA | Pantalla superior limpia inmediatamente tras guardar. |
| 4 | Acumulación de varios resultados | SUPERADA | Historial `"15 + 20 − 5"` acumulado correctamente. |
| 5 | Cambio de signo pendiente por otro en acumulador | SUPERADA | Operador sustituido sin duplicación. |
| 6 | Actualización del resultado provisional | SUPERADA | Total provisional actualizado a `30` o `35` en tiempo real. |
| 7 | Fijación del total mediante el `=` inferior | SUPERADA | Total acumulado fijado correctamente. |
| 8 | Continuación de operaciones a partir de total fijado | SUPERADA | Permite añadir nuevos operadores sin perder historial visible. |
| 9 | Toque corto de C: borrar exactamente 1 elemento | SUPERADA | Se elimina únicamente el último elemento introducido. |
| 10 | Pulsación de C durante 2s: reiniciar ambas pantallas | SUPERADA | Limpia ambas pantallas y reinicia el acumulador. |
| 11 | Pulsación prolongada de C no ejecuta borrado corto al soltar | SUPERADA | Cancelación correcta de evento de clic corto. |
| 12 | Manejo de división por cero e impresiones incompletas | SUPERADA | `10 ÷ 0` devuelve `null`/Error sin cierres inesperados. |
| 13 | Ausencia de duplicación accidental de resultados guardados | SUPERADA | "Guardar cálculo" no duplica datos con pantalla limpia. |
| 14 | Botón rojo de salida borra todos los estados antes de cerrar | SUPERADA | Cierre limpio y reset total en ViewModel. |
| 15 | Orden numérico de arriba a abajo (1..9, 0) | SUPERADA | Verificada secuencia ascendente `1234567890`. |
| 16 | Funciones e intercambio físico de posiciones de C y ÷ | SUPERADA | `10 ÷ 2 = 5` evalúa correctamente y `C` realiza borrado corto. |
| 17 | Compilación correcta y generación de APK instalable | SUPERADA | Compilación Gradle exitosa y archivo `app-debug.apk` de 8.4MB generado. |
| 18 | Funcionamiento sin conexión a Internet | SUPERADA | Sin dependencias de red ni permisos solicitados. |

---

## 4. Errores Pendientes y Limitaciones

- **Limitaciones físicas:** La aplicación se verificó mediante pruebas unitarias JUnit y compilación en el entorno Linux. No se instaló manualmente en un dispositivo móvil físico.
- **Errores pendientes:** Ninguno. Todas las pruebas de compilación y regresión pasaron exitosamente.

---

## 5. Enlaces y Artefactos

- **Pull Request:** [PR #1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
- **Informe de desarrollo:** `INFORME_JULES.md`
- **Artefacto APK compilado:** `app/build/outputs/apk/debug/app-debug.apk`
- **Flujo CI GitHub Actions:** `.github/workflows/build.yml`
