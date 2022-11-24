package com.krawart.spring.security.tutorial.identityaccess.application

import com.krawart.spring.security.tutorial.identityaccess.application.command.AddUserCommand
import com.krawart.spring.security.tutorial.identityaccess.application.command.UpdateUserPasswordCommand
import com.krawart.spring.security.tutorial.identityaccess.application.command.VerifyUserCommand
import com.krawart.spring.security.tutorial.identityaccess.domain.*
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.EmailAlreadyUsedException
import com.krawart.spring.security.tutorial.identityaccess.domain.exception.VerificationTokenExpiredException
import org.jboss.aerogear.security.otp.api.Base32
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
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
    private val passwordEncoder: PasswordEncoder,
    private val verificationTokenService: VerificationTokenService,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val verificationTokenEmailService: VerificationTokenEmailService,
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val userRepository: UserRepository
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
                secret = Base32.random(),
                enabled = false,
                authority = Authority.USER
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

    fun impersonateUserByResetPasswordTokenValue(token: String) {
        val passwordResetToken = passwordResetTokenRepository.findByToken(token)
            ?: throw EntityNotFoundException("Password reset token not found")

        val user = passwordResetToken.user

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(user, null, loadUserByUsername(user.email).authorities)
    }

    fun updateCurrentUserPassword(command: UpdateUserPasswordCommand) {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        user.password = passwordEncoder.encode(command.password)
        userRepository.save(user)
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
        listOf(SimpleGrantedAuthority(this.authority.name)),
    )