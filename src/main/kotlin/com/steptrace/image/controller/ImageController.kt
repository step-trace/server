package com.steptrace.image.controller

import com.steptrace.image.dto.ImageS3UploadRequest
import com.steptrace.image.dto.ImageS3UploadResponse
import com.steptrace.image.service.ImageService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageService: ImageService
) {

    @PostMapping("/v1/manholes")
    fun uploadImages(
        @RequestBody requests: List<ImageS3UploadRequest>
    ): List<ImageS3UploadResponse> {
        return imageService.getPresignedUrls(requests)
    }
}