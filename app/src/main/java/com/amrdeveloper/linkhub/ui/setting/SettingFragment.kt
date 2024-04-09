package com.amrdeveloper.linkhub.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.BuildConfig
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.data.Theme
import com.amrdeveloper.linkhub.databinding.FragmentSettingBinding
import com.amrdeveloper.linkhub.util.PLAY_STORE_URL
import com.amrdeveloper.linkhub.util.REPOSITORY_CONTRIBUTORS_URL
import com.amrdeveloper.linkhub.util.REPOSITORY_ISSUES_URL
import com.amrdeveloper.linkhub.util.REPOSITORY_SPONSORSHIP_URL
import com.amrdeveloper.linkhub.util.REPOSITORY_URL
import com.amrdeveloper.linkhub.util.UiPreferences
import com.amrdeveloper.linkhub.util.openLinkIntent
import com.amrdeveloper.linkhub.util.shareTextIntent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var uiPreferences: UiPreferences

    private var isViewPassedResumedState = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        setupStaticUiInformation()
        setupDynamicUiInformation()
        setupListeners()

        return binding.root
    }

    private fun setupStaticUiInformation() {
        binding.versionTxt.text = getString(R.string.version, BuildConfig.VERSION_NAME)
    }

    private fun setupDynamicUiInformation() {
        // Setup Theme Switch
        binding.themeSwitch.isChecked = (uiPreferences.getThemeType() == Theme.DARK)

        // Setup Show Counter switch
        binding.showCounterSwitch.isChecked = uiPreferences.isClickCounterEnabled()

        // Setup Auto saving
        binding.autoSavingSwitch.isChecked = uiPreferences.isAutoSavingEnabled()

        // Setup default folder
        binding.defaultFolderSwitch.isChecked = uiPreferences.isDefaultFolderEnabled()
    }

    override fun onPause() {
        super.onPause()
        isViewPassedResumedState = false
    }

    override fun onResume() {
        super.onResume()
        isViewPassedResumedState = true
        setupDynamicUiInformation()
    }

    private fun setupListeners() {
        binding.sourceCodeTxt.setOnClickListener {
            openLinkIntent(requireContext(), REPOSITORY_URL)
        }

        binding.sponsorUrlTxt.setOnClickListener {
            openLinkIntent(requireContext(), REPOSITORY_SPONSORSHIP_URL)
        }

        binding.importExportTxt.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_importExportFragment)
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

        binding.passwordTxt.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_configPasswordFragment)
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isViewPassedResumedState) {
                val themeMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(themeMode)

                val theme = if (isChecked) Theme.DARK else Theme.WHITE
                uiPreferences.setThemeType(theme)
            }
        }

        binding.showCounterSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isViewPassedResumedState) {
                uiPreferences.setEnableClickCounter(isChecked)
            }
        }

        binding.autoSavingSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isViewPassedResumedState) {
                uiPreferences.setEnableAutoSave(isChecked)
            }
        }
        binding.defaultFolderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isViewPassedResumedState) {
                uiPreferences.setEnableDefaultFolderEnabled(isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}