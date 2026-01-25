package com.amrdeveloper.linkhub.util

import android.content.Context
import androidx.core.content.edit
import com.amrdeveloper.linkhub.data.Theme

private const val UI_PREFERENCE_NAME = "linkhub_settings"
private const val UI_THEME_KEY = "theme"
private const val UI_FONT_FAMILY_KEY = "font_family"
private const val UI_MINIMAL_MODE_KEY = "minimal_mode"
private const val UI_COUNTER_KEY = "counter"
private const val UI_AUTO_SAVE_KEY = "auto_save"
private const val UI_DEFAULT_FOLDER_KEY = "default_folder_mode"
private const val DEFAULT_FOLDER_NAME = "default_folder_name"
private const val PASSWORD_ENABLE_KEY = "password_enable"
private const val PASSWORD_TEXT_KEY = "password_text"

class UiPreferences(private val context: Context) {

    fun setThemeType(theme: Theme) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putString(UI_THEME_KEY, theme.name)
        }
    }

    fun setEnableClickCounter(enable: Boolean) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(UI_COUNTER_KEY, enable)
        }
    }

    fun setEnableAutoSave(enable: Boolean) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(UI_AUTO_SAVE_KEY, enable)
        }
    }

    fun setEnableDefaultFolderEnabled(enable: Boolean) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(UI_DEFAULT_FOLDER_KEY, enable)
        }
    }

    fun setMinimalModeEnabled(enable: Boolean) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(UI_MINIMAL_MODE_KEY, enable)
        }
    }

    fun setDefaultFolderId(folderId: Int) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putInt(DEFAULT_FOLDER_NAME, folderId)
        }
    }

    fun deleteDefaultFolder() {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putInt(DEFAULT_FOLDER_NAME, -1)
        }
    }

    fun getThemeType(): Theme {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        val themeName = preferences.getString(UI_THEME_KEY, Theme.WHITE.name)
        return Theme.valueOf(themeName.toString())
    }

    fun setFontFamilyName(name : String) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putString(UI_FONT_FAMILY_KEY, name)
        }
    }

    fun setEnablePassword(enable: Boolean) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(PASSWORD_ENABLE_KEY, enable)
        }
    }

    fun setPasswordText(password: String) {
        context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE).edit {
            putString(PASSWORD_TEXT_KEY, password)
        }
    }

    fun isClickCounterEnabled(): Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(UI_COUNTER_KEY, true)
    }

    fun isAutoSavingEnabled(): Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(UI_AUTO_SAVE_KEY, true)
    }

    fun isDefaultFolderEnabled(): Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(UI_DEFAULT_FOLDER_KEY, false)
    }

    fun isMinimalModeEnabled(): Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(UI_MINIMAL_MODE_KEY, false)
    }

    fun getDefaultFolderId(): Int {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(DEFAULT_FOLDER_NAME, -1)
    }

    fun getFontFamilyName(): String {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString(UI_FONT_FAMILY_KEY, "Default") ?: "Default"
    }

    fun isPasswordEnabled(): Boolean {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getBoolean(PASSWORD_ENABLE_KEY, false)
    }

    fun getPasswordText(): String {
        val preferences = context.getSharedPreferences(UI_PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getString(PASSWORD_TEXT_KEY, "") ?: ""
    }
}