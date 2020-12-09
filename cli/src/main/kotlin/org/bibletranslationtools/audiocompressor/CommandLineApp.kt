package org.bibletranslationtools.audiocompressor

import org.bibletranslationtools.audiocompressor.audio.Format
import picocli.CommandLine.Option
import picocli.CommandLine.Command
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

@Command(name = "Audio Compressor")
class CommandLineApp : Runnable {

    private val logger: Logger = Logger.getLogger(javaClass.name)
    private val controller = CommandLineController()

    @Option(names = ["-i", "--input"], required = true, description = ["Input file or directory to convert"])
    lateinit var input: File

    @Option(names = ["-f", "--format"], required = true, description = ["Target format (MP3 or WAV) to convert to"])
    lateinit var format: Format

    @Option(names = ["-b", "--bitrate"], description = ["Target bitrate (mp3 only)"])
    var bitrate = "64"

    @Option(names = ["-d", "--delete"], description = ["Delete source file after conversion"])
    var delete = false

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display a help"])
    private var helpRequested = false

    private fun execute() {
        when {
            !input.exists() -> {
                logger.log(Level.WARNING, "Input does not exist")
            }
            input.isDirectory -> {
                controller.convertDir(input, format, bitrate, delete)
            }
            input.isFile -> {
                when (format) {
                    Format.MP3 -> controller.wavToMp3(input, bitrate, delete)
                    Format.WAV -> controller.mp3ToWav(input, delete)
                }
            }
        }
    }

    override fun run() {
        execute()
    }
}
