import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

old_getStarMessage = """fun getStarMessage(stars: Int): String = when(stars) {
    5 -> "PERFECT SHIFT! Minte ascuțită!"
    4 -> "Excelent! Foarte aproape de perfect!"
    3 -> "Foarte bine! Nivel rezolvat!"
    2 -> "Ai reușit! Poți obține și mai mult!"
    else -> "Nivel terminat! Reîncearcă pentru mai multe stele!"
}"""
new_getStarMessage = """@Composable
fun getStarMessage(stars: Int): String = when(stars) {
    5 -> stringResource(R.string.star_msg_5)
    4 -> stringResource(R.string.star_msg_4)
    3 -> stringResource(R.string.star_msg_3)
    2 -> stringResource(R.string.star_msg_2)
    else -> stringResource(R.string.star_msg_1)
}"""
content = content.replace(old_getStarMessage, new_getStarMessage)

content = content.replace('text = "Nivelul $level"', 'text = stringResource(R.string.level_title, level)')
content = content.replace('Text(if (appLanguage == "ro") "🌐 RO" else "🌐 EN", color = Color.White)', 'Text(if (appLanguage == "ro") "🌐 RO" else "🌐 EN", color = Color.White)') # Kept same

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
