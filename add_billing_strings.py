import os

new_strings = {
    "en": {
        "purchase_already_active": "Purchase is already active on this Google Play account",
        "benefit_1": "• No banners",
        "benefit_2": "• No ads between levels",
        "benefit_3": "• Permanent purchase",
        "benefit_4": "• Automatic restore on the same Google Play account",
        "purchase_title": "Play without interruptions",
        "purchase_description": "Permanently remove banners and automatic ads. This is a one-time payment, no subscription.",
        "product_unavailable": "Product currently unavailable",
        "purchase_restored": "Purchase restored",
        "no_purchase_found": "No purchase found",
        "error_restoring_purchase": "Error restoring purchases",
        "purchase_canceled": "Purchase canceled",
        "purchase_error_code": "Purchase error: %1$d",
        "ads_removed_permanently": "Automatic ads have been permanently removed!",
        "purchase_pending": "Purchase pending",
        "test_price": "TEST PRICE"
    },
    "ro": {
        "purchase_already_active": "Achiziția este activă pe acest cont Google Play",
        "benefit_1": "• Fără bannere",
        "benefit_2": "• Fără reclame între niveluri",
        "benefit_3": "• Achiziție permanentă",
        "benefit_4": "• Restaurare automată pe același cont Google Play",
        "purchase_title": "Joacă fără întreruperi",
        "purchase_description": "Elimină permanent bannerele și reclamele afișate automat. Este o singură plată, fără abonament.",
        "product_unavailable": "Produs indisponibil momentan",
        "purchase_restored": "Achiziție restaurată",
        "no_purchase_found": "Nu a fost găsită nicio achiziție",
        "error_restoring_purchase": "Eroare la restaurarea achizițiilor",
        "purchase_canceled": "Cumpărare anulată",
        "purchase_error_code": "Eroare la cumpărare: %1$d",
        "ads_removed_permanently": "Reclamele automate au fost eliminate permanent!",
        "purchase_pending": "Achiziție în așteptare",
        "test_price": "PREȚ TEST"
    },
    "es": {
        "purchase_already_active": "La compra ya está activa en esta cuenta de Google Play",
        "benefit_1": "• Sin anuncios",
        "benefit_2": "• Sin anuncios entre niveles",
        "benefit_3": "• Compra permanente",
        "benefit_4": "• Restauración automática en la misma cuenta de Google Play",
        "purchase_title": "Juega sin interrupciones",
        "purchase_description": "Elimina permanentemente los banners y los anuncios automáticos. Es un pago único, sin suscripción.",
        "product_unavailable": "Producto no disponible actualmente",
        "purchase_restored": "Compra restaurada",
        "no_purchase_found": "No se ha encontrado ninguna compra",
        "error_restoring_purchase": "Error al restaurar las compras",
        "purchase_canceled": "Compra cancelada",
        "purchase_error_code": "Error de compra: %1$d",
        "ads_removed_permanently": "¡Los anuncios automáticos se han eliminado de forma permanente!",
        "purchase_pending": "Compra pendiente",
        "test_price": "PRECIO DE PRUEBA"
    },
    "it": {
        "purchase_already_active": "L'acquisto è già attivo su questo account Google Play",
        "benefit_1": "• Nessun banner",
        "benefit_2": "• Nessun annuncio tra i livelli",
        "benefit_3": "• Acquisto permanente",
        "benefit_4": "• Ripristino automatico sullo stesso account Google Play",
        "purchase_title": "Gioca senza interruzioni",
        "purchase_description": "Rimuovi permanentemente banner e annunci automatici. Si tratta di un pagamento unico, nessun abbonamento.",
        "product_unavailable": "Prodotto attualmente non disponibile",
        "purchase_restored": "Acquisto ripristinato",
        "no_purchase_found": "Nessun acquisto trovato",
        "error_restoring_purchase": "Errore durante il ripristino degli acquisti",
        "purchase_canceled": "Acquisto annullato",
        "purchase_error_code": "Errore di acquisto: %1$d",
        "ads_removed_permanently": "Gli annunci automatici sono stati rimossi in modo permanente!",
        "purchase_pending": "Acquisto in sospeso",
        "test_price": "PREZZO DI PROVA"
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

with open("app/src/main/java/com/example/BillingRepository.kt", "r") as f:
    text = f.read()

replacements = {
    '"Achiziția este activă pe acest cont Google Play"': "context.getString(R.string.purchase_already_active)",
    '"• Fără bannere"': "context.getString(R.string.benefit_1)",
    '"• Fără reclame între niveluri"': "context.getString(R.string.benefit_2)",
    '"• Achiziție permanentă"': "context.getString(R.string.benefit_3)",
    '"• Restaurare automată pe același cont Google Play"': "context.getString(R.string.benefit_4)",
    '"Joacă fără întreruperi"': "context.getString(R.string.purchase_title)",
    '"Elimină permanent bannerele și reclamele afișate automat. Este o singură plată, fără abonament."': "context.getString(R.string.purchase_description)",
    '"Produs indisponibil momentan"': "context.getString(R.string.product_unavailable)",
    '"Achiziție restaurată"': "context.getString(R.string.purchase_restored)",
    '"Nu a fost găsită nicio achiziție"': "context.getString(R.string.no_purchase_found)",
    '"Eroare la restaurarea achizițiilor"': "context.getString(R.string.error_restoring_purchase)",
    '"Cumpărare anulată"': "context.getString(R.string.purchase_canceled)",
    '"Eroare la cumpărare: ${billingResult.responseCode}"': "context.getString(R.string.purchase_error_code, billingResult.responseCode)",
    '"Reclamele automate au fost eliminate permanent!"': "context.getString(R.string.ads_removed_permanently)",
    '"Achiziție în așteptare"': "context.getString(R.string.purchase_pending)",
    '"PREȚ TEST"': "context.getString(R.string.test_price)"
}

for k, v in replacements.items():
    text = text.replace(k, v)

with open("app/src/main/java/com/example/BillingRepository.kt", "w") as f:
    f.write(text)

