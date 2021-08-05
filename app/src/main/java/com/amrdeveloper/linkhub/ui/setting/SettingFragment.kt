package com.amrdeveloper.linkhub.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.amrdeveloper.linkhub.BuildConfig
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.databinding.FragmentSettingBinding
import com.amrdeveloper.linkhub.util.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var settingUtils: SettingUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        setupUiInformation()
        setupListeners()

        return binding.root
    }

    private fun setupUiInformation() {
        binding.versionTxt.text = getString(R.string.version, BuildConfig.VERSION_NAME)

        // Setup Theme Switch
        val theme = settingUtils.getThemeType()
        binding.themeSwitch.isChecked = theme == Theme.DARK
    }

    private fun setupListeners() {
        binding.sourceCodeTxt.setOnClickListener {
            openLinkIntent(requireContext(), REPOSITORY_URL)
        }

        binding.contributorsTxt.setOnClickListener {
            openLinkIntent(requireContext(), REPOSITORY_CONTRIBUTORS_URL)
        }

        binding.issuesTxt.setOnClickListener {
            openLinkIntent(requireContext(), REPOSITORY_ISSUES_URL)
        }

        binding.shareTxt.setOnClickListener {
            shareTextIntent(requireContext(), PLAY_STORE_URL)
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val themeMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(themeMode)

            val theme = if (isChecked) Theme.DARK else Theme.WHITE
            settingUtils.setThemeType(theme)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}