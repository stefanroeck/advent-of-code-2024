package day08

import day08.MapElement.Antenna
import day08.MapElement.Empty
import util.MapOfThings
import util.MapOfThings.Point

sealed interface MapElement {
    data class Antenna(val char: Char) : MapElement
    object Antinode : MapElement
    object Empty : MapElement
}

typealias AntennaMap = MapOfThings<MapElement>

class AntennaAntinodes(input: List<String>) {
    private val mapChar = { c: Char ->
        when (c) {
            '.' -> Empty
            '#' -> MapElement.Antinode
            else -> {
                Antenna(c)
            }
        }
    }

    private val map: AntennaMap = AntennaMap.parse(input) { mapChar(it) }

    fun distinctAntennaTypes(): Set<Antenna> {
        return map.points().mapNotNull(map::thingAt).filterIsInstance<Antenna>().toSet()
    }

    fun countAntinodes(): Int {
        return antinodes().count()
    }

    private fun antinodes() = distinctAntennaTypes().flatMap { antenna ->
        val antennaPoints = map.points().filter { map.thingAt(it) == antenna }

        val antinodePoints = mutableSetOf<Point>()
        for (firstPoint in antennaPoints) {
            for (secondPoint in antennaPoints) {
                if (firstPoint != secondPoint) {
                    val vector = Point.vector(firstPoint, secondPoint)
                    val antinode = secondPoint.translate(vector)

                    if (antinode within map) {
                        antinodePoints.add(antinode)
                    }
                }
            }
        }
        println("${antennaPoints.size} $antenna have ${antinodePoints.size} antinodes: $antinodePoints")
        antinodePoints
    }.toSet()
}