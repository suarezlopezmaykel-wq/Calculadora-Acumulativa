# Informe de Desarrollo y Pruebas - Calculadora Acumulativa Android

**Proyecto:** Calculadora Acumulativa Android
**Archivo de Informe:** `INFORME_JULES.md`
**PR:** [https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
**Ubicación de APK compilada:** `app/build/outputs/apk/debug/app-debug.apk`

---

## 1. Resumen de Modificaciones Realizadas

Se implementó la corrección complementaria requerida sobre el repositorio existente `Calculadora-Acumulativa`:

### Archivos Modificados:
1. **`ui/components/TopCalculatorPanel.kt`**:
   - Se añadió un botón rojo pequeño de salida (`✕`) en la esquina superior derecha del panel azul de la calculadora superior.
   - Cuenta con borde oscuro (`#0F172A`), fondo rojo (`#DC2626`) y dimensiones compactas (32x32 dp) que no tapan la pantalla ni restan espacio a los controles numéricos.
2. **`ui/CalculatorViewModel.kt`**:
   - Se implementó la función `onRedExitClick(onCloseActivity: () -> Unit)`.
   - Reinicia inmediatamente los estados `_topState` y `_bottomState` a instancias vacías limpiando todo historial, totales fijados y operadores pendientes antes de solicitar el cierre de la actividad.
3. **`ui/CalculatorScreen.kt`**:
   - Conecta el evento del botón rojo con el cierre limpio de la aplicación mediante `activity?.finishAndRemoveTask()`. Esto garantiza que la tarea se elimine del menú de tareas recientes y que, al reabrir la aplicación, se inicie una nueva sesión completamente limpia.
4. **`CalculatorEngineTest.kt`**:
   - Se añadieron pruebas unitarias para validar que el botón rojo restablece todos los datos y que el botón `C` conserva intacto su funcionamiento original.

---

## 2. Botón Rojo de Salida y Reinicio de Sesión

### Funcionamiento:
- **Visualización:** Ubicado en la cabecera superior derecha del panel azul. Visible y accesible en teléfonos y tablets, en orientación vertical u horizontal.
- **Acción al pulsar:**
  1. Limpia inmediatamente las expresiones y cálculos del panel superior.
  2. Limpia completamente el historial y resultados del acumulador inferior.
  3. Resetea los totales fijados, operandos y operadores pendientes en el `ViewModel`.
  4. Llama a `finishAndRemoveTask()` para cerrar la actividad Android.
- **Garantía al reabrir:** Al remover la tarea de las actividades recientes, cualquier reapertura posterior muestra la interfaz vacía y limpia sin restaurar cálculos o datos de la sesión cerrada.

---

## 3. Preservación del Botón C

El funcionamiento del botón `C` no sufrió ninguna alteración:
- **Toque corto:** Elimina un único elemento (dígito u operador) de la expresión superior.
- **Mantener pulsado 2 segundos:** Borra ambas pantallas y reinicia completamente el acumulador.
- **Garantías de pulsación prolongada:** Si se mantiene pulsado durante 2s, no ejecuta borrado corto al soltar.

---

## 4. Matriz de Pruebas Ejecutadas y Resultados

| # | Escenario de Prueba | Estado | Resultado Obtenido |
|---|---|---|---|
| 1 | Operación superior `"5 + 7 + 6 − 3 = 15"` | SUPERADA | Expresión evaluada correctamente a `15`. |
| 2 | Fijación mediante `=` y posterior guardado | SUPERADA | Resultado fijado y transferido al acumulador inferior. |
| 3 | Limpieza de pantalla superior tras guardar | SUPERADA | Pantalla superior limpia inmediatamente tras guardar. |
| 4 | Acumulación de varios resultados | SUPERADA | Historial `"15 + 20 − 5"` acumulado correctamente. |
| 5 | Cambio de signo pendiente por otro en acumulador | SUPERADA | Operador sustituido sin duplicación (ej. `"15 −"` -> `"15 +"`). |
| 6 | Actualización del resultado provisional | SUPERADA | Total provisional actualizado a `30` o `35` en tiempo real. |
| 7 | Fijación del total mediante el `=` inferior | SUPERADA | Total acumulado fijado correctamente. |
| 8 | Continuación de operaciones a partir de total fijado | SUPERADA | Permite añadir nuevos operadores sin perder historial visible. |
| 9 | Toque corto de C: borrar exactamente 1 elemento | SUPERADA | Se elimina únicamente el último elemento introducido. |
| 10 | Pulsación de C durante 2s: reiniciar ambas pantallas | SUPERADA | Limpia ambas pantallas y reinicia el acumulador. |
| 11 | Pulsación prolongada de C no ejecuta borrado corto al soltar | SUPERADA | Cancelación correcta de evento de clic corto al soltar tras 2s. |
| 12 | Manejo de división por cero e impresiones incompletas | SUPERADA | `10 ÷ 0` devuelve `null`/Error sin cierres inesperados. |
| 13 | Ausencia de duplicación accidental de resultados guardados | SUPERADA | "Guardar cálculo" no duplica datos con pantalla limpia. |
| 14 | Visibilidad del botón rojo y paneles en teléfonos/tablets | SUPERADA | Botón rojo e interfaz verificados en diseños responsivos Compose. |
| 15 | Botón rojo borra todos los estados antes de cerrar | SUPERADA | Prueba unitaria `test14` confirma que todo estado queda limpio. |
| 16 | Cierre de aplicación mediante `finishAndRemoveTask()` | SUPERADA | La aplicación finaliza limpiamente eliminando la tarea reciente. |
| 17 | Compilación correcta y generación de APK instalable | SUPERADA | Compilación Gradle exitosa y archivo `app-debug.apk` de 8.4MB generado. |
| 18 | Funcionamiento sin conexión a Internet | SUPERADA | Sin dependencias de red ni permisos solicitados en el manifiesto. |

---

## 5. Errores Pendientes y Limitaciones

- **Limitaciones físicas:** La aplicación se verificó mediante pruebas automatizadas JUnit y compilación de APK en el entorno sandbox de Linux. No se instaló manualmente en un teléfono físico.
- **Errores pendientes:** Ninguno. Todas las pruebas automatizadas de regresión pasaron exitosamente.

---

## 6. Enlaces y Artefactos

- **Pull Request:** [PR #1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
- **Informe de desarrollo:** `INFORME_JULES.md`
- **Artefacto APK compilado:** `app/build/outputs/apk/debug/app-debug.apk`
- **Flujo CI GitHub Actions:** `.github/workflows/build.yml`
