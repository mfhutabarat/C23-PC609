package com.pc609.potholesense.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pc609.potholesense.auth.LoginState
import com.pc609.potholesense.ui.SharedPreferencesHelper

class LoginViewModel(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState> get() = _loginState

    init {
        checkUserLoggedIn()
    }

    private fun checkUserLoggedIn() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            _loginState.value = LoginState.Success
        }
    }

    fun login(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Please enter email and password.")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.isEmailVerified) {
                        _loginState.value = LoginState.Success
                        sharedPreferencesHelper.saveBoolean("isLoggedIn", true)
                        sharedPreferencesHelper.saveString("email", email)
                    } else {
                        auth.signOut()
                        _loginState.value = LoginState.Error("Please verify your email first.")
                    }
                } else {
                    val errorMsg = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Invalid email address."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid password."
                        else -> "Login failed."
                    }
                    _loginState.value = LoginState.Error(errorMsg)
                }
            }
    }
}
