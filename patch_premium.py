import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

imports = """
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.fillMaxHeight
"""
for imp in imports.strip().split("\n"):
    if imp not in content:
        content = content.replace("import androidx.compose.ui.graphics.Color\n", f"import androidx.compose.ui.graphics.Color\n{imp}\n")

# Find boundaries
start_idx = content.find("@Composable\nfun AnimatedMenuButton")
end_idx = content.find("@Composable\nfun LevelSelectScreen")

if start_idx != -1 and end_idx != -1:
    new_code = """@Composable
fun AnimatedMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: androidx.compose.material3.ButtonColors,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    border: androidx.compose.foundation.BorderStroke? = null,
    fontSize: androidx.compose.ui.unit.TextUnit = 16.sp,
    textColor: Color = Color.White,
    fontWeight: FontWeight? = FontWeight.SemiBold,
    entranceDelay: Int = 0,
    glossy: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.92f else 1f, label = "button_scale", animationSpec = tween(150))
    
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(entranceDelay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { 80 }, animationSpec = tween(600, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(600)),
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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
            if (glossy) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
                    .clip(shape)
                    .background(Brush.verticalGradient(listOf(Color.White.copy(alpha=0.3f), Color.Transparent)))
                )
            }
        }
    }
}

@Composable
fun FloatingTile(color1: Color, color2: Color, size: androidx.compose.ui.unit.Dp, offsetX: androidx.compose.ui.unit.Dp, offsetY: androidx.compose.ui.unit.Dp, animOffset: Float, rotationZ: Float, rotationX: Float = 55f, delay: Int = 0) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delay.toLong())
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(1000, delayMillis = delay)),
        modifier = Modifier.offset(x = offsetX, y = offsetY + animOffset.dp)
    ) {
        Box(modifier = Modifier
            .size(size)
            .graphicsLayer {
                this.rotationZ = rotationZ
                this.rotationX = rotationX
                this.rotationY = 15f
                this.shadowElevation = 40f
            }
            .background(
                Brush.linearGradient(listOf(color1, color2)),
                RoundedCornerShape(24.dp)
            )
            .border(2.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(Color.White.copy(alpha=0.5f), Color.Transparent)))
            )
        }
    }
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

    val infiniteTransition = rememberInfiniteTransition(label = "lobby_anim")
    
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)),
        label = "time"
    )
    
    val floatAnim1 by infiniteTransition.animateFloat(initialValue = -15f, targetValue = 15f, animationSpec = infiniteRepeatable(tween(3000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f1")
    val floatAnim2 by infiniteTransition.animateFloat(initialValue = 15f, targetValue = -15f, animationSpec = infiniteRepeatable(tween(4000, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f2")
    val floatAnim3 by infiniteTransition.animateFloat(initialValue = -10f, targetValue = 20f, animationSpec = infiniteRepeatable(tween(3500, easing = FastOutSlowInEasing), RepeatMode.Reverse), label = "f3")

    val sweep by infiniteTransition.animateFloat(
        initialValue = -800f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(tween(4000, delayMillis = 1000, easing = LinearEasing), RepeatMode.Restart),
        label = "title_sweep"
    )

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF030712))) {
        // Ambient Lighting
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize().blur(100.dp)) {
            val w = size.width
            val h = size.height
            val cx1 = w * 0.5f + (Math.cos(time.toDouble()) * w * 0.3f).toFloat()
            val cy1 = h * 0.3f + (Math.sin(time.toDouble()) * h * 0.2f).toFloat()
            
            val cx2 = w * 0.5f + (Math.cos((time + Math.PI).toDouble()) * w * 0.4f).toFloat()
            val cy2 = h * 0.7f + (Math.sin((time + Math.PI).toDouble()) * h * 0.2f).toFloat()

            drawCircle(color = Color(0xFF3B0764).copy(alpha = 0.5f), radius = w * 0.6f, center = Offset(cx1, cy1))
            drawCircle(color = Color(0xFF0C4A6E).copy(alpha = 0.5f), radius = w * 0.7f, center = Offset(cx2, cy2))
        }
        
        // Soft Particles
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val seed = 12345
            val random = java.util.Random(seed.toLong())
            for (i in 0 until 40) {
                val startX = random.nextFloat() * size.width
                val startY = random.nextFloat() * size.height
                val speedY = random.nextFloat() * 1.5f + 0.5f
                val phase = random.nextFloat() * 2 * Math.PI.toFloat()
                val radius = random.nextFloat() * 4f + 2f
                
                val currentY = (startY - (time * 50f * speedY)) % size.height
                val y = if (currentY < 0) currentY + size.height else currentY
                val x = startX + (Math.sin((time + phase).toDouble()) * 30f).toFloat()
                
                drawCircle(color = Color.White.copy(alpha = 0.2f), radius = radius, center = Offset(x, y))
            }
        }

        // Background floating puzzle tiles
        Box(modifier = Modifier.fillMaxWidth().height(350.dp).align(Alignment.TopCenter)) {
            FloatingTile(Color(0xFF00E5FF), Color(0xFF007799), 120.dp, 80.dp, 20.dp, floatAnim1, 35f, delay = 200)
            FloatingTile(Color(0xFFD500F9), Color(0xFF6A0080), 100.dp, (-110).dp, 100.dp, floatAnim2, -20f, delay = 400)
            FloatingTile(Color(0xFFFF3D00), Color(0xFF990000), 140.dp, 0.dp, 140.dp, floatAnim3, 15f, rotationX = 65f, delay = 600)
            FloatingTile(Color(0xFF00E5FF), Color(0xFF007799), 80.dp, (-70).dp, 250.dp, floatAnim1, 45f, delay = 800)
            FloatingTile(Color(0xFFD500F9), Color(0xFF6A0080), 90.dp, 90.dp, 210.dp, floatAnim2, -40f, delay = 1000)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 100.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val titleBrush = Brush.linearGradient(
                colors = listOf(Color.White, Color.White, Color(0xFFE2E8F0), Color(0xFF38BDF8), Color.White, Color.White),
                start = Offset(sweep, 0f),
                end = Offset(sweep + 600f, 0f)
            )
            
            Box {
                Text(
                    "ONE SHIFT", 
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color.Black.copy(alpha = 0.5f),
                        fontSize = 54.sp, 
                        fontWeight = FontWeight.Black, 
                        letterSpacing = (-2).sp
                    ),
                    modifier = Modifier.offset(x = 2.dp, y = 4.dp)
                )
                Text(
                    "ONE SHIFT", 
                    style = androidx.compose.ui.text.TextStyle(
                        brush = titleBrush,
                        fontSize = 54.sp, 
                        fontWeight = FontWeight.Black, 
                        letterSpacing = (-2).sp
                    )
                )
            }
            
            Box(modifier = Modifier
                .padding(top = 8.dp, bottom = 48.dp)
                .height(6.dp)
                .width(100.dp)
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF00E5FF), Color(0xFFD500F9))),
                    RoundedCornerShape(3.dp)
                )
            )
            
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(32.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    var delayStep = 100

                    Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                        AnimatedMenuButton(
                            text = stringResource(R.string.daily_shift),
                            onClick = onDailyShift,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                            textColor = Color(0xFF0F172A),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            entranceDelay = delayStep,
                            glossy = true
                        )
                    }
                    delayStep += 100
                    
                    if (hasCampaign) {
                        Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                            AnimatedMenuButton(
                                text = stringResource(R.string.continue_game),
                                onClick = onContinue,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)),
                                textColor = Color(0xFF0F172A),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                entranceDelay = delayStep,
                                glossy = true
                            )
                        }
                        delayStep += 100
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            entranceDelay = delayStep
                        )
                    }
                    delayStep += 100
                    
                    Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                        AnimatedMenuButton(
                            text = stringResource(R.string.select_level),
                            onClick = onSelectLevel,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            entranceDelay = delayStep
                        )
                    }
                    delayStep += 100
                    
                    if (isAdFree) {
                        Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                            AnimatedMenuButton(
                                text = stringResource(R.string.ads_removed_check),
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                                textColor = Color(0xFF38E887),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38E887).copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(50),
                                entranceDelay = delayStep
                            )
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                            AnimatedMenuButton(
                                text = stringResource(R.string.remove_ads),
                                onClick = onRemoveAds,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00BCD4).copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(50),
                                entranceDelay = delayStep
                            )
                        }
                    }
                    delayStep += 100
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Box(modifier = Modifier.weight(1f).height(56.dp)) {
                            AnimatedMenuButton(
                                text = stringResource(R.string.settings_short),
                                onClick = onSettings,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                                fontSize = 14.sp,
                                entranceDelay = delayStep
                            )
                        }
                        Box(modifier = Modifier.weight(1f).height(56.dp)) {
                            AnimatedMenuButton(
                                text = stringResource(R.string.about_game),
                                onClick = onAbout,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1f)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                                fontSize = 14.sp,
                                entranceDelay = delayStep
                            )
                        }
                    }
                    delayStep += 100
                    
                    Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                        AnimatedMenuButton(
                            text = stringResource(R.string.exit_game),
                            onClick = { showExitAppDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.05f)),
                            textColor = Color(0xFFFF7F50),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFF7F50).copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(50),
                            entranceDelay = delayStep
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LevelSelectScreen"""
    
    new_content = content[:start_idx] + new_code + content[end_idx + 31:]
    with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
        f.write(new_content)
else:
    print("Could not find boundaries")

