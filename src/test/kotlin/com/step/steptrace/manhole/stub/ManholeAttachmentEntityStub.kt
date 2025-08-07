package com.step.steptrace.manhole.stub

import com.steptrace.manhole.dto.ManholeAttachmentEntity

object ManholeAttachmentEntityStub {

    val BEFORE_IMAGE_ATTACHMENT = ManholeAttachmentEntity(
        id = 1L,
        manholeId = 100L,
        imageUrl = "before1.jpg",
        isCompleted = false
    )

    val MULTIPLE_BEFORE_ATTACHMENTS = listOf(
        ManholeAttachmentEntity(
            id = 10L,
            manholeId = 200L,
            imageUrl = "before1.jpg",
            isCompleted = false
        ),
        ManholeAttachmentEntity(
            id = 11L,
            manholeId = 200L,
            imageUrl = "before2.jpg",
            isCompleted = false
        ),
        ManholeAttachmentEntity(
            id = 12L,
            manholeId = 200L,
            imageUrl = "before3.jpg",
            isCompleted = false
        )
    )

    val MIXED_ATTACHMENTS = listOf(
        ManholeAttachmentEntity(
            id = 20L,
            manholeId = 300L,
            imageUrl = "before1.jpg",
            isCompleted = false
        ),
        ManholeAttachmentEntity(
            id = 21L,
            manholeId = 300L,
            imageUrl = "before2.jpg",
            isCompleted = false
        ),
        ManholeAttachmentEntity(
            id = 22L,
            manholeId = 300L,
            imageUrl = "after1.jpg",
            isCompleted = true
        ),
        ManholeAttachmentEntity(
            id = 23L,
            manholeId = 300L,
            imageUrl = "after2.jpg",
            isCompleted = true
        )
    )

    val ONLY_AFTER_ATTACHMENTS = listOf(
        ManholeAttachmentEntity(
            id = 30L,
            manholeId = 400L,
            imageUrl = "after1.jpg",
            isCompleted = true
        ),
        ManholeAttachmentEntity(
            id = 31L,
            manholeId = 400L,
            imageUrl = "after2.jpg",
            isCompleted = true
        )
    )
}