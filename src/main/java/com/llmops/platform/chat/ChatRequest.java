package com.llmops.platform.chat;

import jakarta.validation.constraints.NotBlank;

public record ChatRequest(@NotBlank String message) {
}
