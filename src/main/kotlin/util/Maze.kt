package util

import util.MapOfThings.Point
import util.MazeElement.Empty
import util.MazeElement.End
import util.MazeElement.Start
import util.MazeElement.Wall

enum class MazeElement {
    Wall, Start, End, Empty
}

sealed interface MazeEvent {
    data class Movement(val position: Point) : MazeEvent
    data object FoundSolution : MazeEvent
    data object Abort : MazeEvent
}


class Maze(private val lines: List<String>) {

    private var currentPosition: Point? = null

    val map: MapOfThings<MazeElement> by lazy {
        MapOfThings.parse(lines) { c ->
            when (c) {
                '#' -> Wall
                'S' -> Start
                'E' -> End
                '.' -> Empty
                else -> {
                    throw IllegalArgumentException("Unknown char: $c")
                }
            }
        }
    }

    val startPosition by lazy { map.pointsFor(Start).single() }

    val endPosition by lazy {
        map.pointsFor(End).single()
    }

    fun onEvent(event: MazeEvent, context: String = "") {
        when (event) {
            MazeEvent.Abort -> {}
            MazeEvent.FoundSolution -> println("Found solution at $currentPosition $context".trim())
            is MazeEvent.Movement -> currentPosition = event.position
        }
    }

}