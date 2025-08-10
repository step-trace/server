package com.steptrace.config

import org.springframework.ai.anthropic.AnthropicChatModel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig {
    @Bean
    fun anthropicChatClient(chatModel: AnthropicChatModel): ChatClient {
        return ChatClient.create(chatModel)
    }
}