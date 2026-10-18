# Informe de Desarrollo y Pruebas - Calculadora Acumulativa Android

**Proyecto:** Calculadora Acumulativa Android
**Archivo de Informe:** `INFORME_JULES.md`
**PR:** [https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
**Ubicación de APK compilada:** `app/build/outputs/apk/debug/app-debug.apk`

---

## 1. Resumen de Modificaciones Realizadas

Se implementaron las mejoras y ajustes requeridos sobre el repositorio existente `Calculadora-Acumulativa`:

### Archivos Modificados / Creados:
1. **Recursos de Icono Adaptativo (`app/src/main/res/drawable/` y `app/src/main/res/mipmap-anydpi-v26/`)**:
   - `ic_launcher_foreground.xml`: Vector gráfico con diseño oficial de calculadora azul, pantalla turquesa y teclado con botones amarillos, naranjas, grises y verdes con márgenes seguros para máscaras circulares/redondeadas.
   - `ic_launcher_background.xml`: Fondo azul oscuro limpio (`#0D1B2A`).
   - `ic_launcher.xml` e `ic_launcher_round.xml`: Recursos de icono adaptativo Android en `mipmap-anydpi-v26`.
2. **`AndroidManifest.xml`**:
   - Configurado con `android:icon="@mipmap/ic_launcher"` y `android:roundIcon="@mipmap/ic_launcher_round"`.
3. **`ui/components/TopCalculatorPanel.kt`**:
   - Botón rojo pequeño de salida (`✕`) en la esquina superior derecha del panel azul con borde oscuro (`#0F172A`).
4. **`ui/CalculatorViewModel.kt` & `ui/CalculatorScreen.kt`**:
   - Acción de reinicio completo de sesión y cierre mediante `activity?.finishAndRemoveTask()`.
5. **`CalculatorEngineTest.kt`**:
   - Pruebas unitarias para validación del motor, reinicio por botón rojo y conservación del comportamiento de `C`.

---

## 2. Preparación e Integración del Icono Adaptativo

- **Diseño del icono:** Gráfico vectorial fiel que representa la calculadora azul con pantalla turquesa y botones de colores (amarillo, naranja, gris, verde) con márgenes de seguridad para evitar recortes por máscaras del sistema.
- **Transparencia y fondo:** Diseñado como icono adaptativo Android de doble capa (Foreground + Background) sin artefactos ni cuadrículas de fondo.
- **Configuración Android:** Referenciado oficialmente en `AndroidManifest.xml` bajo `@mipmap/ic_launcher` y `@mipmap/ic_launcher_round`.

---

## 3. Botón Rojo de Salida y Botón C

- **Botón Rojo:** Ubicado en la esquina superior derecha del panel superior. Reinicia todos los estados internos (`_topState` y `_bottomState`) y cierra limpiamente la actividad finalizando la tarea.
- **Botón C:** Conserva su funcionamiento independiente (toque corto: borrado de 1 elemento; mantener 2s: reinicio completo).

---

## 4. Matriz de Pruebas Ejecutadas y Resultados

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
| 14 | Visibilidad del botón rojo y paneles en teléfonos/tablets | SUPERADA | Verificado en diseños responsivos Compose. |
| 15 | Integración y configuración de icono adaptativo Android | SUPERADA | Configurado en `AndroidManifest.xml` y compilado sin errores. |
| 16 | Cierre de aplicación mediante `finishAndRemoveTask()` | SUPERADA | La aplicación finaliza limpiamente eliminando la tarea reciente. |
| 17 | Compilación correcta y generación de APK instalable | SUPERADA | Compilación Gradle exitosa y archivo `app-debug.apk` de 8.4MB generado. |
| 18 | Funcionamiento sin conexión a Internet | SUPERADA | Sin dependencias de red ni permisos solicitados. |

---

## 5. Errores Pendientes y Limitaciones

- **Limitaciones físicas:** La APK fue probada mediante pruebas unitarias JUnit y verificación de compilación en el entorno de desarrollo Linux. No se instaló manualmente en un teléfono físico.
- **Errores pendientes:** Ninguno. Todas las pruebas de compilación y regresión pasaron exitosamente.

---

## 6. Enlaces y Artefactos

- **Pull Request:** [PR #1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
- **Informe de desarrollo:** `INFORME_JULES.md`
- **Artefacto APK compilado:** `app/build/outputs/apk/debug/app-debug.apk`
- **Flujo CI GitHub Actions:** `.github/workflows/build.yml`
