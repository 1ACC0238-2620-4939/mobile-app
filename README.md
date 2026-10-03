# Trakto Route Mobile

Aplicación Android de Trakto Route para gestionar y dar seguimiento a viajes de transporte de carga.

## Alcance TB1

- Panel operativo con viajes activos, disponibilidad de flota y alertas.
- Listado de viajes con estado, avance, conductor, vehículo y hora estimada.
- Seguimiento de ruta con progreso e hitos del recorrido.
- Flota con disponibilidad y asignación de conductores.

## Arquitectura

El código separa `domain`, `application`, `infrastructure` y `presentation`. La fuente `DemoTraktoRepository` concentra los datos temporales de TB1 y cumple el contrato `TraktoRepository`; puede sustituirse por un adaptador REST sin cambiar las pantallas ni las reglas de aplicación.

## Ejecutar

Requisitos: Android Studio con JDK 25 y Android SDK 37.

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
$env:ANDROID_HOME="$env:LOCALAPPDATA\Android\Sdk"
.\gradlew.bat assembleDebug lintDebug
```

El APK de depuración se genera en `app/build/outputs/apk/debug/app-debug.apk`.

## Repositorios relacionados

- [Backend](https://github.com/1ACC0238-2620-4939/backend)
- [Landing page](https://github.com/1ACC0238-2620-4939/landing-page)
- [Informe](https://github.com/1ACC0238-2620-4939/Report)
