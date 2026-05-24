package com.example.data

import kotlinx.coroutines.flow.Flow

class TradingRepository(private val tradingDao: TradingDao) {

    fun getAllSessions(): Flow<List<SocraticSession>> = tradingDao.getAllSessions()

    suspend fun getActiveSession(): SocraticSession? = tradingDao.getActiveSession()

    suspend fun createSession(chartName: String, chartImageUri: String?, currentStrategyContext: String? = null): SocraticSession {
        // Deactivate old sessions first
        val active = tradingDao.getActiveSession()
        if (active != null) {
            tradingDao.deactivateSession(active.id)
        }
        val newSession = SocraticSession(
            chartName = chartName,
            chartImageUri = chartImageUri,
            currentStep = 1,
            isActive = true,
            currentStrategyContext = currentStrategyContext
        )
        val id = tradingDao.insertSession(newSession)
        return newSession.copy(id = id.toInt())
    }

    suspend fun updateSessionStep(sessionId: Int, step: Int) {
        val s = tradingDao.getAllSessions() // we can fetch check
        // We'll update step directly
        val active = tradingDao.getActiveSession()
        if (active != null && active.id == sessionId) {
            tradingDao.updateSession(active.copy(currentStep = step))
        }
    }

    suspend fun endSession(sessionId: Int) {
        tradingDao.deactivateSession(sessionId)
    }

    fun getMessagesForSession(sessionId: Int): Flow<List<SessionMessage>> =
        tradingDao.getMessagesForSession(sessionId)

    suspend fun sendMessage(sessionId: Int, sender: String, text: String) {
        tradingDao.insertMessage(
            SessionMessage(
                sessionId = sessionId,
                sender = sender,
                text = text
            )
        )
    }

    fun getAllProgress(): Flow<List<UserProgress>> = tradingDao.getAllProgress()

    suspend fun markCompleted(levelId: String, score: Int = 100) {
        tradingDao.insertProgress(
            UserProgress(
                levelId = levelId,
                completed = true,
                score = score
            )
        )
    }
}
