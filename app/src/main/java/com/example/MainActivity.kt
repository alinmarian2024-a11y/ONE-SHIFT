package com.example
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.stringResource
import com.example.R
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import android.view.HapticFeedbackConstants
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.ui.zIndex
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.launch
import com.example.ui.theme.MyApplicationTheme
import kotlin.math.abs
import kotlin.random.Random

enum class PieceType(val color: Color, val symbol: String) {
    CYAN(Color(0xFF00E5FF), "●"),
    VIOLET(Color(0xFFD500F9), "◆"),
    CORAL(Color(0xFFFF3D00), "■"),
    LIME(Color(0xFFAEEA00), "▲")
}

data class LevelConfig(val size: Int, val moves: Int)
data class Move(val isRow: Boolean, val index: Int, val direction: Int)
data class PuzzleData(
    val targetBoard: List<List<PieceType>>,
    val initialPlayerBoard: List<List<PieceType>>,
    val solutionMoves: List<Move>,
    val config: LevelConfig
)

fun getLevelConfig(level: Int, random: kotlin.random.Random = kotlin.random.Random.Default): LevelConfig {
    return when (level) {
        1 -> LevelConfig(2, 1)
        in 2..5 -> LevelConfig(2, 1)
        in 6..10 -> LevelConfig(2, 2)
        in 11..15 -> LevelConfig(3, 1)
        in 16..25 -> LevelConfig(3, random.nextInt(2, 4))
        in 26..40 -> LevelConfig(3, random.nextInt(3, 5))
        in 41..50 -> LevelConfig(4, random.nextInt(1, 4))
        else -> LevelConfig(4, random.nextInt(3, 7))
    }
}

fun shiftRow(board: List<List<PieceType>>, rowIndex: Int, direction: Int): List<List<PieceType>> {
    val newBoard = board.map { it.toMutableList() }.toMutableList()
    val row = newBoard[rowIndex]
    if (direction > 0) { // right
        val last = row.removeAt(row.size - 1)
        row.add(0, last)
    } else { // left
        val first = row.removeAt(0)
        row.add(first)
    }
    return newBoard
}

fun shiftCol(board: List<List<PieceType>>, colIndex: Int, direction: Int): List<List<PieceType>> {
    val newBoard = board.map { it.toMutableList() }.toMutableList()
    val col = (0 until newBoard.size).map { newBoard[it][colIndex] }.toMutableList()
    if (direction > 0) { // down
        val last = col.removeAt(col.size - 1)
        col.add(0, last)
    } else { // up
        val first = col.removeAt(0)
        col.add(first)
    }
    for (i in 0 until newBoard.size) {
        newBoard[i][colIndex] = col[i]
    }
    return newBoard
}

fun updatePath(move: Move, currentPath: List<Move>, setPath: (List<Move>) -> Unit) {
    if (currentPath.isNotEmpty() && move == currentPath.first()) {
        setPath(currentPath.drop(1))
    } else {
        val inverse = move.copy(direction = -move.direction)
        val newPath = mutableListOf(inverse)
        newPath.addAll(currentPath)
        val stack = mutableListOf<Move>()
        for (m in newPath) {
            if (stack.isNotEmpty()) {
                val last = stack.last()
                if (last.isRow == m.isRow && last.index == m.index && last.direction == -m.direction) {
                    stack.removeLast()
                    continue
                }
            }
            stack.add(m)
        }
        setPath(stack)
    }
}

fun generatePuzzle(level: Int, seed: Long? = null): PuzzleData {
    val random = if (seed != null) kotlin.random.Random(seed) else kotlin.random.Random.Default
    val config = getLevelConfig(level, random)
    var targetBoard: List<List<PieceType>>
    var playerBoard: List<List<PieceType>>
    var solutionMoves: List<Move>
    do {
        targetBoard = (0 until config.size).map {
            (0 until config.size).map { PieceType.entries.random(random) }
        }
        val moves = mutableListOf<Move>()
        var currentBoard = targetBoard
        var lastMove: Move? = null
        for (i in 0 until config.moves) {
            var move: Move
            do {
                val isRow = random.nextBoolean()
                val index = random.nextInt(config.size)
                val dir = if (random.nextBoolean()) 1 else -1
                move = Move(isRow, index, dir)
            } while (lastMove != null && lastMove.isRow == move.isRow && lastMove.index == move.index && lastMove.direction == -move.direction)
            moves.add(move)
            currentBoard = if (move.isRow) shiftRow(currentBoard, move.index, move.direction) else shiftCol(currentBoard, move.index, move.direction)
            lastMove = move
        }
        playerBoard = currentBoard
        solutionMoves = moves.reversed().map { it.copy(direction = -it.direction) }
    } while (playerBoard == targetBoard)
    return PuzzleData(targetBoard, playerBoard, solutionMoves, config)
}


enum class ScreenState { MENU, LEVEL_SELECT, GAME, ABOUT }

fun getLevelStars(prefs: android.content.SharedPreferences, level: Int): Int = prefs.getInt("level_${level}_stars", 0)
fun getLevelMinMoves(prefs: android.content.SharedPreferences, level: Int): Int = prefs.getInt("level_${level}_moves", -1)

fun saveLevelResult(prefs: android.content.SharedPreferences, level: Int, stars: Int, moves: Int) {
    val editor = prefs.edit()
    val maxUnlocked = prefs.getInt("max_unlocked_level", 1)
    if (level + 1 > maxUnlocked) editor.putInt("max_unlocked_level", level + 1)
    
    val currentStars = getLevelStars(prefs, level)
    val currentMoves = getLevelMinMoves(prefs, level)
    
    if (stars > currentStars || (stars == currentStars && (currentMoves == -1 || moves < currentMoves))) {
        editor.putInt("level_${level}_stars", stars)
        editor.putInt("level_${level}_moves", moves)
    }
    editor.apply()
}

fun calculateStars(movesUsed: Int, recommendedMoves: Int): Int {
    val extraMoves = movesUsed - recommendedMoves
    return when {
        extraMoves <= 0 -> 5
        extraMoves in 1..3 -> 4
        extraMoves in 4..7 -> 3
        extraMoves in 8..14 -> 2
        else -> 1
    }
}

@Composable
fun getStarMessage(stars: Int): String = when(stars) {
    5 -> stringResource(R.string.star_msg_5)
    4 -> stringResource(R.string.star_msg_4)
    3 -> stringResource(R.string.star_msg_3)
    2 -> stringResource(R.string.star_msg_2)
    else -> stringResource(R.string.star_msg_1)
}

@Composable
fun MainMenuScreen(
    modifier: Modifier = Modifier, 
    onContinue: () -> Unit, 
    onNewGame: () -> Unit,
    onSelectLevel: () -> Unit, 
    onDailyShift: () -> Unit,
    onRemoveAds: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onExitApp: () -> Unit,
    isAdFree: Boolean,
    hasCampaign: Boolean,
    appLanguage: String,
    onLanguageChanged: (String) -> Unit
) {
    var showNewGameDialog by remember { mutableStateOf(false) }
    var showExitAppDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitAppDialog = true
    }

    if (showExitAppDialog) {
        AlertDialog(
            onDismissRequest = { showExitAppDialog = false },
            title = { Text(stringResource(R.string.exit_game_title), color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.exit_game_text), color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = { 
                    showExitAppDialog = false
                    onExitApp()
                }) { Text(stringResource(R.string.yes_close), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showExitAppDialog = false }) { Text(stringResource(R.string.stay_in_game), color = Color.Gray) }
            },
            containerColor = Color(0xFF2B2930)
        )
    }

    if (showNewGameDialog) {
        AlertDialog(
            onDismissRequest = { showNewGameDialog = false },
            title = { Text(stringResource(R.string.new_game_title), color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.new_game_text), color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = { 
                    showNewGameDialog = false
                    onNewGame()
                }) { Text(stringResource(R.string.start_new_game), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showNewGameDialog = false }) { Text(stringResource(R.string.cancel), color = Color.Gray) }
            },
            containerColor = Color(0xFF2B2930)
        )
    }

    Box(modifier = modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF020617))))) {
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            TextButton(onClick = { expanded = true }) {
                Text(if (appLanguage == "ro") "🌐 RO" else "🌐 EN", color = Color.White)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("English") },
                    onClick = { 
                        expanded = false
                        onLanguageChanged("en") 
                    }
                )
                DropdownMenuItem(
                    text = { Text("Română") },
                    onClick = { 
                        expanded = false
                        onLanguageChanged("ro") 
                    }
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("ONE SHIFT", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Black, letterSpacing = (-2).sp)
            Box(modifier = Modifier.height(6.dp).width(80.dp).background(Color(0xFFD0BCFF)).padding(bottom = 64.dp))
            Spacer(modifier = Modifier.height(48.dp))
        
        Button(onClick = onDailyShift, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)), modifier = Modifier.fillMaxWidth().height(64.dp)) {
            Text(stringResource(R.string.daily_shift), color = Color(0xFF0F172A), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (hasCampaign) {
            Button(onClick = onContinue, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)), modifier = Modifier.fillMaxWidth().height(64.dp)) {
                Text(stringResource(R.string.continue_game), color = Color(0xFF0F172A), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Button(
            onClick = { 
                if (hasCampaign) {
                    showNewGameDialog = true 
                } else {
                    onNewGame()
                }
            }, 
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), 
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.new_game), color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSelectLevel, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(stringResource(R.string.select_level), color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isAdFree) {
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F), contentColor = Color(0xFF38E887)), modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color(0xFF38E887), RoundedCornerShape(50))) {
                Text(stringResource(R.string.ads_removed_check), fontSize = 16.sp)
            }
        } else {
            Button(onClick = onRemoveAds, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color(0xFF00BCD4), RoundedCornerShape(50))) {
                Text(stringResource(R.string.remove_ads), color = Color.White, fontSize = 16.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onSettings, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), modifier = Modifier.weight(1f).height(56.dp)) {
                Text(stringResource(R.string.settings_short), color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onAbout, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), modifier = Modifier.weight(1f).height(56.dp)) {
                Text(stringResource(R.string.about_game), color = Color.White, fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { showExitAppDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F), contentColor = Color(0xFFFF7F50)),
            modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color(0xFFFF7F50), RoundedCornerShape(50))
        ) {
            Text(stringResource(R.string.exit_game), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
}

@Composable
fun LevelSelectScreen(modifier: Modifier = Modifier, prefs: android.content.SharedPreferences, onLevelClick: (Int) -> Unit, onBack: () -> Unit) {
    val maxUnlocked = prefs.getInt("max_unlocked_level", 1)
    
    val initialChapter = remember { 
        gameChapters.firstOrNull { maxUnlocked in it.startLevel..it.endLevel } ?: gameChapters.last()
    }
    var selectedChapter by remember { mutableStateOf(initialChapter) }

    Column(modifier = modifier.fillMaxSize().background(selectedChapter.backgroundBrush)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back), color = selectedChapter.accentColor)
            }
            Text(stringResource(R.string.select_level_title), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        }
        
        ScrollableTabRow(
            selectedTabIndex = gameChapters.indexOf(selectedChapter),
            containerColor = Color.Transparent,
            contentColor = selectedChapter.accentColor,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                val index = gameChapters.indexOf(selectedChapter)
                if (index in tabPositions.indices) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[index]),
                        color = selectedChapter.accentColor
                    )
                }
            },
            divider = { }
        ) {
            gameChapters.forEach { chapter ->
                Tab(
                    selected = selectedChapter == chapter,
                    onClick = { selectedChapter = chapter },
                    text = { 
                        Text(
                            text = stringResource(chapter.nameResId).uppercase(), 
                            color = if (selectedChapter == chapter) selectedChapter.accentColor else Color.White.copy(alpha = 0.5f),
                            fontWeight = if (selectedChapter == chapter) FontWeight.Bold else FontWeight.Normal
                        ) 
                    }
                )
            }
        }
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val levelCount = selectedChapter.endLevel - selectedChapter.startLevel + 1
            items(levelCount) { index ->
                val level = selectedChapter.startLevel + index
                val isUnlocked = level <= maxUnlocked
                val stars = getLevelStars(prefs, level)
                
                Card(
                    modifier = Modifier.aspectRatio(1f).clickable(enabled = isUnlocked) { onLevelClick(level) },
                    colors = CardDefaults.cardColors(containerColor = if (isUnlocked) Color(0xFF334155) else Color(0xFF2B2930)),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 0.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (isUnlocked) {
                            Text(level.toString(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "★".repeat(stars) + "☆".repeat(5 - stars),
                                color = Color(0xFFFFD700),
                                fontSize = 10.sp
                            )
                        } else {
                            Text("🔒", fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    isAdFree: Boolean,
    onRemoveAds: () -> Unit,
    onRestorePurchases: () -> Unit,
    adManager: AdManager,
    rewardedHintAdProvider: RewardedHintAdProvider
) {
    val context = LocalContext.current
    val audioManager = remember { GameAudioManager.getInstance(context) }
    
    val musicVolume by audioManager.musicVolume.collectAsState()
    val sfxVolume by audioManager.sfxVolume.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings_title), color = Color.White, fontWeight = FontWeight.Bold) },
        text = { 
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.audio), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(stringResource(R.string.music_volume, (musicVolume * 100).toInt()), color = Color.White, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (musicVolume <= 0f) Icons.Filled.MusicOff else Icons.Filled.MusicNote, 
                        contentDescription = stringResource(R.string.desc_music), 
                        tint = Color(0xFFCAC4D0),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = musicVolume,
                        onValueChange = { audioManager.setMusicVolume(it) },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(stringResource(R.string.sfx_volume, (sfxVolume * 100).toInt()), color = Color.White, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (sfxVolume <= 0f) Icons.Filled.VolumeOff else Icons.Filled.VolumeUp, 
                        contentDescription = stringResource(R.string.desc_sfx), 
                        tint = Color(0xFFCAC4D0),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = sfxVolume,
                        onValueChange = { audioManager.setSfxVolume(it) },
                        onValueChangeFinished = { audioManager.playTestSfx() },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text(stringResource(R.string.purchases), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isAdFree) {
                    Text(stringResource(R.string.ads_removed), color = Color(0xFF38E887), fontSize = 14.sp)
                } else {
                    Button(onClick = onRemoveAds, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth().height(48.dp)) {
                        Text(stringResource(R.string.remove_ads), color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = onRestorePurchases, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(R.string.restore_purchase), color = Color(0xFFCAC4D0))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                audioManager.saveSettings()
                onDismiss()
            }) { Text(stringResource(R.string.save_and_back), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
        },
        dismissButton = {
            TextButton(onClick = { audioManager.restoreDefaults() }) { Text(stringResource(R.string.restore_defaults), color = Color.Gray) }
        },
        containerColor = Color(0xFF2B2930)
    )
}

@Composable
fun AboutScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF020617)))).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back), color = Color(0xFFD0BCFF))
            }
            Text(stringResource(R.string.about_title), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        }
        
        Text("ONE SHIFT", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
        Text(stringResource(R.string.about_desc), color = Color(0xFFCAC4D0), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 16.dp))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text(stringResource(R.string.how_to_play), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.how_to_play_desc), color = Color.White, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.objective), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.objective_desc), color = Color.White, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.master_each_level), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.master_each_level_desc), color = Color.White, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(stringResource(R.string.version_1_0), color = Color.Gray, fontSize = 12.sp)
        Text(stringResource(R.string.made_in_romania), color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(stringResource(R.string.back_to_menu), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResultBox(label: String, value: String, valueColor: Color) {
    Column(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, color = valueColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
    }
}

data class ConfettiParticle(val vx: Float, val vy: Float, val size: Float, val color: Color, val rotSpeed: Float)

@Composable
fun CompletionDialog(
    level: Int,
    movesUsed: Int,
    recommendedMoves: Int,
    bestMoves: Int,
    perfectStreak: Int,
    accentColor: Color = Color(0xFFD0BCFF),
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onSettings: () -> Unit,
    isDailyShift: Boolean = false
) {
    val stars = calculateStars(movesUsed, recommendedMoves)
    var animatedStarCount by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showDialog = true
        for (i in 1..5) {
            delay(150)
            if (i <= stars) animatedStarCount = i
        }
    }

    val cardScale by animateFloatAsState(
        targetValue = if (showDialog) 1f else 0.5f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "card_scale"
    )
    
    val cardAlpha by animateFloatAsState(
        targetValue = if (showDialog) 1f else 0f,
        animationSpec = tween(400),
        label = "card_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        val confettiProgress = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            confettiProgress.animateTo(1f, tween(2500, easing = LinearOutSlowInEasing))
        }
        val particles = remember {
            List(50) {
                val angle = Random.nextFloat() * 2 * Math.PI
                val speed = Random.nextFloat() * 400f + 100f
                val size = Random.nextFloat() * 15f + 10f
                val color = listOf(accentColor, Color(0xFFFFD700), Color.White, Color(0xFF38E887)).random()
                val rotSpeed = Random.nextFloat() * 360f - 180f
                ConfettiParticle(
                    vx = (Math.cos(angle) * speed).toFloat(), 
                    vy = (Math.sin(angle) * speed - 200f).toFloat(), 
                    size = size, 
                    color = color, 
                    rotSpeed = rotSpeed
                )
            }
        }
        
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val p = confettiProgress.value
            val center = Offset(size.width / 2, size.height / 2 - 200f)
            if (p < 1f) {
                for (particle in particles) {
                    val currentX = center.x + particle.vx * p * 2f
                    val currentY = center.y + particle.vy * p * 2f + (800f * p * p)
                    val alpha = (1f - p).coerceIn(0f, 1f)
                    
                    rotate(particle.rotSpeed * p * 2f, pivot = Offset(currentX, currentY)) {
                        drawRect(
                            color = particle.color.copy(alpha = alpha),
                            topLeft = Offset(currentX, currentY),
                            size = androidx.compose.ui.geometry.Size(particle.size, particle.size)
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = cardScale
                    scaleY = cardScale
                    alpha = cardAlpha
                }
                .padding(24.dp)
                .shadow(24.dp, RoundedCornerShape(24.dp), spotColor = accentColor)
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.verticalGradient(listOf(Color(0xFF2B2930), Color(0xFF1A1A1E))))
                .border(2.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getStarMessage(stars),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = if (isDailyShift) stringResource(R.string.daily_shift) else stringResource(R.string.level_title, level),
                color = accentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                for (i in 1..5) {
                    val isEarned = i <= stars
                    val isAnimated = i <= animatedStarCount
                    val scale by animateFloatAsState(
                        targetValue = if (isAnimated) 1.2f else if (isEarned) 1f else 0.8f,
                        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
                        label = "star_scale_$i"
                    )
                    Text(
                        text = "★",
                        color = if (isAnimated) Color(0xFFFFD700) else Color.DarkGray.copy(alpha = 0.5f),
                        fontSize = 40.sp,
                        modifier = Modifier.scale(scale),
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = if (isAnimated) Shadow(color = Color(0xFFFFD700).copy(alpha=0.5f), blurRadius = 12f) else null
                        )
                    )
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                ResultBox(stringResource(R.string.result_moves), "$movesUsed", Color.White)
                ResultBox(stringResource(R.string.result_target), "$recommendedMoves", Color.Gray)
                if (bestMoves > 0) {
                    ResultBox(stringResource(R.string.result_best), "$bestMoves", Color(0xFF38E887))
                }
            }
            
            if (perfectStreak > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .background(Color(0xFFFFD700).copy(alpha = 0.15f), RoundedCornerShape(50))
                        .border(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f), RoundedCornerShape(50))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔥", fontSize = 16.sp, modifier = Modifier.padding(end = 6.dp))
                    Text(
                        text = if (isDailyShift) stringResource(R.string.daily_streak_text, perfectStreak) else stringResource(R.string.perfect_streak, perfectStreak),
                        color = Color(0xFFFFD700),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = if (isDailyShift) onMenu else onNextLevel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(accentColor.copy(alpha = 0.9f), accentColor.copy(alpha = 0.6f))))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isDailyShift) stringResource(R.string.main_menu) else stringResource(R.string.next_level), 
                        color = Color(0xFF0F172A), 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 18.sp,
                        style = androidx.compose.ui.text.TextStyle(
                            shadow = Shadow(color = Color.White.copy(alpha = 0.5f), offset = Offset(0f, 1f), blurRadius = 2f)
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onReplay,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), 
                shape = RoundedCornerShape(12.dp), 
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(stringResource(R.string.retry_level), color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                if (!isDailyShift) {
                    TextButton(onClick = onMenu) {
                        Text(stringResource(R.string.main_menu), color = Color(0xFFCAC4D0), fontSize = 12.sp)
                    }
                }
                TextButton(onClick = onSettings) {
                    Text(stringResource(R.string.settings_short), color = Color(0xFFCAC4D0), fontSize = 12.sp)
                }
            }
        }
    }
}

data class Chapter(
    val nameResId: Int,
    val backgroundBrush: Brush,
    val accentColor: Color,
    val startLevel: Int,
    val endLevel: Int
)

val gameChapters = listOf(
    Chapter(R.string.chapter_1, Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF020617))), Color(0xFFD0BCFF), 1, 18),
    Chapter(R.string.chapter_2, Brush.verticalGradient(listOf(Color(0xFF1A365D), Color(0xFF0D1B2A))), Color(0xFF90CDF4), 19, 36),
    Chapter(R.string.chapter_3, Brush.verticalGradient(listOf(Color(0xFF1C4532), Color(0xFF081C15))), Color(0xFF6EE7B7), 37, 54),
    Chapter(R.string.chapter_4, Brush.verticalGradient(listOf(Color(0xFF4A192C), Color(0xFF2D0A16))), Color(0xFFF6AD55), 55, 72),
    Chapter(R.string.chapter_5, Brush.verticalGradient(listOf(Color(0xFF2D3748), Color(0xFF1A202C))), Color(0xFFB794F4), 73, 90),
    Chapter(R.string.chapter_6, Brush.verticalGradient(listOf(Color(0xFF4C1D95), Color(0xFF2E1065))), Color(0xFFFBCFE8), 91, 106)
)

class MainActivity : ComponentActivity() {

    

    private lateinit var billingRepository: BillingRepository
    lateinit var adManager: AdManager
    lateinit var rewardedHintAdProvider: RewardedHintAdProvider


    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_language", "en") ?: "en"
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = android.content.res.Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        
        val prefs = getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)
        billingRepository = BillingRepositoryFactory.create(this, prefs)
        adManager = AdManager(this)
        adManager.initialize(this)
        rewardedHintAdProvider = AdMobRewardedHintAdProvider(this, adManager)
        
        GameAudioManager.getInstance(this)

        enableEdgeToEdge()

        // Force decor view and content view creation to prevent NPE in ComponentActivity.setContent on recreate
        setContentView(android.view.View(this))

        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val isAdFree by billingRepository.isAdFree.collectAsState()
                val billingUiState by billingRepository.uiState.collectAsState()
                
                var currentScreen by remember { mutableStateOf(ScreenState.MENU) }
                var isDailyShift by remember { mutableStateOf(false) }
                var dailySeed by remember { mutableStateOf<Long?>(null) }
                var gameLevel by remember { mutableIntStateOf(prefs.getInt("max_unlocked_level", 1)) }
                var showSettings by remember { mutableStateOf(false) }
                var hasCampaign by remember { 
                    mutableStateOf(
                        prefs.getBoolean("campaign_started", false) ||
                        prefs.getInt("max_unlocked_level", 1) > 1
                    )
                }

    

                val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

                LaunchedEffect(Unit) {
                    HintEventBus.events.collect { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }
                
                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
                LaunchedEffect(lifecycleState) {
                    if (lifecycleState == androidx.lifecycle.Lifecycle.State.RESUMED && isDailyShift) {
                        val newSeed = getDailyShiftSeed()
                        if (dailySeed != newSeed) {
                            dailySeed = newSeed
                            gameLevel = getDailyShiftLevelIndex(newSeed, gameChapters.last().endLevel) + 1
                        }
                    }
                }

                LaunchedEffect(currentScreen) {
                    val audioManager = GameAudioManager.getInstance(this@MainActivity)
                    if (currentScreen == ScreenState.GAME) {
                        audioManager.playGameplayMusic()
                    } else {
                        audioManager.playMenuTheme()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { androidx.compose.material3.SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    when (currentScreen) {
                        ScreenState.MENU -> MainMenuScreen(
                            appLanguage = prefs.getString("app_language", "en") ?: "en",
                            onLanguageChanged = { newLang ->
                                prefs.edit().putString("app_language", newLang).apply()
                                recreate()
                            },
                            modifier = Modifier.padding(innerPadding),
                            onContinue = {
                                isDailyShift = false
                                val maxUnlocked = prefs.getInt("max_unlocked_level", 1)
                                gameLevel = prefs.getInt("last_played_level", maxUnlocked).coerceIn(1, maxUnlocked)
                                currentScreen = ScreenState.GAME
                            },
                            onDailyShift = {
                                isDailyShift = true
                                val seed = getDailyShiftSeed()
                                dailySeed = seed
                                gameLevel = getDailyShiftLevelIndex(seed, gameChapters.last().endLevel) + 1
                                currentScreen = ScreenState.GAME
                            },
                            onNewGame = {
                                isDailyShift = false
                                val keysToKeep = listOf(
                                    "is_ad_free",
                                    "music_volume",
                                    "sfx_volume",
                                    "app_language",
                                    "last_daily_completed_date"
                                )
                                val savedValues = prefs.all.filterKeys { 
                                    it in keysToKeep || it.startsWith("daily_")
                                }
                                prefs.edit().clear().apply()
                                
                                val editor = prefs.edit()
                                savedValues.forEach { (key, value) ->
                                    if (value != null) {
                                        when (value) {
                                            is Boolean -> editor.putBoolean(key, value)
                                            is Int -> editor.putInt(key, value)
                                            is Float -> editor.putFloat(key, value)
                                            is Long -> editor.putLong(key, value)
                                            is String -> editor.putString(key, value)
                                            is Set<*> -> editor.putStringSet(key, value as Set<String>)
                                        }
                                    }
                                }
                                editor.putBoolean("campaign_started", true)
                                editor.putInt("last_played_level", 1)
                                editor.apply()
                                
                                HintRepository.getInstance(context).resetForNewCampaign()
                                
                                hasCampaign = true
                                gameLevel = 1
                                currentScreen = ScreenState.GAME
                            },
                            onSelectLevel = { 
                                isDailyShift = false
                                currentScreen = ScreenState.LEVEL_SELECT 
                            },
                            onRemoveAds = { billingRepository.initiatePurchaseFlow(this@MainActivity) },
                            onSettings = { showSettings = true },
                            onAbout = { currentScreen = ScreenState.ABOUT },
                            onExitApp = { this@MainActivity.finishAndRemoveTask() },
                            isAdFree = isAdFree,
                            hasCampaign = hasCampaign
                        )
                        ScreenState.LEVEL_SELECT -> LevelSelectScreen(
                            modifier = Modifier.padding(innerPadding),
                            prefs = prefs,
                            onLevelClick = { level ->
                                prefs.edit()
                                    .putBoolean("campaign_started", true)
                                    .putInt("last_played_level", level)
                                    .apply()
                                hasCampaign = true
                                gameLevel = level
                                currentScreen = ScreenState.GAME
                            },
                            onBack = { currentScreen = ScreenState.MENU }
                        )
                        ScreenState.GAME -> GameScreen(
                            modifier = Modifier.padding(innerPadding),
                            initialLevel = gameLevel,
                            prefs = prefs,
                            onBackToMenu = { 
                                isDailyShift = false
                                currentScreen = ScreenState.MENU 
                            },
                            isAdFree = isAdFree,
                            onRemoveAds = { billingRepository.initiatePurchaseFlow(this@MainActivity) },
                            onRestorePurchases = { billingRepository.restorePurchases() },
                            adManager = adManager,
                            rewardedHintAdProvider = rewardedHintAdProvider,
                            isDailyShift = isDailyShift,
                            dailySeed = dailySeed
                        )
                        ScreenState.ABOUT -> AboutScreen(
                            modifier = Modifier.padding(innerPadding),
                            onBack = { currentScreen = ScreenState.MENU }
                        )
                    }
                    
                    if (showSettings) {
                        SettingsDialog(
                            onDismiss = { showSettings = false },
                            isAdFree = isAdFree,
                            onRemoveAds = {
                                showSettings = false
                                billingRepository.initiatePurchaseFlow(this@MainActivity)
                            },
                            onRestorePurchases = { billingRepository.restorePurchases() },
                            adManager = adManager,
                            rewardedHintAdProvider = rewardedHintAdProvider
                        )
                    }
                    
                    when (val state = billingUiState) {
                        is BillingUiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.5f))
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = {}
                                    )
                                    .zIndex(50f),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFFD0BCFF))
                            }
                        }
                        is BillingUiState.Success -> {
                            AlertDialog(
                                onDismissRequest = { billingRepository.clearUiState() },
                                title = { Text(stringResource(R.string.success), color = Color.White) },
                                text = { Text(state.message, color = Color.White) },
                                confirmButton = {
                                    TextButton(onClick = { billingRepository.clearUiState() }) { Text(stringResource(R.string.ok), color = Color(0xFFD0BCFF)) }
                                },
                                containerColor = Color(0xFF2B2930)
                            )
                        }
                        is BillingUiState.Error -> {
                            AlertDialog(
                                onDismissRequest = { billingRepository.clearUiState() },
                                title = { Text(stringResource(R.string.error), color = Color.White) },
                                text = { Text(state.message, color = Color.White) },
                                confirmButton = {
                                    TextButton(onClick = { billingRepository.clearUiState() }) { Text(stringResource(R.string.ok), color = Color(0xFFD0BCFF)) }
                                },
                                containerColor = Color(0xFF2B2930)
                            )
                        }
                        is BillingUiState.PurchaseDialog -> {
                            AlertDialog(
                                onDismissRequest = { billingRepository.clearUiState() },
                                title = { Text(state.title, color = Color.White, fontWeight = FontWeight.Bold) },
                                text = {
                                    Column {
                                        Text(state.description, color = Color.Gray, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        state.benefits.forEach { benefit ->
                                            Text(benefit, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(stringResource(R.string.price, state.price), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        billingRepository.clearUiState()
                                        if (billingRepository is FakeBillingRepository) {
                                            (billingRepository as FakeBillingRepository).launchActualPurchaseFlow(this@MainActivity)
                                        } else if (billingRepository is PlayBillingRepository) {
                                            (billingRepository as PlayBillingRepository).launchActualPurchaseFlow(this@MainActivity)
                                        }
                                    }) { Text(stringResource(R.string.buy), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
                                },
                                dismissButton = {
                                    TextButton(onClick = { billingRepository.clearUiState() }) { Text(stringResource(R.string.cancel), color = Color.Gray) }
                                },
                                containerColor = Color(0xFF2B2930)
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GameAudioManager.getInstance(this).onAppForeground()
    }

    override fun onStop() {
        super.onStop()
        if (!isChangingConfigurations) {
            GameAudioManager.getInstance(this).onAppBackground()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isChangingConfigurations) {
            GameAudioManager.getInstance(this).release()
        }
    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    initialLevel: Int,
    prefs: android.content.SharedPreferences,
    onBackToMenu: () -> Unit,
    isAdFree: Boolean,
    onRemoveAds: () -> Unit,
    onRestorePurchases: () -> Unit,
    adManager: AdManager,
    rewardedHintAdProvider: RewardedHintAdProvider,
    isDailyShift: Boolean = false,
    dailySeed: Long? = null
) {
    var currentLevel by remember(initialLevel) { mutableIntStateOf(initialLevel) }
    var puzzleData by remember { mutableStateOf(generatePuzzle(currentLevel, dailySeed)) }
    var playerBoard by remember { mutableStateOf(puzzleData.initialPlayerBoard) }
    var movesCount by remember { mutableIntStateOf(0) }
    var isSolved by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    var currentSolutionPath by remember { mutableStateOf(puzzleData.solutionMoves) }
    var levelFinished by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showStreakBadge by remember { mutableStateOf(false) }

    val lastCompletedForDialog = prefs.getLong("last_daily_completed_date", 0L)
    var showAlreadyClaimed by remember(dailySeed) {
        mutableStateOf(isDailyShift && dailySeed != null && lastCompletedForDialog == dailySeed)
    }

    var showExitDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val isAppInForeground = lifecycleState.isAtLeast(androidx.lifecycle.Lifecycle.State.RESUMED)
    
    val isActive = !levelFinished && !showExitDialog && !showSettings && isAppInForeground && !adManager.isAdShowing

    DisposableEffect(isActive) {
        if (isActive) {
            adManager.startTracking()
        } else {
            adManager.stopTracking()
        }
        onDispose {
            adManager.stopTracking()
        }
    }

    var isAdLoading by remember { mutableStateOf(false) }
    
    var inputLocked by remember { mutableStateOf(false) }
    val canAcceptGameInput = !inputLocked && !showExitDialog && !showSettings && !adManager.isAdShowing && !levelFinished
    var replayCount by remember { mutableIntStateOf(0) }

    val context = LocalContext.current
    val audioManager = remember { GameAudioManager.getInstance(context) }
    
    val hintRepository = remember { HintRepository.getInstance(context) }
    val hintState by hintRepository.hintState.collectAsState()

    val boardSize = puzzleData.config.size
    val isTutorial = currentLevel == 1
    var actualHintMove by remember { mutableStateOf<Move?>(null) }
    var isCalculatingHint by remember { mutableStateOf(false) }
    val hintMove = if (isTutorial || showHint) actualHintMove else null
    LaunchedEffect(playerBoard, currentLevel, replayCount) {
        if (!isSolved) {
            isCalculatingHint = true
            actualHintMove = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Default) {
                Solver.getHintMove(playerBoard, puzzleData.targetBoard, currentSolutionPath)
            }
            isCalculatingHint = false
        }
    }


    LaunchedEffect(currentLevel, dailySeed) {
        if (!isDailyShift) {
            prefs.edit()
                .putBoolean("campaign_started", true)
                .putInt("last_played_level", currentLevel)
                .apply()
        }
            
        puzzleData = generatePuzzle(currentLevel, dailySeed)
        playerBoard = puzzleData.initialPlayerBoard
        currentSolutionPath = puzzleData.solutionMoves
        movesCount = 0
        isSolved = false
        showHint = false
        levelFinished = false
        inputLocked = false
        
        if (prefs.getInt("perfect_streak", 0) > 0) {
            showStreakBadge = true
            launch {
                delay(2000)
                showStreakBadge = false
            }
        }
    }
    LaunchedEffect(playerBoard) {
        if (playerBoard == puzzleData.targetBoard && !isSolved) {
            isSolved = true
            val stars = calculateStars(movesCount, puzzleData.config.moves)
            
            if (isDailyShift && dailySeed != null) {
                val seedStr = dailySeed.toString()
                val currentDailyBest = prefs.getInt("daily_best_moves_$seedStr", -1)
                if (currentDailyBest == -1 || movesCount < currentDailyBest) {
                    prefs.edit().putInt("daily_best_moves_$seedStr", movesCount).apply()
                }
                val lastCompleted = prefs.getLong("last_daily_completed_date", 0L)
                if (lastCompleted != dailySeed) {
                    val currentDailyStreak = prefs.getInt("daily_streak", 0)
                    val newStreak = if (lastCompleted == dailySeed - 1) currentDailyStreak + 1 else 1
                    prefs.edit()
                        .putLong("last_daily_completed_date", dailySeed)
                        .putInt("daily_streak", newStreak)
                        .apply()
                    hintRepository.addDailyShiftHint()
                    launch {
                        HintEventBus.emitEvent(context.getString(R.string.daily_shift_reward))
                    }
                }
            } else {
                saveLevelResult(prefs, currentLevel, stars, movesCount)
                
                val currentStreak = prefs.getInt("perfect_streak", 0)
                if (movesCount <= puzzleData.config.moves) {
                    prefs.edit().putInt("perfect_streak", currentStreak + 1).apply()
                } else {
                    prefs.edit().putInt("perfect_streak", 0).apply()
                }
                hintRepository.checkAndGrantThresholdBonus(currentLevel, context)
            }
            
            delay(300)
            audioManager.playSolve(stars)
            levelFinished = true
        }
    }

    val lockReason = when {
        showSettings -> "DIALOG"
        showExitDialog -> "DIALOG"
        levelFinished -> "LEVEL COMPLETE"
        inputLocked -> "ANIMATION"
        else -> null
    }

    val currentChapter = remember(currentLevel) {
        gameChapters.firstOrNull { currentLevel in it.startLevel..it.endLevel } ?: gameChapters.first()
    }
    val totalLevels = 106

    BackHandler(enabled = !levelFinished && !showExitDialog) {
        showExitDialog = true
        inputLocked = true
    }

    LaunchedEffect(showHint) {
        if (showHint) {
            delay(3000)
            showHint = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(currentChapter.backgroundBrush)
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (isDailyShift) {
                        Text(
                            text = stringResource(R.string.daily_shift).uppercase(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val formatter = java.time.format.DateTimeFormatter.ofLocalizedDate(java.time.format.FormatStyle.MEDIUM)
                        val dateString = if (dailySeed != null) java.time.LocalDate.ofEpochDay(dailySeed).format(formatter) else java.time.LocalDate.now().format(formatter)
                        Text(
                            text = "$dateString • " + stringResource(R.string.daily_streak_text, prefs.getInt("daily_streak", 0)),
                            color = Color(0xFFFFD700),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { 1f },
                            modifier = Modifier.height(4.dp).width(120.dp).clip(RoundedCornerShape(2.dp)),
                            color = Color(0xFFFFD700),
                            trackColor = Color(0xFFFFD700).copy(alpha = 0.3f),
                        )
                    } else {
                        Text(
                            text = stringResource(currentChapter.nameResId).uppercase(),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1).sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.level_progress, currentLevel, totalLevels),
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { currentLevel.toFloat() / totalLevels },
                            modifier = Modifier.height(4.dp).width(120.dp).clip(RoundedCornerShape(2.dp)),
                            color = currentChapter.accentColor,
                            trackColor = currentChapter.accentColor.copy(alpha = 0.3f),
                        )
                    }
                }
                TextButton(
                    onClick = {
                        showExitDialog = true
                        inputLocked = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFCAC4D0))
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.desc_exit_game), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.exit_game_short), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isDailyShift) stringResource(R.string.daily_shift) else stringResource(R.string.level_title, currentLevel),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.moves_count, movesCount, puzzleData.config.moves),
                        color = Color(0xFFCAC4D0),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.target_pattern),
                    color = currentChapter.accentColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .shadow(8.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B).copy(alpha = 0.7f))
                        .border(1.dp, Color.White.copy(alpha=0.1f), RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    val modelPx = 108.dp
                    val modelSpacing = 4.dp
                    val modelPieceSize = (modelPx - modelSpacing * (boardSize - 1)) / boardSize
                    Board(
                        board = puzzleData.targetBoard,
                        pieceSize = modelPieceSize,
                        spacing = modelSpacing,
                        interactable = false,
                        onShiftRow = { _, _ -> },
                        onShiftCol = { _, _ -> },
                        hintMove = null
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isTutorial && !isSolved) {
                    Text(
                        text = stringResource(R.string.tutorial_swipe),
                        color = currentChapter.accentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                } else if (!isSolved) {
                    Text(
                        text = stringResource(R.string.swipe_to_rebuild),
                        color = Color(0xFFCAC4D0),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.height(44.dp))
                }

                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black, spotColor = Color.Black)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF1E293B).copy(alpha = 0.8f))
                            .border(2.dp, Color.White.copy(alpha=0.05f), RoundedCornerShape(24.dp))
                            .padding(12.dp)
                    ) {
                        val boardPx = 292.dp
                        val spacing = 8.dp
                        val pieceSize = (boardPx - spacing * (boardSize - 1)) / boardSize
                        key(currentLevel, replayCount) {
                            Board(
                                board = playerBoard,
                                pieceSize = pieceSize,
                                spacing = spacing,
                                interactable = !isSolved && canAcceptGameInput,
                                onShiftRow = { row, dir ->
                                    playerBoard = shiftRow(playerBoard, row, dir)
                                    movesCount++
                                    val move = Move(true, row, dir)
                                    updatePath(move, currentSolutionPath) { currentSolutionPath = it }
                                    showHint = false
                                },
                                onShiftCol = { col, dir ->
                                    playerBoard = shiftCol(playerBoard, col, dir)
                                    movesCount++
                                    val move = Move(false, col, dir)
                                    updatePath(move, currentSolutionPath) { currentSolutionPath = it }
                                    showHint = false
                                },
                                hintMove = hintMove,
                                isInputLocked = !canAcceptGameInput,
                                onInputLockedChange = { inputLocked = it },
                                onAnimationStart = { audioManager.playSlide() }
                            )
                        }
                    }
                }
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                if (!isSolved) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = {
                                playerBoard = puzzleData.initialPlayerBoard
                                movesCount = 0
                                currentSolutionPath = puzzleData.solutionMoves
                                showHint = false
                                inputLocked = false
                                replayCount++
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f).height(48.dp),
                            enabled = canAcceptGameInput
                        ) {
                            Text(stringResource(R.string.reset), color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = { 
                                if (currentLevel <= 5) {
                                    if (actualHintMove != null) {
                                        showHint = true
                                    } else {
                                        HintEventBus.emitEvent(context.getString(R.string.no_guaranteed_move))
                                    }
                                } else {
                                    if (hintState.totalHints > 0) {
                                        if (!showHint) {
                                            if (actualHintMove != null) {
                                                hintRepository.consumeHint()
                                                showHint = true
                                            } else {
                                                HintEventBus.emitEvent(context.getString(R.string.no_guaranteed_move))
                                            }
                                        }
                                    } else {
                                        val activity = context as? android.app.Activity
                                        if (activity != null && !isAdLoading) {
                                            isAdLoading = true
                                            rewardedHintAdProvider.loadAndShow(
                                                activity,
                                                onReward = { 
                                                    hintRepository.addRewardedAdHint()
                                                    isAdLoading = false 
                                                },
                                                onFailedOrClosed = { isAdLoading = false }
                                            )
                                        }
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                            modifier = Modifier.weight(1f).height(48.dp),
                            enabled = canAcceptGameInput && !isAdLoading && !showHint && !isCalculatingHint,
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                if (showHint) {
                                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(stringResource(R.string.hint), color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center)
                                } else if (isAdLoading || isCalculatingHint) {
                                    Text(stringResource(R.string.loading), color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center)
                                } else {
                                    if (currentLevel <= 5 || hintState.totalHints > 0) {
                                        Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    } else {
                                        Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    
                                    val text = if (currentLevel <= 5) {
                                        stringResource(R.string.free_hint)
                                    } else if (hintState.totalHints > 0) {
                                        stringResource(R.string.hint_count, hintState.totalHints)
                                    } else {
                                        stringResource(R.string.watch_video_hint)
                                    }
                                    Text(text, color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center, maxLines = 1)
                                }
                            }
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.width(128.dp).height(4.dp).background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(50)))
            }
        }

        if (levelFinished) {
            CompletionDialog(
                level = currentLevel,
                movesUsed = movesCount,
                recommendedMoves = puzzleData.config.moves,
                bestMoves = if (isDailyShift && dailySeed != null) prefs.getInt("daily_best_moves_$dailySeed", -1) else getLevelMinMoves(prefs, currentLevel),
                perfectStreak = if (isDailyShift) prefs.getInt("daily_streak", 0) else prefs.getInt("perfect_streak", 0),
                accentColor = currentChapter.accentColor,
                isDailyShift = isDailyShift,
                onNextLevel = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = { currentLevel++ }
                    )
                },
                onReplay = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = {
                            playerBoard = puzzleData.initialPlayerBoard
                            movesCount = 0
                            currentSolutionPath = puzzleData.solutionMoves
                            showHint = false
                            isSolved = false
                            levelFinished = false
                            inputLocked = false
                            replayCount++
                        }
                    )
                },
                onMenu = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = onBackToMenu
                    )
                },
                onSettings = { showSettings = true }
            )
        }
        
        AnimatedVisibility(
            visible = showStreakBadge,
            enter = fadeIn(animationSpec = tween(300)) + slideInVertically(initialOffsetY = { -50 }, animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(targetOffsetY = { -50 }, animationSpec = tween(300)),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 80.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFFFD700).copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.perfect_streak, prefs.getInt("perfect_streak", 0)),
                    color = Color(0xFFFFD700),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        if (showSettings) {
            SettingsDialog(
                onDismiss = { showSettings = false },
                isAdFree = isAdFree,
                onRemoveAds = onRemoveAds,
                onRestorePurchases = onRestorePurchases,
                adManager = adManager,
                rewardedHintAdProvider = rewardedHintAdProvider
            )
        }

        var isProcessingExit by remember { mutableStateOf(false) }
        if (showExitDialog) {
            AlertDialog(
                properties = androidx.compose.ui.window.DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                ),
                onDismissRequest = { 
                    // Do nothing here because we want explicit button press
                },
                title = { Text(stringResource(R.string.are_you_sure), color = Color.White, fontWeight = FontWeight.Bold) },
                text = { Text(stringResource(R.string.lose_moves_warning), color = Color.Gray) },
                confirmButton = {
                    
                    TextButton(
                        enabled = !isProcessingExit,
                        onClick = { 
                            if (!isProcessingExit) {
                                isProcessingExit = true
                                adManager.showPendingInterstitialIfAny(
                                    activity = context as android.app.Activity,
                                    isAdFree = isAdFree,
                                    onFinished = {
                                        showExitDialog = false
                                        inputLocked = false
                                        onBackToMenu()
                                    }
                                )
                            }
                        }
                    ) { Text(stringResource(R.string.yes_exit), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
                },
                dismissButton = {
                    TextButton(
                        enabled = !isProcessingExit,
                        onClick = { 
                        showExitDialog = false
                        inputLocked = false
                    }) { Text(stringResource(R.string.return_to_game), color = Color.Gray) }
                },
                containerColor = Color(0xFF2B2930)
            )
        }

        if (showAlreadyClaimed) {
            AlertDialog(
                properties = androidx.compose.ui.window.DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                ),
                onDismissRequest = { },
                title = { Text(stringResource(R.string.reward_claimed_title), color = Color.White, fontWeight = FontWeight.Bold) },
                text = { Text(stringResource(R.string.reward_claimed_desc), color = Color.Gray) },
                confirmButton = {
                    TextButton(onClick = { showAlreadyClaimed = false }) {
                        Text(stringResource(R.string.play_again), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showAlreadyClaimed = false
                        onBackToMenu()
                    }) {
                        Text(stringResource(R.string.back), color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF2B2930)
            )
        }
    }
}

@Composable
fun Board(
    board: List<List<PieceType>>,
    pieceSize: Dp,
    spacing: Dp,
    interactable: Boolean,
    onShiftRow: (Int, Int) -> Unit,
    onShiftCol: (Int, Int) -> Unit,
    hintMove: Move?,
    isInputLocked: Boolean = false,
    onInputLockedChange: (Boolean) -> Unit = {},
    onAnimationStart: () -> Unit = {}
) {
    val boardSize = board.size
    val boardPx = pieceSize * boardSize + spacing * (boardSize - 1)
    val cellPx = with(LocalDensity.current) { (pieceSize + spacing).toPx() }
    
    var startRow by remember { mutableIntStateOf(0) }
    var startCol by remember { mutableIntStateOf(0) }
    var accumulatedDx by remember { mutableFloatStateOf(0f) }
    var accumulatedDy by remember { mutableFloatStateOf(0f) }
    var actionCommitted by remember { mutableStateOf(false) }

    var animatingRow by remember { mutableStateOf<Int?>(null) }
    var animatingCol by remember { mutableStateOf<Int?>(null) }
    val offsetAnim = remember { Animatable(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val pulseAlpha = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    val currentInteractable by rememberUpdatedState(interactable)
    val currentIsInputLocked by rememberUpdatedState(isInputLocked)
    
    val view = LocalView.current
    
    val dragScale by animateFloatAsState(targetValue = if (isDragging) 1.05f else 1f, label = "scale")
    val dragShadow by animateFloatAsState(targetValue = if (isDragging) 8f else 0f, label = "shadow")

    Box(
        modifier = Modifier
            .size(boardPx)
            .clipToBounds()
            .pointerInput(boardSize) {
                detectDragGestures(
                    onDragStart = { offset ->
                        if (!currentInteractable || currentIsInputLocked) return@detectDragGestures
                        startCol = (offset.x / cellPx).toInt().coerceIn(0, boardSize - 1)
                        startRow = (offset.y / cellPx).toInt().coerceIn(0, boardSize - 1)
                        accumulatedDx = 0f
                        accumulatedDy = 0f
                        actionCommitted = false
                        isDragging = true
                    },
                    onDragEnd = {
                        if (!isDragging) return@detectDragGestures
                        isDragging = false
                        if (animatingRow != null || animatingCol != null) {
                            val isRow = animatingRow != null
                            val index = animatingRow ?: animatingCol!!
                            val offset = offsetAnim.value
                            val threshold = cellPx * 0.4f
                            
                            onInputLockedChange(true)
                            coroutineScope.launch {
                                if (abs(offset) > threshold) {
                                    val direction = if (offset > 0) 1 else -1
                                    onAnimationStart()
                                    // Smooth 200 ms bounce animation
                                    offsetAnim.animateTo(
                                        targetValue = direction * cellPx,
                                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f)
                                    )
                                    view.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
                                    
                                    launch {
                                        pulseAlpha.snapTo(0.4f)
                                        pulseAlpha.animateTo(0f, tween(300))
                                    }
                                    
                                    if (isRow) onShiftRow(index, direction) else onShiftCol(index, direction)
                                } else {
                                    offsetAnim.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f)
                                    )
                                }
                                offsetAnim.snapTo(0f)
                                animatingRow = null
                                animatingCol = null
                                onInputLockedChange(false)
                            }
                        }
                    },
                    onDragCancel = {
                        isDragging = false
                        if (animatingRow != null || animatingCol != null) {
                            coroutineScope.launch {
                                offsetAnim.animateTo(0f, spring(dampingRatio = 0.7f, stiffness = 400f))
                                offsetAnim.snapTo(0f)
                                animatingRow = null
                                animatingCol = null
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (!currentInteractable || currentIsInputLocked || actionCommitted) return@detectDragGestures

                        accumulatedDx += dragAmount.x
                        accumulatedDy += dragAmount.y

                        if (animatingRow == null && animatingCol == null) {
                            val triggerThreshold = 10f
                            if (abs(accumulatedDx) > triggerThreshold && abs(accumulatedDx) > abs(accumulatedDy)) {
                                animatingRow = startRow
                            } else if (abs(accumulatedDy) > triggerThreshold && abs(accumulatedDy) > abs(accumulatedDx)) {
                                animatingCol = startCol
                            }
                        }

                        if (animatingRow != null) {
                            coroutineScope.launch {
                                offsetAnim.snapTo(accumulatedDx.coerceIn(-cellPx, cellPx))
                            }
                        } else if (animatingCol != null) {
                            coroutineScope.launch {
                                offsetAnim.snapTo(accumulatedDy.coerceIn(-cellPx, cellPx))
                            }
                        }
                    }
                )
            }
    ) {
        if (animatingRow != null) {
            val yOffset = animatingRow!! * cellPx
            Box(modifier = Modifier
                .offset { IntOffset(0, yOffset.toInt()) }
                .size(width = boardPx, height = pieceSize)
                .background(Color.White.copy(alpha = pulseAlpha.value.coerceIn(0f, 1f)), RoundedCornerShape(8.dp))
            )
        }
        if (animatingCol != null) {
            val xOffset = animatingCol!! * cellPx
            Box(modifier = Modifier
                .offset { IntOffset(xOffset.toInt(), 0) }
                .size(width = pieceSize, height = boardPx)
                .background(Color.White.copy(alpha = pulseAlpha.value.coerceIn(0f, 1f)), RoundedCornerShape(8.dp))
            )
        }

        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val piece = board[row][col]
                var xOffset = col * cellPx
                var yOffset = row * cellPx
                var isAnimatingPiece = false

                if (animatingRow == row) {
                    xOffset += offsetAnim.value
                    isAnimatingPiece = true
                } else if (animatingCol == col) {
                    yOffset += offsetAnim.value
                    isAnimatingPiece = true
                }

                val scale = if (isAnimatingPiece) dragScale else 1f
                val shadow = if (isAnimatingPiece) dragShadow else 0f
                val zIndex = if (isAnimatingPiece) 1f else 0f

                Box(modifier = Modifier
                    .offset { IntOffset(xOffset.toInt(), yOffset.toInt()) }
                    .zIndex(zIndex)
                    .scale(scale)
                    .shadow(shadow.dp, RoundedCornerShape(8.dp))
                ) {
                    GamePiece(piece = piece, size = pieceSize)
                }

                if (animatingRow == row) {
                    if (col == 0) {
                        val ghostXOffset = boardSize * cellPx + offsetAnim.value
                        Box(modifier = Modifier
                            .offset { IntOffset(ghostXOffset.toInt(), yOffset.toInt()) }
                            .zIndex(1f)
                            .scale(dragScale)
                            .shadow(dragShadow.dp, RoundedCornerShape(8.dp))
                        ) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                    if (col == boardSize - 1) {
                        val ghostXOffset = -1 * cellPx + offsetAnim.value
                        Box(modifier = Modifier
                            .offset { IntOffset(ghostXOffset.toInt(), yOffset.toInt()) }
                            .zIndex(1f)
                            .scale(dragScale)
                            .shadow(dragShadow.dp, RoundedCornerShape(8.dp))
                        ) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                }

                if (animatingCol == col) {
                    if (row == 0) {
                        val ghostYOffset = boardSize * cellPx + offsetAnim.value
                        Box(modifier = Modifier
                            .offset { IntOffset(xOffset.toInt(), ghostYOffset.toInt()) }
                            .zIndex(1f)
                            .scale(dragScale)
                            .shadow(dragShadow.dp, RoundedCornerShape(8.dp))
                        ) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                    if (row == boardSize - 1) {
                        val ghostYOffset = -1 * cellPx + offsetAnim.value
                        Box(modifier = Modifier
                            .offset { IntOffset(xOffset.toInt(), ghostYOffset.toInt()) }
                            .zIndex(1f)
                            .scale(dragScale)
                            .shadow(dragShadow.dp, RoundedCornerShape(8.dp))
                        ) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                }
            }
        }
        
        if (hintMove != null && animatingRow == null && animatingCol == null) {
            val isRow = hintMove.isRow
            val index = hintMove.index
            val dir = hintMove.direction
            val highlightColor = Color.White.copy(alpha = 0.15f)
            
            val infiniteTransition = rememberInfiniteTransition(label = "arrow")
            val animOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 10f * dir,
                animationSpec = infiniteRepeatable(
                    animation = tween(500),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "arrow_offset"
            )
            
            if (isRow) {
                val yOffset = (pieceSize + spacing) * index
                Box(modifier = Modifier
                    .offset(y = yOffset)
                    .size(width = boardPx, height = pieceSize)
                    .background(highlightColor, RoundedCornerShape(8.dp))
                )
                val icon = if (dir > 0) "→" else "←"
                Box(modifier = Modifier
                    .offset(x = animOffset.dp, y = yOffset)
                    .size(width = boardPx, height = pieceSize),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, color = Color.White, fontSize = (pieceSize.value * 0.5f).sp, fontWeight = FontWeight.Bold)
                }
            } else {
                val xOffset = (pieceSize + spacing) * index
                Box(modifier = Modifier
                    .offset(x = xOffset)
                    .size(width = pieceSize, height = boardPx)
                    .background(highlightColor, RoundedCornerShape(8.dp))
                )
                val icon = if (dir > 0) "↓" else "↑"
                Box(modifier = Modifier
                    .offset(x = xOffset, y = animOffset.dp)
                    .size(width = pieceSize, height = boardPx),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, color = Color.White, fontSize = (pieceSize.value * 0.5f).sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun GamePiece(piece: PieceType, size: Dp) {
    val isSmall = size < 40.dp
    val cornerRadius = if (isSmall) 6.dp else 16.dp
    Box(
        modifier = Modifier
            .size(size)
            .shadow(
                elevation = if (isSmall) 2.dp else 8.dp, 
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = piece.color,
                spotColor = piece.color
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        piece.color.copy(alpha = 0.85f),
                        piece.color,
                        piece.color.copy(alpha = 0.65f)
                    )
                )
            )
            .border(
                width = if (isSmall) 1.dp else 2.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(cornerRadius)
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(if (isSmall) 1.dp else 2.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.25f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(cornerRadius - 1.dp)
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius, bottomStart = 4.dp, bottomEnd = 4.dp)
                )
        )
        Text(
            text = piece.symbol,
            fontSize = if (isSmall) 12.sp else (size.value * 0.45f).sp,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            style = androidx.compose.ui.text.TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}
fun getDailyShiftSeed(): Long {
    val utcMillis = System.currentTimeMillis()
    // Align to UTC day. 
    return utcMillis / (1000 * 60 * 60 * 24)
}

fun getDailyShiftLevelIndex(seed: Long, totalLevels: Int): Int {
    // Basic pseudo-random using seed, ensuring we don't crash
    val random = java.util.Random(seed)
    return random.nextInt(totalLevels)
}
