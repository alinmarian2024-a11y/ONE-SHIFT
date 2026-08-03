import os
import xml.etree.ElementTree as ET

strings_map = {
    'values': {'no_moves_reset': 'No guaranteed move available. Use Reset!'},
    'values-ro': {'no_moves_reset': 'Nu există o mutare garantată. Folosește Reset!'},
    'values-es': {'no_moves_reset': 'No hay movimiento garantizado. ¡Usa Reiniciar!'},
    'values-it': {'no_moves_reset': 'Nessuna mossa garantita. Usa Reset!'}
}

for val_dir, strings in strings_map.items():
    path = f'app/src/main/res/{val_dir}/strings.xml'
    if not os.path.exists(path):
        continue
    tree = ET.parse(path)
    root = tree.getroot()
    
    for name, value in strings.items():
        exists = False
        for child in root:
            if child.attrib.get('name') == name:
                child.text = value
                exists = True
                break
        if not exists:
            elem = ET.Element('string', {'name': name})
            elem.text = value
            root.append(elem)
            
    tree.write(path, encoding='utf-8', xml_declaration=True)
