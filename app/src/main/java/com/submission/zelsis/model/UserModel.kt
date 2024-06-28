package com.submission.zelsis.model

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)