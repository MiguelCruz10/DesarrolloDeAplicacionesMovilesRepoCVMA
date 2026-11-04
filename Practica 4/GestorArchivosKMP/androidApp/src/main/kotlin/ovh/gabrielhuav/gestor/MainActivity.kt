package ovh.gabrielhuav.gestor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Inicializar singletons de Android
        AndroidContext.appContext = applicationContext
        AndroidPreferences.sharedPreferences =
            getSharedPreferences("gestor_prefs", MODE_PRIVATE)
        
        // Crear archivos de muestra en la primera ejecución
        createSampleFiles()
        
        setContent { App() }
    }

    private fun createSampleFiles() {
        val filesDir = AndroidContext.appContext.filesDir
        
        // Archivo de texto de muestra
        val txtFile = java.io.File(filesDir, "bienvenida.txt")
        if (!txtFile.exists()) {
            txtFile.writeText("""
                ¡Bienvenido al Gestor de Archivos!
                
                Esta es la Práctica 4 de Desarrollo de Aplicaciones Móviles.
                Instituto Politécnico Nacional - ESCOM
                
                Funcionalidades disponibles:
                - Exploración de archivos internos
                - Visualización de TXT, JSON, XML
                - Historial de archivos recientes
                - Favoritos persistentes
                - Dos temas: Guinda (IPN) y Azul (ESCOM)
            """.trimIndent())
        }
        
        // Archivo JSON de muestra
        val jsonFile = java.io.File(filesDir, "datos.json")
        if (!jsonFile.exists()) {
            jsonFile.writeText("""
                {
                  "aplicacion": "Gestor de Archivos KMP",
                  "version": "1.0.0",
                  "materia": "Desarrollo de Aplicaciones Móviles",
                  "institucion": {
                    "nombre": "ESCOM IPN",
                    "semestre": "2027-1"
                  },
                  "caracteristicas": [
                    "Kotlin Multiplatform",
                    "Compose Multiplatform",
                    "Android e iOS"
                  ]
                }
            """.trimIndent())
        }
        
        // Archivo XML de muestra
        val xmlFile = java.io.File(filesDir, "configuracion.xml")
        if (!xmlFile.exists()) {
            xmlFile.writeText("""
                <?xml version="1.0" encoding="UTF-8"?>
                <configuracion>
                    <app nombre="Gestor de Archivos KMP" version="1.0.0"/>
                    <temas>
                        <tema id="guinda" color="#722F37" descripcion="Tema IPN"/>
                        <tema id="azul" color="#003F87" descripcion="Tema ESCOM"/>
                    </temas>
                    <plataformas>
                        <plataforma>Android (API 24+)</plataforma>
                        <plataforma>iOS 15+</plataforma>
                    </plataformas>
                </configuracion>
            """.trimIndent())
        }
        
        // Subdirectorio de muestra
        val subDir = java.io.File(filesDir, "documentos")
        subDir.mkdirs()
        val subFile = java.io.File(subDir, "notas.md")
        if (!subFile.exists()) {
            subFile.writeText("""
                # Notas de Práctica 4
                
                ## Tecnología
                - **Framework**: Kotlin Multiplatform (KMP)
                - **UI**: Compose Multiplatform
                - **Plataformas**: Android e iOS
                
                ## Implementación
                El código compartido vive en `commonMain` y las implementaciones
                específicas de plataforma en `androidMain` e `iosMain`.
            """.trimIndent())
        }
    }
}
