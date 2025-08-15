package com.steptrace.ai.controller

import com.steptrace.ai.dto.ImageAnalysisRequest
import com.steptrace.ai.service.AiService
import org.springframework.ai.content.Media
import org.springframework.core.io.UrlResource
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AiController(
        private val aiService: AiService
) {

    @PostMapping("/v1/ai/abnormal-manhole/image")
    fun analyzeAbnormalManholeImages(
            @RequestBody imageAnalysisRequest: List<ImageAnalysisRequest>
    ): String? {
        return aiService.analyzeAbnormalManholeImages(imageAnalysisRequest.map { request ->
            val resource = UrlResource(request.imageUrl)
            val mimeType = MimeTypeUtils.parseMimeType(request.contentType)
            Media(mimeType, resource)
        })
    }

    @PostMapping("/v1/ai/normal-manhole/image")
    fun analyzeNormalManholeImage(
            @RequestBody imageAnalysisRequest: List<ImageAnalysisRequest>
    ): String? {
        return aiService.analyzeNormalManholeImages(imageAnalysisRequest.map { request ->
            val resource = UrlResource(request.imageUrl)
            val mimeType = MimeTypeUtils.parseMimeType(request.contentType)
            Media(mimeType, resource)
        })
    }
}