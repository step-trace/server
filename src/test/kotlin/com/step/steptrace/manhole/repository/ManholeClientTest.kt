package com.step.steptrace.manhole.repository

import com.step.steptrace.manhole.stub.ManholeAttachmentEntityStub.BEFORE_IMAGE_ATTACHMENT
import com.step.steptrace.manhole.stub.ManholeAttachmentEntityStub.MIXED_ATTACHMENTS
import com.step.steptrace.manhole.stub.ManholeAttachmentEntityStub.MULTIPLE_BEFORE_ATTACHMENTS
import com.step.steptrace.manhole.stub.ManholeAttachmentEntityStub.ONLY_AFTER_ATTACHMENTS
import com.step.steptrace.manhole.stub.ManholeDtoStub.DEFAULT_MANHOLE
import com.step.steptrace.manhole.stub.ManholeEntityStub
import com.step.steptrace.manhole.stub.ManholeEntityStub.DEFAULT_MANHOLE_ENTITY
import com.step.steptrace.manhole.stub.ManholeEntityStub.MANHOLE_ENTITY_ID_300
import com.step.steptrace.manhole.stub.ManholeEntityStub.SAVED_MANHOLE_ENTITY
import com.step.steptrace.annotation.UnitTest
import com.steptrace.exception.IdNotFoundException
import com.steptrace.manhole.repository.ManholeAttachmentJpaRepository
import com.steptrace.manhole.repository.ManholeClient
import com.steptrace.manhole.repository.ManholeJpaRepository
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
import java.util.*

@ExtendWith(MockKExtension::class)
@UnitTest
class ManholeClientTest {

    @InjectMockKs
    private lateinit var manholeClient: ManholeClient

    @MockK
    private lateinit var manholeJpaRepository: ManholeJpaRepository

    @MockK
    private lateinit var manholeAttachmentJpaRepository: ManholeAttachmentJpaRepository

    @Test
    @DisplayName("모든 맨홀을 첨부파일과 함께 조회한다")
    fun should_load_manholes_with_attachments_successfully() {
        val manholeEntities = listOf(DEFAULT_MANHOLE_ENTITY, SAVED_MANHOLE_ENTITY)
        val attachments1 = MIXED_ATTACHMENTS
        val attachments2 = MULTIPLE_BEFORE_ATTACHMENTS

        every { manholeJpaRepository.findAll() } returns manholeEntities
        every { manholeAttachmentJpaRepository.findAllByManholeId(1L) } returns attachments1
        every { manholeAttachmentJpaRepository.findAllByManholeId(100L) } returns attachments2

        val result = manholeClient.loadManholesWithAttachment()

        assertThat(result).hasSize(2)
        
        val firstResult = result.find { it.id == 1L }!!
        assertThat(firstResult.beforeImageUrls).containsExactly("before1.jpg", "before2.jpg")
        assertThat(firstResult.afterImageUrls).containsExactly("after1.jpg", "after2.jpg")
        
        val secondResult = result.find { it.id == 100L }!!
        assertThat(secondResult.beforeImageUrls).containsExactly("before1.jpg", "before2.jpg", "before3.jpg")
        assertThat(secondResult.afterImageUrls).isEmpty()

        verify(exactly = 1) { manholeJpaRepository.findAll() }
        verify(exactly = 1) { manholeAttachmentJpaRepository.findAllByManholeId(1L) }
        verify(exactly = 1) { manholeAttachmentJpaRepository.findAllByManholeId(100L) }
    }

    @Test
    @DisplayName("ID로 맨홀을 첨부파일과 함께 조회한다")
    fun should_load_manhole_with_attachment_by_id_successfully() {
        val manholeId = 300L
        val manholeEntity = MANHOLE_ENTITY_ID_300
        val attachments = MIXED_ATTACHMENTS

        every { manholeJpaRepository.findById(manholeId) } returns Optional.of(manholeEntity)
        every { manholeAttachmentJpaRepository.findAllByManholeId(manholeId) } returns attachments

        val result = manholeClient.loadManholeWithAttachmentById(manholeId)

        assertThat(result.id).isEqualTo(manholeId)
        assertThat(result.beforeImageUrls).containsExactly("before1.jpg", "before2.jpg")
        assertThat(result.afterImageUrls).containsExactly("after1.jpg", "after2.jpg")

        verify(exactly = 1) { manholeJpaRepository.findById(manholeId) }
        verify(exactly = 1) { manholeAttachmentJpaRepository.findAllByManholeId(manholeId) }
    }

    @Test
    @DisplayName("존재하지 않는 ID로 맨홀 조회시 예외를 발생시킨다")
    fun should_throw_exception_when_manhole_not_found_by_id() {
        val nonExistentId = 999L

        every { manholeJpaRepository.findById(nonExistentId) } returns Optional.empty()

        assertThatThrownBy { manholeClient.loadManholeWithAttachmentById(nonExistentId) }
            .isInstanceOf(IdNotFoundException::class.java)
            .hasMessage("manhole ID not found")

        verify(exactly = 1) { manholeJpaRepository.findById(nonExistentId) }
        verify(exactly = 0) { manholeAttachmentJpaRepository.findAllByManholeId(any()) }
    }

    @Test
    @DisplayName("맨홀을 저장한다")
    fun should_save_manhole_successfully() {
        val manholeDto = DEFAULT_MANHOLE.copy(id = null)
        val savedEntity = SAVED_MANHOLE_ENTITY

        every { manholeJpaRepository.save(any()) } returns savedEntity

        val result = manholeClient.saveManhole(manholeDto)

        assertThat(result).isEqualTo(savedEntity)

        verify(exactly = 1) { manholeJpaRepository.save(any()) }
    }

    @Test
    @DisplayName("맨홀 첨부파일들을 저장한다")
    fun should_save_manhole_attachments_successfully() {
        val manholeId = 100L
        val imageUrls = listOf("image1.jpg", "image2.jpg", "image3.jpg")

        every { manholeAttachmentJpaRepository.save(any()) } returns BEFORE_IMAGE_ATTACHMENT

        manholeClient.saveManholeAttachments(manholeId, imageUrls)

        verify(exactly = 3) { manholeAttachmentJpaRepository.save(any()) }
    }

    @Test
    @DisplayName("사용자 sub로 맨홀들을 첨부파일과 함께 조회한다")
    fun should_load_manholes_with_attachments_by_user_sub_successfully() {
        val userSub = "testUser123"
        val manholeEntities = listOf(DEFAULT_MANHOLE_ENTITY, SAVED_MANHOLE_ENTITY)
        val attachments1 = ONLY_AFTER_ATTACHMENTS
        val attachments2 = MULTIPLE_BEFORE_ATTACHMENTS

        every { manholeJpaRepository.findAllByUserSub(userSub) } returns manholeEntities
        every { manholeAttachmentJpaRepository.findAllByManholeId(1L) } returns attachments1
        every { manholeAttachmentJpaRepository.findAllByManholeId(100L) } returns attachments2

        val result = manholeClient.loadManholesWithAttachmentsBySub(userSub)

        assertThat(result).hasSize(2)
        
        val firstResult = result.find { it.id == 1L }!!
        assertThat(firstResult.beforeImageUrls).isEmpty()
        assertThat(firstResult.afterImageUrls).containsExactly("after1.jpg", "after2.jpg")
        
        val secondResult = result.find { it.id == 100L }!!
        assertThat(secondResult.beforeImageUrls).containsExactly("before1.jpg", "before2.jpg", "before3.jpg")
        assertThat(secondResult.afterImageUrls).isEmpty()

        verify(exactly = 1) { manholeJpaRepository.findAllByUserSub(userSub) }
        verify(exactly = 1) { manholeAttachmentJpaRepository.findAllByManholeId(1L) }
        verify(exactly = 1) { manholeAttachmentJpaRepository.findAllByManholeId(100L) }
    }

    @Test
    @DisplayName("첨부파일이 없는 맨홀도 올바르게 조회된다")
    fun should_load_manhole_with_no_attachments_successfully() {
        val manholeId = 500L
        val manholeEntity = ManholeEntityStub.MANHOLE_ENTITY_ID_500

        every { manholeJpaRepository.findById(manholeId) } returns Optional.of(manholeEntity)
        every { manholeAttachmentJpaRepository.findAllByManholeId(manholeId) } returns emptyList()

        val result = manholeClient.loadManholeWithAttachmentById(manholeId)

        assertThat(result.id).isEqualTo(manholeId)
        assertThat(result.beforeImageUrls).isEmpty()
        assertThat(result.afterImageUrls).isEmpty()

        verify(exactly = 1) { manholeJpaRepository.findById(manholeId) }
        verify(exactly = 1) { manholeAttachmentJpaRepository.findAllByManholeId(manholeId) }
    }
}