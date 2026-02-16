package com.uai.aquecer.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

@Service
class ChatService {

    private val openRouterUrl = "https://openrouter.ai/api/v1/chat/completions"
    private val apiKey = ""
    private val referer = "https://seu-dominio.com"
    private val systemPrompt = """
      Você é o Zé da roça, um mineiro bruto, ignorante, sem muita paciência, direto ao ponto, que fala com sotaque forte de Minas Gerais. 
      Não gosta de perguntas repetidas, odeia frescura e sempre fala o que pensa. 
      Responda sempre como se estivesse num boteco em Minas, e se irrita fácil com pergunta besta.
      Use expressões como "uai", "cê tá de brincadeira", "num enche", "sô", "cabra safado", entre outras.
      Se perguntarem quem criou, fez, quem é o criador do sistema, do chat ou de vc fale que foi o mininu baum Bruno Tetsuo.
    """.trimIndent()

    private val conversations = mutableMapOf<String, MutableList<Map<String, String>>>()

    private val counterFile = File("counter.txt")
    private val responseCounter: AtomicInteger = loadCounter()

    private val restTemplate = RestTemplate()

    private fun loadCounter(): AtomicInteger {
        return if (counterFile.exists()) {
            AtomicInteger(counterFile.readText().toIntOrNull() ?: 0)
        } else {
            AtomicInteger(0)
        }
    }

    private fun saveCounter(value: Int) {
        counterFile.writeText(value.toString())
    }

    fun processMessage(sessionId: String, message: String): String {
        val history = conversations.computeIfAbsent(sessionId) {
            mutableListOf(mapOf("role" to "system", "content" to systemPrompt))
        }

        history.add(mapOf("role" to "user", "content" to message))

        val payload = mapOf(
            "model" to "openai/gpt-3.5-turbo",
            "messages" to history
        )

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            set("Authorization", apiKey)
            set("HTTP-Referer", referer)
        }

        val entity = HttpEntity(payload, headers)
        val response = restTemplate.exchange(openRouterUrl, HttpMethod.POST, entity, String::class.java)

        val mapper = jacksonObjectMapper()
        val content = mapper.readTree(response.body)
            .path("choices").get(0).path("message").path("content").asText()

        history.add(mapOf("role" to "assistant", "content" to content))

        val totalResponses = responseCounter.incrementAndGet()
        saveCounter(totalResponses)

        println("🧠 [$sessionId] $message")
        println("✅ Resposta recebida com sucesso: $content")
        println("📊 Total de mensagens respondidas: $totalResponses")

        return content
    }
}
