package com.krawart.spring.security.tutorial.identityaccess.application

import com.krawart.spring.security.tutorial.identityaccess.domain.PasswordResetToken
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class PasswordResetTokenEmailService(
    private val request: HttpServletRequest,
) {
    /**
     * Token is printed to console instead. Email impl is out of the scope
     */
    fun sendPasswordResetEmail(passwordResetToken: PasswordResetToken) {
        val uri = "http://${request.serverName}:${request.serverPort}${request.contextPath}/users/reset-password"
        print("Go to $uri?token=${passwordResetToken.token}")
    }
}