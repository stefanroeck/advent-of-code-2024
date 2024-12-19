package day04

import util.MapOfThings
import util.MapOfThings.Point

object WordSearch {

    class WordPuzzle(points: Map<Point, Char>, width: Int, height: Int) :
        MapOfThings<Char>(points, width, height) {

        private fun charAt(point: Point) = thingAt(point) ?: "." // return invalid char to not have to deal with bounds

        fun countWordsAt(point: Point, word: String): Int {
            if (charAt(point) == word[0]) {
                return countWordsInAllDirectionsAt(point, word)
            }
            return 0
        }

        private fun countWordsInAllDirectionsAt(point: Point, word: String): Int {
            val wordsInAllDirections =
                Direction.entries.map { direction -> wordAt(point, point.translate(word.length - 1, direction)) }

            return countMatchingWords(word, wordsInAllDirections)
        }


        private fun wordAt(start: Point, end: Point) = Point.pointsBetween(start, end)
            .map { charAt(it) }
            .joinToString("")

        private fun countMatchingWords(expectedWord: String, words: List<String>): Int {
            words.forEach { check(expectedWord.length == it.length) }

            return words.count { it == expectedWord }
        }

        fun countXMasPatterns(): Int {
            return points().filter { point ->
                charAt(point) == 'A'
            }.sumOf { countXMasPatternsAround(it) }
        }

        private fun countXMasPatternsAround(point: Point): Int {
            val firstXLegWord =
                wordAt(point.translate(1, Direction.TopLeft), point.translate(1, Direction.BottomRight))
            val secondXLegWord =
                wordAt(point.translate(1, Direction.TopRight), point.translate(1, Direction.BottomLeft))

            val words = listOf(firstXLegWord, secondXLegWord)
            return if (words.all { it == "MAS" || it == "SAM" }) 1 else 0
        }
    }

    fun count(input: String, word: String): Int {
        val puzzle = parse(input)
        return puzzle.points().sumOf { point ->
            puzzle.countWordsAt(point, word)
        }
    }

    fun parse(input: String): WordPuzzle {
        val rows = input.split("\n")
            .map { it.trim() }
            .filterNot { it.isEmpty() }

        val letterMap = rows
            .flatMapIndexed { row, letters ->
                letters.mapIndexed { col, letter -> Point(col, row) to letter }
            }.toMap()

        return WordPuzzle(letterMap, height = rows.size, width = rows[0].length)
    }

    fun countXmasPattern(input: String): Int {
        val puzzle = parse(input)

        return puzzle.countXMasPatterns()
    }
}