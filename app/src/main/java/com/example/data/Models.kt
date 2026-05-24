package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "socratic_sessions")
data class SocraticSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chartName: String,
    val chartImageUri: String? = null,
    val currentStep: Int = 1,
    val isActive: Boolean = true,
    val currentStrategyContext: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_messages")
data class SessionMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionId: Int,
    val sender: String, // "VERT_TRADES", "USER", or "EXPLANATION"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey val levelId: String, // e.g. "LEVEL_BEGINNER", "STRATEGY_SMC"
    val score: Int = 0,
    val completed: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
