package com.krawart.spring.security.tutorial.identityaccess.domain

interface VerificationTokenRepository {

    fun add(entity: VerificationToken): VerificationToken

    fun findByToken(token: String): VerificationToken?

}