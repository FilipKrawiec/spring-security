package com.krawart.spring.security.tutorial.identityaccess.presentation.mail

import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationToken
import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationTokenEmailService
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

@Service
class DummyVerificationTokenEmailService(
    private val request: HttpServletRequest,
) : VerificationTokenEmailService {
    /**
     * Token is printed to console instead. Email impl is out of the scope
     */
    override fun sendVerificationEmail(verificationToken: VerificationToken) {
        val uri = "http://${request.serverName}:${request.serverPort}${request.contextPath}/registration-confirmation"
        print("Go to $uri?token=${verificationToken.token}")
    }
}