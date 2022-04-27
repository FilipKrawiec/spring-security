package com.krawart.spring.security.tutorial.identityaccess.domain

interface VerificationTokenEmailService {
    fun sendVerificationEmail(verificationToken: VerificationToken)
}