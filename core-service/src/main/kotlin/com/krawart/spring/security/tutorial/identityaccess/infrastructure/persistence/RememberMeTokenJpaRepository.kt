package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.RememberMeToken
import com.krawart.spring.security.tutorial.identityaccess.domain.RememberMeTokenRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*
import javax.persistence.EntityNotFoundException

@Repository
internal class RememberMeTokenJpaRepository(
    private val rememberMeTokenDao: RememberMeTokenDao
) : RememberMeTokenRepository {

    override fun save(entity: RememberMeToken): RememberMeToken {
        return rememberMeTokenDao.save(entity)
    }

    override fun updateById(id: String, token: String, lastUsed: Instant) {
        val entity = rememberMeTokenDao.findById(id)
            .orElseThrow { EntityNotFoundException("Remember me token not found") }

        entity.lastUsed = lastUsed
        entity.token = token

        rememberMeTokenDao.save(entity)
    }

    override fun findById(id: String): RememberMeToken? {
        return rememberMeTokenDao.findById(id).orElse(null)
    }

    override fun removeByUsername(username: String) {
        rememberMeTokenDao.deleteByUsername(username)
    }
}