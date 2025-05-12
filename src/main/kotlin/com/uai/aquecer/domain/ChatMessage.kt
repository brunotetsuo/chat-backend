package com.uai.aquecer.domain

data class ChatMessage(
    val role: String, // "user" ou "assistant"
    val content: String
)