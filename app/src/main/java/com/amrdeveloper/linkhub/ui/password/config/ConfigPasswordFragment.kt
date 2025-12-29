package com.amrdeveloper.linkhub.ui.password.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigPasswordBinding.inflate(inflater, container, false)

        tempActions()
        setupStaticUiInformation()
        setupListeners()

        return binding.root
    }

    // TODO: Will be refactor later and moved to Jetpack compose part
    private fun tempActions() {
        binding.composeView.setContent {
            Row(
                modifier = Modifier.padding(5.dp),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedIconButton(
                    border = BorderStroke(1.dp, colorResource(R.color.light_blue_600)),
                    onClick = { savePasswordConfiguration() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = "Save",
                        tint = colorResource(R.color.light_blue_600),
                    )
                }
            }
        }
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