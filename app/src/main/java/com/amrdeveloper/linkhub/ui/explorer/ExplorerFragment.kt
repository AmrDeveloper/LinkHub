package com.amrdeveloper.linkhub.ui.explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.ui.theme.LinkhubAppTheme
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ExplorerFragment : Fragment() {

    @Inject
    lateinit var uiPreferences: UiPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            val safeArguments by navArgs<ExplorerFragmentArgs>()
            setContent {
                LinkhubAppTheme(
                    isSystemInDarkTheme = uiPreferences.getThemeType() == Theme.DARK,
                    fontFamilyName = uiPreferences.getFontFamilyName()
                ) {
                    ExplorerScreen(
                        currentFolder = safeArguments.folder,
                        viewModel = viewModel(),
                        uiPreferences = uiPreferences,
                        navController = findNavController(),
                    )
                }
            }
        }
    }
}