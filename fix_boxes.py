with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Replace Box for Target Model
content = content.replace('.background(Color(0xFF334155))', '.background(Color(0xFF1E293B).copy(alpha = 0.7f)).border(1.dp, Color.White.copy(alpha=0.1f), RoundedCornerShape(8.dp))', 1)

# Replace Box for Game Board
# The other one was clip(RoundedCornerShape(28.dp))
content = content.replace('.clip(RoundedCornerShape(28.dp))\n                            .background(Color(0xFF334155))', '.clip(RoundedCornerShape(24.dp))\n                            .background(Color(0xFF1E293B).copy(alpha = 0.8f))\n                            .border(2.dp, Color.White.copy(alpha=0.05f), RoundedCornerShape(24.dp))')

# There might be another 28.dp clip. Let's fix button shapes.
content = content.replace('Color(0xFF381E72)', 'Color(0xFF0F172A)') # Continue button text
with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
