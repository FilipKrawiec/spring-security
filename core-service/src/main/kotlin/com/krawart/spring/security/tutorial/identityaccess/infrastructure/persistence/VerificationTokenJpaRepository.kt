package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationToken
import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationTokenRepository
import org.springframework.stereotype.Repository

@Repository
internal class VerificationTokenJpaRepository(
    private val verificationTokenDao: VerificationTokenDao
) : VerificationTokenRepository {

    override fun add(entity: VerificationToken): VerificationToken {
        return verificationTokenDao.save(entity)
    }

    override fun findByToken(token: String): VerificationToken? {
        return verificationTokenDao.findByToken(token)
    }
}