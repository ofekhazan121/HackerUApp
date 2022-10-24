package com.example.hackeruapp.fragments

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hackeruapp.LoginActivity
import com.example.hackeruapp.R
import com.example.hackeruapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

class SignInFragment() : Fragment(R.layout.signin_fragment) {

    lateinit var googleGetContent: ActivityResultLauncher<Intent>
    val firebaseAuth = FirebaseAuth.getInstance()
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        val signInButton = activity.findViewById<Button>(R.id.signin_button)
        val signupLink = activity.findViewById<TextView>(R.id.to_signup)

        val signInUsername = activity.findViewById<EditText>(R.id.signin_email)
        signInUsername.setText(loginViewModel.email)
        signInUsername.addTextChangedListener {
            loginViewModel.email = it.toString()
        }

        val signInPassword = activity.findViewById<EditText>(R.id.signin_password)
        signInPassword.setText(loginViewModel.password)
        signInPassword.addTextChangedListener {
            loginViewModel.password = it.toString()
        }

        signInButton.setOnClickListener {
            onLoginClick()
        }

        signupLink.setOnClickListener {
            activity.supportFragmentManager.beginTransaction().replace(R.id.login_container,SignupFragment()).commit()
        }
    }


    fun onLoginClick() {
        firebaseAuth.signInWithEmailAndPassword(loginViewModel.email!!,loginViewModel.password!!)
            .addOnSuccessListener {
                requireActivity().getPreferences(Context.MODE_PRIVATE).edit()
                    .putLong("Last_Login", System.currentTimeMillis()).apply()
                (requireActivity() as LoginActivity).goInApp()
            }
            .addOnFailureListener {
                displayToast("Wrong Email/Password")
            }
    }

    fun displayToast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
    }

}