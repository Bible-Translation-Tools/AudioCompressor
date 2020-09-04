package org.bibletranslationtools.audiocompressor

import picocli.CommandLine
import kotlin.system.exitProcess

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val exitCode = CommandLine(CommandLineApp())
            .setCaseInsensitiveEnumValuesAllowed(true)
            .execute(*args)
        exitProcess(exitCode)
    }
}
