import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

fun String.parseIntPair(): Pair<Int, Int> {
    val (a, b) = split(Regex("\\s+"))
    return a.toInt() to b.toInt()
}

fun String.parseIntList(): List<Int> =
    split(Regex("\\s+")).map(String::toInt)

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> List<T>.removeIndices(vararg indices: Int): List<T> =
    removeIndices(indices.toSet())

fun <T> List<T>.removeIndices(indices: Set<Int>): List<T> =
    filterIndexed { index, _ -> index !in indices }
