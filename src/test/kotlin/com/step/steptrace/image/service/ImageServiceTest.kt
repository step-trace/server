package com.step.steptrace.image.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.step.steptrace.image.stub.ImageStub
import com.step.steptrace.annotation.UnitTest
import com.steptrace.config.AwsProperties
import com.steptrace.image.service.ImageService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.net.URL

@ExtendWith(MockKExtension::class)
@UnitTest
class ImageServiceTest {

    @InjectMockKs
    private lateinit var imageService: ImageService

    @MockK
    private lateinit var amazonS3: AmazonS3

    @MockK
    private lateinit var awsProperties: AwsProperties


    @BeforeEach
    fun setUp() {
        every { awsProperties.s3.bucket } returns "test-bucket"
    }

    @Test
    @DisplayName("단일 이미지에 대한 presigned URL을 생성한다")
    fun should_generate_presigned_url_for_single_image_successfully() {
        val mockUrl = mockk<URL>()

        every { mockUrl.toString() } returns ImageStub.DEFAULT_UPLOAD_RESPONSE.presignedUrl
        every { amazonS3.generatePresignedUrl(any()) } returns mockUrl

        val result = imageService.getPresignedUrls(listOf(ImageStub.DEFAULT_UPLOAD_REQUEST))

        assertThat(result).hasSize(1)
        assertThat(result[0].fileName).isEqualTo(ImageStub.DEFAULT_UPLOAD_REQUEST.fileName)
        assertThat(result[0].presignedUrl).isEqualTo(ImageStub.DEFAULT_UPLOAD_RESPONSE.presignedUrl)

        verify(exactly = 1) { amazonS3.generatePresignedUrl(any()) }
    }

    @Test
    @DisplayName("여러 이미지에 대한 presigned URL들을 생성한다")
    fun should_generate_presigned_urls_for_multiple_images_successfully() {
        val requests = listOf(
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "image1.jpg", contentType = "image/jpeg"),
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "image2.png", contentType = "image/png"),
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "image3.webp", contentType = "image/webp")
        )

        val mockUrl1 = mockk<URL>()
        val mockUrl2 = mockk<URL>()
        val mockUrl3 = mockk<URL>()

        every { mockUrl1.toString() } returns "https://test-bucket.s3.amazonaws.com/manholes/uuid1-image1.jpg"
        every { mockUrl2.toString() } returns "https://test-bucket.s3.amazonaws.com/manholes/uuid2-image2.png"
        every { mockUrl3.toString() } returns "https://test-bucket.s3.amazonaws.com/manholes/uuid3-image3.webp"

        every { amazonS3.generatePresignedUrl(any()) } returnsMany listOf(mockUrl1, mockUrl2, mockUrl3)

        val result = imageService.getPresignedUrls(requests)

        assertThat(result).hasSize(3)
        assertThat(result[0].fileName).isEqualTo("image1.jpg")
        assertThat(result[1].fileName).isEqualTo("image2.png")
        assertThat(result[2].fileName).isEqualTo("image3.webp")

        assertThat(result[0].presignedUrl).contains("image1.jpg")
        assertThat(result[1].presignedUrl).contains("image2.png")
        assertThat(result[2].presignedUrl).contains("image3.webp")

        verify(exactly = 3) { amazonS3.generatePresignedUrl(any()) }
    }

    @Test
    @DisplayName("빈 요청 리스트에 대해 빈 응답 리스트를 반환한다")
    fun should_return_empty_list_when_empty_request_list_provided() {
        val result = imageService.getPresignedUrls(emptyList())

        assertThat(result).isEmpty()

        verify(exactly = 0) { amazonS3.generatePresignedUrl(any()) }
    }

    @Test
    @DisplayName("생성되는 object key가 올바른 형식을 갖는다")
    fun should_generate_object_key_with_correct_format() {
        val mockUrl = mockk<URL>()
        val slot = slot<GeneratePresignedUrlRequest>()

        every { mockUrl.toString() } returns "https://test-url.com"
        every { amazonS3.generatePresignedUrl(capture(slot)) } returns mockUrl

        imageService.getPresignedUrls(listOf(ImageStub.DEFAULT_UPLOAD_REQUEST))

        val capturedRequest = slot.captured
        assertThat(capturedRequest.key).matches("manholes/[a-f0-9]{8}-test-image\\.jpg")
        assertThat(capturedRequest.bucketName).isEqualTo("test-bucket")
        assertThat(capturedRequest.method.toString()).isEqualTo("PUT")
        assertThat(capturedRequest.contentType).isEqualTo("image/jpeg")

        verify(exactly = 1) { amazonS3.generatePresignedUrl(any()) }
    }

    @Test
    @DisplayName("다양한 파일 확장자에 대해 올바르게 처리한다")
    fun should_handle_various_file_extensions_correctly() {
        val requests = listOf(
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "profile.jpeg", contentType = "image/jpeg"),
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "banner.PNG", contentType = "image/png"),
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "logo.gif", contentType = "image/gif"),
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "photo.WEBP", contentType = "image/webp")
        )

        val mockUrl = mockk<URL>()
        val slot = mutableListOf<GeneratePresignedUrlRequest>()

        every { mockUrl.toString() } returns "https://test-url.com"
        every { amazonS3.generatePresignedUrl(capture(slot)) } returns mockUrl

        val result = imageService.getPresignedUrls(requests)

        assertThat(result).hasSize(4)
        assertThat(slot[0].key).contains("profile.jpeg")
        assertThat(slot[1].key).contains("banner.PNG")
        assertThat(slot[2].key).contains("logo.gif")
        assertThat(slot[3].key).contains("photo.WEBP")

        assertThat(slot[0].contentType).isEqualTo("image/jpeg")
        assertThat(slot[1].contentType).isEqualTo("image/png")
        assertThat(slot[2].contentType).isEqualTo("image/gif")
        assertThat(slot[3].contentType).isEqualTo("image/webp")

        verify(exactly = 4) { amazonS3.generatePresignedUrl(any()) }
    }

    @Test
    @DisplayName("특수 문자가 포함된 파일명도 올바르게 처리한다")
    fun should_handle_special_characters_in_filename_correctly() {
        val specialCharRequest = ImageStub.DEFAULT_UPLOAD_REQUEST.copy(
            fileName = "test image (1).jpg",
            contentType = "image/jpeg"
        )

        val mockUrl = mockk<URL>()
        val slot = slot<GeneratePresignedUrlRequest>()

        every { mockUrl.toString() } returns "https://test-url.com"
        every { amazonS3.generatePresignedUrl(capture(slot)) } returns mockUrl

        val result = imageService.getPresignedUrls(listOf(specialCharRequest))

        assertThat(result).hasSize(1)
        assertThat(result[0].fileName).isEqualTo("test image (1).jpg")
        assertThat(slot.captured.key).contains("test image (1).jpg")

        verify(exactly = 1) { amazonS3.generatePresignedUrl(any()) }
    }

    @Test
    @DisplayName("각 요청마다 고유한 UUID가 생성된다")
    fun should_generate_unique_uuid_for_each_request() {
        val requests = listOf(
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "same-name.jpg"),
            ImageStub.DEFAULT_UPLOAD_REQUEST.copy(fileName = "same-name.jpg")
        )
        val mockUrl = mockk<URL>()
        val slot = mutableListOf<GeneratePresignedUrlRequest>()

        every { mockUrl.toString() } returns "https://test-url.com"
        every { amazonS3.generatePresignedUrl(capture(slot)) } returns mockUrl

        imageService.getPresignedUrls(requests)

        val firstKey = slot[0].key
        val secondKey = slot[1].key

        assertThat(firstKey).isNotEqualTo(secondKey)
        assertThat(firstKey).matches("manholes/[a-f0-9]{8}-same-name\\.jpg")
        assertThat(secondKey).matches("manholes/[a-f0-9]{8}-same-name\\.jpg")

        verify(exactly = 2) { amazonS3.generatePresignedUrl(any()) }
    }
}