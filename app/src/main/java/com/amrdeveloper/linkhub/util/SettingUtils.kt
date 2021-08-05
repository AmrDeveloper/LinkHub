package com.amrdeveloper.linkhub.util

import android.content.Context
import com.amrdeveloper.linkhub.data.Theme

private const val SETTINGS_PREFERENCE_NAME = "linkhub_settings"
private const val SETTINGS_THEME_KEY = "theme"

class SettingUtils(private val context: Context) {

    fun setThemeType(theme: Theme) {
        val editor = context.getSharedPreferences(SETTINGS_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(SETTINGS_THEME_KEY, theme.name)
        editor.apply()
    }

    fun getThemeType() : Theme {
        val preferences = context.getSharedPreferences(SETTINGS_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val themeName = preferences.getString(SETTINGS_THEME_KEY, Theme.WHITE.name)
        return Theme.valueOf(themeName.toString())
    }
}