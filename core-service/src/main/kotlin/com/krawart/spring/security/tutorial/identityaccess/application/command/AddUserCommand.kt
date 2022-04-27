package com.krawart.spring.security.tutorial.identityaccess.application.command

data class AddUserCommand(
    val email: String,
    val password: String,
)
