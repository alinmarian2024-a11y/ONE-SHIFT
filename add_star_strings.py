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
    "star_msg_5": "PERFECT SHIFT! Sharp mind!",
    "star_msg_4": "Excellent! Very close to perfect!",
    "star_msg_3": "Very good! Level solved!",
    "star_msg_2": "You did it! You can do even better!",
    "star_msg_1": "Level completed! Try again for more stars!",
    "level_title": "Level %1$d"
}
ro = {
    "star_msg_5": "PERFECT SHIFT! Minte ascuțită!",
    "star_msg_4": "Excelent! Foarte aproape de perfect!",
    "star_msg_3": "Foarte bine! Nivel rezolvat!",
    "star_msg_2": "Ai reușit! Poți obține și mai mult!",
    "star_msg_1": "Nivel terminat! Reîncearcă pentru mai multe stele!",
    "level_title": "Nivelul %1$d"
}

append_strings("app/src/main/res/values/strings.xml", en)
append_strings("app/src/main/res/values-ro/strings.xml", ro)
