import xml.etree.ElementTree as ET

tree = ET.parse('app/src/main/res/values/strings.xml')
root = tree.getroot()

for child in root:
    if child.text and ('%1$d' in child.text or '%1$s' in child.text):
        print(f"{child.attrib['name']}: {child.text}")
