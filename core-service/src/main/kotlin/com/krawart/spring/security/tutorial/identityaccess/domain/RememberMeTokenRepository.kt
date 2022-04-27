package com.krawart.spring.security.tutorial.identityaccess.domain

import java.time.Instant

interface RememberMeTokenRepository {

    fun save(entity: RememberMeToken): RememberMeToken

    fun updateById(id: String, token: String, lastUsed: Instant)

    fun findById(id: String): RememberMeToken?

    fun removeByUsername(username: String)

}