package com.krawart.spring.security.tutorial.identityaccess.presentation.mail

import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetToken
import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetTokenEmailService
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class DummyPasswordResetTokenEmailService(
    private val request: HttpServletRequest,
) : PasswordResetTokenEmailService {
    /**
     * Token is printed to console instead. Email impl is out of the scope
     */
    override fun sendPasswordResetEmail(passwordResetToken: PasswordResetToken) {
        val uri = "http://${request.serverName}:${request.serverPort}${request.contextPath}/users/reset-password"
        print("Go to $uri?token=${passwordResetToken.token}")
    }
}