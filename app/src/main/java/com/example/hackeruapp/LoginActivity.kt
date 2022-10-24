package com.example.hackeruapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display.Mode
import android.widget.Button
import com.example.hackeruapp.fragments.SignInFragment
import com.example.hackeruapp.fragments.SignupFragment

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onStart() {
        super.onStart()
        sharedPreferences = getSharedPreferences("LoginActivity", MODE_PRIVATE)
        displaySignIn()
        //checkPref()
    }

    fun displaySignIn() {
        supportFragmentManager.beginTransaction().replace(R.id.login_container,SignInFragment()).commit()
    }

    fun displaySignup() {
        supportFragmentManager.beginTransaction().replace(R.id.login_container, SignupFragment()).commit()
    }

    fun checkPref() {
        var lastLogin = sharedPreferences.getLong("Last_Login", -1)

        if ( lastLogin - System.currentTimeMillis() < 60000 && lastLogin != -1L) {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    fun goInApp() {
        startActivity(Intent(this,MainActivity::class.java))
    }
}