package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TradingDao {
    // Session management
    @Query("SELECT * FROM socratic_sessions WHERE isActive = 1 ORDER BY timestamp DESC LIMIT 1")
    suspend fun getActiveSession(): SocraticSession?

    @Query("SELECT * FROM socratic_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<SocraticSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SocraticSession): Long

    @Update
    suspend fun updateSession(session: SocraticSession)

    @Query("UPDATE socratic_sessions SET isActive = 0 WHERE id = :sessionId")
    suspend fun deactivateSession(sessionId: Int)

    // Message management
    @Query("SELECT * FROM session_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: Int): Flow<List<SessionMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: SessionMessage)

    // Progress management
    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgress>>

    @Query("SELECT * FROM user_progress WHERE levelId = :levelId")
    suspend fun getProgressById(levelId: String): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgress)
}
