package com.amrdeveloper.linkhub.ui.password.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.databinding.FragmentConfigPasswordBinding
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ConfigPasswordFragment : Fragment() {

    private var _binding: FragmentConfigPasswordBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var uiPreferences: UiPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigPasswordBinding.inflate(inflater, container, false)

        setupStaticUiInformation()
        setupListeners()

        return binding.root
    }

    private fun setupStaticUiInformation() {
        val currentPassword = uiPreferences.getPasswordText()
        binding.passwordEdit.setText(currentPassword)

        val isPasswordEnabled = uiPreferences.isPasswordEnabled()
        binding.enablePasswordSwitch.isChecked = isPasswordEnabled
    }

    private fun setupListeners() {
        binding.enablePasswordSwitch.setOnCheckedChangeListener { _, isChecked ->
            val message =
                if (isChecked) R.string.message_password_enabled else R.string.message_password_disabled
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_action -> {
                savePasswordConfiguration()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun savePasswordConfiguration() {
        val password = binding.passwordEdit.text.toString()
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), R.string.error_password_empty, Toast.LENGTH_SHORT)
                .show()
            return
        }

        uiPreferences.setPasswordText(password)

        val isPasswordEnabled = binding.enablePasswordSwitch.isChecked
        uiPreferences.setEnablePassword(isPasswordEnabled)

        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}