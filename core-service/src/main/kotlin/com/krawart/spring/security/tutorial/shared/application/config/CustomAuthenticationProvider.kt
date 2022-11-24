package com.krawart.spring.security.tutorial.shared.application.config

import com.krawart.spring.security.tutorial.identityaccess.domain.User
import com.krawart.spring.security.tutorial.identityaccess.domain.UserRepository
import org.jboss.aerogear.security.otp.Totp
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationProvider(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()
        val user = userRepository.findByEmail(username) ?: throw BadCredentialsException("User with username not found")

        verifyCredentials(user, password)
        verify2FACode(authentication, user)

        return UsernamePasswordAuthenticationToken(user, password, listOf(SimpleGrantedAuthority(user.authority.name)))
    }

    private fun verifyCredentials(user: User, password: String) {
        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("Invalid username or password")
        }
    }

    private fun verify2FACode(
        authentication: Authentication,
        user: User
    ) {
        val verificationCode = (authentication.details as CustomWebAuthenticationDetails).verificationCode
        val totp = Totp(user.secret)
        if (!totp.verify(verificationCode)) {
            throw BadCredentialsException("Invalid verification code")
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
