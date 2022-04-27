package com.krawart.spring.security.tutorial.identityaccess.application

import com.krawart.spring.security.tutorial.identityaccess.domain.User
import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationToken
import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@Transactional
class VerificationTokenService(
    private val verificationTokenRepository: VerificationTokenRepository,
    @Value("\${tutorial.verification-token.expiration-offset-in-seconds}") private val expirationOffset: Long,
    ) {

    fun generateVerificationTokenForRegisteredUser(user: User): VerificationToken {
        return verificationTokenRepository.save(
            VerificationToken(
                id = UUID.randomUUID(),
                token = generateVerificationTokenValue(),
                user = user,
                expirationDate = calculateExpirationDate(),
            )
        )
    }

    private fun generateVerificationTokenValue() = UUID.randomUUID().toString()

    private fun calculateExpirationDate() = Instant.now().plusSeconds(expirationOffset)
}