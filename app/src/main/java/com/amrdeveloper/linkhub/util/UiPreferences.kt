package com.amrdeveloper.linkhub.util

import android.content.Context
import com.amrdeveloper.linkhub.data.Theme

private const val UI_PREFERENCE_NAME = "linkhub_settings"
private const val UI_THEME_KEY = "theme"
private const val UI_COUNTER_KEY = "counter"
private const val UI_AUTO_SAVE_KEY = "auto_save"
private const val PASSWORD_ENABLE_KEY = "password_enable"
private const val PASSWORD_TEXT_KEY = "password_text"

class UiPreferences(private val context: Context) {

    fun setThemeType(theme: Theme) {
        val editor = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(UI_THEME_KEY, theme.name)
        editor.apply()
    }

    fun setEnableClickCounter(enable : Boolean) {
        val editor = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(UI_COUNTER_KEY, enable)
        editor.apply()
    }

    fun setEnableAutoSave(enable : Boolean) {
        val editor = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(UI_AUTO_SAVE_KEY, enable)
        editor.apply()
    }

    fun getThemeType() : Theme {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val themeName = preferences.getString(UI_THEME_KEY, Theme.WHITE.name)
        return Theme.valueOf(themeName.toString())
    }

    fun setEnablePassword(enable : Boolean) {
        val editor = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(PASSWORD_ENABLE_KEY, enable)
        editor.apply()
    }

    fun setPasswordText(password : String) {
        val editor = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit()
        editor.putString(PASSWORD_TEXT_KEY, password)
        editor.apply()
    }

    fun isClickCounterEnabled() : Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(UI_COUNTER_KEY, true)
    }

    fun isAutoSavingEnabled() : Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(UI_AUTO_SAVE_KEY, true)
    }

    fun isPasswordEnabled() : Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(PASSWORD_ENABLE_KEY, false)
    }

    fun getPasswordText() : String {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PASSWORD_TEXT_KEY, "") ?: ""
    }
}