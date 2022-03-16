package com.krawart.spring.security.tutorial.identityaccess.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id @Column(name = "user_id") val id: Long,
    @Column(name = "email", nullable = false) val email: String,
    @Column(name = "password", nullable = false) val password: String,
)