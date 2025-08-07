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
}