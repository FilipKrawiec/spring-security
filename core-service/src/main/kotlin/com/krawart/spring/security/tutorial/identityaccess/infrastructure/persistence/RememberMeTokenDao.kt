package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.RememberMeToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
internal interface RememberMeTokenDao : JpaRepository<RememberMeToken, String> {

    fun deleteByUsername(username: String)
}