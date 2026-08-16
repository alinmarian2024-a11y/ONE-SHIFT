import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

imports_to_add = """
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.interaction.MutableInteractionSource
"""

for imp in imports_to_add.strip().split("\n"):
    if imp not in content:
        content = content.replace("import androidx.compose.ui.graphics.Color\n", f"import androidx.compose.ui.graphics.Color\n{imp}\n")

# add AnimatedMenuButton
animated_btn = """
@Composable
fun AnimatedMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: androidx.compose.material3.ButtonColors,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(12.dp),
    border: androidx.compose.foundation.BorderStroke? = null,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    textColor: Color = Color.White,
    fontWeight: FontWeight? = null,
    entranceDelay: Int = 0
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "button_scale", animationSpec = tween(150))
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(entranceDelay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { 50 }, animationSpec = tween(500, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(500)),
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    ) {
        Button(
            onClick = onClick,
            colors = colors,
            shape = shape,
            border = border,
            interactionSource = interactionSource,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text, color = textColor, fontSize = fontSize, fontWeight = fontWeight)
        }
    }
}

fun MainMenuScreen(
"""

content = content.replace("fun MainMenuScreen(", animated_btn)

# Now we replace the Box content of MainMenuScreen.
# The Box starts exactly with `Box(modifier = modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF020617))))) {`

box_start = r"Box\(modifier = modifier\.fillMaxSize\(\)\.background\(Brush\.verticalGradient\(listOf\(Color\(0xFF0F172A\), Color\(0xFF020617\)\)\)\)\) \{"
box_end = r"\}\s*\}\s*\}\s*@Composable\s*fun LevelSelectScreen"

replacement = """
    val infiniteTransition = rememberInfiniteTransition(label = "lobby_anim")
    
    val color1 by infiniteTransition.animateColor(
        initialValue = Color(0xFF020617),
        targetValue = Color(0xFF0F172A),
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Reverse),
        label = "bg_color_1"
    )
    val color2 by infiniteTransition.animateColor(
        initialValue = Color(0xFF0F172A),
        targetValue = Color(0xFF1E293B),
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), RepeatMode.Reverse),
        label = "bg_color_2"
    )
    val tileGlow by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(2500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "tile_glow"
    )
    val sweep by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 1500f,
        animationSpec = infiniteRepeatable(tween(3500, delayMillis = 1500, easing = LinearEasing), RepeatMode.Restart),
        label = "title_sweep"
    )

    Box(modifier = modifier.fillMaxSize().background(Brush.verticalGradient(listOf(color1, color2)))) {
        // Glowing tiles Canvas
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2
            val cy = size.height * 0.28f
            val tileSize = 80f
            
            for (i in -1..1) {
                for (j in -1..1) {
                    val x = cx + i * tileSize * 1.3f - tileSize / 2
                    val y = cy + j * tileSize * 1.3f - tileSize / 2
                    val alpha = (0.1f * tileGlow * (if(i == 0 && j == 0) 2.0f else 0.6f)).coerceIn(0f, 1f)
                    drawRoundRect(
                        color = Color(0xFFD0BCFF).copy(alpha = alpha),
                        topLeft = Offset(x, y),
                        size = androidx.compose.ui.geometry.Size(tileSize, tileSize),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f, 16f)
                    )
                }
            }
        }

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
            val titleBrush = Brush.linearGradient(
                colors = listOf(Color.White, Color.White, Color(0xFFD0BCFF), Color.White, Color.White),
                start = Offset(sweep, 0f),
                end = Offset(sweep + 400f, 0f)
            )
            
            Text(
                "ONE SHIFT", 
                style = androidx.compose.ui.text.TextStyle(
                    brush = titleBrush,
                    fontSize = 48.sp, 
                    fontWeight = FontWeight.Black, 
                    letterSpacing = (-2).sp
                )
            )
            Box(modifier = Modifier.height(6.dp).width(80.dp).background(Color(0xFFD0BCFF)).padding(bottom = 64.dp))
            Spacer(modifier = Modifier.height(48.dp))
            
            var delayStep = 0
            
            Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                AnimatedMenuButton(
                    text = stringResource(R.string.daily_shift),
                    onClick = onDailyShift,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                    textColor = Color(0xFF0F172A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    entranceDelay = delayStep
                )
            }
            delayStep += 100
            Spacer(modifier = Modifier.height(16.dp))
            
            if (hasCampaign) {
                Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    AnimatedMenuButton(
                        text = stringResource(R.string.continue_game),
                        onClick = onContinue,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)),
                        textColor = Color(0xFF0F172A),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        entranceDelay = delayStep
                    )
                }
                delayStep += 100
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                AnimatedMenuButton(
                    text = stringResource(R.string.new_game),
                    onClick = { 
                        if (hasCampaign) {
                            showNewGameDialog = true 
                        } else {
                            onNewGame()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                    entranceDelay = delayStep
                )
            }
            delayStep += 100
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                AnimatedMenuButton(
                    text = stringResource(R.string.select_level),
                    onClick = onSelectLevel,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                    entranceDelay = delayStep
                )
            }
            delayStep += 100
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isAdFree) {
                Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    AnimatedMenuButton(
                        text = stringResource(R.string.ads_removed_check),
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F)),
                        textColor = Color(0xFF38E887),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38E887)),
                        shape = RoundedCornerShape(50),
                        entranceDelay = delayStep
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    AnimatedMenuButton(
                        text = stringResource(R.string.remove_ads),
                        onClick = onRemoveAds,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00BCD4)),
                        shape = RoundedCornerShape(50),
                        entranceDelay = delayStep
                    )
                }
            }
            delayStep += 100
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(modifier = Modifier.weight(1f).height(56.dp)) {
                    AnimatedMenuButton(
                        text = stringResource(R.string.settings_short),
                        onClick = onSettings,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                        fontSize = 14.sp,
                        entranceDelay = delayStep
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f).height(56.dp)) {
                    AnimatedMenuButton(
                        text = stringResource(R.string.about_game),
                        onClick = onAbout,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                        fontSize = 14.sp,
                        entranceDelay = delayStep
                    )
                }
            }
            delayStep += 100
            Spacer(modifier = Modifier.height(16.dp))
            
            Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                AnimatedMenuButton(
                    text = stringResource(R.string.exit_game),
                    onClick = { showExitAppDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1B1F)),
                    textColor = Color(0xFFFF7F50),
                    fontWeight = FontWeight.Bold,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF7F50)),
                    shape = RoundedCornerShape(50),
                    entranceDelay = delayStep
                )
            }
        }
    }
}

@Composable
fun LevelSelectScreen"""

content = re.sub(box_start + r".*?" + box_end, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

