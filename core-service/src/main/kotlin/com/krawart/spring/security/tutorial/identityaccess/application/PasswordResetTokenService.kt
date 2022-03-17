package com.krawart.spring.security.tutorial.identityaccess.application

import com.krawart.spring.security.tutorial.identityaccess.application.command.CreateResetPasswordTokenCommand
import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetToken
import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetTokenEmailService
import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetTokenRepository
import com.krawart.spring.security.tutorial.identityaccess.domain.UserRepository
import com.krawart.spring.security.tutorial.identityaccess.presentation.mail.DummyPasswordResetTokenEmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class PasswordResetTokenService(
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val userRepository: UserRepository,
    private val passwordResetTokenEmailService: PasswordResetTokenEmailService,
    @Value("\${tutorial.password-reset-token.expiration-offset-in-seconds}") private val expirationOffset: Long,
) {

    @Throws(EntityNotFoundException::class)
    fun create(command: CreateResetPasswordTokenCommand) {
        val user = userRepository.findByEmail(command.email)
            ?: throw EntityNotFoundException("User with email ${command.email} not found")

        val passwordResetToken = passwordResetTokenRepository.save(
            PasswordResetToken(
                id = UUID.randomUUID(),
                generatePasswordResetTokenValue(),
                user,
                calculateExpirationDate()
            )
        )

        passwordResetTokenEmailService.sendPasswordResetEmail(passwordResetToken)
    }

    private fun generatePasswordResetTokenValue() = UUID.randomUUID().toString()

    private fun calculateExpirationDate() = Instant.now().plusSeconds(expirationOffset)
}