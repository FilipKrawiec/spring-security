package com.krawart.spring.security.tutorial.identityaccess.application

import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationToken
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class VerificationTokenEmailService(
    private val request: HttpServletRequest,
) {
    /**
     * Token is printed to console instead. Email impl is out of the scope
     */
    fun sendVerificationEmail(verificationToken: VerificationToken) {
        val uri = "http://${request.serverName}:${request.serverPort}${request.contextPath}/registration-confirmation"
        print("Go to $uri?token=${verificationToken.token}")
    }
}