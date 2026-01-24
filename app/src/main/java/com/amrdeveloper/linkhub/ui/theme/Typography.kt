package com.amrdeveloper.linkhub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

val supportedFontFamilies = mutableMapOf<String, FontFamily>(
    "Default" to FontFamily.Default,
    FontFamily.Serif.name to FontFamily.Serif,
    FontFamily.SansSerif.name to FontFamily.SansSerif,
    FontFamily.Monospace.name to FontFamily.Monospace,
    FontFamily.Cursive.name to FontFamily.Cursive
)

fun createTypographyWithFontFamily(fontFamily: FontFamily): Typography {
    return Typography().run {
        Typography(
            displayLarge = displayLarge.copy(fontFamily = fontFamily),
            displayMedium = displayMedium.copy(fontFamily = fontFamily),
            displaySmall = displaySmall.copy(fontFamily = fontFamily),

            headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily),

            titleLarge = titleLarge.copy(fontFamily = fontFamily),
            titleMedium = titleMedium.copy(fontFamily = fontFamily),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),

            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily),

            labelLarge = labelLarge.copy(fontFamily = fontFamily),
            labelMedium = labelMedium.copy(fontFamily = fontFamily),
            labelSmall = labelSmall.copy(fontFamily = fontFamily),
        )
    }
}

fun getLinkhubTypography(fontFamily: FontFamily = FontFamily.Default): Typography {
    return createTypographyWithFontFamily(fontFamily)
}
