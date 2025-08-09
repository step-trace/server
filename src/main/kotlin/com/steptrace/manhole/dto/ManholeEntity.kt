package com.steptrace.manhole.dto

import com.steptrace.auth.userAccount.BaseAuditEntity
import com.steptrace.common.converter.JsonStringToArrayStringConverter
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Table(name = "manhole")
@Entity
class ManholeEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "manhole_id")
        @Id
        val id: Long? = null,

        @Column(name = "latitude")
        val latitude: Double,

        @Column(name = "longitude")
        val longitude: Double,

        @Column(name = "status")
        var status: String,

        @Column(name = "title")
        var title: String,

        @Column(name = "place")
        val place: String,

        @Column(name = "generated_description")
        @Convert(converter = JsonStringToArrayStringConverter::class)
        var generatedDescription: List<String>,

        @Column(name = "user_description")
        var userDescription: String? = null,

        @Column(name = "process_agency")
        var processAgency: String? = null,

        @Column(name = "process_description")
        var processDescription: String? = null,

        @Column(name = "user_sub")
        val userSub: String
): BaseAuditEntity()