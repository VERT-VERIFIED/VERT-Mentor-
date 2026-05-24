package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiRetrofitClient
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream

class TradingViewModel(
    application: Application,
    private val repository: TradingRepository
) : AndroidViewModel(application) {

    // Main App States
    private val _activeSession = MutableStateFlow<SocraticSession?>(null)
    val activeSession: StateFlow<SocraticSession?> = _activeSession.asStateFlow()

    private val _messages = MutableStateFlow<List<SessionMessage>>(emptyList())
    val messages: StateFlow<List<SessionMessage>> = _messages.asStateFlow()

    private val _allProgress = MutableStateFlow<List<UserProgress>>(emptyList())
    val allProgress: StateFlow<List<UserProgress>> = _allProgress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Status or custom messages
    private val _statusText = MutableStateFlow<String?>(null)
    val statusText: StateFlow<String?> = _statusText.asStateFlow()

    // Active strategy chosen by the user in this run
    private val _selectedStrategyId = MutableStateFlow<String?>("SMC")
    val selectedStrategyId: StateFlow<String?> = _selectedStrategyId.asStateFlow()

    // Temporary variables for current image attachments during chat initiation
    private val _attachedImageBase64 = MutableStateFlow<String?>(null)
    val attachedImageBase64: StateFlow<String?> = _attachedImageBase64.asStateFlow()
    private val _attachedImageUri = MutableStateFlow<Uri?>(null)
    val attachedImageUri: StateFlow<Uri?> = _attachedImageUri.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load current progress
            repository.getAllProgress().collect {
                _allProgress.value = it
            }
        }

        viewModelScope.launch {
            // Check active session
            val session = repository.getActiveSession()
            if (session != null) {
                _activeSession.value = session
                observeMessages(session.id)
            }
        }
    }

    private fun observeMessages(sessionId: Int) {
        viewModelScope.launch {
            repository.getMessagesForSession(sessionId).collect {
                _messages.value = it
            }
        }
    }

    fun selectStrategy(strategyId: String) {
        _selectedStrategyId.value = strategyId
    }

    fun attachImage(uri: Uri?) {
        _attachedImageUri.value = uri
        if (uri == null) {
            _attachedImageBase64.value = null
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>().applicationContext
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
                val base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
                _attachedImageBase64.value = base64
            } catch (e: Exception) {
                _attachedImageBase64.value = null
                _statusText.value = "Failed to load custom image: ${e.localizedMessage}"
            }
        }
    }

    // Curated Practice Scenarios
    data class PracticeScenario(
        val id: String,
        val name: String,
        val brief: String,
        val fullSetup: String,
        val startQuestion: String,
        val systemInstructions: String
    )

    val practiceScenarios = listOf(
        PracticeScenario(
            id = "scenario_double_top",
            name = "Double Top Liquidity Trap",
            brief = "You entered Short right after a clean double top formed, but got stopped out before the price crashed.",
            fullSetup = "A clean Double Top appeared on GBPUSD at the 1.2500 psychological resistance block. I set my Stop Loss 15 pips above the highs and took a Short. Almost instantly, a quick wick surged upwards, grabbed my Stop Loss, and then the market tanked exactly as planned. Why was I trapped?",
            startQuestion = "Greetings, friend. I see the frustration here—it is a classic trap. Take a close look at the highs of that double top before the meltdown. What do retail traders' books tell them to do at a resistance double top, and where does that leave their stop losses?",
            systemInstructions = "You are VERT TRADES, a highly compassionate Socratic trading tutor. The student was trapped in a Double Top Liquidity Sweep. Guide them down a 3-4 step path to discover that: 1. Double tops attract massive pool of 'buy stop' liquidity. 2. Large institutions trigger buy stops to fill their huge short orders. 3. Entering after the sweep is safer. Ask questions step-by-step; never explain everything in one go."
        ),
        PracticeScenario(
            id = "scenario_support_fomo",
            name = "Support Breakout Trap",
            brief = "You entered a Short on a massive red breakout candle, but it instantly reversed as a pinbar.",
            fullSetup = "I saw EURUSD crashing down on the 1-hour chart, printing a huge red breakout candle breaking a support line. I placed a Sell Order at the very low of the candle to ride the momentum. The candle closed as a wick, and then zoomed in the opposite direction, blowing my account. What was my error?",
            startQuestion = "I appreciate your courage in sharing this trade. Breakouts are highly magnetic! Look at the candle's shape upon close. When a candle closes near support with a long shadow (wick) at the bottom, who has entered the arena, and is that high or low value for a short?",
            systemInstructions = "You are VERT TRADES, a highly compassionate Socratic trading tutor. The student fell into a Breakout FOMO Support Trap. Guide them step-by-step (3-4 steps) to realize that: 1. entering on the very low of a support breakout is buying when price is oversold, 2. the wick means buyers absorbed the supply, 3. they should look for a pull-back re-test of the broken level before taking any momentum trades. Ask questions; encourage active recall."
        ),
        PracticeScenario(
            id = "scenario_fvg_fail",
            name = "SMC Flipped Order Block FVG Run",
            brief = "Your Order Block Buy limit was run over completely during a higher timeframe bearish slide.",
            fullSetup = "I found a beautiful Order Block (OB) with an intact Fair Value Gap (FVG) on the 15m chart. I set a Buy limit there expecting a strong bounce. Instead, the market sliced clean through my OB without even stopping. It felt like standard support was ignored. What went wrong?",
            startQuestion = "Ah, the promise of Smart Money Concepts! They can be beautiful, but the context is king. Let's look beyond the 15-minute OB. What was the higher timeframe (HTF) trend doing when price ran down, and what happens to a local bullish OB when HTF bearish momentum is in power?",
            systemInstructions = "You are VERT TRADES, a wise and warm Socratic trading tutor. The student's bullish Order Block got ran over. Guide them step-by-step (3-4 steps) to learn that: 1. Local Order Blocks fail when fighting the Higher Time Frame trend, 2. an order block is just a high probability zone, not a magic support level, 3. checking clean trend alignment is a professional trader filter. Hold high Socratic standards."
        )
    )

    fun startSessionWithScenario(scenario: PracticeScenario) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Create session in Database
                val session = repository.createSession(
                    chartName = scenario.name,
                    chartImageUri = scenario.id,
                    currentStrategyContext = scenario.systemInstructions
                )
                _activeSession.value = session

                // Seed first message (Tutor Welcome Question)
                repository.sendMessage(session.id, "VERT_TRADES", scenario.startQuestion)

                observeMessages(session.id)
            } catch (e: Exception) {
                _statusText.value = "Failed to start scenario: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startSessionWithCustomUpload() {
        val imageBase64 = _attachedImageBase64.value
        val imageUri = _attachedImageUri.value

        if (imageBase64 == null) {
            _statusText.value = "Please select or capture a chart image to begin custom analysis!"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val chartTitle = "My Custom Chart Setup"
                val systemPrompt = "You are VERT TRADES, a patient, compassionate Socratic trading tutor. The student is showing you a custom chart image of their trade with explanation. Do not give them direct answers. First, ask a gentle question pointing to the most critical area of their chart (such as support/resistance, trendlines, or specific candlestick structures). Do not give full explanations immediately, keep it Socratic."

                val session = repository.createSession(
                    chartName = chartTitle,
                    chartImageUri = imageUri?.toString(),
                    currentStrategyContext = systemPrompt
                )
                _activeSession.value = session

                // Initial prompt from USER that passes the custom image
                val introText = "Here is my custom chart. Please tutor me through finding my mistake and help me learn."
                repository.sendMessage(session.id, "USER", "Uploaded Trade Chart")

                // Fetch AI's first response using the attached image
                val aiFirstResponse = GeminiRetrofitClient.askSocraticTutor(
                    systemInstruction = systemPrompt,
                    history = emptyList(),
                    latestPrompt = introText,
                    imageBase64 = imageBase64,
                    imageMime = "image/jpeg"
                )

                // Save first tutor message
                repository.sendMessage(session.id, "VERT_TRADES", aiFirstResponse)

                // Clean up attachments
                _attachedImageBase64.value = null
                _attachedImageUri.value = null

                observeMessages(session.id)
            } catch (e: Exception) {
                _statusText.value = "Failed to launch analysis: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(text: String) {
        val session = _activeSession.value ?: return
        if (text.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Save user's answer
                repository.sendMessage(session.id, "USER", text)

                // Build context history from Database
                val history = _messages.value.map { Pair(it.sender, it.text) }

                // Ask the AI tutor for the next step feedback
                val systemInstruction = session.currentStrategyContext ?: "You are VERT TRADES, a compassionate Socratic trading tutor. Guide the user step-by-step."
                val nextTutorResponse = GeminiRetrofitClient.askSocraticTutor(
                    systemInstruction = systemInstruction,
                    history = history,
                    latestPrompt = text
                )

                // Check or increment session step locally to track visual progress in UI
                val matchesStep = Regex("step\\s*([1-5])", RegexOption.IGNORE_CASE).find(nextTutorResponse)
                if (matchesStep != null) {
                    val nextStepNumber = matchesStep.groupValues[1].toInt()
                    repository.updateSessionStep(session.id, nextStepNumber)
                    _activeSession.value = _activeSession.value?.copy(currentStep = nextStepNumber)
                } else if (session.currentStep < 5) {
                    val nextStepNumber = session.currentStep + 1
                    repository.updateSessionStep(session.id, nextStepNumber)
                    _activeSession.value = _activeSession.value?.copy(currentStep = nextStepNumber)
                }

                // Save next tutor message
                repository.sendMessage(session.id, "VERT_TRADES", nextTutorResponse)
            } catch (e: Exception) {
                _statusText.value = "Communication error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun askWhyDidWeDoThat() {
        val session = _activeSession.value ?: return
        val currentHistory = _messages.value

        if (currentHistory.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val whyPrompt = "Why did we do that? Please explain specifically the core concept underlying the tutor's latest question or instruction in a clear, deep-dive way without giving away the final setup answers."

                // Map history
                val historyMapped = currentHistory.map { Pair(it.sender, it.text) }

                val systemInstruction = """
                    You are VERT TRADES, the wise and patient Socratic trading tutor. 
                    The user has asked "Why did we do that?" or requested an in-depth breakdown of the latest trading concept under discussion. 
                    Provide a beautifully articulated, simple, and detailed conceptual explanation (e.g. explain what Liquidity sweeps, equal highs, wicks, breakout traps or order flow are) so they gain deep intuition. 
                    Ensure it feels like sitting down with a patient teacher, not a robot. 
                    Be encouraging, and promote true active recall at the end by reminding them what concept they should keep in mind.
                """.trimIndent()

                val response = GeminiRetrofitClient.askSocraticTutor(
                    systemInstruction = systemInstruction,
                    history = historyMapped,
                    latestPrompt = whyPrompt
                )

                repository.sendMessage(session.id, "EXPLANATION", response)
            } catch (e: Exception) {
                _statusText.value = "Failed to fetch concept explanation: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun askHint() {
        val session = _activeSession.value ?: return
        val currentHistory = _messages.value

        if (currentHistory.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val hintPrompt = "Please give me a small hint for this step without telling me the full answer."

                val historyMapped = currentHistory.map { Pair(it.sender, it.text) }

                val systemInstruction = """
                    You are VERT TRADES, the patient Socratic trading tutor. 
                    The student requested a hint on the current step. 
                    Give them a subtle, highly encouraging clue to guide their thoughts in 1-2 friendly sentences. 
                    Do not solve the problem or reveal the direct visual key, just light the path for active recall.
                """.trimIndent()

                val response = GeminiRetrofitClient.askSocraticTutor(
                    systemInstruction = systemInstruction,
                    history = historyMapped,
                    latestPrompt = hintPrompt
                )

                // We append it as VERT_TRADES or as a distinct custom sender. Let's make it a nice tutor tip!
                repository.sendMessage(session.id, "VERT_TRADES", "💡 Tutor Tip: $response")
            } catch (e: Exception) {
                _statusText.value = "Failed to fetch hint: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun exitActiveSession() {
        val session = _activeSession.value ?: return
        viewModelScope.launch {
            repository.endSession(session.id)
            _activeSession.value = null
            _messages.value = emptyList()
        }
    }

    fun completeCurriculumLevel(levelId: String) {
        viewModelScope.launch {
            repository.markCompleted(levelId)
            _statusText.value = "Congratulations! You completed this block of your Professional Trading Path!"
        }
    }

    fun clearStatus() {
        _statusText.value = null
    }
}

// ViewModel Factory
class TradingViewModelFactory(
    private val application: Application,
    private val repository: TradingRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TradingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TradingViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
