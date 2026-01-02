package com.llmops.platform.chat;

public record ChatResponseDto(
        String answer,
        String traceId,
        String spanId,
        Usage usage) {
    public record Usage(long promptTokens, long completionTokens, long totalTokens) {
    }
}