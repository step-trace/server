package com.steptrace.manhole.dto

import com.steptrace.auth.userAccount.BaseAuditEntity
import com.steptrace.common.converter.BooleanToYNConverter
import jakarta.persistence.*

@Table(name = "manhole_attachment")
@Entity
class ManholeAttachmentEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "manhole_attachment_id")
        @Id
        val id: Long? = null,

        @Column(name = "manhole_id")
        val manholeId: Long,

        @Column(name = "image_url")
        val imageUrl: String,

        @Column(name = "completed_yn")
        @Convert(converter = BooleanToYNConverter::class)
        val isCompleted: Boolean
) : BaseAuditEntity()
