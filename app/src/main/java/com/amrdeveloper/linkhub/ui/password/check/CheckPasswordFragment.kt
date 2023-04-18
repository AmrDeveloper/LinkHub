package com.amrdeveloper.linkhub.ui.password.check

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.amrdeveloper.linkhub.R
import com.amrdeveloper.linkhub.databinding.FragmentCheckPasswordBinding
import com.amrdeveloper.linkhub.ui.MainActivity
import com.amrdeveloper.linkhub.util.UiPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CheckPasswordFragment : Fragment() {

    private var _binding: FragmentCheckPasswordBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var uiPreferences: UiPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentCheckPasswordBinding.inflate(inflater, container, false)

        setupListeners()

        return binding.root
    }

    private fun setupListeners() {
        binding.passwrdChecKButton.setOnClickListener {
            val userPassword = binding.passwordEdit.text.toString()
            if (userPassword.isEmpty()) {
                Toast.makeText(requireContext(), R.string.error_password_empty, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedPassword = uiPreferences.getPasswordText()
            if (userPassword != savedPassword) {
                Toast.makeText(requireContext(), R.string.error_incorrect_password, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            findNavController().navigateUp()
            (requireActivity() as MainActivity).handleLinkHubIntent()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(requireContext(), R.string.error_password_back_from_check, Toast.LENGTH_SHORT).show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}