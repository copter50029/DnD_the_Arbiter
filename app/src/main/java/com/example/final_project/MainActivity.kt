package com.example.final_project

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.final_project.data.local.entity.AppLanguage
import com.example.final_project.ui.navigation.ArbiterMainScreen
import com.example.final_project.ui.theme.ArbiterTheme
import com.example.final_project.ui.util.LocaleHelper

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ArbiterApplication
        val userSettingsRepo = app.userSettingsRepository

        setContent {
            val settings by userSettingsRepo.settingsFlow.collectAsState(initial = null)
            val preferredLanguage = settings?.preferredLanguage ?: AppLanguage.EN

            // Dynamically apply runtime locale to Compose Configuration and LocalContext
            val currentLocale = LocaleHelper.getLocaleForLanguage(preferredLanguage)
            val currentConfig = LocalConfiguration.current
            val updatedConfig = Configuration(currentConfig).apply {
                setLocale(currentLocale)
            }
            val localizedContext = LocalContext.current.createConfigurationContext(updatedConfig)

            CompositionLocalProvider(
                LocalConfiguration provides updatedConfig,
                LocalContext provides localizedContext
            ) {
                ArbiterTheme {
                    ArbiterMainScreen()
                }
            }
        }
    }
}