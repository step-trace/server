package com.steptrace.ai.service

import com.steptrace.exception.AiResponseGenerateException
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.content.Media
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class AiService(
        @Value("classpath:/prompts/abnormal-manhole-analysis.st")
        private val abnormalManholeRiskAssessmentPrompt: Resource,

        @Value("classpath:/prompts/abnormal-manhole-analysis-by-sub-ai.st")
        private val abnormalManholeValidationPrompt: Resource,

        @Value("classpath:/prompts/normal-manhole-analysis.st")
        private val normalManholeValidationPrompt: Resource,

        @Qualifier("anthropicChatClient")
        private val anthropicClient: ChatClient,
) {
    fun analyzeAbnormalManholeImages(mediaList: List<Media>): String? {
        return analyzeManholeImages(
                mediaList = mediaList,
                validationPromptResource = abnormalManholeValidationPrompt,
                riskAssessmentPromptResource = abnormalManholeRiskAssessmentPrompt
        )
    }

    fun analyzeNormalManholeImages(mediaList: List<Media>): String? {
        return validateManholeCondition(
                mediaList = mediaList,
                promptResource = normalManholeValidationPrompt
        )
    }

    private fun analyzeManholeImages(
            mediaList: List<Media>,
            validationPromptResource: Resource,
            riskAssessmentPromptResource: Resource
    ): String? {
        return try {
            when (validateManholeCondition(mediaList, validationPromptResource)) {
                "false" -> "false"
                else -> evaluateRiskLevel(mediaList, riskAssessmentPromptResource)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw AiResponseGenerateException()
        }
    }

    private fun validateManholeCondition(
            mediaList: List<Media>,
            promptResource: Resource
    ): String {
        val response = executeAiPrompt(
                client = anthropicClient,
                mediaList = mediaList,
                promptResource = promptResource
        )

        if (response == null || response.contains("false")) {
            return "false"
        }

        return "true"
    }

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

    private fun executeAiPrompt(
            client: ChatClient,
            mediaList: List<Media>,
            promptResource: Resource
    ): String? {
        val promptTemplate = PromptTemplate(promptResource)
        val userMessage = promptTemplate.createMessage(mediaList)

        return client.prompt()
                .messages(userMessage)
                .call()
                .content()
    }
}