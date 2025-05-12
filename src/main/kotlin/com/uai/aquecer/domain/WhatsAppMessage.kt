package com.uai.aquecer.domain

data class WhatsAppMessage(
    val messaging_product: String = "whatsapp",
    val to: String,
    val type: String = "text",
    val text: TextContent
)

data class TextContent(
    val body: String
)