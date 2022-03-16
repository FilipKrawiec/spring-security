package com.krawart.spring.security.tutorial.identityaccess.infrastructure.persistence

import com.krawart.spring.security.tutorial.identityaccess.domain.User
import com.krawart.spring.security.tutorial.identityaccess.domain.UserRepository
import org.springframework.stereotype.Repository

@Repository
internal class UserJpaRepository(val userDao: UserDao) : UserRepository {
    override fun add(user: User): User {
        return userDao.save(user)
    }

    override fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    override fun existsByEmail(email: String): Boolean {
        return userDao.existsByEmail(email)
    }
}