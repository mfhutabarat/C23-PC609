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
import com.pc609.potholesense.R
import com.pc609.potholesense.auth.RegisterState
import com.pc609.potholesense.ui.SharedPreferencesHelper
import com.pc609.potholesense.ui.viewmodel.RegisterViewModel
import com.pc609.potholesense.ui.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: TextView
    private lateinit var viewModel: RegisterViewModel

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        nameEditText = findViewById(R.id.nameEt)
        emailEditText = findViewById(R.id.emailEt)
        passwordEditText = findViewById(R.id.passET)
        confirmPasswordEditText = findViewById(R.id.confirm_passET)
        loginButton = findViewById(R.id.textView_login)
        registerButton = findViewById(R.id.button)

        sharedPreferencesHelper = SharedPreferencesHelper(this)

        val viewModelFactory = ViewModelFactory(this, sharedPreferencesHelper)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

        viewModel.registerState.observe(this, Observer { state ->
            when (state) {
                is RegisterState.Success -> {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                is RegisterState.Error -> {
                    Toast.makeText(this, state.errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        })

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()
            viewModel.register(name, email, password, confirmPassword)
        }
        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}