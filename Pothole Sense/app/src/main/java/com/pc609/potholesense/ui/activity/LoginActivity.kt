package com.pc609.potholesense.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.pc609.potholesense.R
import com.pc609.potholesense.auth.LoginState
import com.pc609.potholesense.ui.SharedPreferencesHelper
import com.pc609.potholesense.ui.viewmodel.LoginViewModel
import com.pc609.potholesense.ui.viewmodel.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: TextView

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        emailEditText = findViewById(R.id.emailEt)
        passwordEditText = findViewById(R.id.passET)
        loginButton = findViewById(R.id.button)
        registerButton = findViewById(R.id.textView_register)

        sharedPreferencesHelper = SharedPreferencesHelper(this)
        auth = FirebaseAuth.getInstance()

        val viewModelFactory = ViewModelFactory(this, sharedPreferencesHelper)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.loginState.observe(this, Observer { state ->
            when (state) {
                is LoginState.Success -> {
                    sharedPreferencesHelper.saveBoolean("isLoggedIn", true)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                is LoginState.Error -> {
                    Toast.makeText(this, state.errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        })

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
