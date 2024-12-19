package util

import kotlin.math.sign

open class MapOfThings<T>(private val points: Map<Point, T>, val width: Int, val height: Int) {

    enum class Direction { Left, Right, Up, Down, TopRight, BottomRight, BottomLeft, TopLeft }
    data class Point(val col: Int, val row: Int) {

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

    fun thingAt(point: Point): T? {
        return points[point]
    }

    fun pointCount() = points.size

    fun points() = points.keys
    
}