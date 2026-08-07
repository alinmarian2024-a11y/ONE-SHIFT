with open("app/src/main/java/com/example/GameAudioManager.kt", "r") as f:
    content = f.read()

old_init = """    private fun initMediaPlayer(context: Context) {
        try {
            mediaPlayer = MediaPlayer().apply {
                val afd = context.resources.openRawResourceFd(R.raw.background_music)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = true
                setVolume(_musicVolume.value, _musicVolume.value)
                prepare()
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }"""

new_init = """    private var currentMusicResId: Int = R.raw.menu_theme
    private val gameplayTracks = listOf(R.raw.gameplay_track_01, R.raw.gameplay_track_02, R.raw.gameplay_track_03)
    private var currentGameplayTrackIndex = 0
    private var isPlayingGameplay = false

    private fun initMediaPlayer(context: Context) {
        playMenuTheme()
    }

    private fun playMusic(resId: Int, loop: Boolean = true) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                val afd = context.resources.openRawResourceFd(resId)
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                isLooping = loop
                setVolume(_musicVolume.value, _musicVolume.value)
                if (!loop) {
                    setOnCompletionListener {
                        playNextGameplayTrack()
                    }
                }
                prepare()
                if (_musicVolume.value > 0f && isAppInForeground) {
                    start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playNextGameplayTrack() {
        if (!isPlayingGameplay) return
        currentGameplayTrackIndex = (currentGameplayTrackIndex + 1) % gameplayTracks.size
        currentMusicResId = gameplayTracks[currentGameplayTrackIndex]
        playMusic(currentMusicResId, loop = false)
    }

    fun playMenuTheme() {
        if (isPlayingGameplay || mediaPlayer == null) {
            isPlayingGameplay = false
            currentMusicResId = R.raw.menu_theme
            playMusic(R.raw.menu_theme, loop = true)
        }
    }

    fun playGameplayMusic() {
        if (!isPlayingGameplay) {
            isPlayingGameplay = true
            currentGameplayTrackIndex = gameplayTracks.indices.random()
            currentMusicResId = gameplayTracks[currentGameplayTrackIndex]
            playMusic(currentMusicResId, loop = false)
        }
    }"""

content = content.replace(old_init, new_init)

with open("app/src/main/java/com/example/GameAudioManager.kt", "w") as f:
    f.write(content)
