package com.amrdeveloper.linkhub.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.ui.theme.LinkhubAppTheme
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    @Inject
    lateinit var uiPreferences: UiPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                LinkhubAppTheme(
                    isSystemInDarkTheme = uiPreferences.getThemeType() == Theme.DARK,
                    fontFamilyName = uiPreferences.getFontFamilyName()
                ) {
                    SettingsScreen(
                        uiPreferences = uiPreferences,
                        navController = findNavController()
                    )
                }
            }
        }
    }
}