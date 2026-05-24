package com.example.api

import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiRetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    // Helper method to send a Socratic request
    suspend fun askSocraticTutor(
        systemInstruction: String,
        history: List<Pair<String, String>>, // sender to text
        latestPrompt: String,
        imageBase64: String? = null,
        imageMime: String? = null
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return "Demo Mode: Socratic Tutor is ready. Please configure your GEMINI_API_KEY in the AI Studio environment secrets panel for live AI dialog!"
        }

        // Build Gemini Request contents array from history and latest prompt
        val contentsList = mutableListOf<Content>()

        // 1. Map history
        for ((sender, text) in history) {
            val role = if (sender == "USER") "user" else "model"
            // Wait, in GeminiAPI, standard contents require content objects which might not strictly support arbitrary roles or structure, but simpler works best or just formatting everything as user/model. Let's do formatting or map Roles.
            // Wait! The REST API supports simple structure:
            // contents: [ { parts: [ { text: "message" } ] } ]
            // We can just pass the entire conversation history as a formatted block or separate turns. Let's pass separate turns of content, where parts are either text or image.
            contentsList.add(
                Content(parts = listOf(Part(text = text)))
            )
        }

        // 2. Add latest prompt (including image if exists)
        val latestParts = mutableListOf<Part>()
        if (imageBase64 != null && imageMime != null) {
            latestParts.add(Part(inlineData = InlineData(mimeType = imageMime, data = imageBase64)))
        }
        latestParts.add(Part(text = latestPrompt))
        contentsList.add(Content(parts = latestParts))

        val request = GeminiRequest(
            contents = contentsList,
            systemInstruction = Content(parts = listOf(Part(text = systemInstruction))),
            generationConfig = GenerationConfig(temperature = 0.5f)
        )

        return try {
            val response = service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "I apologize, but I struggled to inspect this setup. Let's try to look closely at the candlesticks again. What trends do you see?"
        } catch (e: Exception) {
            "I could not connect to my AI core (${e.localizedMessage}). Let's work together manually! Tell me, what was your entry trigger for this trade?"
        }
    }
}
