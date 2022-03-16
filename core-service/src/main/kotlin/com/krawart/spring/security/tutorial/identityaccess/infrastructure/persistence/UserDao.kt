package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component

@Component
internal interface UserDao : JpaRepository<User, Long>