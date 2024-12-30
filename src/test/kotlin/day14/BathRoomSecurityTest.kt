package day14

import org.junit.jupiter.api.Test
import util.InputUtils
import kotlin.test.assertEquals

class BathRoomSecurityTest {
    @Test
    fun `roboter movement`() {
        val input = """
         p=0,4 v=3,-3
         p=6,3 v=-1,-3
         p=10,3 v=-1,2
         p=2,0 v=2,-1
         p=0,0 v=1,3
         p=3,0 v=-2,-2
         p=7,6 v=-1,-3
         p=3,0 v=-1,-2
         p=9,3 v=2,3
         p=7,3 v=-1,2
         p=2,4 v=2,-3
         p=9,5 v=-3,-3
        """.trimIndent()
     
        val safetyFactor: Long =
            BathRoomSecurity(lines = InputUtils.parseLines(input), width = 11, height = 7).predictMovement(steps = 100)

        assertEquals(12, safetyFactor)
    }
}