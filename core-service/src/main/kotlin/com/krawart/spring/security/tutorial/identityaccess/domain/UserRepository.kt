package com.krawart.spring.security.tutorial.identityaccess.domain

interface UserRepository {
    fun add(user: User) : User
}