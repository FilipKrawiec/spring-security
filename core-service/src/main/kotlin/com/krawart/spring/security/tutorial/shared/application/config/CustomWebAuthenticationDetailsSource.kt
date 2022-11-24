package com.krawart.spring.security.tutorial.shared.application.config

import org.springframework.security.authentication.AuthenticationDetailsSource
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class CustomWebAuthenticationDetailsSource : AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    override fun buildDetails(context: HttpServletRequest): WebAuthenticationDetails {
        return CustomWebAuthenticationDetails(context)
    }
}

data class CustomWebAuthenticationDetails(
    val context: HttpServletRequest,
    val verificationCode: String = context.getParameter("code"),
) : WebAuthenticationDetails(context)