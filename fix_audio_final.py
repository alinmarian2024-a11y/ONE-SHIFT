with open("app/src/main/java/com/example/GameAudioManager.kt", "r") as f:
    text = f.read()

text = text.replace('        // initSoundPool()\n        // initMediaPlayer()', '        initSoundPool(attributedContext)\n        initMediaPlayer(attributedContext)')

text = text.replace('    private fun initSoundPool() {', '    private fun initSoundPool(context: Context) {')
text = text.replace('    private fun initMediaPlayer() {', '    private fun initMediaPlayer(context: Context) {')

with open("app/src/main/java/com/example/GameAudioManager.kt", "w") as f:
    f.write(text)
