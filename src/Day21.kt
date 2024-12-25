import androidx.compose.ui.unit.IntOffset
import java.util.*

data class PartialState(
    val keypadPositions: List<IntOffset>,
    val output: List<NumericKey>,
)

val memoizedPartialStateUpdateMap = mutableMapOf<Pair<PartialState, DirectionalKey>, PartialState?>()

fun calculatePartialStateUpdateImpl(
    partialState: PartialState,
    directionalKey: DirectionalKey,
): PartialState? {
    return memoizedPartialStateUpdateMap.getOrPut(partialState to directionalKey) {
        return if (partialState.keypadPositions.size == 1) {
            val numericKeyPosition = partialState.keypadPositions.first()
            val numericKey = NumericKey.fromPosition(numericKeyPosition) ?: return null

            when (directionalKey) {
                DirectionalKey.A -> {
                    PartialState(
                        keypadPositions = listOf(numericKeyPosition),
                        output = partialState.output + numericKey,
                    )
                }

                DirectionalKey.Down -> {
                    val newPosition = numericKeyPosition + IntOffset(0, 1)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = listOf(newPosition),
                        output = partialState.output,
                    )
                }

                DirectionalKey.Left -> {
                    val newPosition = numericKeyPosition + IntOffset(-1, 0)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = listOf(newPosition),
                        output = partialState.output,
                    )
                }

                DirectionalKey.Right -> {
                    val newPosition = numericKeyPosition + IntOffset(1, 0)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = listOf(newPosition),
                        output = partialState.output,
                    )
                }

                DirectionalKey.Up -> {
                    val newPosition = numericKeyPosition + IntOffset(0, -1)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = listOf(newPosition),
                        output = partialState.output,
                    )
                }
            }
        } else {
            val embeddedDirectionalKeyPosition = partialState.keypadPositions.last()
            val embeddedDirectionalKey = DirectionalKey.fromPosition(embeddedDirectionalKeyPosition) ?: return null

            when (directionalKey) {
                DirectionalKey.A -> {
                    val embeddedPartialState = calculatePartialStateUpdate(
                        PartialState(
                            keypadPositions = partialState.keypadPositions.dropLast(1),
                            output = partialState.output,
                        ),
                        directionalKey = embeddedDirectionalKey,
                    ) ?: return null

                    PartialState(
                        keypadPositions = embeddedPartialState.keypadPositions + embeddedDirectionalKeyPosition,
                        output = embeddedPartialState.output,
                    )
                }

                DirectionalKey.Down -> {
                    val newPosition = embeddedDirectionalKeyPosition + IntOffset(0, 1)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = partialState.keypadPositions.dropLast(1) + newPosition,
                        output = partialState.output,
                    )
                }

                DirectionalKey.Left -> {
                    val newPosition = embeddedDirectionalKeyPosition + IntOffset(-1, 0)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = partialState.keypadPositions.dropLast(1) + newPosition,
                        output = partialState.output,
                    )
                }

                DirectionalKey.Right -> {
                    val newPosition = embeddedDirectionalKeyPosition + IntOffset(1, 0)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = partialState.keypadPositions.dropLast(1) + newPosition,
                        output = partialState.output,
                    )
                }

                DirectionalKey.Up -> {
                    val newPosition = embeddedDirectionalKeyPosition + IntOffset(0, -1)
                    if (NumericKey.fromPosition(newPosition) == null) return null
                    PartialState(
                        keypadPositions = partialState.keypadPositions.dropLast(1) + newPosition,
                        output = partialState.output,
                    )
                }
            }
        }
    }
}

fun calculatePartialStateUpdate(
    partialState: PartialState,
    directionalKey: DirectionalKey,
): PartialState? =
    memoizedPartialStateUpdateMap.getOrPut(partialState to directionalKey) {
        calculatePartialStateUpdateImpl(partialState, directionalKey)
    }

sealed class NumericKey(
    val position: IntOffset,
) {
    data object Seven : NumericKey(
        position = IntOffset(0, 0),
    )
    data object Eight : NumericKey(
        position = IntOffset(1, 0),
    )
    data object Nine : NumericKey(
        position = IntOffset(2, 0),
    )
    data object Four : NumericKey(
        position = IntOffset(0, 1),
    )
    data object Five : NumericKey(
        position = IntOffset(1, 1),
    )
    data object Six : NumericKey(
        position = IntOffset(2, 1),
    )
    data object One : NumericKey(
        position = IntOffset(0, 2),
    )
    data object Two : NumericKey(
        position = IntOffset(1, 2),
    )
    data object Three : NumericKey(
        position = IntOffset(2, 2),
    )
    data object Zero : NumericKey(
        position = IntOffset(1, 3),
    )
    data object A : NumericKey(
        position = IntOffset(2, 3),
    )

    companion object {
        fun fromPosition(position: IntOffset): NumericKey? =
            when (position) {
                IntOffset(0, 0) -> Seven
                IntOffset(1, 0) -> Eight
                IntOffset(2, 0) -> Nine
                IntOffset(0, 1) -> Four
                IntOffset(1, 1) -> Five
                IntOffset(2, 1) -> Six
                IntOffset(0, 2) -> One
                IntOffset(1, 2) -> Two
                IntOffset(2, 2) -> Three
                IntOffset(1, 3) -> Zero
                IntOffset(2, 3) -> A
                else -> null
            }
    }
}

sealed class DirectionalKey(
    val position: IntOffset,
) {
    data object Up : DirectionalKey(
        position = IntOffset(1, 0),
    )
    data object A : DirectionalKey(
        position = IntOffset(2, 0),
    )
    data object Left : DirectionalKey(
        position = IntOffset(0, 1),
    )
    data object Down : DirectionalKey(
        position = IntOffset(1, 1),
    )
    data object Right : DirectionalKey(
        position = IntOffset(2, 1),
    )

    companion object {
        fun fromPosition(position: IntOffset): DirectionalKey? =
            when (position) {
                IntOffset(1, 0) -> Up
                IntOffset(2, 0) -> A
                IntOffset(0, 1) -> Left
                IntOffset(1, 1) -> Down
                IntOffset(2, 1) -> Right
                else -> null
            }
    }
}

data class MemoizedInput(
    val keys: List<DirectionalKey>,
    val remainingDirectionalKeypads: Int,
) {
    init {
        require(keys.last() == DirectionalKey.A)
    }
}

val memoizedMap = mutableMapOf<MemoizedInput, Long>()

fun shortestNumericalKeySubsequence(
    keys: List<NumericKey>,
    numDirectionalKeypads: Int,
): Long =
    (listOf(NumericKey.A) + keys).zipWithNext()
        .sumOf { (a, b) ->
            val diff = b.position - a.position

            val lefts = if (diff.x < 0) {
                List(-diff.x) { DirectionalKey.Left }
            } else {
                emptyList()
            }
            val rights = if (diff.x > 0) {
                List(diff.x) { DirectionalKey.Right }
            } else {
                emptyList()
            }
            val ups = if (diff.y < 0) {
                List(-diff.y) { DirectionalKey.Up }
            } else {
                emptyList()
            }
            val downs = if (diff.y > 0) {
                List(diff.y) { DirectionalKey.Down }
            } else {
                emptyList()
            }

            setOfNotNull(
                (lefts + ups + downs + rights + DirectionalKey.A).takeUnless {
                    (a.position.x == 0 && b.position.y == 3) ||
                            (b.position.x == 0 && a.position.y == 3)
                },
                rights + ups + downs + lefts + DirectionalKey.A,
            ).minOf { keySubsequence ->
                shortestKeySequence(
                    MemoizedInput(
                        keys = keySubsequence,
                        remainingDirectionalKeypads = numDirectionalKeypads,
                    )
                )
            }
        }

fun shortestKeySequence(
    memoizedInput: MemoizedInput,
): Long =
    if (memoizedInput in memoizedMap) {
        memoizedMap[memoizedInput]!!
    } else {
        if (memoizedInput.remainingDirectionalKeypads == 0) {
            memoizedInput.keys.size.toLong()
        } else {
            (listOf(DirectionalKey.A) + memoizedInput.keys).zipWithNext()
                .sumOf { (a, b) ->
                    val diff = b.position - a.position

                    val lefts = if (diff.x < 0) {
                        List(-diff.x) { DirectionalKey.Left }
                    } else {
                        emptyList()
                    }
                    val rights = if (diff.x > 0) {
                        List(diff.x) { DirectionalKey.Right }
                    } else {
                        emptyList()
                    }
                    val ups = if (diff.y < 0) {
                        List(-diff.y) { DirectionalKey.Up }
                    } else {
                        emptyList()
                    }
                    val downs = if (diff.y > 0) {
                        List(diff.y) { DirectionalKey.Down }
                    } else {
                        emptyList()
                    }

                    setOfNotNull(
                        (ups + rights + lefts + downs + DirectionalKey.A).takeUnless {
                            (a.position.x == 0 && b.position.y == 0) ||
                                    (b.position.x == 0 && a.position.y == 0)
                        },
                        downs + lefts + rights + ups + DirectionalKey.A,
                    ).minOf { keySubsequence ->
                        shortestKeySequence(
                            MemoizedInput(
                                keys = keySubsequence,
                                remainingDirectionalKeypads = memoizedInput.remainingDirectionalKeypads - 1,
                            )
                        )
                    }
                }
        }.also {
            memoizedMap[memoizedInput] = it
        }
    }

fun greedyNumericalKeys(
    keys: List<NumericKey>
): List<DirectionalKey> =
    (listOf(NumericKey.A) + keys).zipWithNext()
        .flatMap { (a, b) ->
            val diff = b.position - a.position

            val lefts = if (diff.x < 0) {
                List(-diff.x) { DirectionalKey.Left }
            } else {
                emptyList()
            }
            val rights = if (diff.x > 0) {
                List(diff.x) { DirectionalKey.Right }
            } else {
                emptyList()
            }
            val ups = if (diff.y < 0) {
                List(-diff.y) { DirectionalKey.Up }
            } else {
                emptyList()
            }
            val downs = if (diff.y > 0) {
                List(diff.y) { DirectionalKey.Down }
            } else {
                emptyList()
            }

            if (a.position.y == 3 && b.position.x == 0) {
                ups + rights + lefts + downs
            } else {
                lefts + ups + rights + downs
            } + DirectionalKey.A
        }

fun greedyDirectionalKeys(
    keys: List<DirectionalKey>
): List<DirectionalKey> =
    (listOf(DirectionalKey.A) + keys).zipWithNext()
        .flatMap { (a, b) ->
            val diff = b.position - a.position

            val lefts = if (diff.x < 0) {
                List(-diff.x) { DirectionalKey.Left }
            } else {
                emptyList()
            }
            val rights = if (diff.x > 0) {
                List(diff.x) { DirectionalKey.Right }
            } else {
                emptyList()
            }
            val ups = if (diff.y < 0) {
                List(-diff.y) { DirectionalKey.Up }
            } else {
                emptyList()
            }
            val downs = if (diff.y > 0) {
                List(diff.y) { DirectionalKey.Down }
            } else {
                emptyList()
            }

            val rightsUps = if (a.position == DirectionalKey.Left.position) {
                rights + ups
            } else {
                ups + rights
            }
            val downsLefts =                 lefts + downs


            downsLefts + rightsUps + DirectionalKey.A
        }

fun main() {
    data class Code(
        val keys: List<NumericKey>,
        val value: Long,
    ) {
        init {
            require(keys.last() == NumericKey.A)
        }
    }

    data class Input(
        val codes: List<Code>,
    )

    data class State(
        val keypadPositions: List<IntOffset>,
        val output: List<NumericKey>,
        val length: Int,
    )

    fun score(
        keys: List<NumericKey>,
        state: State,
    ): Int =
        state.length +
            (listOf(state.keypadPositions.first()) + keys.drop(state.output.size).map(NumericKey::position))
                .zipWithNext { a, b ->
                    (a - b).manhattanDistance
                }
                .sum() +
            state.keypadPositions
                .drop(1)
                .map { it - DirectionalKey.A.position }
                .sumOf(IntOffset::manhattanDistance)

    fun search(
        keys: List<NumericKey>,
        initialKeypadPositions: List<IntOffset>,
    ): Int {
        val searched = mutableSetOf<Pair<List<IntOffset>, List<NumericKey>>>()
        val queue = PriorityQueue<State>(
            { a, b ->
                score(keys, a).compareTo(score(keys, b))
            },
        )
        queue.add(
            State(
                keypadPositions = initialKeypadPositions,
                output = emptyList(),
                length = 0,
            )
        )

        while (queue.isNotEmpty()) {
            val next = queue.remove()
            if (next.keypadPositions to next.output in searched) continue
            searched.add(next.keypadPositions to next.output)
            if (next.output == keys) {
                return next.length
            } else if (keys.take(next.output.size) != next.output) {
                continue
            }

            listOf(
                DirectionalKey.A,
                DirectionalKey.Up,
                DirectionalKey.Down,
                DirectionalKey.Right,
                DirectionalKey.Left,
            ).forEach { directionalKey ->
                val newPartialState = calculatePartialStateUpdate(
                    partialState = PartialState(
                        keypadPositions = next.keypadPositions,
                        output = next.output,
                    ),
                    directionalKey = directionalKey,
                )

                if (newPartialState != null) {
                    queue.add(
                        State(
                            keypadPositions = newPartialState.keypadPositions,
                            output = newPartialState.output,
                            length = next.length + 1,
                        )
                    )
                }
            }
        }

        error("Should not happen")
    }

    fun parseInput(input: List<String>): Input =
        Input(
            input.map { line ->
                Code(
                    keys = line.toCharArray().map { char ->
                        when (char) {
                            '0' -> NumericKey.Zero
                            '1' -> NumericKey.One
                            '2' -> NumericKey.Two
                            '3' -> NumericKey.Three
                            '4' -> NumericKey.Four
                            '5' -> NumericKey.Five
                            '6' -> NumericKey.Six
                            '7' -> NumericKey.Seven
                            '8' -> NumericKey.Eight
                            '9' -> NumericKey.Nine
                            'A' -> NumericKey.A
                            else -> error("Unknown numeric key")
                        }
                    },
                    value = line.dropLast(1).toLong()
                )
            }
        )

    fun part(input: List<String>, numDirectionalKeypads: Int): Long {
        println("part: $input, $numDirectionalKeypads")

        val parsedInput = parseInput(input)

        return parsedInput.codes.sumOf { code ->
            val shortestSequence = search(
                keys = code.keys,
                initialKeypadPositions = listOf(NumericKey.A.position) + List(numDirectionalKeypads) { DirectionalKey.A.position },
            )
            println("finished: $code, $shortestSequence")
            shortestSequence * code.value
        }
    }

    fun partAlpha(input: List<String>, numDirectionalKeypads: Int): Long {
        println("partAlpha: $input, $numDirectionalKeypads")

        val parsedInput = parseInput(input)

        return parsedInput.codes.sumOf { code ->
            val shortestSequence = shortestNumericalKeySubsequence(
                code.keys,
                numDirectionalKeypads,
            )
            println("finished: $code, $shortestSequence")
            shortestSequence * code.value
        }
    }

    fun greedyPart1(input: List<String>): Long {
        val parsedInput = parseInput(input)

        return parsedInput.codes.sumOf { code ->
            val directionalKeys = greedyNumericalKeys(code.keys)
            generateSequence(directionalKeys, ::greedyDirectionalKeys).take(3)
                .onEach {
                    println("directionalKeys: ${it.size} ${it.map { when (it) {
                        DirectionalKey.A -> 'A'
                        DirectionalKey.Down -> 'v'
                        DirectionalKey.Left -> '<'
                        DirectionalKey.Right -> '>'
                        DirectionalKey.Up -> '^'
                    }
                    }.joinToString("")}")
                }
                .last().size.toLong() * code.value
        }
    }

    fun part2(input: List<String>): Long {
        val parsedInput = parseInput(input)

        return parsedInput.codes.sumOf { code ->
            val shortestSequence = search(
                keys = code.keys,
                initialKeypadPositions = listOf(NumericKey.A.position) + List(4) { DirectionalKey.A.position },
            )
            println("finished: $code, $shortestSequence")
            shortestSequence * code.value
        }
    }

    fun greedyPart(input: List<String>, directionalKeypads: Int): Long {
        val parsedInput = parseInput(input)

        return parsedInput.codes.sumOf { code ->
            val directionalKeys = greedyNumericalKeys(code.keys)
            generateSequence(directionalKeys, ::greedyDirectionalKeys)
                .take(directionalKeypads + 1)
                .onEach {
                    println("directionalKeys: $it")
                }
                .last().size.toLong() * code.value
        }
    }

    // Or read a large test input from the `src/Day21_test.txt` file:
    val testInput = readInput("Day21_test")

    check(part(testInput, 2).also(::println) == 126384L)
    check(partAlpha(testInput, 2) == part(testInput, 2))

    // Read the input from the `src/Day21.txt` file.
    val input = readInput("Day21")

    check(partAlpha(input, 3) == part(input, 3))

    partAlpha(input, 2).println()
    part(input, 2).println()
    partAlpha(input, 25).println()
    part(input, 25).println()
}
