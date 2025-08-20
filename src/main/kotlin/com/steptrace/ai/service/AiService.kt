package com.steptrace.ai.service

import com.steptrace.exception.AiResponseGenerateException
import com.steptrace.exception.BadRequestToAiException
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.content.Media
import org.springframework.ai.retry.NonTransientAiException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

/**
 * AI 기반 맨홀 이미지 분석 서비스
 * 
 * Anthropic AI를 사용하여 맨홀 상태를 분석하고 위험도를 평가합니다.
 * 정상/비정상 맨홀 이미지를 구분하고, 위험 수준을 자동으로 판단합니다.
 */
@Service
class AiService(
        @Value("classpath:/prompts/abnormal-manhole-analysis.st")
        private val abnormalManholeRiskAssessmentPrompt: Resource,  // 비정상 맨홀 위험도 평가 프롬프트

        @Value("classpath:/prompts/abnormal-manhole-analysis-by-sub-ai.st")
        private val abnormalManholeValidationPrompt: Resource,      // 비정상 맨홀 유효성 검증 프롬프트

        @Value("classpath:/prompts/normal-manhole-analysis.st")
        private val normalManholeValidationPrompt: Resource,        // 정상 맨홀 유효성 검증 프롬프트

        @Qualifier("anthropicChatClient")
        private val anthropicClient: ChatClient,                    // Anthropic AI 클라이언트
) {

    /**
     * 비정상 맨홀 이미지를 분석하여 위험도를 평가합니다.
     * 
     * 2단계 분석 프로세스:
     * 1. 이미지가 실제로 비정상적인 맨홀인지 유효성 검증
     * 2. 유효한 경우 위험도 수준을 평가하여 반환
     * 
     * @param mediaList 분석할 이미지 미디어 목록
     * @return AI 분석 결과 ("false" 또는 위험도 수준 문자열)
     */
    fun analyzeAbnormalManholeImages(mediaList: List<Media>): String? {
        return analyzeManholeImages(
                mediaList = mediaList,
                validationPromptResource = abnormalManholeValidationPrompt,
                riskAssessmentPromptResource = abnormalManholeRiskAssessmentPrompt
        )
    }

    /**
     * 정상 맨홀 이미지의 유효성을 검증합니다.
     * 
     * 이미지가 정상적인 맨홀 상태를 보여주는지 확인하여
     * 적절한 맨홀 신고인지 판단합니다.
     * 
     * @param mediaList 검증할 이미지 미디어 목록
     * @return 유효성 검증 결과 ("true" 또는 "false")
     */
    fun analyzeNormalManholeImages(mediaList: List<Media>): String? {
        return validateManholeCondition(
                mediaList = mediaList,
                promptResource = normalManholeValidationPrompt
        )
    }

    /**
     * 맨홀 이미지 분석의 핵심 로직을 처리합니다.
     * 
     * @param mediaList 분석할 이미지 목록
     * @param validationPromptResource 유효성 검증용 프롬프트
     * @param riskAssessmentPromptResource 위험도 평가용 프롬프트
     * @return 분석 결과 또는 null
     * @throws BadRequestToAiException AI 요청이 잘못된 경우
     * @throws AiResponseGenerateException AI 응답 생성 중 오류 발생
     */
    private fun analyzeManholeImages(
            mediaList: List<Media>,
            validationPromptResource: Resource,
            riskAssessmentPromptResource: Resource
    ): String? {
        return try {
            // 1단계: 맨홀 이미지 유효성 검증
            when (validateManholeCondition(mediaList, validationPromptResource)) {
                "false" -> "false"  // 유효하지 않은 맨홀 이미지
                else -> evaluateRiskLevel(mediaList, riskAssessmentPromptResource)  // 2단계: 위험도 평가
            }
        }
        catch (e: NonTransientAiException) {
            throw BadRequestToAiException(e)  // AI 서비스 요청 오류
        }
        catch (e: Exception) {
            throw AiResponseGenerateException(e)  // 일반적인 AI 응답 생성 오류
        }
    }

    /**
     * 맨홀 상태의 유효성을 검증합니다.
     * 
     * AI에게 이미지를 보내서 적절한 맨홀 이미지인지 판단하고,
     * 응답에 "false"가 포함되어 있으면 유효하지 않은 것으로 처리합니다.
     * 
     * @param mediaList 검증할 이미지 목록
     * @param promptResource 사용할 프롬프트 리소스
     * @return 유효성 검증 결과 ("true" 또는 "false")
     */
    private fun validateManholeCondition(
            mediaList: List<Media>,
            promptResource: Resource
    ): String {
        val response = executeAiPrompt(
                client = anthropicClient,
                mediaList = mediaList,
                promptResource = promptResource
        )

        // AI 응답이 null이거나 "false"를 포함하면 유효하지 않음
        if (response == null || response.contains("false")) {
            return "false"
        }

        return "true"
    }

    /**
     * 맨홀의 위험 수준을 평가합니다.
     * 
     * 이미 유효성이 확인된 비정상 맨홀 이미지에 대해
     * 위험도 수준을 상세히 분석하여 반환합니다.
     * 
     * @param mediaList 위험도를 평가할 이미지 목록
     * @param promptResource 위험도 평가용 프롬프트 리소스
     * @return 위험도 평가 결과 문자열
     */
    private fun evaluateRiskLevel(
            mediaList: List<Media>,
            promptResource: Resource
    ): String? {
        return executeAiPrompt(
                client = anthropicClient,
                mediaList = mediaList,
                promptResource = promptResource
        )
    }

    /**
     * AI 프롬프트를 실제로 실행하여 응답을 받아옵니다.
     * 
     * 프롬프트 템플릿과 이미지 미디어를 결합하여 메시지를 생성하고,
     * AI 클라이언트에게 전송하여 분석 결과를 받습니다.
     * 
     * @param client 사용할 AI 체트 클라이언트
     * @param mediaList 전송할 이미지 미디어 목록
     * @param promptResource 사용할 프롬프트 리소스
     * @return AI가 생성한 응답 내용
     */
    private fun executeAiPrompt(
            client: ChatClient,
            mediaList: List<Media>,
            promptResource: Resource
    ): String? {
        val promptTemplate = PromptTemplate(promptResource)  // 프롬프트 템플릿 로드
        val userMessage = promptTemplate.createMessage(mediaList)  // 이미지와 함께 메시지 생성

        return client.prompt()
                .messages(userMessage)
                .call()
                .content()  // AI 응답의 내용만 추출
    }
}