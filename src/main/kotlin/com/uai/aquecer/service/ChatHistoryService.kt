package com.uai.aquecer.service

import com.uai.aquecer.domain.ChatMessage
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatHistoryService {
    private val historyMap = ConcurrentHashMap<String, MutableList<ChatMessage>>()

    fun addMessage(userId: String, message: ChatMessage) {
        val history = historyMap.getOrPut(userId) { mutableListOf() }
        history.add(message)
    }

    fun getHistory(userId: String): List<ChatMessage> {
        return historyMap[userId] ?: emptyList()
    }

    fun clearHistory(userId: String) {
        historyMap.remove(userId)
    }
}