package com.steptrace.steptrace.auth.userAccount

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditEntity(
        now: LocalDateTime = LocalDateTime.now()
){
    @CreatedDate
    @Column(nullable = false)
    var createdAt: LocalDateTime = now

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime = now

    @CreatedBy
    @Column(name = "created_by", nullable = false)
    var createdBy: String = ""

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    var updatedBy: String = ""
}