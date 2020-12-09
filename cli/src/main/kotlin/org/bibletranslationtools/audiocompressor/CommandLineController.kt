package org.bibletranslationtools.audiocompressor

import io.reactivex.Completable
import org.bibletranslationtools.audiocompressor.audio.ConvertAudio
import org.bibletranslationtools.audiocompressor.audio.Format
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

class CommandLineController {
    private val logger: Logger = Logger.getLogger(javaClass.name)

    fun convertDir(dir: File, format: Format, bitrate: String, delete: Boolean) {
        ConvertAudio().convertDir(dir, format, bitrate, delete)
            .onErrorResumeNext {
                Completable.complete()
            }
            .doOnError { logger.log(Level.SEVERE, "Conversion failed! ${it.message}") }
            .doFinally {
                logger.log(Level.INFO, "Conversion complete!")
            }
            .subscribe()
    }

    fun wavToMp3(src: File, bitrate: String, delete: Boolean) {
        val targetFilename = src.name.substringBefore(".wav") + ".mp3"
        val target = File(src.parentFile, targetFilename)

        ConvertAudio().wavToMp3(src, target, bitrate, delete)
    }

    fun mp3ToWav(src: File, delete: Boolean) {
        val targetFilename = src.name.substringBefore(".mp3") + ".wav"
        val target = File(src.parentFile, targetFilename)

        val cueFilename = src.name.substringBefore(".mp3") + ".cue"
        val cue = File(src.parentFile, cueFilename)

        ConvertAudio().mp3ToWav(src, target, cue, delete)
    }
}
