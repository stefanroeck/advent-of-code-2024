package day11

class StonesOnPluto {
    companion object {
        fun blink(input: String, iterations: Int): String {
            val numbers = input.split(" ").map { it.toLong() }

            return IntRange(0, iterations - 1).fold(numbers) { previous, _ ->
                applyRules(previous)
            }.joinToString(" ")
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