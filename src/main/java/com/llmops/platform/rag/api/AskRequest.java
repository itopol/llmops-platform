package com.llmops.platform.rag.api;

import jakarta.validation.constraints.NotBlank;

public record AskRequest(@NotBlank String question, Integer topK) {
}
