package com.step.steptrace.manhole.dto

import com.step.steptrace.manhole.stub.ManholeDtoStub
import com.step.steptrace.annotation.UnitTest
import com.steptrace.manhole.dto.ManholesFromMyReportResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@UnitTest
class ManholeResponseTest {

    @Test
    @DisplayName("afterImageUrls가 있으면 첫 번째 after 이미지를 imageUrl로 사용한다")
    fun should_use_first_after_image_url_when_after_images_exist() {
        val result = ManholesFromMyReportResponse.from(ManholeDtoStub.MANHOLE_WITH_AFTER_IMAGES)

        assertThat(result.id).isEqualTo(100L)
        assertThat(result.status).isEqualTo("처리 완료")
        assertThat(result.title).isEqualTo("Test Manhole with After Images")
        assertThat(result.createdAt).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0))
        assertThat(result.imageUrl).isEqualTo("after1.jpg")
    }

    @Test
    @DisplayName("afterImageUrls가 없으면 첫 번째 before 이미지를 imageUrl로 사용한다")
    fun should_use_first_before_image_url_when_no_after_images_exist() {
        val result = ManholesFromMyReportResponse.from(ManholeDtoStub.MANHOLE_WITH_ONLY_BEFORE_IMAGES)

        assertThat(result.id).isEqualTo(200L)
        assertThat(result.status).isEqualTo("처리 중")
        assertThat(result.title).isEqualTo("Test Manhole with Only Before Images")
        assertThat(result.createdAt).isEqualTo(LocalDateTime.of(2024, 2, 1, 14, 30))
        assertThat(result.imageUrl).isEqualTo("before1.jpg")
    }

    @Test
    @DisplayName("afterImageUrls가 빈 리스트면 첫 번째 before 이미지를 imageUrl로 사용한다")
    fun should_use_first_before_image_url_when_after_images_list_is_empty() {
        val result = ManholesFromMyReportResponse.from(ManholeDtoStub.MANHOLE_WITH_EMPTY_AFTER_IMAGES)

        assertThat(result.id).isEqualTo(300L)
        assertThat(result.status).isEqualTo("접수 전")
        assertThat(result.title).isEqualTo("Test Manhole with Empty After Images")
        assertThat(result.createdAt).isEqualTo(LocalDateTime.of(2024, 3, 15, 9, 45))
        assertThat(result.imageUrl).isEqualTo("before_main.jpg")
    }

    @Test
    @DisplayName("afterImageUrls의 첫 번째 요소가 null이면 before 이미지를 사용한다")
    fun should_use_before_image_when_first_after_image_is_null() {
        val result = ManholesFromMyReportResponse.from(ManholeDtoStub.MANHOLE_WITH_NULL_AFTER_IMAGE)

        assertThat(result.imageUrl).isEqualTo("fallback_before.jpg")
    }

    @Test
    @DisplayName("단일 before 이미지만 있는 경우 정상적으로 변환한다")
    fun should_convert_successfully_when_only_single_before_image_exists() {
        val result = ManholesFromMyReportResponse.from(ManholeDtoStub.MANHOLE_WITH_SINGLE_BEFORE_IMAGE)

        assertThat(result.id).isEqualTo(500L)
        assertThat(result.status).isEqualTo("처리 중")
        assertThat(result.title).isEqualTo("Single Image Manhole")
        assertThat(result.createdAt).isEqualTo(LocalDateTime.of(2024, 5, 10, 11, 20))
        assertThat(result.imageUrl).isEqualTo("single_image.jpg")
    }
}