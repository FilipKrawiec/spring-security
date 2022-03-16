package com.krawart.spring.security.tutorial.identityaccess.presentation.rest.requestbody

data class RegisterUserRequestBody(
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
)
