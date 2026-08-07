import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

imports_to_add = """
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Offset
import androidx.compose.ui.graphics.Shadow
"""
content = content.replace("import androidx.compose.ui.graphics.Color", "import androidx.compose.ui.graphics.Color" + imports_to_add)

# Change GameScreen background
content = content.replace('.background(Color(0xFF1C1B1F))', '.background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF020617))))')

# Change Buttons
content = content.replace('Color(0xFF49454F)', 'Color(0xFF334155)')
content = content.replace('colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155))', 'colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B)), shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155).copy(alpha = 0.5f))')

old_game_piece = """fun GamePiece(piece: PieceType, size: Dp) {
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
}"""

new_game_piece = """fun GamePiece(piece: PieceType, size: Dp) {
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
}"""

content = content.replace(old_game_piece, new_game_piece)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
