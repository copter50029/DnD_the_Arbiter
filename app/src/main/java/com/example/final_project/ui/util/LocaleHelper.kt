package com.example.final_project.ui.util

import android.content.Context
import android.content.res.Configuration
import com.example.final_project.data.local.entity.AppLanguage
import java.util.Locale

/**
 * Utility to apply runtime language changes (EN/TH) across the Activity and Compose.
 */
object LocaleHelper {

    fun getLocaleForLanguage(language: AppLanguage): Locale {
        return when (language) {
            AppLanguage.TH -> Locale.forLanguageTag("th")
            AppLanguage.EN -> Locale.ENGLISH
        }
    }

    fun applyLocale(context: Context, language: AppLanguage): Context {
        val locale = getLocaleForLanguage(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
