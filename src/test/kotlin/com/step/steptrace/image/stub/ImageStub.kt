package com.step.steptrace.image.stub

import com.steptrace.image.dto.ImageS3UploadRequest
import com.steptrace.image.dto.ImageS3UploadResponse

object ImageStub {

    val DEFAULT_UPLOAD_REQUEST = ImageS3UploadRequest(
        fileName = "test-image.jpg",
        contentType = "image/jpeg"
    )

    val DEFAULT_UPLOAD_RESPONSE = ImageS3UploadResponse(
        fileName = "test-image.jpg",
        presignedUrl = "https://test-bucket.s3.amazonaws.com/manholes/12345678-test-image.jpg"
    )

    val MULTIPLE_UPLOAD_REQUESTS = listOf(
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "image1.jpg", contentType = "image/jpeg"),
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "image2.png", contentType = "image/png"),
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "image3.webp", contentType = "image/webp")
    )

    val VARIOUS_EXTENSIONS_REQUESTS = listOf(
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "profile.jpeg", contentType = "image/jpeg"),
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "banner.PNG", contentType = "image/png"),
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "logo.gif", contentType = "image/gif"),
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "photo.WEBP", contentType = "image/webp")
    )

    val SPECIAL_CHAR_UPLOAD_REQUEST = DEFAULT_UPLOAD_REQUEST.copy(
        fileName = "test image (1).jpg",
        contentType = "image/jpeg"
    )

    val SAME_NAME_UPLOAD_REQUESTS = listOf(
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "same-name.jpg"),
        DEFAULT_UPLOAD_REQUEST.copy(fileName = "same-name.jpg")
    )
}