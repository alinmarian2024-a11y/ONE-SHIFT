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
    "test_ad": "TEST AD (3s)"
}
ro = {
    "test_ad": "RECLAMĂ TEST (3s)"
}

append_strings("app/src/main/res/values/strings.xml", en)
append_strings("app/src/main/res/values-ro/strings.xml", ro)
