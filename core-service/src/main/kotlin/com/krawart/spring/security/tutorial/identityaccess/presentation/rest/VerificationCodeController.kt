package com.krawart.spring.security.tutorial.identityaccess.presentation.rest

import com.krawart.spring.security.tutorial.identityaccess.domain.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.net.URLEncoder
import javax.validation.constraints.NotNull

@Controller
class VerificationCodeController(
    private val userRepository: UserRepository,
    @Value("\${spring.application.name}") val appName: String,
    @Value("\${security.2-factor.qr-prefix}") val qrPrefix: String,
) {
    @RequestMapping(value = ["/code"], method = [RequestMethod.GET])
    @ResponseBody
    fun getQRUrl(@RequestParam("username") @NotNull username: String): Map<String, String>? {
        val result: MutableMap<String, String> = HashMap()

        val user = userRepository.findByEmail(username)
        if (user == null) {
            result["url"] = ""
        } else {
            result["url"] = generateQRUrl(user.secret, user.email)
        }
        return result
    }

    private fun generateQRUrl(secret: String, username: String): String {
        return qrPrefix + URLEncoder.encode(
            "otpauth://totp/$appName:$username?secret=$secret&issuer=$appName",
            "UTF-8"
        )
    }
}