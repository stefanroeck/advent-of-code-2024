package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.MapOfThings.Point
import util.MapOfThings.Vector

class MapOfThingsTest {

    @Test
    fun vectors() {
        val vector = Vector(1, 2)
        assertEquals(vector, vector.invert().invert())
    }

    @Test
    fun pointsAndVectors() {
        val point = Point(1, 1)
        val vector = Vector(1, 2)

        assertEquals(Point(2, 3), point.translate(vector))
        assertEquals(Point(0, -1), point.translate(vector.invert()))

        assertEquals(vector, Point.vector(point, point.translate(vector)))
    }

    @Test
    fun pointsAndMaps() {
        val points = listOf(Point(0, 0), Point(1, 0), Point(0, 1), Point(1, 1))
        val map = MapOfThings(points.associateWith { '.' }, 2, 2)

        points.forEach { point ->
            assertTrue(point within map)
        }
        assertFalse(Point(-1, 0) within map)
        assertFalse(Point(0, -1) within map)
        assertFalse(Point(0, 2) within map)
        assertFalse(Point(2, 0) within map)
    }

}