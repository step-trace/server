package com.steptrace.manhole.repository

import com.steptrace.manhole.dto.ManholeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ManholeJpaRepository: JpaRepository<ManholeEntity, Long> {
    fun findAllByUserSub(sub: String) : List<ManholeEntity>
    fun findByCreatedAtBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<ManholeEntity>
}