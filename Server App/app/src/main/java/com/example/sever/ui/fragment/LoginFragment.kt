package com.example.sever.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.sever.databinding.FragmentLoginBinding
import com.example.sever.ui.utli.isValidId
import com.example.sever.ui.utli.safeNavigate
import com.example.sever.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var buttonStart: Button
    private lateinit var editUserId: EditText
    private val viewModel: LoginViewModel by viewModels()

    companion object {
        const val INVALID_INPUT_ID_MESSAGE = "Invalid input"
        const val ID_NOT_FOUND_MESSAGE = "Id not found"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        buttonStart = binding.buttonStart
        editUserId = binding.editUserId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonStart.setOnClickListener {
            onClickStartButton()
        }
    }

    private fun onClickStartButton() {
        val input = editUserId.text.toString()
        if (!input.isValidId()) {
            editUserId.error = INVALID_INPUT_ID_MESSAGE
            return
        }
        val userid = input.toInt()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.getUser(userid).collect {
                if (it != null) {
                    withContext(Dispatchers.Main) {
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                it.id,
                                it.name
                            )
                        findNavController().safeNavigate(action)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        editUserId.error = ID_NOT_FOUND_MESSAGE
                    }
                }
            }
        }
    }
}