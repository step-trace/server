package com.step.steptrace.manhole.dto

import com.step.steptrace.annotation.UnitTest
import com.steptrace.manhole.dto.ManholeRequest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@UnitTest
class ManholeRequestTest {

    @Test
    @DisplayName("imageUrls가 빈 리스트면 IllegalArgumentException을 발생시킨다")
    fun should_throw_illegal_argument_exception_when_image_urls_is_empty() {
        assertThatThrownBy {
            ManholeRequest(
                imageUrls = emptyList(),
                latitude = 37.5665,
                longitude = 126.978,
                place = "서울특별시 중구",
                title = "맨홀 수리 요청",
                userDescription = "맨홀이 파손되었습니다.",
                generatedDescription = listOf("위험도: 상")
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Image URL은 빈 값일 수 없습니다.")
    }

    @Test
    @DisplayName("generatedDescription이 빈 리스트면 IllegalArgumentException을 발생시킨다")
    fun should_throw_illegal_argument_exception_when_generated_description_is_empty() {
        assertThatThrownBy {
            ManholeRequest(
                imageUrls = listOf("image1.jpg"),
                latitude = 37.5665,
                longitude = 126.978,
                place = "서울특별시 중구",
                title = "맨홀 수리 요청",
                userDescription = "맨홀이 파손되었습니다.",
                generatedDescription = emptyList()
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Generated description은 빈 값일 수 없습니다.")
    }
}