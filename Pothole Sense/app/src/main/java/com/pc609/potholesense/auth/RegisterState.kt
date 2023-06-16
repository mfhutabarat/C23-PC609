package com.pc609.potholesense.auth

sealed class RegisterState {
    object Success : RegisterState()
    data class Error(val errorMsg: String) : RegisterState()
}
