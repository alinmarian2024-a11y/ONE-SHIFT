import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_dialog = """fun CompletionDialog(
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
                text = stringResource(R.string.level_title, level),
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
                Text(stringResource(R.string.best_result, bestMoves), color = Color(0xFF38E887), fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onNextLevel,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0BCFF)),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.next_level), color = Color(0xFF0F172A), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onReplay,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(stringResource(R.string.retry_level), color = Color.White)
            }
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = onMenu) {
                    Text(stringResource(R.string.main_menu), color = Color(0xFFCAC4D0), fontSize = 12.sp)
                }
                TextButton(onClick = onSettings) {
                    Text(stringResource(R.string.settings_short), color = Color(0xFFCAC4D0), fontSize = 12.sp)
                }
            }
        }
    }
}"""

new_dialog = """fun CompletionDialog(
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
    var showCelebration by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        for (i in 1..5) {
            delay(120)
            if (i <= stars) animatedStarCount = i
        }
        delay(200)
        showCelebration = true
    }
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
        val hasGlow = animatedStarCount == 5
        val celebrationAlpha by animateFloatAsState(
            targetValue = if (showCelebration && hasGlow) 0.15f else 0f,
            animationSpec = tween(1000),
            label = "celebration_glow"
        )
        
        if (celebrationAlpha > 0f) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(Color(0xFFFFD700).copy(alpha = celebrationAlpha), Color.Transparent)
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
        
        Column(
            modifier = Modifier
                .padding(24.dp)
                .shadow(if (hasGlow) 32.dp else 16.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black, spotColor = if (hasGlow) Color(0xFFFFD700) else Color(0xFF0F172A))
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1E293B).copy(alpha = 0.95f),
                            Color(0xFF0F172A).copy(alpha = 0.95f)
                        )
                    )
                )
                .border(
                    width = if (hasGlow) 2.dp else 1.dp, 
                    color = if (hasGlow) Color(0xFFFFD700).copy(alpha = 0.5f) else Color.White.copy(alpha = 0.1f), 
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = getStarMessage(stars),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                style = androidx.compose.ui.text.TextStyle(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(0f, 4f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = stringResource(R.string.level_title, level),
                color = Color(0xFF94A3B8),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                for (i in 1..5) {
                    val isEarned = i <= stars
                    val isAnimated = i <= animatedStarCount
                    val scale by animateFloatAsState(
                        targetValue = if (isAnimated) 1.25f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
                        label = "star_scale_$i"
                    )
                    val rotation by animateFloatAsState(
                        targetValue = if (isAnimated) 360f else 0f,
                        animationSpec = tween(500, easing = androidx.compose.animation.core.FastOutSlowInEasing),
                        label = "star_rot_$i"
                    )
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp)) {
                        Text(
                            text = "★",
                            color = if (isAnimated) Color(0xFFFFD700) else Color(0xFF334155),
                            fontSize = 38.sp,
                            modifier = Modifier
                                .scale(if (isEarned) scale else 1f)
                                .androidx.compose.ui.graphics.graphicsLayer { rotationZ = if(isEarned) rotation else 0f }
                        )
                    }
                }
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A).copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.moves_used, movesUsed), color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(stringResource(R.string.recommended_moves, recommendedMoves), color = Color(0xFF94A3B8), fontSize = 13.sp)
                    if (bestMoves > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.best_result, bestMoves), color = Color(0xFF38E887), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onNextLevel,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF38E887),
                    contentColor = Color(0xFF0F172A)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.next_level), fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onReplay,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text(stringResource(R.string.retry_level), color = Color.White)
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                TextButton(onClick = onMenu) {
                    Text(stringResource(R.string.main_menu), color = Color(0xFF94A3B8), fontSize = 13.sp)
                }
                TextButton(onClick = onSettings) {
                    Text(stringResource(R.string.settings_short), color = Color(0xFF94A3B8), fontSize = 13.sp)
                }
            }
        }
    }
}"""

content = content.replace(old_dialog, new_dialog)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)

