package day11

typealias SumsPerNumber = MutableMap<Long, Long>

class Cache {
    private val cache = mutableMapOf<Int, SumsPerNumber>()

    companion object {
        fun createCache() = Cache()
    }

    fun cachedSums(iteration: Int) = cache.getOrPut(iteration) { mutableMapOf() }
}


class StonesOnPluto {

    companion object {
        fun blink(input: String, iterations: Int): String {
            val numbers = input.split(" ").map { it.toLong() }

            return IntRange(0, iterations - 1).fold(numbers) { previous, iteration ->
                println("iteration $iteration on ${previous.size} numbers")
                applyRules(previous)
            }.joinToString(" ")
        }

        fun countBlinks(input: String, iterations: Int): Long {
            val inputNumbers = input.split(" ").map { it.toLong() }
            val start = System.currentTimeMillis()
            val cache = Cache.createCache()

            val count = inputNumbers.sumOf { number ->
                countRecursively(
                    number,
                    0,
                    iterations,
                    cache,
                )
            }
            println("Count took ${System.currentTimeMillis() - start}ms")
            return count
        }

        private fun countRecursively(
            inputNumber: Long,
            iteration: Int,
            totalIterations: Int,
            cache: Cache
        ): Long {
            if (iteration == totalIterations) return 1

            return cache.cachedSums(iteration).getOrPut(inputNumber) {
                val newNumbers = applyRules(inputNumber)
                println("Processing non-cached calc at $iteration/$totalIterations")

                newNumbers.sumOf { newNumber ->
                    countRecursively(
                        newNumber,
                        iteration + 1,
                        totalIterations,
                        cache,
                    )
                }
            }

        }

        private fun applyRules(numbers: List<Long>): List<Long> {
            return buildList {
                numbers.forEach { n -> addAll(applyRules(n)) }
            }
        }

        private fun applyRules(number: Long): List<Long> {
            val length = number.toString().length
            return if (number == 0L) {
                listOf(1)
            } else if (length % 2 == 0) {
                listOf(
                    number.toString().substring(0, length / 2).toLong(),
                    number.toString().substring(length / 2).toLong(),
                )
            } else {
                listOf(number * 2024L)
            }
        }
    }
}