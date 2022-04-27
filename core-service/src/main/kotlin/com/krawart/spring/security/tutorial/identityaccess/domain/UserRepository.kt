package com.krawart.spring.security.tutorial.identityaccess.domain

interface UserRepository {
    fun save(user: User) : User

    fun findByEmail(email: String) : User?

    fun existsByEmail(email: String) : Boolean
}