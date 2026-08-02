import os
import re

new_strings = {
    "en": {
        "cd_music": "Music",
        "cd_effects": "Sound Effects",
        "cd_exit_game": "Exit Game"
    },
    "ro": {
        "cd_music": "Muzică",
        "cd_effects": "Efecte",
        "cd_exit_game": "Ieșire Joc"
    },
    "es": {
        "cd_music": "Música",
        "cd_effects": "Efectos de sonido",
        "cd_exit_game": "Salir del juego"
    },
    "it": {
        "cd_music": "Musica",
        "cd_effects": "Effetti sonori",
        "cd_exit_game": "Uscire dal gioco"
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

text = text.replace('contentDescription = "Muzică"', 'contentDescription = stringResource(R.string.cd_music)')
text = text.replace('contentDescription = "Efecte"', 'contentDescription = stringResource(R.string.cd_effects)')
text = text.replace('contentDescription = "Ieșire Joc"', 'contentDescription = stringResource(R.string.cd_exit_game)')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(text)
