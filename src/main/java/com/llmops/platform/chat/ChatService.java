package com.llmops.platform.chat;

import io.opentelemetry.api.trace.Span;

import java.util.Objects;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;

    public ChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Result ask(String message) {
        Objects.requireNonNull(message, "message must not be null");

        // Make the call
        var callResponse = chatClient
                .prompt(message)
                .call();

        // Defensive: some APIs mark this nullable
        ChatResponse response = callResponse.chatResponse();
        if (response == null) {
            throw new IllegalStateException("Spring AI returned a null ChatResponse. Check model config/deployment and provider response.");
        }

        var result = response.getResult();
        if (result == null || result.getOutput() == null) {
            throw new IllegalStateException("Spring AI response missing result/output.");
        }
        String answer = result.getOutput().getText();

        // Usage can be absent depending on provider/settings
        Usage usage = (response.getMetadata() != null) ? response.getMetadata().getUsage() : null;

        long promptTokens = (usage != null) ? usage.getPromptTokens() : 0L;
        long completionTokens = (usage != null) ? usage.getCompletionTokens() : 0L;
        long totalTokens = (usage != null) ? usage.getTotalTokens() : 0L;

        // Attach to current span for App Insights custom dimensions
        Span.current().setAttribute("llm.usage.prompt_tokens", promptTokens);
        Span.current().setAttribute("llm.usage.completion_tokens", completionTokens);
        Span.current().setAttribute("llm.usage.total_tokens", totalTokens);

        return new Result(answer, promptTokens, completionTokens, totalTokens);
    }

    public record Result(String answer, long promptTokens, long completionTokens, long totalTokens) {}
}