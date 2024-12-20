package day04

import util.MapOfThings
import util.MapOfThings.Direction
import util.MapOfThings.Point

typealias WordPuzzle = MapOfThings<Char>

class WordSearch {

    private lateinit var wordPuzzle: WordPuzzle

    companion object {
        private fun countMatchingWords(expectedWord: String, words: List<String>): Int {
            words.forEach { check(expectedWord.length == it.length) }

            return words.count { it == expectedWord }
        }
    }

    private fun charAt(point: Point) =
        wordPuzzle.thingAt(point) ?: "." // return invalid char to not have to deal with bounds

    private fun wordAt(start: Point, end: Point) = Point.pointsBetween(start, end)
        .map { charAt(it) }
        .joinToString("")

    private fun countXMasPatternsAround(point: Point): Int {
        val firstXLegWord =
            wordAt(point.translate(1, Direction.TopLeft), point.translate(1, Direction.BottomRight))
        val secondXLegWord =
            wordAt(point.translate(1, Direction.TopRight), point.translate(1, Direction.BottomLeft))

        val words = listOf(firstXLegWord, secondXLegWord)
        return if (words.all { it == "MAS" || it == "SAM" }) 1 else 0
    }

    private fun countWordsAt(point: Point, word: String): Int {
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

    private fun countXMasPatterns(): Int {
        return wordPuzzle.points().filter { point ->
            charAt(point) == 'A'
        }.sumOf { countXMasPatternsAround(it) }
    }


    fun count(input: List<String>, word: String): Int {
        val puzzle = parse(input)
        return puzzle.points().sumOf { point ->
            countWordsAt(point, word)
        }
    }

    fun parse(input: List<String>): WordPuzzle {
        this.wordPuzzle = MapOfThings.parse(input) { c -> c }

        return wordPuzzle
    }

    fun countXmasPattern(input: List<String>): Int {
        this.wordPuzzle = parse(input)

        return countXMasPatterns()
    }
}