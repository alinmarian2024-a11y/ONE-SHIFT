import os
import re

new_strings = {
    "en": {
        "star_msg_5": "PERFECT SHIFT! Sharp mind!",
        "star_msg_4": "Excellent! Very close to perfect!",
        "star_msg_3": "Very good! Level solved!",
        "star_msg_2": "You did it! You can do even better!",
        "star_msg_1": "Level completed! Try again for more stars!"
    },
    "ro": {
        "star_msg_5": "PERFECT SHIFT! Minte ascuțită!",
        "star_msg_4": "Excelent! Foarte aproape de perfect!",
        "star_msg_3": "Foarte bine! Nivel rezolvat!",
        "star_msg_2": "Ai reușit! Poți obține și mai mult!",
        "star_msg_1": "Nivel terminat! Reîncearcă pentru mai multe stele!"
    },
    "es": {
        "star_msg_5": "¡PERFECT SHIFT! ¡Mente aguda!",
        "star_msg_4": "¡Excelente! ¡Muy cerca de ser perfecto!",
        "star_msg_3": "¡Muy bien! ¡Nivel resuelto!",
        "star_msg_2": "¡Lo lograste! ¡Puedes hacerlo aún mejor!",
        "star_msg_1": "¡Nivel completado! ¡Inténtalo de nuevo para conseguir más estrellas!"
    },
    "it": {
        "star_msg_5": "PERFECT SHIFT! Mente acuta!",
        "star_msg_4": "Eccellente! Molto vicino alla perfezione!",
        "star_msg_3": "Molto bene! Livello risolto!",
        "star_msg_2": "Ce l'hai fatta! Puoi fare ancora meglio!",
        "star_msg_1": "Livello completato! Riprova per ottenere più stelle!"
    }
}

files = {
    "en": "app/src/main/res/values/strings.xml",
    "ro": "app/src/main/res/values-ro/strings.xml",
    "es": "app/src/main/res/values-es/strings.xml",
    "it": "app/src/main/res/values-it/strings.xml"
}

for lang, data in new_strings.items():
    filepath = files[lang]
    with open(filepath, "r") as f:
        content = f.read()
    
    insert_str = ""
    for k, v in data.items():
        escaped_v = v.replace("'", "\\'").replace("&", "&amp;")
        insert_str += f'    <string name="{k}">{escaped_v}</string>\n'
    
    content = content.replace("</resources>", insert_str + "</resources>")
    with open(filepath, "w") as f:
        f.write(content)

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    text = f.read()

replacement = """@Composable
fun getStarMessage(stars: Int): String = when(stars) {
    5 -> stringResource(R.string.star_msg_5)
    4 -> stringResource(R.string.star_msg_4)
    3 -> stringResource(R.string.star_msg_3)
    2 -> stringResource(R.string.star_msg_2)
    else -> stringResource(R.string.star_msg_1)
}"""

# Using regex to replace the old getStarMessage
text = re.sub(r'fun getStarMessage\(stars: Int\): String = when\(stars\) \{.*?\n\}', replacement, text, flags=re.DOTALL)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
