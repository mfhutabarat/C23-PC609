package com.pc609.potholesense.auth

sealed class LoginState {
    object Success : LoginState()
    data class Error(val errorMsg: String) : LoginState()
}
