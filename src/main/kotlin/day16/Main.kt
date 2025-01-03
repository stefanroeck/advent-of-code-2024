package day16

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day16/input.txt")
    val rows = parseLines(input)

    val reindeerMaze = ReindeerMaze(rows)

    val cost = reindeerMaze.shortestPathCost()
    println("Shortest path cost: $cost")
    
    val seats = reindeerMaze.seatsOnShortestPaths()
    println("Shortest path seats: $seats")
}