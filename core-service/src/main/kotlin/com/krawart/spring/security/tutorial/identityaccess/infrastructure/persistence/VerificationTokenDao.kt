package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
internal interface VerificationTokenDao : JpaRepository<VerificationToken, UUID> {
    fun findByToken(token: String): VerificationToken?
}