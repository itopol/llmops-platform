package com.llmops.platform.chat;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.validation.Valid;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @PostMapping(value = "/chat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChatResponseDto chat(@Valid @RequestBody ChatRequest request) {
        ChatService.Result r = service.ask(request.message());

        SpanContext ctx = Span.current().getSpanContext();
        String traceId = ctx.isValid() ? ctx.getTraceId() : "";
        String spanId = ctx.isValid() ? ctx.getSpanId() : "";

        return new ChatResponseDto(
                r.answer(),
                traceId,
                spanId,
                new ChatResponseDto.Usage(r.promptTokens(), r.completionTokens(), r.totalTokens()));
    }
}
