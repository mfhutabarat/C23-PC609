package com.pc609.potholesense.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pc609.potholesense.ui.SharedPreferencesHelper

class ViewModelFactory(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(context, sharedPreferencesHelper) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(context, sharedPreferencesHelper) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(context, sharedPreferencesHelper) as T
        } else if (modelClass.isAssignableFrom(PredictViewModel::class.java)) {
            return PredictViewModel(context, sharedPreferencesHelper) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
