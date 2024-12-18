package day04

import day04.WordSearch.Point.Direction
import kotlin.math.sign

object WordSearch {

    data class Point(val col: Int, val row: Int) {
        enum class Direction { Left, Right, Up, Down, TopRight, BottomRight, BottomLeft, TopLeft }

        companion object {
            fun pointsBetween(start: Point, end: Point): List<Point> {
                val points = mutableListOf<Point>()
                val deltaCol = sign((end.col - start.col).toDouble()).toInt()
                val deltaRow = sign((end.row - start.row).toDouble()).toInt()

                var nextPoint = start
                do {
                    points.add(nextPoint)
                    nextPoint = Point(col = nextPoint.col + deltaCol, row = nextPoint.row + deltaRow)
                } while (nextPoint != end)

                points.add(end)
                return points.toList()
            }
        }

        fun translate(delta: Int, direction: Direction): Point {
            return when (direction) {
                Direction.Left -> Point(this.col - delta, this.row)
                Direction.Right -> Point(this.col + delta, this.row)
                Direction.Up -> Point(this.col, this.row - delta)
                Direction.Down -> Point(this.col, this.row + delta)
                Direction.TopRight -> Point(this.col + delta, this.row - delta)
                Direction.BottomRight -> Point(this.col + delta, this.row + delta)
                Direction.BottomLeft -> Point(this.col - delta, this.row + delta)
                Direction.TopLeft -> Point(this.col - delta, this.row - delta)
            }
        }
    }

    class WordPuzzle(private val points: Map<Point, Char>, val width: Int, val height: Int) {
        fun charAt(point: Point): Char {
            return points[point] ?: '.' // return any non-word character do not have to deal with bounds...
        }

        fun pointCount() = points.size

        fun points() = points.keys

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