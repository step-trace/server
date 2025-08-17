package com.steptrace.manhole.stub

import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeDto
import java.time.LocalDateTime

object ManholeDtoStub {
    
    val DEFAULT_MANHOLE = ManholeDto(
        id = 1L,
        latitude = 37.5665,
        longitude = 126.978,
        status = ProcessStatus.REPORTED,
        title = "Test Manhole",
        place = "Test Location",
        generatedDescription = listOf("Test Description"),
        userDescription = "Test User Description",
        processAgency = "Test Agency",
        processDescription = "Test Process Description",
        userSub = "testUser123",
        beforeImageUrls = listOf("test-before-image.jpg"),
        afterImageUrls = listOf("test-after-image.jpg"),
        createdBy = "testUser",
        createdAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0),
        updatedBy = "testUser",
        updatedAt = LocalDateTime.of(2024, 1, 1, 12, 0, 0)
    )
    
    val IN_BOUNDS_MANHOLE = DEFAULT_MANHOLE.copy(
        id = 10L,
        latitude = 37.5500,
        longitude = 126.9500,
        status = ProcessStatus.REPORTED,
        title = "In Bounds Manhole"
    )
    
    val OUT_OF_BOUNDS_MANHOLE = DEFAULT_MANHOLE.copy(
        id = 20L,
        latitude = 37.7000,
        longitude = 127.1000,
        status = ProcessStatus.PENDING,
        title = "Out of Bounds Manhole"
    )
    
    val BOUNDARY_SOUTHWEST_MANHOLE = DEFAULT_MANHOLE.copy(
        id = 30L,
        latitude = 37.5000,
        longitude = 126.9000,
        status = ProcessStatus.PENDING,
        title = "Boundary Southwest Manhole"
    )
    
    val BOUNDARY_NORTHEAST_MANHOLE = DEFAULT_MANHOLE.copy(
        id = 40L,
        latitude = 37.6000,
        longitude = 127.0000,
        status = ProcessStatus.REPORTED,
        title = "Boundary Northeast Manhole"
    )

    val MANHOLE_WITH_AFTER_IMAGES = DEFAULT_MANHOLE.copy(
        id = 100L,
        status = ProcessStatus.COMPLETED,
        title = "Test Manhole with After Images",
        createdAt = LocalDateTime.of(2024, 1, 1, 12, 0),
        beforeImageUrls = listOf("before1.jpg", "before2.jpg"),
        afterImageUrls = listOf("after1.jpg", "after2.jpg", "after3.jpg")
    )

    val MANHOLE_WITH_ONLY_BEFORE_IMAGES = DEFAULT_MANHOLE.copy(
        id = 200L,
        status = ProcessStatus.REPORTED,
        title = "Test Manhole with Only Before Images",
        createdAt = LocalDateTime.of(2024, 2, 1, 14, 30),
        beforeImageUrls = listOf("before1.jpg", "before2.jpg", "before3.jpg"),
        afterImageUrls = null
    )

    val MANHOLE_WITH_EMPTY_AFTER_IMAGES = DEFAULT_MANHOLE.copy(
        id = 300L,
        status = ProcessStatus.PENDING,
        title = "Test Manhole with Empty After Images",
        createdAt = LocalDateTime.of(2024, 3, 15, 9, 45),
        beforeImageUrls = listOf("before_main.jpg", "before_detail.jpg"),
        afterImageUrls = emptyList()
    )

    val MANHOLE_WITH_NULL_AFTER_IMAGE = DEFAULT_MANHOLE.copy(
        id = 400L,
        status = ProcessStatus.COMPLETED,
        title = "Test Manhole with Null After Image",
        createdAt = LocalDateTime.of(2024, 4, 20, 16, 15),
        beforeImageUrls = listOf("fallback_before.jpg"),
        afterImageUrls = emptyList()
    )

    val MANHOLE_WITH_SINGLE_BEFORE_IMAGE = DEFAULT_MANHOLE.copy(
        id = 500L,
        status = ProcessStatus.REPORTED,
        title = "Single Image Manhole",
        createdAt = LocalDateTime.of(2024, 5, 10, 11, 20),
        beforeImageUrls = listOf("single_image.jpg"),
        afterImageUrls = null
    )
}