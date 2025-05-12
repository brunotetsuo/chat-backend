package com.uai.aquecer.domain

data class ChatRequest(
    val sessionId: String,
    val message: String
)