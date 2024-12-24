package day10

import util.MapOfThings
import util.MapOfThings.Point

data class Height(val height: Int)

val unknownHeight = Height(-99)

class HikingTrails(lines: List<String>) {

    private val trailMap = MapOfThings.parse(lines) { if (it != '.') Height(it.digitToInt()) else unknownHeight }

    fun parseMap(): MapOfThings<Height> {
        return trailMap
    }

    fun trailHeads() = trailMap.pointsFor(Height(0))

    fun score() = trailHeads().sumOf {
        scoreForTrailHead(it)
    }

    private fun scoreForTrailHead(trailHead: Point): Int {
        var currentHeight = 0
        var pointsOnPathes = setOf(trailHead)
        do {
            currentHeight++
            pointsOnPathes = pointsOnPathes.flatMap { point ->
                trailMap.adjacentPoints(point).filter { trailMap.thingAt(it)!!.height == currentHeight }
            }.toSet()
        } while (pointsOnPathes.isNotEmpty() && currentHeight < 9)

        return pointsOnPathes.size
    }
}