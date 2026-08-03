with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    text = f.read()

target = 'val hintRepository = remember { HintRepository.getInstance(context) }'
replacement = 'val hintRepository = remember { HintRepository.getInstance(context) }\n    val noMovesResetMsg = stringResource(R.string.no_moves_reset)'
text = text.replace(target, replacement)

text = text.replace('HintEventBus.emitEvent("Nu există o mutare garantată. Folosește Reset!")',
                    'HintEventBus.emitEvent(noMovesResetMsg)')

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(text)
