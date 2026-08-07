with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

content = content.replace('.clip(RoundedCornerShape(8.dp))\n                        .background(Color(0xFF1E293B).copy(alpha = 0.7f)).border(1.dp, Color.White.copy(alpha=0.1f), RoundedCornerShape(8.dp))\n                        .padding(6.dp)', '.shadow(8.dp, RoundedCornerShape(8.dp))\n                        .clip(RoundedCornerShape(8.dp))\n                        .background(Color(0xFF1E293B).copy(alpha = 0.7f))\n                        .border(1.dp, Color.White.copy(alpha=0.1f), RoundedCornerShape(8.dp))\n                        .padding(6.dp)')

content = content.replace('.clip(RoundedCornerShape(24.dp))\n                            .background(Color(0xFF1E293B).copy(alpha = 0.8f))\n                            .border(2.dp, Color.White.copy(alpha=0.05f), RoundedCornerShape(24.dp))\n                            .padding(12.dp)', '.shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black, spotColor = Color.Black)\n                            .clip(RoundedCornerShape(24.dp))\n                            .background(Color(0xFF1E293B).copy(alpha = 0.8f))\n                            .border(2.dp, Color.White.copy(alpha=0.05f), RoundedCornerShape(24.dp))\n                            .padding(12.dp)')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
