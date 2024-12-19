package day07

import java.util.function.BiFunction

private data class Equation(val result: Long, val operands: List<Long>)

private enum class Operation(private val function: BiFunction<Long, Long, Long>) {
    Sum({ a, b -> a + b }), Product({ a, b -> a * b });

    fun apply(a: Long, b: Long) = function.apply(a, b)
}

private typealias Solution = List<Operation>
private typealias Solutions = Map<Equation, List<Solution>>

object BridgeRepair {

    fun findOperatorsAndCalcSum(lines: List<String>): Long {
        val equations = parseEquations(lines)
        val solvableEquations = solveEquations(equations)
        return sumForEquationsWithSolutions(solvableEquations)
    }

    private fun parseEquations(lines: List<String>) = lines.map { it.split(":") }
        .map { line ->
            Equation(
                result = line[0].trim().toLong(),
                operands = line[1].trim().split(" ").map { it.toLong() })
        }

    private fun sumForEquationsWithSolutions(solvableEquations: Solutions) = solvableEquations.entries
        .filter { (_, solutions) -> solutions.isNotEmpty() }
        .sumOf { (equation, _) ->
            equation.result
        }

    private fun solveEquations(equations: List<Equation>): Solutions {
        return equations.associateWith { equation -> solveEquation(equation) }
    }

    private fun solveEquation(equation: Equation): List<Solution> {
        val solutions = mutableListOf<Solution>()

        recursiveCalc(equation.operands.removeFirst(), equation.operands[0], emptyList(), equation.result, solutions)
        return solutions.toList()
    }

    private fun recursiveCalc(
        operands: List<Long>,
        previousResult: Long,
        operations: List<Operation>,
        expectedResult: Long,
        solutions: MutableList<Solution>
    ) {
        if (operands.isEmpty()) {
            return
        }

        for (operation in Operation.entries) {
            val newResult = operation.apply(previousResult, operands[0])
            val newOperations = operations + operation

            if (newResult == expectedResult) {
                solutions.add(newOperations)
            } else if (newResult > expectedResult) {
                // stop recursion, we're too big already
            } else {
                recursiveCalc(
                    operands.removeFirst(),
                    newResult,
                    newOperations,
                    expectedResult,
                    solutions
                )
            }

        }
    }

    private fun <T> List<T>.removeFirst() = this.takeLast(size - 1)
}