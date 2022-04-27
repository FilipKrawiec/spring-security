package com.krawart.spring.security.tutorial.identityaccess.application.command

data class CreateResetPasswordTokenCommand(
    val email: String,
)
