package com.krawart.spring.security.tutorial.identityaccess.infrastructure.springsecurity

import com.krawart.spring.security.tutorial.identityaccess.domain.RememberMeToken
import com.krawart.spring.security.tutorial.identityaccess.domain.RememberMeTokenRepository
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import javax.persistence.EntityNotFoundException

@Component
@Transactional
internal class CustomPersistentTokenRepository(
    private val rememberMeTokenRepository: RememberMeTokenRepository
) : PersistentTokenRepository {
    override fun createNewToken(token: PersistentRememberMeToken) {
        rememberMeTokenRepository.save(
            RememberMeToken(
                id = token.series,
                username = token.username,
                lastUsed = Instant.now(),
                token = token.tokenValue
            )
        )
    }

    override fun updateToken(series: String, tokenValue: String, lastUsed: Date) {
        rememberMeTokenRepository.updateById(
            id = series,
            token = tokenValue,
            lastUsed = lastUsed.toInstant()
        )
    }

    override fun getTokenForSeries(seriesId: String): PersistentRememberMeToken {
        val token = rememberMeTokenRepository.findById(seriesId)
            ?: throw EntityNotFoundException("Remember me token not found")

        return PersistentRememberMeToken(
            token.username,
            token.id,
            token.token,
            Date.from(token.lastUsed)

        )
    }

    override fun removeUserTokens(username: String) {
        rememberMeTokenRepository.removeByUsername(username)
    }

}