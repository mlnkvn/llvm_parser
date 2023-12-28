class OperationException(private val str: String) : RuntimeException() {
    override val message: String?
        get() = super.message + System.lineSeparator() +
                "'" + str + "' is incorrect operand"
}

enum class OperationPriority { LOW, MEDIUM, HIGH }
class Operator(str: String) : Token, Comparable<Operator> {
    private val symbol: Char = str.trim()[0]
    private val priority: OperationPriority = when (symbol) {
        in highPriority -> OperationPriority.HIGH
        in mediumPriority -> OperationPriority.MEDIUM
        in lowPriority -> OperationPriority.LOW
        else -> throw OperationException(str)
    }

    override fun compareTo(other: Operator): Int = priority.ordinal - other.priority.ordinal

    override fun toString() = symbol.toString()
    override fun getAllVariables(): Set<Variable> = setOf()

    companion object {
        private val highPriority = listOf('*', '/')
        private val mediumPriority = listOf('+', '-')
        private val lowPriority = listOf('<', '>')

        fun isOperator(symbol: Char) =
            symbol in highPriority || symbol in mediumPriority || symbol in lowPriority
    }
}