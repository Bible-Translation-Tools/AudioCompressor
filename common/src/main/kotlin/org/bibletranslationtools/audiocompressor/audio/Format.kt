package org.bibletranslationtools.audiocompressor.audio

enum class Format(val value: String) {
    MP3("mp3"),
    WAV("wav");

    companion object {
        fun of(ext: String) =
            values().singleOrNull {
                it.name == ext.toUpperCase() || it.value == ext
            }
    }
}
