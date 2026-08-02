package com.example

import android.content.Context
import java.util.Locale
import android.content.res.Configuration

fun getLocalizedContext(context: Context, lang: String): Context {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    return context.createConfigurationContext(config)
}
