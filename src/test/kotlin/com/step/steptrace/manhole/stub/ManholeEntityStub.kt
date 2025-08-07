package com.step.steptrace.manhole.stub

import com.steptrace.manhole.code.ProcessStatus
import com.steptrace.manhole.dto.ManholeEntity

object ManholeEntityStub {

    val SAVED_MANHOLE_ENTITY = ManholeEntity(
            id = 100L,
            latitude = 37.5665,
            longitude = 126.978,
            status = ProcessStatus.IN_PROGRESS.value,
            title = "Test Manhole",
            place = "Test Location",
            generatedDescription = listOf("Test Description"),
            userDescription = "Test User Description",
            processAgency = "Test Agency",
            processDescription = "Test Process Description",
            userSub = "testUser123"
    )

    val MANHOLE_ENTITY_WITH_NULL_ID = ManholeEntity(
            id = null,
            latitude = 37.5665,
            longitude = 126.978,
            status = ProcessStatus.IN_PROGRESS.value,
            title = "Test Manhole",
            place = "Test Location",
            generatedDescription = listOf("Test Description"),
            userDescription = "Test User Description",
            processAgency = "Test Agency",
            processDescription = "Test Process Description",
            userSub = "testUser123"
    )

    val DEFAULT_MANHOLE_ENTITY = ManholeEntity(
        id = 1L,
        latitude = 37.5665,
        longitude = 126.978,
        status = ProcessStatus.IN_PROGRESS.value,
        title = "Test Manhole",
        place = "Test Location",
        generatedDescription = listOf("Test Description"),
        userDescription = "Test User Description",
        processAgency = "Test Agency",
        processDescription = "Test Process Description",
        userSub = "testUser123"
    )

    val MANHOLE_ENTITY_ID_300 = ManholeEntity(
        id = 300L,
        latitude = 37.5665,
        longitude = 126.978,
        status = ProcessStatus.IN_PROGRESS.value,
        title = "Test Manhole ID 300",
        place = "Test Location",
        generatedDescription = listOf("Test Description"),
        userDescription = "Test User Description",
        processAgency = "Test Agency",
        processDescription = "Test Process Description",
        userSub = "testUser123"
    )

    val MANHOLE_ENTITY_ID_500 = ManholeEntity(
        id = 500L,
        latitude = 37.5665,
        longitude = 126.978,
        status = ProcessStatus.PENDING.value,
        title = "Test Manhole ID 500",
        place = "Test Location",
        generatedDescription = listOf("Test Description"),
        userDescription = "Test User Description",
        processAgency = "Test Agency",
        processDescription = "Test Process Description",
        userSub = "testUser123"
    )
}