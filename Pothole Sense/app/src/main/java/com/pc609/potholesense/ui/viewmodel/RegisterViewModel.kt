package com.pc609.potholesense.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.pc609.potholesense.auth.RegisterState
import com.pc609.potholesense.ui.SharedPreferencesHelper

class RegisterViewModel(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) : ViewModel() {
    private val _registerState = MutableLiveData<RegisterState>()
    val registerState: LiveData<RegisterState> get() = _registerState

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        val auth = FirebaseAuth.getInstance()

        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _registerState.value = RegisterState.Error("Please fill in all fields.")
            return
        }

        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Passwords do not match.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {
                                _registerState.value = RegisterState.Success
                                sharedPreferencesHelper.saveString("name", name)
                                sharedPreferencesHelper.saveString("email", email)
                            } else {
                                _registerState.value =
                                    RegisterState.Error("Registration failed. Please try again.")
                            }
                        }
                } else {
                    val errorMsg = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Invalid email address."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid password."
                        else -> "Registration failed. Please try again."
                    }
                    _registerState.value = RegisterState.Error(errorMsg)
                }
            }
    }
}

