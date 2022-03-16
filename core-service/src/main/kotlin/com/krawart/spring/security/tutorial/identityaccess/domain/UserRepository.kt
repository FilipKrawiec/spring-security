package com.krawart.spring.security.tutorial.identityaccess.domain

interface UserRepository {
    fun add(user: User) : User

    fun findByEmail(email: String) : User?

    fun existsByEmail(email: String) : Boolean
}