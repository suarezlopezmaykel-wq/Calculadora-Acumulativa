# Informe de Desarrollo y Pruebas - Calculadora Acumulativa Android

**Proyecto:** Calculadora Acumulativa Android
**Archivo de Informe:** `INFORME_JULES.md`
**PR:** [https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1](https://github.com/suarezlopezmaykel-wq/Calculadora-Acumulativa/pull/1)
**Ubicación de APK compilada:** `app/build/outputs/apk/debug/app-debug.apk`

---

## 1. Resumen de Modificaciones Realizadas

Se diseñó e integró el icono oficial ilustrado para la aplicación Android `Calculadora-Acumulativa` cumpliendo de forma estricta con todas las reglas de coloración y estructura gráfica requeridas:

### Archivos Modificados / Creados:
1. **`app/src/main/res/drawable/ic_launcher_foreground.xml`**:
   - Gráfico vectorial del icono oficial con márgenes de seguridad para iconos adaptativos Android.
   - **Cuerpo de la calculadora:** Azul (`#1D4ED8`).
   - **Pantalla:** Turquesa (`#06B6D4`) mostrando el número `"0"` en azul oscuro (`#0F172A`).
   - **Botones numéricos (del 0 al 9):** Exclusivamente amarillos (`#EAB308`) con dígitos en azul oscuro (`#0F172A`).
   - **Botones de operaciones (`+`, `−`, `×`, `÷`, `=`):** Exclusivamente verdes (`#10B981`) con símbolos en blanco (`#FFFFFF`).
   - **Botón de encendido:** Rojo (`#DC2626`) en la esquina superior derecha con símbolo en blanco (`#FFFFFF`).
2. **`app/src/main/res/drawable/ic_launcher_background.xml`**:
   - Fondo azul intenso (`#0D1B2A`).
3. **`app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` e `ic_launcher_round.xml`**:
   - Configuración oficial de icono adaptativo Android.
4. **`app/src/main/AndroidManifest.xml`**:
   - Referenciado mediante `android:icon="@mipmap/ic_launcher"` y `android:roundIcon="@mipmap/ic_launcher_round"`.

---

## 2. Icono Oficial y Distribución Estricta de Colores

- **Cumplimiento de reglas de coloración del icono:**
  - **Amarillo:** Exclusivamente los botones numéricos del 0 al 9.
  - **Verde:** Exclusivamente los botones de operaciones matemáticas (`+`, `−`, `×`, `÷`, `=`).
  - **Rojo:** Únicamente el botón de encendido en la esquina superior derecha.
  - **Azul:** El cuerpo y el fondo exterior de la calculadora.
  - **Turquesa:** La pantalla principal.
- **Protección de la interfaz:** Los colores de la interfaz de usuario de la calculadora se mantuvieron intactos sin modificaciones innecesarias.

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
| 17 | Integración y distribución estricta de colores del icono | SUPERADA | Icono adaptativo con amarillo (0-9), verde (+,−,×,÷,=), rojo (encendido), turquesa y azul. |
| 18 | Compilación correcta y generación de APK instalable | SUPERADA | Compilación Gradle exitosa y archivo `app-debug.apk` de 8.4MB generado. |
| 19 | Funcionamiento sin conexión a Internet | SUPERADA | Sin dependencias de red ni permisos solicitados. |

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
