import xml.etree.ElementTree as ET

def append_strings(filepath, new_strings):
    tree = ET.parse(filepath)
    root = tree.getroot()
    for name, text in new_strings.items():
        child = ET.SubElement(root, "string", name=name)
        child.text = text
    ET.indent(tree, space="    ", level=0)
    tree.write(filepath, encoding="utf-8", xml_declaration=True)

en = {
    "moves_count": "Moves: %1$d / %2$d",
    "target_pattern": "TARGET PATTERN",
    "tutorial_swipe": "Swipe the indicated row to rebuild the pattern",
    "swipe_to_rebuild": "SWIPE TO REBUILD THE PATTERN",
    "no_guaranteed_move": "No guaranteed move available. Use Reset!",
    "free_hint": "FREE HINT",
    "hint_count": "HINT • %1$d",
    "watch_video_hint": "WATCH VIDEO • +1 HINT"
}
ro = {
    "moves_count": "Mutări: %1$d / %2$d",
    "target_pattern": "MODEL",
    "tutorial_swipe": "Glisează rândul indicat pentru a reface modelul",
    "swipe_to_rebuild": "GLISEAZĂ PENTRU A REFACE MODELUL",
    "no_guaranteed_move": "Nu există o mutare garantată. Folosește Reset!",
    "free_hint": "INDICIU GRATUIT",
    "hint_count": "INDICIU • %1$d",
    "watch_video_hint": "VEZI VIDEO • +1 INDICIU"
}

append_strings("app/src/main/res/values/strings.xml", en)
append_strings("app/src/main/res/values-ro/strings.xml", ro)
