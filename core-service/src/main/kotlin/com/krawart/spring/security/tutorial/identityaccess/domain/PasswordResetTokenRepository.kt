package com.krawart.spring.security.tutorial.identityaccess.domain

interface PasswordResetTokenRepository {

    fun save(entity: PasswordResetToken): PasswordResetToken

    fun findByToken(token: String): PasswordResetToken?

}