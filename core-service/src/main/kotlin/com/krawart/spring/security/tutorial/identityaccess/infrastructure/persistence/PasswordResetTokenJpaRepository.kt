package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetToken
import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetTokenRepository
import org.springframework.stereotype.Repository

@Repository
internal class PasswordResetTokenJpaRepository(
    private val passwordResetTokenDao: PasswordResetTokenDao
) : PasswordResetTokenRepository {

    override fun save(entity: PasswordResetToken): PasswordResetToken {
        return passwordResetTokenDao.save(entity)
    }

    override fun findByToken(token: String): PasswordResetToken? {
        return passwordResetTokenDao.findByToken(token)
    }
}