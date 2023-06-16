package com.pc609.potholesense.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pc609.potholesense.auth.User
import com.pc609.potholesense.ui.SharedPreferencesHelper

class ProfileViewModel(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        val isLoggedIn = sharedPreferencesHelper.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            val email = sharedPreferencesHelper.getString("email") ?: ""
            val name = sharedPreferencesHelper.getString("name") ?: ""
            _user.value = User(name, email)
        } else {
          //
        }
    }

    fun editName(newName: String) {
        val user = _user.value
        if (user != null) {
            val updatedUser = user.copy(name = newName)
            _user.value = updatedUser
            sharedPreferencesHelper.saveString("name", newName)
        }
    }

    fun editEmail(newEmail: String) {
        val user = _user.value
        if (user != null) {
            val updatedUser = user.copy(email = newEmail)
            _user.value = updatedUser
            sharedPreferencesHelper.saveString("email", newEmail)
        }
    }

    fun editPassword(newPassword: String) {

    }
}
