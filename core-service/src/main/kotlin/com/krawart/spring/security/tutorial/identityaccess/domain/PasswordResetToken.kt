package com.krawart.spring.security.tutorial.identityaccess.domain

import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "password_reset_tokens")
class PasswordResetToken(
    @Id @Column(name = "password_reset_token_id") val id: UUID,
    @Column(name = "token", nullable = false) val token: String,
    @OneToOne @JoinColumn(name = "user_id", nullable = false) val user: User,
    @Column(name = "expiration_date", nullable = false) val expirationDate: Instant,
)