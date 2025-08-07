package com.step.steptrace.manhole.service

import com.step.steptrace.manhole.stub.ManholeDtoStub.BOUNDARY_NORTHEAST_MANHOLE
import com.step.steptrace.manhole.stub.ManholeDtoStub.BOUNDARY_SOUTHWEST_MANHOLE
import com.step.steptrace.manhole.stub.ManholeDtoStub.DEFAULT_MANHOLE
import com.step.steptrace.manhole.stub.ManholeDtoStub.IN_BOUNDS_MANHOLE
import com.step.steptrace.manhole.stub.ManholeDtoStub.OUT_OF_BOUNDS_MANHOLE
import com.step.steptrace.manhole.stub.ManholeEntityStub.MANHOLE_ENTITY_WITH_NULL_ID
import com.step.steptrace.manhole.stub.ManholeEntityStub.SAVED_MANHOLE_ENTITY
import com.steptrace.common.annotation.UnitTest
import com.steptrace.exception.IdNotFoundException
import com.steptrace.manhole.repository.ManholeRepository
import com.steptrace.manhole.service.ManholeService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@UnitTest
internal class ManholeServiceTest {
    @InjectMockKs
    private lateinit var manholeService: ManholeService

    @MockK
    private lateinit var manholeRepository: ManholeRepository

    @Test
    @DisplayName("지정된 범위 내의 맨홀 마커들을 조회한다")
    fun should_return_manholes_within_specified_bounds_when_getting_manhole_markers() {
        val southwestLatitude = 37.5000
        val southwestLongitude = 126.9000
        val northeastLatitude = 37.6000
        val northeastLongitude = 127.0000
        
        val allManholes = listOf(IN_BOUNDS_MANHOLE, OUT_OF_BOUNDS_MANHOLE)
        
        every { manholeRepository.loadManholesWithAttachment() } returns allManholes
        
        val result = manholeService.getManholeMarkers(
            southwestLatitude, 
            southwestLongitude, 
            northeastLatitude, 
            northeastLongitude
        )
        
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(IN_BOUNDS_MANHOLE)
        
        verify(exactly = 1) { manholeRepository.loadManholesWithAttachment() }
    }
    
    @Test
    @DisplayName("범위 내에 맨홀이 없으면 빈 리스트를 반환한다")
    fun should_return_empty_list_when_no_manholes_exist_within_specified_bounds() {
        val southwestLatitude = 37.5000
        val southwestLongitude = 126.9000
        val northeastLatitude = 37.6000
        val northeastLongitude = 127.0000
        
        every { manholeRepository.loadManholesWithAttachment() } returns listOf(OUT_OF_BOUNDS_MANHOLE)
        
        val result = manholeService.getManholeMarkers(
            southwestLatitude, 
            southwestLongitude, 
            northeastLatitude, 
            northeastLongitude
        )
        
        assertThat(result).isEmpty()
        
        verify(exactly = 1) { manholeRepository.loadManholesWithAttachment() }
    }
    
    @Test
    @DisplayName("경계값에 있는 맨홀도 포함해서 반환한다")
    fun should_include_boundary_manholes_when_getting_manholes_within_bounds() {
        val southwestLatitude = 37.5000
        val southwestLongitude = 126.9000
        val northeastLatitude = 37.6000
        val northeastLongitude = 127.0000
        
        val boundaryManholes = listOf(BOUNDARY_SOUTHWEST_MANHOLE, BOUNDARY_NORTHEAST_MANHOLE)
        
        every { manholeRepository.loadManholesWithAttachment() } returns boundaryManholes
        
        val result = manholeService.getManholeMarkers(
            southwestLatitude, 
            southwestLongitude, 
            northeastLatitude, 
            northeastLongitude
        )
        
        assertThat(result).hasSize(2)
        assertThat(result).containsExactlyInAnyOrderElementsOf(boundaryManholes)
        
        verify(exactly = 1) { manholeRepository.loadManholesWithAttachment() }
    }
    
    @Test
    @DisplayName("데이터베이스에 맨홀이 없으면 빈 리스트를 반환한다")
    fun should_return_empty_list_when_no_manholes_exist_in_database() {
        val southwestLatitude = 37.5000
        val southwestLongitude = 126.9000
        val northeastLatitude = 37.6000
        val northeastLongitude = 127.0000
        
        every { manholeRepository.loadManholesWithAttachment() } returns emptyList()
        
        val result = manholeService.getManholeMarkers(
            southwestLatitude, 
            southwestLongitude, 
            northeastLatitude, 
            northeastLongitude
        )
        
        assertThat(result).isEmpty()
        
        verify(exactly = 1) { manholeRepository.loadManholesWithAttachment() }
    }

    @Test
    @DisplayName("맨홀을 성공적으로 생성한다")
    fun should_create_manhole_successfully_when_valid_data_provided() {
        val manholeDto = DEFAULT_MANHOLE.copy(id = null)
        val manholeId = 100L

        every { manholeRepository.saveManhole(manholeDto) } returns SAVED_MANHOLE_ENTITY
        every { manholeRepository.saveManholeAttachments(manholeId, manholeDto.beforeImageUrls) } returns Unit

        manholeService.create(manholeDto)

        verify(exactly = 1) { manholeRepository.saveManhole(manholeDto) }
        verify(exactly = 1) { manholeRepository.saveManholeAttachments(manholeId, manholeDto.beforeImageUrls) }
    }

    @Test
    @DisplayName("맨홀 저장 후 ID가 null이면 예외를 발생시킨다")
    fun should_throw_exception_when_saved_manhole_id_is_null() {
        val manholeDto = DEFAULT_MANHOLE.copy(id = null)

        every { manholeRepository.saveManhole(manholeDto) } returns MANHOLE_ENTITY_WITH_NULL_ID

        assertThatThrownBy { manholeService.create(manholeDto) }
            .isInstanceOf(IdNotFoundException::class.java)
            .hasMessage("manhole ID not found")

        verify(exactly = 1) { manholeRepository.saveManhole(manholeDto) }
        verify(exactly = 0) { manholeRepository.saveManholeAttachments(any(), any()) }
    }
}