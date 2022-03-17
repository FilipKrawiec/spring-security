package com.krawart.spring.security.tutorial.identityaccess.application

import com.krawart.spring.security.tutorial.identityaccess.application.command.AddUserCommand
import com.krawart.spring.security.tutorial.identityaccess.application.command.VerifyUserCommand
import com.krawart.spring.security.tutorial.identityaccess.domain.User
import com.krawart.spring.security.tutorial.identityaccess.domain.UserRepository
import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationTokenRepository
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.EmailAlreadyUsedException
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.VerificationTokenExpiredException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
@Transactional
class UserService(
    val passwordEncoder: PasswordEncoder,
    val verificationTokenService: VerificationTokenService,
    val verificationTokenRepository: VerificationTokenRepository,
    val verificationTokenEmailService: VerificationTokenEmailService,
    val userRepository: UserRepository
) : UserDetailsService {

    @Transactional(readOnly = true)
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository
            .findByEmail(username)
            ?.userDetails()
            ?: throw UsernameNotFoundException("Username with provided email not found")
    }

    @Throws(EmailAlreadyUsedException::class)
    fun addUser(command: AddUserCommand): User {
        if (userRepository.existsByEmail(command.email)) {
            throw EmailAlreadyUsedException("Username with provided email is already in use")
        }

        val user = userRepository.save(
            User(
                id = UUID.randomUUID(),
                email = command.email,
                password = passwordEncoder.encode(command.password),
                enabled = false,
            )
        )

        verificationTokenEmailService.sendVerificationEmail(
            verificationTokenService.generateVerificationTokenForRegisteredUser(user)
        )

        return user
    }

    @Throws(EntityNotFoundException::class, VerificationTokenExpiredException::class)
    fun verifyUser(command: VerifyUserCommand): User {
        val verificationToken = verificationTokenRepository.findByToken(command.token)
            ?: throw EntityNotFoundException("Verification token not found")

        if (verificationToken.expirationDate < Instant.now()) throw VerificationTokenExpiredException()

        val user = verificationToken.user
        user.enabled = true

        return userRepository.save(user)
    }
}

fun User.userDetails(): UserDetails =
    org.springframework.security.core.userdetails.User(
        this.email,
        this.password,
        this.enabled,
        true,
        true,
        true,
        emptyList(),
    )