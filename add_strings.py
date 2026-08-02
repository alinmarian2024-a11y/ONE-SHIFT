import os
import re

new_strings = {
    "en": {
        "tutorial_swipe_row": "Swipe the indicated row to recreate the pattern",
        "swipe_to_recreate_upper": "SWIPE TO RECREATE THE PATTERN"
    },
    "ro": {
        "tutorial_swipe_row": "Glisează rândul indicat pentru a reface modelul",
        "swipe_to_recreate_upper": "GLISEAZĂ PENTRU A REFACE MODELUL"
    },
    "es": {
        "tutorial_swipe_row": "Desliza la fila indicada para recrear el patrón",
        "swipe_to_recreate_upper": "DESLIZA PARA RECREAR EL PATRÓN"
    },
    "it": {
        "tutorial_swipe_row": "Scorri la riga indicata per ricreare il modello",
        "swipe_to_recreate_upper": "SCORRI PER RICREARE IL MODELLO"
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

text = text.replace('text = "Glisează rândul indicat pentru a reface modelul"', 'text = stringResource(R.string.tutorial_swipe_row)')
text = text.replace('text = "GLISEAZĂ PENTRU A REFACE MODELUL"', 'text = stringResource(R.string.swipe_to_recreate_upper)')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)

