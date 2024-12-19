package day07

import java.util.EnumSet
import java.util.function.BiFunction

private data class Equation(val result: Long, val operands: List<Long>)

private fun ensurePositive(number: Long): Long {
    check(number > 0)
    return number
}

private enum class Operation(private val function: BiFunction<Long, Long, Long>) {
    Sum({ a, b -> ensurePositive(a + b) }),
    Product({ a, b -> ensurePositive(a * b) }),
    Concat({ a, b -> "$a$b".toLong() })
    ;

    fun apply(a: Long, b: Long) = function.apply(a, b)
}

private typealias Solution = List<Operation>
private typealias Solutions = Map<Equation, List<Solution>>

object BridgeRepair {
    fun findTwoOperatorsAndCalcSum(lines: List<String>): Long {
        val equations = parseEquations(lines)
        val solvableEquations = solveEquations(equations, EnumSet.of(Operation.Sum, Operation.Product))
        return sumForEquationsWithSolutions(solvableEquations)
    }

    fun findThreeOperatorsAndCalcSum(lines: List<String>): Long {
        val equations = parseEquations(lines)

        val solvableEquations =
            solveEquations(equations, EnumSet.of(Operation.Sum, Operation.Product, Operation.Concat))
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

    private fun solveEquations(equations: List<Equation>, operators: EnumSet<Operation>): Solutions {
        return equations.associateWith { equation -> solveEquation(equation, operators) }
    }

    private fun solveEquation(equation: Equation, operators: EnumSet<Operation>): List<Solution> {
        val solutions = mutableListOf<Solution>()

        recursiveCalc(
            equation.operands.removeFirst(),
            equation.operands[0],
            emptyList(),
            equation.result,
            operators,
            solutions
        )
        return solutions.toList()
    }

    private fun recursiveCalc(
        operands: List<Long>,
        previousResult: Long,
        operations: List<Operation>,
        expectedResult: Long,
        operators: EnumSet<Operation>,
        solutions: MutableList<Solution>
    ) {
        if (operands.isEmpty()) {
            if (previousResult == expectedResult) {
                solutions.add(operations)
            }
            return
        }

        for (operation in operators) {
            val newResult = operation.apply(previousResult, operands[0])
            val newOperations = operations + operation

            if (newResult > expectedResult) {
                // stop recursion, we're too big already
            } else {
                recursiveCalc(
                    operands.removeFirst(),
                    newResult,
                    newOperations,
                    expectedResult,
                    operators,
                    solutions
                )
            }

        }
    }

    private fun <T> List<T>.removeFirst() = this.takeLast(size - 1)
}