package com.example.hackeruapp.fragments

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.hackeruapp.MainActivity
import com.example.hackeruapp.R
import com.example.hackeruapp.managers.LoginManager
import com.example.hackeruapp.viewmodel.LoginViewModel
import org.w3c.dom.Text

class SignInFragment() : Fragment(R.layout.signin_fragment) {

    override fun onResume() {
        super.onResume()
        val activity = requireActivity()
        val signInButton = activity.findViewById<Button>(R.id.signin_button)
        val signupLink = activity.findViewById<TextView>(R.id.to_signup)

        val signInUsername = activity.findViewById<EditText>(R.id.signin_username)
        signInUsername.setText(LoginViewModel.username)
        signInUsername.addTextChangedListener {
            LoginViewModel.username = it.toString()
        }

        val signInPassword = activity.findViewById<EditText>(R.id.signin_password)
        signInPassword.setText(LoginViewModel.password)
        signInPassword.addTextChangedListener {
            LoginViewModel.password = it.toString()
        }

        signInButton.setOnClickListener {
            onButtonClick()
        }

        signupLink.setOnClickListener {
            activity.supportFragmentManager.beginTransaction().replace(R.id.login_container,SignupFragment()).commit()
        }
    }


    fun onButtonClick(){
        val activity = requireActivity()

        if (LoginManager.login(LoginViewModel.username!! ,LoginViewModel.password!!)) {
            activity.getPreferences(Context.MODE_PRIVATE).edit().putLong("Last_Login",System.currentTimeMillis()).apply()
            startActivity(Intent(activity,MainActivity::class.java))
        }else{
            Toast.makeText(activity,"Wrong Username or Password",Toast.LENGTH_LONG).show()
        }
    }

}