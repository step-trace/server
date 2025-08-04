package com.steptrace.manhole.repository

import com.steptrace.manhole.dto.ManholeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ManholeJpaRepository: JpaRepository<ManholeEntity, Long> {
    fun findAllByUserSub(sub: String) : List<ManholeEntity>
}