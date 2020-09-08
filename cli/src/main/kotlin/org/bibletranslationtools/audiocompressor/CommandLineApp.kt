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

    @Option(names = ["-i", "--input"], description = ["Input file or directory to convert"])
    private val input: File? = null

    @Option(names = ["-f", "--format"], description = ["Target format (MP3 or WAV) to convert to"])
    private val format: Format? = null

    @Option(names = ["-h", "--help"], usageHelp = true, description = ["display a help"])
    private var helpRequested = false

    private fun execute() {
        if (format != null && input != null) {
            when {
                !input.exists() -> {
                    logger.log(Level.WARNING, "Input does not exist")
                }
                input.isDirectory -> {
                    controller.convertDir(input, format)
                }
                input.isFile -> {
                    when (format) {
                        Format.MP3 -> controller.wavToMp3(input)
                        Format.WAV -> controller.mp3ToWav(input)
                    }
                }
            }
        }
    }

    override fun run() {
        execute()
    }
}
