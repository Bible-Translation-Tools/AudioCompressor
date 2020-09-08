package org.bibletranslationtools.audiocompressor.audio

import io.reactivex.Completable
import io.reactivex.rxkotlin.toObservable
import javazoom.jl.converter.Converter
import org.bibletranslationtools.cuesheetmanager.CueSheetWriter
import org.bibletranslationtools.cuesheetmanager.CueWavWriter
import java.io.File
import de.sciss.jump3r.Main as jump3r

class ConvertAudio {

    fun convertDir(dir: File, to: Format): Completable {
        val walk = dir.walkTopDown().filter { it.isFile }
        return walk.toObservable()
            .concatMapCompletable { file ->
                Completable.fromCallable {
                    val from = Format.of(file.extension)
                    if (from != to) {
                        when (from) {
                            Format.MP3 -> mp3ToWav(
                                file,
                                File(file.parentFile, file.name.substringBefore(".mp3") + ".wav"),
                                File(file.parentFile, file.name.substringBefore(".mp3") + ".cue")
                            )
                            Format.WAV -> wavToMp3(
                                file,
                                File(file.parentFile, file.name.substringBefore(".wav") + ".mp3")
                            )
                        }
                    }
                }
            }
    }

    fun mp3ToWav(mp3: File, wav: File, cue: File?) {
        val c = Converter()
        c.convert(mp3.absolutePath, wav.absolutePath)
        mp3.delete()

        if (cue != null && cue.exists()) {
            CueWavWriter(cue).write()
            cue.delete()
        }
    }

    fun wavToMp3(wav: File, mp3: File) {
        val mp3Args = arrayOf(
            "-q", "9",
            "-m", "m",
            wav.absolutePath,
            mp3.absolutePath
        )
        jump3r().run(mp3Args)
        CueSheetWriter(wav).write()
        wav.delete()
    }
}
