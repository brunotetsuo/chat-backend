package com.uai.aquecer.controller

import com.uai.aquecer.domain.ChatRequest
import com.uai.aquecer.service.ChatService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = ["*"]) // Permite que o frontend acesse esse endpoint
class ChatController(private val chatService: ChatService)
{

        @PostMapping
        fun chat(@RequestBody request: ChatRequest): String {
            return chatService.processMessage(request.sessionId, request.message)
        }
}

