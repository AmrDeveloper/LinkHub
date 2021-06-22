package com.amrdeveloper.linkhub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amrdeveloper.linkhub.databinding.FragmentSettingBinding
import com.amrdeveloper.linkhub.util.*

class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.versionTxt.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        setupListeners()

        return binding.root
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}