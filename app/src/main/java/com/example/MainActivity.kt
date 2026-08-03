package com.example

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
    CYAN(Color(0xFF00BCD4), "●"),
    VIOLET(Color(0xFF9C27B0), "◆"),
    CORAL(Color(0xFFFF7F50), "■"),
    LIME(Color(0xFFCDDC39), "▲")
}

data class LevelConfig(val size: Int, val moves: Int)
data class Move(val isRow: Boolean, val index: Int, val direction: Int)
data class PuzzleData(
    val targetBoard: List<List<PieceType>>,
    val initialPlayerBoard: List<List<PieceType>>,
    val solutionMoves: List<Move>,
    val config: LevelConfig
)

fun getLevelConfig(level: Int): LevelConfig {
    return when (level) {
        1 -> LevelConfig(2, 1)
        in 2..5 -> LevelConfig(2, 1)
        in 6..10 -> LevelConfig(2, 2)
        in 11..15 -> LevelConfig(3, 1)
        in 16..25 -> LevelConfig(3, Random.nextInt(2, 4))
        in 26..40 -> LevelConfig(3, Random.nextInt(3, 5))
        in 41..50 -> LevelConfig(4, Random.nextInt(1, 4))
        else -> LevelConfig(4, Random.nextInt(3, 7))
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

fun generatePuzzle(level: Int): PuzzleData {
    val config = getLevelConfig(level)
    var targetBoard: List<List<PieceType>>
    var playerBoard: List<List<PieceType>>
    var solutionMoves: List<Move>
    do {
        targetBoard = (0 until config.size).map {
            (0 until config.size).map { PieceType.entries.random() }
        }
        val moves = mutableListOf<Move>()
        var currentBoard = targetBoard
        var lastMove: Move? = null
        for (i in 0 until config.moves) {
            var move: Move
            do {
                val isRow = Random.nextBoolean()
                val index = Random.nextInt(config.size)
                val dir = if (Random.nextBoolean()) 1 else -1
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
    onRemoveAds: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onExitApp: () -> Unit,
    isAdFree: Boolean
) {
    var showNewGameDialog by remember { mutableStateOf(false) }
    var showExitAppDialog by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(false) }

    BackHandler {
        showExitAppDialog = true
    }

    if (showExitAppDialog) {
        AlertDialog(
            properties = androidx.compose.ui.window.DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false
            ),
            onDismissRequest = { 
                // Do nothing
            },
            title = { Text(stringResource(R.string.exit_game_dialog_title), color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.exit_game_dialog_text), color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = { 
                    showExitAppDialog = false
                    onExitApp()
                }) { Text(stringResource(R.string.yes_exit), color = Color(0xFFD0BCFF), fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showExitAppDialog = false }) { Text(stringResource(R.string.return_to_menu), color = Color.Gray) }
            },
            containerColor = Color(0xFF2B2930)
        )
    }

    if (showNewGameDialog) {
        AlertDialog(
            onDismissRequest = { showNewGameDialog = false },
            title = { Text(stringResource(R.string.new_game_dialog_title), color = Color.White, fontWeight = FontWeight.Bold) },
            text = { Text(stringResource(R.string.new_game_dialog_text), color = Color.Gray) },
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

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF1C1B1F))) {
        LanguageSelector(
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp),
            enabled = !showExitAppDialog && !showNewGameDialog,
            onLanguageChangeStarted = {},
            onLanguageChangeEnded = {}
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("ONE SHIFT", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Black, letterSpacing = (-2).sp)
            Box(modifier = Modifier.height(6.dp).width(80.dp).background(Color(0xFFD0BCFF)).padding(bottom = 64.dp))
            Spacer(modifier = Modifier.height(48.dp))
        
        Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = onContinue, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)), modifier = Modifier.fillMaxWidth().height(64.dp)) {
            Text(stringResource(R.string.continue_game), color = Color(0xFF381E72), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = { showNewGameDialog = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(stringResource(R.string.new_game), color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = onSelectLevel, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(stringResource(R.string.select_level), color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isAdFree) {
            Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F), contentColor = Color(0xFF38E887)), modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color(0xFF38E887), RoundedCornerShape(50))) {
                Text(stringResource(R.string.ads_removed), fontSize = 16.sp)
            }
        } else {
            Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = onRemoveAds, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.fillMaxWidth().height(56.dp).border(1.dp, Color(0xFF00BCD4), RoundedCornerShape(50))) {
                Text(stringResource(R.string.remove_ads), color = Color.White, fontSize = 16.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = onSettings, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.weight(1f).height(56.dp)) {
                Text(stringResource(R.string.settings_upper), color = Color.White, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(enabled = !showExitAppDialog && !showNewGameDialog, onClick = onAbout, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.weight(1f).height(56.dp)) {
                Text(stringResource(R.string.about_game), color = Color.White, fontSize = 14.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = !showExitAppDialog && !showNewGameDialog,
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
    
    Column(modifier = modifier.fillMaxSize().background(Color(0xFF1C1B1F))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back), color = Color(0xFFD0BCFF))
            }
            Text(stringResource(R.string.select_level), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        }
        
        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(maxOf(50, maxUnlocked + 5)) { index ->
                val level = index + 1
                val isUnlocked = level <= maxUnlocked
                val stars = getLevelStars(prefs, level)
                
                Card(
                    modifier = Modifier.aspectRatio(1f).clickable(enabled = isUnlocked) { onLevelClick(level) },
                    colors = CardDefaults.cardColors(containerColor = if (isUnlocked) Color(0xFF49454F) else Color(0xFF2B2930)),
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
                            Text(stringResource(R.string.level_locked), fontSize = 24.sp)
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
        title = { Text(stringResource(R.string.settings_upper), color = Color.White, fontWeight = FontWeight.Bold) },
        text = { 
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.audio), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(stringResource(R.string.music_volume, (musicVolume * 100).toInt()), color = Color.White, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (musicVolume <= 0f) Icons.Filled.MusicOff else Icons.Filled.MusicNote, 
                        contentDescription = stringResource(R.string.cd_music), 
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
                        contentDescription = stringResource(R.string.cd_effects), 
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
                    Text(stringResource(R.string.auto_ads_removed), color = Color(0xFF38E887), fontSize = 14.sp)
                } else {
                    Button(onClick = onRemoveAds, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.fillMaxWidth().height(48.dp)) {
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
        modifier = modifier.fillMaxSize().background(Color(0xFF1C1B1F)).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back), color = Color(0xFFD0BCFF))
            }
            Text(stringResource(R.string.about_title), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
        }
        
        Text("ONE SHIFT", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Black, letterSpacing = (-1).sp)
        Text(stringResource(R.string.about_description), color = Color(0xFFCAC4D0), fontSize = 16.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 16.dp))
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text(stringResource(R.string.how_to_play), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.how_to_play_text), color = Color.White, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.objective), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.objective_text), color = Color.White, fontSize = 14.sp)
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(stringResource(R.string.master_each_level), color = Color(0xFFD0BCFF), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.master_each_level_text), color = Color.White, fontSize = 14.sp)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Text(stringResource(R.string.version_text), color = Color.Gray, fontSize = 12.sp)
        Text(stringResource(R.string.created_in_romania), color = Color.Gray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)), modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(stringResource(R.string.back_to_menu), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CompletionDialog(
    level: Int,
    movesUsed: Int,
    recommendedMoves: Int,
    bestMoves: Int,
    onNextLevel: () -> Unit,
    onReplay: () -> Unit,
    onMenu: () -> Unit,
    onSettings: () -> Unit
) {
    val stars = calculateStars(movesUsed, recommendedMoves)
    var animatedStarCount by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        for (i in 1..5) {
            delay(150)
            if (i <= stars) animatedStarCount = i
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {}
            ),
        contentAlignment = Alignment.Center
    ) {
        val hasGlow = animatedStarCount == 5
        Column(
            modifier = Modifier
                .padding(24.dp)
                .shadow(if (hasGlow) 24.dp else 8.dp, RoundedCornerShape(24.dp), spotColor = if (hasGlow) Color(0xFFFFD700) else Color.Black)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF2B2930))
                .border(
                    width = if (hasGlow) 2.dp else 0.dp, 
                    color = if (hasGlow) Color(0xFFFFD700).copy(alpha = 0.5f) else Color.Transparent, 
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getStarMessage(stars),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.level_text, level),
                color = Color(0xFFD0BCFF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                for (i in 1..5) {
                    val isEarned = i <= stars
                    val isAnimated = i <= animatedStarCount
                    val scale by animateFloatAsState(
                        targetValue = if (isAnimated) 1.2f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                        label = "star_scale_$i"
                    )
                    Text(
                        text = if (isEarned) "★" else "☆",
                        color = if (isAnimated) Color(0xFFFFD700) else Color.Gray.copy(alpha = 0.3f),
                        fontSize = 36.sp,
                        modifier = Modifier.scale(if (isEarned) scale else 1f)
                    )
                }
            }

            Text(stringResource(R.string.moves_used, movesUsed), color = Color.White, fontSize = 14.sp)
            Text(stringResource(R.string.recommended_moves, recommendedMoves), color = Color.Gray, fontSize = 14.sp)
            if (bestMoves > 0) {
                Text(stringResource(R.string.best_score, bestMoves), color = Color(0xFF38E887), fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNextLevel,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.next_level), color = Color(0xFF381E72), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onReplay,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(stringResource(R.string.replay_level), color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = onMenu) {
                    Text(stringResource(R.string.main_menu), color = Color(0xFFCAC4D0), fontSize = 12.sp)
                }
                TextButton(onClick = onSettings) {
                    Text(stringResource(R.string.settings_upper), color = Color(0xFFCAC4D0), fontSize = 12.sp)
                }
            }
        }
    }
}

class MainActivity : AppCompatActivity() {

    

    private lateinit var billingRepository: BillingRepository
    lateinit var adManager: AdManager
    lateinit var rewardedHintAdProvider: RewardedHintAdProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        
        val prefs = getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)
        val languageSet = prefs.getBoolean("language_set", false)
        if (!languageSet) {
            androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(androidx.core.os.LocaleListCompat.forLanguageTags("en"))
            prefs.edit().putBoolean("language_set", true).apply()
        }

        billingRepository = BillingRepositoryFactory.create(this, prefs)
        adManager = AdManager(this)
        adManager.initialize(this)
        rewardedHintAdProvider = AdMobRewardedHintAdProvider(this, adManager)
        
        GameAudioManager.getInstance(this)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val isAdFree by billingRepository.isAdFree.collectAsState()
                val billingUiState by billingRepository.uiState.collectAsState()
                
                var currentScreen by remember { mutableStateOf(ScreenState.MENU) }
                var gameLevel by remember { mutableIntStateOf(prefs.getInt("max_unlocked_level", 1)) }
                var showSettings by remember { mutableStateOf(false) }

    

                val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }

                LaunchedEffect(Unit) {
                    HintEventBus.events.collect { message ->
                        snackbarHostState.showSnackbar(message)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { androidx.compose.material3.SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    when (currentScreen) {
                        ScreenState.MENU -> MainMenuScreen(
                            modifier = Modifier.padding(innerPadding),
                            onContinue = {
                                gameLevel = prefs.getInt("max_unlocked_level", 1)
                                currentScreen = ScreenState.GAME
                            },
                            onNewGame = {
                                val keysToKeep = listOf(
                                    "is_ad_free",
                                    "music_volume",
                                    "sfx_volume",
                                    "daily_hints",
                                    "bonus_hints",
                                    "initial_hint_bonus_granted",
                                    "last_daily_hint_epoch_day",
                                    "rewarded_thresholds"
                                )
                                val savedValues = keysToKeep.associateWith { prefs.all[it] }
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
                                editor.apply()
                                
                                gameLevel = 1
                                currentScreen = ScreenState.GAME
                            },
                            onSelectLevel = { currentScreen = ScreenState.LEVEL_SELECT },
                            onRemoveAds = { billingRepository.initiatePurchaseFlow(this@MainActivity) },
                            onSettings = { showSettings = true },
                            onAbout = { currentScreen = ScreenState.ABOUT },
                            onExitApp = { this@MainActivity.finish() },
                            isAdFree = isAdFree
                        )
                        ScreenState.LEVEL_SELECT -> LevelSelectScreen(
                            modifier = Modifier.padding(innerPadding),
                            prefs = prefs,
                            onLevelClick = { level ->
                                gameLevel = level
                                currentScreen = ScreenState.GAME
                            },
                            onBack = { currentScreen = ScreenState.MENU }
                        )
                        ScreenState.GAME -> GameScreen(
                            modifier = Modifier.padding(innerPadding),
                            initialLevel = gameLevel,
                            prefs = prefs,
                            onBackToMenu = { currentScreen = ScreenState.MENU },
                            isAdFree = isAdFree,
                            onRemoveAds = { billingRepository.initiatePurchaseFlow(this@MainActivity) },
                            onRestorePurchases = { billingRepository.restorePurchases() },
                            adManager = adManager,
                            rewardedHintAdProvider = rewardedHintAdProvider
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
                                        Text(stringResource(R.string.price_text, state.price), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
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
        GameAudioManager.getInstance(this).onAppBackground()
    }

    override fun onDestroy() {
        super.onDestroy()
        GameAudioManager.getInstance(this).release()
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
    rewardedHintAdProvider: RewardedHintAdProvider
) {
    var currentLevel by remember(initialLevel) { mutableIntStateOf(initialLevel) }

    var puzzleData by remember { mutableStateOf(generatePuzzle(currentLevel)) }
    var playerBoard by remember { mutableStateOf(puzzleData.initialPlayerBoard) }
    var movesCount by remember { mutableIntStateOf(0) }
    var isSolved by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    var currentSolutionPath by remember { mutableStateOf(puzzleData.solutionMoves) }
    var levelFinished by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }

    

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
    val noMovesResetMsg = stringResource(R.string.no_moves_reset)
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


    LaunchedEffect(currentLevel) {
        puzzleData = generatePuzzle(currentLevel)
        playerBoard = puzzleData.initialPlayerBoard
        currentSolutionPath = puzzleData.solutionMoves
        movesCount = 0
        isSolved = false
        showHint = false
        levelFinished = false
        inputLocked = false
    }

    LaunchedEffect(playerBoard) {
        if (playerBoard == puzzleData.targetBoard && !isSolved) {
            isSolved = true
            val stars = calculateStars(movesCount, puzzleData.config.moves)
            saveLevelResult(prefs, currentLevel, stars, movesCount)
            delay(300)
            audioManager.playSolve(stars)
            hintRepository.checkAndGrantThresholdBonus(currentLevel)
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
                .background(Color(0xFF1C1B1F))
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
                    Text(
                        text = "ONE SHIFT",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-1).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.height(4.dp).width(48.dp).background(Color(0xFFD0BCFF)))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    LanguageSelector(
                        enabled = !showExitDialog && !levelFinished,
                        onLanguageChangeStarted = { inputLocked = true },
                        onLanguageChangeEnded = { inputLocked = false }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            showExitDialog = true
                            inputLocked = true
                        },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFCAC4D0))
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = stringResource(R.string.cd_exit_game), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.exit_game), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
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
                        text = stringResource(R.string.level_text, currentLevel),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.moves_text, "$movesCount / ${puzzleData.config.moves}"),
                        color = Color(0xFFCAC4D0),
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.model),
                    color = Color(0xFFD0BCFF),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF49454F))
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
                        text = stringResource(R.string.tutorial_swipe_row),
                        color = Color(0xFFD0BCFF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                } else if (!isSolved) {
                    Text(
                        text = stringResource(R.string.swipe_to_recreate_upper),
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
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFF49454F))
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)),
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
                                        HintEventBus.emitEvent(noMovesResetMsg)
                                    }
                                } else {
                                    if (hintState.totalHints > 0) {
                                        if (!showHint) {
                                            if (actualHintMove != null) {
                                                hintRepository.consumeHint()
                                                showHint = true
                                            } else {
                                                HintEventBus.emitEvent(noMovesResetMsg)
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)),
                            modifier = Modifier.weight(1f).height(48.dp),
                            enabled = canAcceptGameInput && !isAdLoading && !showHint && !isCalculatingHint,
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                if (showHint) {
                                    Icon(Icons.Filled.Lightbulb, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(stringResource(R.string.hint_button), color = Color.White, fontSize = 11.sp, textAlign = TextAlign.Center)
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
                                        stringResource(R.string.free_hint).uppercase()
                                    } else if (hintState.totalHints > 0) {
                                        stringResource(R.string.hint_count, hintState.totalHints).uppercase()
                                    } else {
                                        stringResource(R.string.watch_video_hint).uppercase()
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
                bestMoves = getLevelMinMoves(prefs, currentLevel),
                onNextLevel = {
                    adManager.showPendingInterstitialIfAny(
                        activity = context as android.app.Activity,
                        isAdFree = isAdFree,
                        onFinished = { currentLevel++ }
                    )
                },
                onReplay = {
                    playerBoard = puzzleData.initialPlayerBoard
                    movesCount = 0
                    currentSolutionPath = puzzleData.solutionMoves
                    showHint = false
                    isSolved = false
                    levelFinished = false
                    inputLocked = false
                    replayCount++
                },
                onMenu = onBackToMenu,
                onSettings = { showSettings = true }
            )
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
                text = { Text(stringResource(R.string.are_you_sure_text), color = Color.Gray) },
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
    var animationDir by remember { mutableIntStateOf(0) }
    val offsetAnim = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    val currentInteractable by rememberUpdatedState(interactable)
    val currentIsInputLocked by rememberUpdatedState(isInputLocked)

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
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        if (!currentInteractable || currentIsInputLocked || actionCommitted) return@detectDragGestures

                        accumulatedDx += dragAmount.x
                        accumulatedDy += dragAmount.y

                        val threshold = 50f

                        if (abs(accumulatedDx) > threshold && abs(accumulatedDx) > abs(accumulatedDy)) {
                            val direction = if (accumulatedDx > 0) 1 else -1
                            actionCommitted = true
                            onInputLockedChange(true)
                            onAnimationStart()
                            coroutineScope.launch {
                                try {
                                    animatingRow = startRow
                                    animationDir = direction
                                    offsetAnim.animateTo(
                                        targetValue = direction * cellPx,
                                        animationSpec = tween(250, easing = FastOutSlowInEasing)
                                    )
                                    onShiftRow(startRow, direction)
                                } catch (e: Exception) {
                                    // Ignoră erorile sau anulările, important e să trecem prin finally
                                } finally {
                                    offsetAnim.snapTo(0f)
                                    animatingRow = null
                                    onInputLockedChange(false)
                                }
                            }
                        } else if (abs(accumulatedDy) > threshold && abs(accumulatedDy) > abs(accumulatedDx)) {
                            val direction = if (accumulatedDy > 0) 1 else -1
                            actionCommitted = true
                            onInputLockedChange(true)
                            onAnimationStart()
                            coroutineScope.launch {
                                try {
                                    animatingCol = startCol
                                    animationDir = direction
                                    offsetAnim.animateTo(
                                        targetValue = direction * cellPx,
                                        animationSpec = tween(250, easing = FastOutSlowInEasing)
                                    )
                                    onShiftCol(startCol, direction)
                                } catch (e: Exception) {
                                    // Ignoră erorile sau anulările
                                } finally {
                                    offsetAnim.snapTo(0f)
                                    animatingCol = null
                                    onInputLockedChange(false)
                                }
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
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            )
        }
        if (animatingCol != null) {
            val xOffset = animatingCol!! * cellPx
            Box(modifier = Modifier
                .offset { IntOffset(xOffset.toInt(), 0) }
                .size(width = pieceSize, height = boardPx)
                .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            )
        }

        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                val piece = board[row][col]
                var xOffset = col * cellPx
                var yOffset = row * cellPx

                if (animatingRow == row) {
                    xOffset += offsetAnim.value
                } else if (animatingCol == col) {
                    yOffset += offsetAnim.value
                }

                Box(modifier = Modifier.offset { IntOffset(xOffset.toInt(), yOffset.toInt()) }) {
                    GamePiece(piece = piece, size = pieceSize)
                }

                if (animatingRow == row) {
                    if (animationDir > 0 && col == boardSize - 1) {
                        val ghostXOffset = -1 * cellPx + offsetAnim.value
                        Box(modifier = Modifier.offset { IntOffset(ghostXOffset.toInt(), yOffset.toInt()) }) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                    if (animationDir < 0 && col == 0) {
                        val ghostXOffset = boardSize * cellPx + offsetAnim.value
                        Box(modifier = Modifier.offset { IntOffset(ghostXOffset.toInt(), yOffset.toInt()) }) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                }

                if (animatingCol == col) {
                    if (animationDir > 0 && row == boardSize - 1) {
                        val ghostYOffset = -1 * cellPx + offsetAnim.value
                        Box(modifier = Modifier.offset { IntOffset(xOffset.toInt(), ghostYOffset.toInt()) }) {
                            GamePiece(piece = piece, size = pieceSize)
                        }
                    }
                    if (animationDir < 0 && row == 0) {
                        val ghostYOffset = boardSize * cellPx + offsetAnim.value
                        Box(modifier = Modifier.offset { IntOffset(xOffset.toInt(), ghostYOffset.toInt()) }) {
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
    Box(
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(if (isSmall) 4.dp else 16.dp))
            .background(piece.color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = piece.symbol,
            fontSize = if (isSmall) 12.sp else (size.value * 0.4f).sp,
            color = Color(0xFFE6E1E5)
        )
    }
}

@Composable
fun LanguageSelector(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onLanguageChangeStarted: () -> Unit,
    onLanguageChangeEnded: () -> Unit
) {
    val currentConfig = androidx.compose.ui.platform.LocalConfiguration.current
    val currentLang = currentConfig.locales.get(0)?.language ?: "en"
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = { 
                showDialog = true 
                onLanguageChangeStarted()
            },
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF49454F)),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
        ) {
            Text("🌐 ${currentLang.uppercase()}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { 
                // Do nothing
            },
            title = null,
            text = {
                Column {
                    val languages = listOf("en" to "English", "ro" to "Română", "es" to "Español", "it" to "Italiano")
                    languages.forEach { (code, name) ->
                        TextButton(
                            onClick = {
                                androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(androidx.core.os.LocaleListCompat.forLanguageTags(code))
                                showDialog = false
                                onLanguageChangeEnded()
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(name, color = Color.White, fontSize = 18.sp, fontWeight = if (code == currentLang) FontWeight.Bold else FontWeight.Normal)
                                if (code == currentLang) Text("✓", color = Color(0xFF38E887), fontSize = 18.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { 
                    showDialog = false 
                    onLanguageChangeEnded()
                }) {
                    Text(stringResource(R.string.cancel), color = Color.Gray)
                }
            },
            containerColor = Color(0xFF2B2930),
            properties = androidx.compose.ui.window.DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false)
        )
    }
}
