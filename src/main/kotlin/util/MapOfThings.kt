package util

import java.util.EnumSet
import kotlin.math.sign

class MapOfThings<T>(private val points: Map<Point, T>, val width: Int, val height: Int) {

    enum class Direction {
        Left, Right, Up, Down, TopRight, BottomRight, BottomLeft, TopLeft;

        companion object {
            fun xyDirections() = EnumSet.of(Left, Right, Up, Down)
        }
    }

    data class Vector(val dx: Int, val dy: Int) {
        fun invert() = Vector(dx = this.dx * -1, dy = this.dy * -1)
    }

    data class Point(val col: Int, val row: Int) : Comparable<Point> {

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

            fun gradient(start: Point, end: Point): Double {
                return (end.row - start.row).toDouble() / (end.col - start.col).toDouble()
            }

            fun vector(start: Point, end: Point): Vector = Vector(dx = end.col - start.col, dy = end.row - start.row)
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

        fun translate(vector: Vector) = Point(col + vector.dx, row + vector.dy)

        infix fun <T> within(map: MapOfThings<T>): Boolean {
            return col >= 0 && this.col < map.width && row >= 0 && this.row < map.height
        }

        infix fun <T> outside(map: MapOfThings<T>): Boolean {
            return !within(map)
        }

        override fun compareTo(other: Point): Int {
            return if (row != other.row) row.compareTo(other.row) else (col.compareTo(other.col))
        }
    }

    companion object {
        fun <T> parse(rows: List<String>, mapper: (c: Char) -> T): MapOfThings<T> {
            val pointMap = rows
                .flatMapIndexed { row, letters ->
                    letters.mapIndexed { col, char -> Point(col, row) to mapper(char) }
                }.toMap()

            return MapOfThings(pointMap, height = rows.size, width = rows[0].length)
        }
    }

    fun thingAt(point: Point): T? {
        return points[point]
    }

    fun pointCount() = points.size

    fun points() = points.keys

    fun pointsFor(thing: T): Set<Point> = points.filter { it.value == thing }.keys

    fun adjacentPoints(
        point: Point,
        directions: Set<Direction> = Direction.xyDirections()
    ): Set<Point> = directions.map { point.translate(1, it) }.filter { it within this }.toSet()


}