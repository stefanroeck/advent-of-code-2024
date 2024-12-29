package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import util.MapOfThings.Point
import util.MapOfThings.Vector
import kotlin.test.assertSame

class MapOfThingsTest {

    @Test
    fun vectors() {
        assertEquals(Vector(1, 2), Vector(1, 2).invert().invert())

        assertEquals(Vector(3, 4), Vector(12, 16).reduce())
        assertEquals(Vector(1, 2), Vector(5, 10).reduce())
        val nonReducibleVector = Vector(1, 5)
        assertSame(nonReducibleVector, nonReducibleVector.reduce())

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

    @Test
    fun gradient() {
        assertEquals(1.0, Point.gradient(Point(0, 0), Point(2, 2)))
        assertEquals(2.0, Point.gradient(Point(1, 2), Point(2, 4)))
    }

}