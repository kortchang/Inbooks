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

                /**
                 * TODO("
                 *  這邊算是 iOS 那邊的鍋，反正有一些問題讓我們沒辦法使用正式的 format，
                 *  所以連官方的 stringResource 都是直接把參數取代，而沒有做更多的處理：
                 *  例如浮點數要不要補零之類的設定，目前都是不支援的
                 *  ")
                 * [issues](https://youtrack.jetbrains.com/issue/KT-25506/Stdlib-String.format-in-common)
                 * 未來比較好是這邊要改成：
                 * `"%$argumentType".format(argument)`
                 */
                val formattedArgument = argument.toString()

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

fun String.rangeFormat(vararg arguments: Any): RangeStringResult {
    return RangeStringFormatter.format(this, *arguments)
}