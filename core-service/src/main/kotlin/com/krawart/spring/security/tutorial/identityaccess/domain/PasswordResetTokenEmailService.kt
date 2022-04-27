package com.krawart.spring.security.tutorial.identityaccess.domain

interface PasswordResetTokenEmailService {
    fun sendPasswordResetEmail(passwordResetToken: PasswordResetToken)
}