package com.example

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaPlayer
import android.media.SoundPool
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sin

class GameAudioManager private constructor(baseContext: Context) {
    private val context = baseContext

    private val prefs = context.getSharedPreferences("OneShiftPrefs", Context.MODE_PRIVATE)

    private val _musicVolume = MutableStateFlow(prefs.getFloat("music_volume", 0.35f))
    val musicVolume: StateFlow<Float> = _musicVolume

    private val _sfxVolume = MutableStateFlow(prefs.getFloat("sfx_volume", 0.7f))
    val sfxVolume: StateFlow<Float> = _sfxVolume

    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    
    private var isAppInForeground = true
    private var slideSoundId = -1
    private var solveSoundId = -1
    
    init {

        

        initSoundPool(context)
        initMediaPlayer(context)
    }

    private fun initSoundPool(context: Context) {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(attributes)
            .build()
        
        slideSoundId = soundPool?.load(generateSlideWav(context).absolutePath, 1) ?: -1
        solveSoundId = soundPool?.load(generateSolveWav(context).absolutePath, 1) ?: -1
    }

    private var currentMusicResId: Int = R.raw.menu_theme
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
    }

    fun setMusicVolume(volume: Float) {
        val v = volume.coerceIn(0f, 1f)
        _musicVolume.value = v
        mediaPlayer?.setVolume(v, v)
        if (v > 0f && isAppInForeground && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        } else if (v <= 0f && mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    fun setSfxVolume(volume: Float) {
        val v = volume.coerceIn(0f, 1f)
        _sfxVolume.value = v
    }
    
    fun playTestSfx() {
        playSlide()
    }

    fun saveSettings() {
        prefs.edit()
            .putFloat("music_volume", _musicVolume.value)
            .putFloat("sfx_volume", _sfxVolume.value)
            .apply()
    }

    fun restoreDefaults() {
        setMusicVolume(0.35f)
        setSfxVolume(0.7f)
    }

    fun playSlide() {
        if (_sfxVolume.value <= 0f) return
        soundPool?.play(slideSoundId, _sfxVolume.value, _sfxVolume.value, 1, 0, 1f)
    }

    fun playSolve(stars: Int) {
        if (_sfxVolume.value <= 0f) return
        // Cling
        soundPool?.play(solveSoundId, _sfxVolume.value, _sfxVolume.value, 1, 0, 1f)
        
        if (stars == 5) {
            // delay and play 2 extra notes?
            // Actually, soundPool doesn't have a good way to delay. 
            // We can just rely on the solve wav having the extra notes or generate a separate 5-star wav.
            // For simplicity, let's just let it be cling for now, or play it with higher pitch.
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                soundPool?.play(solveSoundId, _sfxVolume.value, _sfxVolume.value, 1, 0, 1.2f)
            }, 300)
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                soundPool?.play(solveSoundId, _sfxVolume.value, _sfxVolume.value, 1, 0, 1.5f)
            }, 600)
        }
    }

    fun onAppForeground() {
        isAppInForeground = true
        if (_musicVolume.value > 0f) {
            mediaPlayer?.start()
        }
    }

    fun onAppBackground() {
        isAppInForeground = false
        mediaPlayer?.pause()
    }
    
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        soundPool?.release()
        soundPool = null
        instance = null
    }

    companion object {
        @Volatile private var instance: GameAudioManager? = null
        fun getInstance(context: Context): GameAudioManager =
            instance ?: synchronized(this) {
                instance ?: GameAudioManager(context.applicationContext).also { instance = it }
            }
            
        private fun writeWavFile(file: File, sampleRate: Int, numSamples: Int, buffer: ShortArray) {
            FileOutputStream(file).use { out ->
                val channels = 1
                val bitsPerSample = 16
                val byteRate = sampleRate * channels * bitsPerSample / 8
                
                val header = ByteArray(44)
                header[0] = 'R'.code.toByte()
                header[1] = 'I'.code.toByte()
                header[2] = 'F'.code.toByte()
                header[3] = 'F'.code.toByte()
                
                val chunkSize = 36 + numSamples * 2
                header[4] = (chunkSize and 0xff).toByte()
                header[5] = (chunkSize shr 8 and 0xff).toByte()
                header[6] = (chunkSize shr 16 and 0xff).toByte()
                header[7] = (chunkSize shr 24 and 0xff).toByte()
                
                header[8] = 'W'.code.toByte()
                header[9] = 'A'.code.toByte()
                header[10] = 'V'.code.toByte()
                header[11] = 'E'.code.toByte()
                
                header[12] = 'f'.code.toByte()
                header[13] = 'm'.code.toByte()
                header[14] = 't'.code.toByte()
                header[15] = ' '.code.toByte()
                
                header[16] = 16
                header[17] = 0
                header[18] = 0
                header[19] = 0
                
                header[20] = 1
                header[21] = 0
                
                header[22] = channels.toByte()
                header[23] = 0
                
                header[24] = (sampleRate and 0xff).toByte()
                header[25] = (sampleRate shr 8 and 0xff).toByte()
                header[26] = (sampleRate shr 16 and 0xff).toByte()
                header[27] = (sampleRate shr 24 and 0xff).toByte()
                
                header[28] = (byteRate and 0xff).toByte()
                header[29] = (byteRate shr 8 and 0xff).toByte()
                header[30] = (byteRate shr 16 and 0xff).toByte()
                header[31] = (byteRate shr 24 and 0xff).toByte()
                
                header[32] = (channels * bitsPerSample / 8).toByte()
                header[33] = 0
                
                header[34] = bitsPerSample.toByte()
                header[35] = 0
                
                header[36] = 'd'.code.toByte()
                header[37] = 'a'.code.toByte()
                header[38] = 't'.code.toByte()
                header[39] = 'a'.code.toByte()
                
                val dataSize = numSamples * 2
                header[40] = (dataSize and 0xff).toByte()
                header[41] = (dataSize shr 8 and 0xff).toByte()
                header[42] = (dataSize shr 16 and 0xff).toByte()
                header[43] = (dataSize shr 24 and 0xff).toByte()
                
                out.write(header, 0, 44)
                
                val data = ByteArray(numSamples * 2)
                for (i in 0 until numSamples) {
                    val s = buffer[i].toInt()
                    data[i * 2] = (s and 0xff).toByte()
                    data[i * 2 + 1] = (s shr 8 and 0xff).toByte()
                }
                out.write(data)
            }
        }

        private fun generateSlideWav(context: Context): File {
            val file = File(context.cacheDir, "slide.wav")
            if (file.exists()) return file
            
            val sampleRate = 44100
            val durationMs = 150
            val numSamples = (sampleRate * durationMs / 1000)
            val buffer = ShortArray(numSamples)
            
            // filtered noise with envelope
            var lastVal = 0.0
            for (i in 0 until numSamples) {
                val noise = (Math.random() * 2.0 - 1.0)
                lastVal = lastVal + 0.1 * (noise - lastVal) // lowpass filter
                val env = Math.sin(Math.PI * i / numSamples)
                buffer[i] = (lastVal * env * Short.MAX_VALUE * 0.3).toInt().toShort()
            }
            writeWavFile(file, sampleRate, numSamples, buffer)
            return file
        }

        private fun generateSolveWav(context: Context): File {
            val file = File(context.cacheDir, "solve.wav")
            if (file.exists()) return file
            
            val sampleRate = 44100
            val durationMs = 800
            val numSamples = (sampleRate * durationMs / 1000)
            val buffer = ShortArray(numSamples)
            
            // Cling (layered sine waves)
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                val env = Math.exp(-4.0 * t)
                val freq1 = 880.0
                val freq2 = 1760.0
                val s = (Math.sin(2.0 * Math.PI * freq1 * t) + 0.5 * Math.sin(2.0 * Math.PI * freq2 * t)) * env
                buffer[i] = (s * Short.MAX_VALUE * 0.3).toInt().toShort()
            }
            writeWavFile(file, sampleRate, numSamples, buffer)
            return file
        }
        
        private fun generateAmbientWav(context: Context): File {
            val file = File(context.cacheDir, "ambient.wav")
            if (file.exists()) return file
            
            val sampleRate = 44100
            val durationMs = 5000 // 5 seconds loop
            val numSamples = (sampleRate * durationMs / 1000)
            val buffer = ShortArray(numSamples)
            
            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate
                // gentle pad, low frequency
                val freq1 = 110.0
                val freq2 = 112.0
                val s = (Math.sin(2.0 * Math.PI * freq1 * t) + Math.sin(2.0 * Math.PI * freq2 * t)) * 0.5
                val env = Math.sin(Math.PI * i / numSamples) // simple envelope to avoid click on loop
                buffer[i] = (s * env * Short.MAX_VALUE * 0.2).toInt().toShort()
            }
            writeWavFile(file, sampleRate, numSamples, buffer)
            return file
        }
    }
}
