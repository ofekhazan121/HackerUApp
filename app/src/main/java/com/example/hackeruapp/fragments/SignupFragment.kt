package com.example.hackeruapp.fragments

import android.content.Context
import androidx.activity.result.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hackeruapp.LoginActivity
import com.example.hackeruapp.R
import com.example.hackeruapp.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.signup_fragment.*

class SignupFragment() : Fragment(R.layout.signup_fragment) {
    lateinit var googleGetContent: ActivityResultLauncher<Intent>
    val firebaseAuth = FirebaseAuth.getInstance()
    private val loginViewModel: LoginViewModel by activityViewModels()
    override fun onResume() {
        super.onResume()

        val activity = requireActivity()
        val signupButton = activity.findViewById<Button>(R.id.signup_button)
        val signInLink = activity.findViewById<TextView>(R.id.to_signin)

        val signupUsername = activity.findViewById<EditText>(R.id.signup_email)
        signupUsername.setText(loginViewModel.email)
        signupUsername.addTextChangedListener {
            loginViewModel.email = it.toString()
        }

        val signupPassword = activity.findViewById<EditText>(R.id.signup_password)
        signupPassword.setText(loginViewModel.password)
        signupPassword.addTextChangedListener {
            loginViewModel.password = it.toString()
        }


        signupButton.setOnClickListener {
            onSignUpClick()
        }

        signInLink.setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.login_container, SignInFragment()).commit()
        }
    }

    override fun onStart() {
        super.onStart()

        setOnGoogleSignInClickListener()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleGetContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onGoogleIntentResult(result)
            }
    }

    private fun setOnGoogleSignInClickListener() {
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()
        val googleIntent =
            GoogleSignIn.getClient(requireActivity(), googleSignInOptions).signInIntent
        google_signin_button.setOnClickListener {

            googleGetContent.launch(googleIntent)
        }
    }

    private fun onGoogleIntentResult(content: ActivityResult) {
        val task: Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(content.data)
                .addOnSuccessListener {
                    displayToast("Hey " + it.displayName.toString())
                    loginOrSignupToFirebase(it)
                }
                .addOnFailureListener {
                    displayToast("if you cant use GoogleSignIn please sign in the regular way")
                }
    }


    private fun loginOrSignupToFirebase(googleSignInAccount: GoogleSignInAccount) {
        firebaseAuth.fetchSignInMethodsForEmail(googleSignInAccount.email!!)
            .addOnSuccessListener {
                if (it.signInMethods.isNullOrEmpty()) {
                    //Register user
                    registerToNotesAppWithFirebase(googleSignInAccount)
                } else {
                    //login
                    requireActivity().getPreferences(Context.MODE_PRIVATE).edit()
                        .putLong("Last_Login", System.currentTimeMillis()).apply()
                    getIntoApp()
                }
            }
            .addOnFailureListener {
                displayToast("failed on firebaseAuth.fetchSignInMethodsForEmail")
            }
    }

    private fun registerToNotesAppWithFirebase(googleSignInAccount: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        firebaseAuth.signInWithCredential(authCredential)
            .addOnSuccessListener {
                getIntoApp()
            }
            .addOnFailureListener { "Please Try Again, Exeption: ${it.message}" }
    }

    private fun getIntoApp() {
        (requireActivity() as LoginActivity).goInApp()
    }


    fun displayToast(text: String) {
        Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG).show()
    }

    fun onSignUpClick() {
        firebaseAuth.createUserWithEmailAndPassword(
            loginViewModel.email!!,
            loginViewModel.password!!
        )
            .addOnSuccessListener {
                gotToSignIn()
            }
            .addOnFailureListener {
                displayToast("Try Again Later")
            }

    }

    fun gotToSignIn() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.login_container, SignInFragment()).commit()
    }
}