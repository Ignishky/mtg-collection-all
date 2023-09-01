package fr.ignishky.mtgcollection.infrastructure

import java.io.File
import kotlin.text.Charsets.UTF_8

object TestUtils {

    fun readFile(fileName: String): String {
        return File("src/test/resources/$fileName").readText(UTF_8)
    }

}
