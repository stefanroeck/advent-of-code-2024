package day03

object Multiplier {
    private val extractRegExp = Regex("""(mul\(\d+,\d+\))""")
    private val doesAndDontsRegExp = Regex("""(do\(\)|don't\(\))""")


    data class Product(private val s1: Int, private val s2: Int) {
        fun product() = s1 * s2
    }

    fun multiply(input: String): Int {
        val products = extractRegExp.findAll(input)
            .map { it.value }
            .map { it.replace("mul(", "") }
            .map { it.replace(")", "") }
            .map { it.split(",") }
            .map { Product(it[0].toInt(), it[1].toInt()) }

        return products.sumOf { it.product() }
    }

    fun multiplyWithDoAndDont(input: String): Int {
        var doActive = true
        var startIdx = 0
        var productSum = 0
        doesAndDontsRegExp.findAll(input).forEach { matchResult ->
            if (doActive) {
                productSum += multiply(input.substring(startIdx, matchResult.range.first))
            }
            doActive = matchResult.value == "do()"
            startIdx = matchResult.range.last + 1
        }

        if (doActive) {
            productSum += multiply(input.substring(startIdx, input.length))
        }
        return productSum
    }
}