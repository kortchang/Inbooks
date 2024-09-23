package io.kort.inbooks.common

typealias ArgumentIndex = Int

data class RangeStringResult(val string: String, val ranges: Map<ArgumentIndex, IntRange>)
object RangeStringFormatter {
    private val formatPattern: Regex = "%([0-9]+\\$|<?)([^a-zA-z%]*)([[a-zA-Z%]&&[^tT]]|[tT][a-zA-Z])".toRegex()

    fun format(string: String, vararg arguments: Any): RangeStringResult {
        val rangeMap: MutableMap<Int, IntRange> = mutableMapOf()
        val result = StringBuilder(string)
        var findIndex = 0
        var argumentCurrentIndex = 0
        while (findIndex < result.length) {
            val match = formatPattern.find(result, findIndex)
            if (match != null) {
                findIndex = match.range.first

                val argumentPlaceholder = match.groupValues[0]
                val argumentIndex = match.groupValues[1].run {
                    takeIf { isNotEmpty() }?.substring(0 until length - 1)?.toIntOrNull()?.minus(1) ?: argumentCurrentIndex++
                }
                val argumentMod = match.groupValues[2]
                val argumentType = match.groupValues[3]

                val argument = arguments[argumentIndex]

                val formattedArgument = "%$argumentType".format(argument)

                result.setRange(match.range.first, match.range.last + 1, formattedArgument)
                findIndex += formattedArgument.length
                rangeMap[argumentIndex] = match.range.first..findIndex
            } else {
                findIndex = result.length
            }
        }

        return RangeStringResult(result.toString(), rangeMap)
    }
}

expect fun String.format(vararg arguments: Any): String

fun String.rangeFormat(vararg arguments: Any): RangeStringResult {
    return RangeStringFormatter.format(this, *arguments)
}