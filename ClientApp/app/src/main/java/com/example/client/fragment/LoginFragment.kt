package com.example.client.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.client.databinding.FragmentLoginBinding
import com.example.client.utli.isValidId
import com.example.client.utli.safeNavigate
import com.example.sever.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var buttonStart: Button
    private lateinit var editUserId: EditText
    private lateinit var loginFragmentCallBack: LoginFragmentCallBack

    companion object {
        const val INVALID_INPUT_ID_MESSAGE = "Invalid input"
        const val ID_NOT_FOUND_MESSAGE = "Id not found"
    }

    interface LoginFragmentCallBack {
        fun getUser(userId: Int): Flow<User?>
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check if the activity implements the callback interface
        if (context is LoginFragmentCallBack) {
            loginFragmentCallBack = context
        } else {
            throw RuntimeException("$context must implement LoginFragmentCallBack")
        }
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
            onClickStartButton(editUserId)
        }
    }

    private fun onClickStartButton(editText: EditText) {
        val input = editText.text.toString()
        if (!input.isValidId()) {
            editText.error = INVALID_INPUT_ID_MESSAGE
            return
        }
        val userid = input.toInt()
        lifecycleScope.launch(Dispatchers.IO) {
            loginFragmentCallBack.getUser(userid).collect {
                withContext(Dispatchers.Main) {
                    if (it != null) {
                        val action =
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment(
                                it.id,
                                it.name
                            )
                        findNavController().safeNavigate(action)
                    } else {
                        editUserId.error = ID_NOT_FOUND_MESSAGE
                    }
                }
            }
        }
    }
}