package com.krawart.spring.security.tutorial.identityaccess.domain

import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "remember_me_tokens")
class RememberMeToken(
    @Id @Column(name = "id") val id: String,
    @Column(name = "username", nullable = false) val username: String,
    @Column(name = "token", nullable = false) var token: String,
    @Column(name = "last_used", nullable = false) var lastUsed: Instant,
)