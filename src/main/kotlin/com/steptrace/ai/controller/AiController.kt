package com.steptrace.ai.controller

import com.steptrace.ai.dto.ImageAnalysisRequest
import com.steptrace.ai.service.AiService
import org.springframework.ai.content.Media
import org.springframework.core.io.UrlResource
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.*

/**
 * AI 기반 이미지 분석 REST API 컨트롤러
 * 
 * Anthropic AI를 활용하여 맨홀 이미지를 분석하고 위험도를 평가하는
 * REST API 엔드포인트를 제공합니다. 정상/비정상 맨홀 구분 및 위험 수준 평가 기능을 제공합니다.
 */
@RestController
@RequestMapping("/api")
class AiController(
        private val aiService: AiService  // AI 이미지 분석 서비스
) {

    /**
     * 비정상 맨홀 이미지를 AI로 분석하여 위험도를 평가합니다.
     * 
     * 사용자가 업로드한 비정상 맨홀 이미지들을 Anthropic AI로 분석하여
     * 실제로 위험한 맨홀인지 확인하고, 위험 수준을 평가합니다.
     * 
     * @param imageAnalysisRequest 분석할 이미지 URL 및 컨텐츠 타입 목록
     * @return AI 분석 결과 ("false" 또는 위험도 설명)
     */
    @PostMapping("/v1/ai/abnormal-manhole/image")
    fun analyzeAbnormalManholeImages(
            @RequestBody imageAnalysisRequest: List<ImageAnalysisRequest>
    ): String? {
        return aiService.analyzeAbnormalManholeImages(imageAnalysisRequest.map { request ->
            val resource = UrlResource(request.imageUrl)                    // 이미지 URL에서 리소스 생성
            val mimeType = MimeTypeUtils.parseMimeType(request.contentType) // MIME 타입 파싱
            Media(mimeType, resource)  // AI 분석용 미디어 객체 생성
        })
    }

    /**
     * 정상 맨홀 이미지의 유효성을 AI로 검증합니다.
     * 
     * 사용자가 업로드한 정상 맨홀 이미지들이 실제로 적절한 맨홀 이미지인지
     * Anthropic AI로 검증하여 신고의 유효성을 확인합니다.
     * 
     * @param imageAnalysisRequest 검증할 이미지 URL 및 컨텐츠 타입 목록
     * @return AI 검증 결과 ("true" 또는 "false")
     */
    @PostMapping("/v1/ai/normal-manhole/image")
    fun analyzeNormalManholeImage(
            @RequestBody imageAnalysisRequest: List<ImageAnalysisRequest>
    ): String? {
        return aiService.analyzeNormalManholeImages(imageAnalysisRequest.map { request ->
            val resource = UrlResource(request.imageUrl)                    // 이미지 URL에서 리소스 생성
            val mimeType = MimeTypeUtils.parseMimeType(request.contentType) // MIME 타입 파싱
            Media(mimeType, resource)  // AI 분석용 미디어 객체 생성
        })
    }
}