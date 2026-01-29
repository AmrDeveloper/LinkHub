package com.amrdeveloper.linkhub.ui.link

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
import com.amrdeveloper.linkhub.data.Link
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.ui.theme.LinkhubAppTheme
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LinkFragment : Fragment() {

    @Inject
    lateinit var uiPreferences: UiPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val safeArguments by navArgs<LinkFragmentArgs>()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            val sharedLink = arguments?.getString("shared_link")
            val sharedLinkSubject = arguments?.getString("shared_link_subject") ?: ""
            val isSharedLink = sharedLink != null
            val currentLink = if (isSharedLink) Link(title = sharedLinkSubject, subtitle = "", url = sharedLink)
            else safeArguments.link

            setContent {
                LinkhubAppTheme(
                    isSystemInDarkTheme = uiPreferences.getThemeType() == Theme.DARK,
                    fontFamilyName = uiPreferences.getFontFamilyName()
                ) {
                    LinkScreen(
                        currentLink = currentLink,
                        isSharedLink = isSharedLink,
                        viewModel = viewModel(),
                        uiPreferences = uiPreferences,
                        navController = findNavController()
                    )
                }
            }
        }
    }
}