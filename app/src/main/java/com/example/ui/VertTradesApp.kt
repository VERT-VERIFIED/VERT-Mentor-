package com.example.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.data.SocraticSession
import com.example.data.SessionMessage
import com.example.ui.theme.*

@Composable
fun VertTradesApp(viewModel: TradingViewModel) {
    var currentTab by remember { mutableStateOf(TradingTab.TUTOR) }
    val context = LocalContext.current
    val statusText by viewModel.statusText.collectAsStateWithLifecycle()

    LaunchedEffect(statusText) {
        statusText?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearStatus()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MidnightCard,
                contentColor = LightText,
                tonalElevation = 8.dp,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                NavigationBarItem(
                    selected = currentTab == TradingTab.ACADEMY,
                    onClick = { currentTab = TradingTab.ACADEMY },
                    label = { Text("Academy", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.School, contentDescription = "Academy") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = EmeraldBull,
                        indicatorColor = EmeraldBull,
                        unselectedIconColor = LightText.copy(alpha = 0.6f),
                        unselectedTextColor = LightText.copy(alpha = 0.6f)
                    )
                )
                NavigationBarItem(
                    selected = currentTab == TradingTab.TUTOR,
                    onClick = { currentTab = TradingTab.TUTOR },
                    label = { Text("Socratic AI", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "Socratic Tutor") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = EmeraldBull,
                        indicatorColor = EmeraldBull,
                        unselectedIconColor = LightText.copy(alpha = 0.6f),
                        unselectedTextColor = LightText.copy(alpha = 0.6f)
                    )
                )
                NavigationBarItem(
                    selected = currentTab == TradingTab.SUPPORT,
                    onClick = { currentTab = TradingTab.SUPPORT },
                    label = { Text("VIP Hub", fontWeight = FontWeight.Bold, fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.SupportAgent, contentDescription = "VIP Partner Hub") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = EmeraldBull,
                        indicatorColor = EmeraldBull,
                        unselectedIconColor = LightText.copy(alpha = 0.6f),
                        unselectedTextColor = LightText.copy(alpha = 0.6f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBg)
                .padding(innerPadding)
        ) {
            // Elegant Sleek Sticky Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.sweepGradient(
                                    colors = listOf(EmeraldBull, SecondaryTeal, EmeraldBull)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "VERT TRADES LOGO",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "VERT TRADES",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = LightText,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "SOCRATIC TUTOR",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldBull,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = {
                            val infoUrl = "https://partner-tracking.deriv.com/click?a=3166&o=1&c=3&link_id=1"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(infoUrl))
                            context.startActivity(intent)
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = ObsidianBg)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Broker status link",
                            tint = EmeraldBull,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Screen Tab viewport
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (currentTab) {
                    TradingTab.ACADEMY -> AcademyScreen(viewModel) { currentTab = TradingTab.TUTOR }
                    TradingTab.TUTOR -> SocraticTutorScreen(viewModel)
                    TradingTab.SUPPORT -> VipSupportScreen()
                }
            }
        }
    }
}

enum class TradingTab {
    ACADEMY, TUTOR, SUPPORT
}

// ==================== 1. ACADEMY & STRATEGIES ====================
@Composable
fun AcademyScreen(viewModel: TradingViewModel, onNavigateToTutor: () -> Unit) {
    val progressList by viewModel.allProgress.collectAsStateWithLifecycle()
    val selectedStrategyId by viewModel.selectedStrategyId.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Big Brand Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(1.dp, SoftDivider)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "VERT TRADES ACADEMY",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldBull,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Active recall curriculum guiding you from a Beginner to Intermediate, all the way to a Professional institutional trader.",
                        fontSize = 14.sp,
                        color = LightText.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Active levels checklist
        item {
            Text(
                text = "My Progression Tracker",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldBull
            )
        }

        val curriculumModules = listOf(
            CurriculumItem("BEGINNER_CANDLES", "Beginner", "Candlestick Anatomy & Market Flow", "Learn bull vs bear pins, bodies, shadows, and how to identify dynamic trend flow."),
            CurriculumItem("BEGINNER_SR", "Beginner", "Support & Resistance Boundaries", "Find structural turning zones where commercial banks buy and retail traders place frantic orders."),
            CurriculumItem("INTERMEDIATE_PATTERNS", "Intermediate", "Retail Chart Pattern Traps", "Double tops, double bottoms, head & shoulders. Study why 90% of breakout pattern traders fail."),
            CurriculumItem("INTERMEDIATE_RISK", "Intermediate", "Professional Risk Strategy", "Draw down rules, standard R:R targets (minimum 1:2), and compounding accounts safely."),
            CurriculumItem("PRO_SMC_LIQ", "Professional", "SMC Liquidity Sweeps", "Order Blocks, Fair Value Gaps (FVG), Sellside and Buyside liquidity sweeps. Spot institutional footprints."),
            CurriculumItem("PRO_PSYCH", "Professional", "Zen Trading Psychology", "Emotions override math. Realize that winning trade charts look identical to losing trade charts before they complete.")
        )

        items(curriculumModules) { item ->
            val isCompleted = progressList.any { it.levelId == item.id && it.completed }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) MidnightCard.copy(alpha = 0.5f) else MidnightCard
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isCompleted) EmeraldBull.copy(alpha = 0.4f) else SoftDivider
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.tier.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (item.tier == "Professional") CharcoalGold else SecondaryTeal,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) LightText.copy(alpha = 0.6f) else LightText
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.desc,
                            fontSize = 12.sp,
                            color = LightText.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            modifier = Modifier.size(28.dp),
                            tint = EmeraldBull
                        )
                    } else {
                        Button(
                            onClick = { viewModel.completeCurriculumLevel(item.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = SoftDivider),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Complete", fontSize = 11.sp, color = EmeraldBull)
                        }
                    }
                }
            }
        }

        // Strategy Suite Selection
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Professional Trading Strategies",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldBull
            )
            Text(
                text = "Match your style and practice Socratic analysis of relevant failure patterns.",
                fontSize = 12.sp,
                color = LightText.copy(alpha = 0.6f)
            )
        }

        val tradingStrategiesList = listOf(
            StrategyItem(
                id = "SMC",
                title = "Smart Money Concept Sweeps",
                desc = "Institutions hunt liquidity. Spot equal highs, buy-stops pools, and enter purely after the sweep in key Fair Value Gaps. Best for patient, high R:R setups.",
                accent = CharcoalGold
            ),
            StrategyItem(
                id = "PRICE_ACTION",
                title = "Pure Price Action Rejection",
                desc = "Track momentum candles, massive volume pins, and support-resistance level flips. Avoid high-risk breakouts; buy the retest or look for pin rejection. Clean & simple.",
                accent = SecondaryTeal
            ),
            StrategyItem(
                id = "TREND_FLOW",
                title = "Dynamic Momentum Flow",
                desc = "Stay trend aligned using higher time-frames (4H/1D). Look for pulls back into the dynamic EMA crossover channel. High winning accuracy, smooth rides.",
                accent = EmeraldBull
            )
        )

        items(tradingStrategiesList) { strat ->
            val isSelected = selectedStrategyId == strat.id
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectStrategy(strat.id) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MidnightCard else MidnightCard.copy(alpha = 0.4f)
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = if (isSelected) EmeraldBull else SoftDivider
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = strat.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = strat.accent
                        )

                        if (isSelected) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = EmeraldBull.copy(alpha = 0.2f)),
                                border = BorderStroke(1.dp, EmeraldBull)
                            ) {
                                Text(
                                    "ACTIVE STYLE",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldBull,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = strat.desc,
                        fontSize = 13.sp,
                        color = LightText.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            viewModel.selectStrategy(strat.id)
                            onNavigateToTutor()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) EmeraldBull else SoftDivider
                        ),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = if (isSelected) "Practice This in AI Tutor" else "Select & Practice",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) ObsidianBg else LightText
                        )
                    }
                }
            }
        }
    }
}

data class CurriculumItem(val id: String, val tier: String, val title: String, val desc: String)
data class StrategyItem(val id: String, val title: String, val desc: String, val accent: Color)


// ==================== 2. SOCRATIC AI TUTOR SCREEN ====================
@Composable
fun SocraticTutorScreen(viewModel: TradingViewModel) {
    val activeSession by viewModel.activeSession.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val attachedUri by viewModel.attachedImageUri.collectAsStateWithLifecycle()

    if (activeSession == null) {
        // Welcome / Launcher setup
        SocraticTutorSetupScreen(
            viewModel = viewModel,
            attachedUri = attachedUri,
            isLoading = isLoading
        )
    } else {
        // Live chat / Interactive tutoring
        SocraticChatRoomScreen(
            viewModel = viewModel,
            session = activeSession!!,
            messages = messages,
            isLoading = isLoading
        )
    }
}

@Composable
fun SocraticTutorSetupScreen(
    viewModel: TradingViewModel,
    attachedUri: Uri?,
    isLoading: Boolean
) {
    val context = LocalContext.current
    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.attachImage(uri)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Socratic Chart Tutor",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = EmeraldBull
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Upload your losing trade, or select a classic trap below. I will lead you through step-by-step Socratic inquiry so you discover the solution yourself.",
                fontSize = 14.sp,
                color = LightText.copy(alpha = 0.7f)
            )
        }

        // Upload custom trade
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(1.dp, SoftDivider)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CUSTOM CHART ANALYSIS",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryTeal,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (attachedUri != null) {
                        // Display attached thumbnail
                        Image(
                            painter = rememberAsyncImagePainter(model = attachedUri),
                            contentDescription = "Attached Chart",
                            modifier = Modifier
                                .height(150.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { viewModel.attachImage(null) },
                                colors = ButtonDefaults.buttonColors(containerColor = CrimsonBear),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Remove", color = Color.White)
                            }
                            Button(
                                onClick = { viewModel.startSessionWithCustomUpload() },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldBull),
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = ObsidianBg, strokeWidth = 2.dp)
                                } else {
                                    Text("Analyze Setup!", color = ObsidianBg, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        IconButton(
                            onClick = { imageLauncher.launch("image/*") },
                            modifier = Modifier
                                .size(72.dp)
                                .background(SoftDivider, RoundedCornerShape(36.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = "Select Attachment",
                                tint = EmeraldBull,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Select Chart Screenshot",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightText
                        )
                        Text(
                            text = "Supports trading logs or live setups (JPEG/PNG)",
                            fontSize = 11.sp,
                            color = LightText.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // Preloaded scenarios
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Interactive Practice Scenarios",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldBull
            )
            Text(
                text = "Perfect for instant offline trial when you don't have personal setup charts active.",
                fontSize = 12.sp,
                color = LightText.copy(alpha = 0.6f)
            )
        }

        items(viewModel.practiceScenarios) { scenario ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(1.dp, SoftDivider)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = scenario.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldBull
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = scenario.brief,
                        fontSize = 13.sp,
                        color = LightText.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.startSessionWithScenario(scenario) },
                        colors = ButtonDefaults.buttonColors(containerColor = SoftDivider),
                        border = BorderStroke(1.dp, EmeraldBull),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Socratic Learning Session", color = EmeraldBull, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun SocraticChatRoomScreen(
    viewModel: TradingViewModel,
    session: SocraticSession,
    messages: List<SessionMessage>,
    isLoading: Boolean
) {
    var replyText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Automatically scroll to bottom as soon as messages expand or load
    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat Header with session details & step indicators
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightCard),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = session.chartName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldBull
                        )
                        Text(
                            text = "Level progression path: Step ${session.currentStep} of 5",
                            fontSize = 11.sp,
                            color = LightText.copy(alpha = 0.6f)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.exitActiveSession() },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = SoftDivider)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Exit active session", tint = CrimsonBear)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Progress Slider indicating Socratic steps completed
                LinearProgressIndicator(
                    progress = { session.currentStep / 5f },
                    modifier = Modifier.fillMaxWidth(),
                    color = EmeraldBull,
                    trackColor = SoftDivider
                )
            }
        }

        // Messages Box
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { message ->
                when (message.sender) {
                    "USER" -> UserChatBubble(message.text)
                    "VERT_TRADES" -> SocraticTutorBubble(message.text)
                    "EXPLANATION" -> ConceptExplanationBubble(message.text)
                }
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = EmeraldBull,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tutor VERT TRADES is writing...",
                            color = LightText.copy(alpha = 0.6f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Dynamic helper triggers row (Why did we do that? Hint bulb)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MidnightCard)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.askWhyDidWeDoThat() },
                colors = ButtonDefaults.buttonColors(containerColor = SoftDivider),
                border = BorderStroke(1.dp, EmeraldBull),
                modifier = Modifier.weight(1.1f),
                enabled = !isLoading,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.Help, contentDescription = "Why questions", modifier = Modifier.size(14.dp), tint = EmeraldBull)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Why did we do that?", fontSize = 11.sp, color = EmeraldBull, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.askHint() },
                colors = ButtonDefaults.buttonColors(containerColor = SoftDivider),
                modifier = Modifier.weight(0.9f),
                enabled = !isLoading,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Icon(Icons.Default.Lightbulb, contentDescription = "Request hint", modifier = Modifier.size(14.dp), tint = CharcoalGold)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Give me a Hint", fontSize = 11.sp, color = LightText, fontWeight = FontWeight.Bold)
            }
        }

        // Keyboard reply inputs row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MidnightCard)
                .windowInsetsPadding(WindowInsets.ime)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("What do you think is our next step?", color = LightText.copy(0.4f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldBull,
                    unfocusedBorderColor = SoftDivider,
                    focusedTextColor = LightText,
                    unfocusedTextColor = LightText
                ),
                maxLines = 4,
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = {
                    if (replyText.isNotBlank()) {
                        viewModel.submitAnswer(replyText)
                        replyText = ""
                        keyboardController?.hide()
                    }
                })
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (replyText.isNotBlank()) {
                        viewModel.submitAnswer(replyText)
                        replyText = ""
                        keyboardController?.hide()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = EmeraldBull),
                enabled = !isLoading && replyText.isNotBlank()
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send reply", tint = ObsidianBg)
            }
        }
    }
}

@Composable
fun UserChatBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            colors = CardDefaults.cardColors(containerColor = SoftDivider),
            shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "STUDENT",
                    fontSize = 9.sp,
                    color = SecondaryTeal,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = LightText
                )
            }
        }
    }
}

@Composable
fun SocraticTutorBubble(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = 300.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightCard),
            border = BorderStroke(1.dp, EmeraldBull.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(EmeraldBull, RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VERT TRADES (TUTOR)",
                        fontSize = 9.sp,
                        color = EmeraldBull,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = LightText
                )
            }
        }
    }
}

@Composable
fun ConceptExplanationBubble(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MidnightCard),
        border = BorderStroke(1.dp, CharcoalGold),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(60.dp)
                    .background(CharcoalGold, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = "Concept Breakdown", tint = CharcoalGold, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CONCEPT DEEP-DIVE (WHY DID WE DO THAT?)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalGold,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = text,
                    fontSize = 13.sp,
                    color = LightText.copy(alpha = 0.9f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}


// ==================== 3. BRONSON / MONETIZATION & SUPPORT ====================
@Composable
fun VipSupportScreen() {
    val context = LocalContext.current
    var liveChatInput by remember { mutableStateOf("") }
    val directPhoneNum = "+263784268437"

    // Custom simulated direct tickets
    val ticketLogs = remember { mutableStateListOf<Pair<String, Boolean>>() } // Message, isUser

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Deriv Promotion Module
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(2.dp, EmeraldBull)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "RECOMMENDED BROKER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SecondaryTeal,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Deriv Official VIP Partner Link",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Get access to exclusive synthetic indices (Crash 1000, Boom 500, Volatility 75), zero-spread accounts, instant local transfers, and up to 1:1000 leverage.",
                        fontSize = 13.sp,
                        color = LightText.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val derivUrl = "https://partner-tracking.deriv.com/click?a=3166&o=1&c=3&link_id=1"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(derivUrl))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldBull),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TrendingUp, contentDescription = "Trade Now", tint = ObsidianBg)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Create Account on Deriv (Click Here)",
                                fontWeight = FontWeight.Bold,
                                color = ObsidianBg,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }

        // Deposits / Funding Instructions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(1.dp, SoftDivider)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, contentDescription = "Account deposit", tint = CharcoalGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Safe & Instant Deriv Deposits",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalGold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To deposit into your newly registered Deriv account instantly, you can use Deriv P2P directly inside the portal, or contact the official VERT TRADES support line below for manual payment agent services.",
                        fontSize = 13.sp,
                        color = LightText.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SoftDivider),
                        border = BorderStroke(1.dp, SoftDivider)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PhoneInTalk, contentDescription = "Agent contact", tint = EmeraldBull)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    "P2P / WHATSAPP PAYMENT DESK",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = EmeraldBull
                                )
                                Text(
                                    text = "$directPhoneNum",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Direct VIP Human Communication Desk (VERT TRADES Client comms)
        item {
            Text(
                text = "Secure Direct Support (VERT TRADES)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldBull
            )
            Text(
                text = "Need 1-on-1 expert coaching, account assistance, or payment processing? Message VERT TRADES directly.",
                fontSize = 12.sp,
                color = LightText.copy(alpha = 0.6f)
            )
        }

        // Quick WhatsApp/Call triggers
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        val msg = Uri.encode("Hi VERT TRADES! I signed up under your Deriv broker link, and I would love personalized trading support.")
                        val wUrl = "https://api.whatsapp.com/send?phone=263784268437&text=$msg"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(wUrl)))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoftDivider),
                    border = BorderStroke(1.dp, EmeraldBull),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "WhatsApp", tint = EmeraldBull, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chat WhatsApp", color = EmeraldBull, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$directPhoneNum"))
                        context.startActivity(callIntent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoftDivider),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call direct desk", color = Color.White, fontSize = 11.sp)
                }
            }
        }

        // Live Chat Box Mockup mimicking client support desk communication
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MidnightCard),
                border = BorderStroke(1.dp, SoftDivider)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "VIP DECK CLIENT MESSENGER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryTeal,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (ticketLogs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(ObsidianBg, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Direct messaging channel to VERT TRADES.",
                                fontSize = 11.sp,
                                color = LightText.copy(0.4f)
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                                .background(ObsidianBg, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            ticketLogs.forEach { (text, isUserPost) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isUserPost) Arrangement.End else Arrangement.Start
                                ) {
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isUserPost) SoftDivider else MidnightCard
                                        ),
                                        modifier = Modifier.widthIn(max = 220.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = text,
                                            fontSize = 12.sp,
                                            color = LightText,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = liveChatInput,
                            onValueChange = { liveChatInput = it },
                            placeholder = { Text("Ask about signals, VIP...", color = LightText.copy(0.5f), fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldBull,
                                unfocusedBorderColor = SoftDivider,
                                focusedTextColor = LightText,
                                unfocusedTextColor = LightText
                            ),
                            maxLines = 1,
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (liveChatInput.isNotBlank()) {
                                    val taskText = liveChatInput
                                    ticketLogs.add(Pair(taskText, true))
                                    liveChatInput = ""

                                    // Simulate agent direct automatic prompt answer
                                    ticketLogs.add(Pair(
                                        "Message recorded! I have forwarded your secure client desk ticket to VERT TRADES (+263784268437). To receive immediate high priority responses on your signal access, please copy your message and contact me raw on WhatsApp!",
                                        false
                                    ))
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(containerColor = EmeraldBull),
                            enabled = liveChatInput.isNotBlank()
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Send", tint = ObsidianBg, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
