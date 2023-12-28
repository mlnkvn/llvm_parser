class LanguageExpressionException(private val str: String) : RuntimeException() {
    override val message: String
        get() = super.message +
                System.lineSeparator() +
                "'" + str + "' is not an Expression"
}

class Expression(str: String) : Token {
    companion object {
        enum class StatementType { VAR, CONST, EXPR, BINARY }

        fun retrieveFirstExpression(str: String): Pair<Expression, String> {
            val trimmed = str.trim()
            var expression: Expression?
            for (end in trimmed.length downTo 1) {
                try {
                    expression = Expression(trimmed.substring(0, end))
                    return Pair(expression, trimmed.substring(end))
                } catch (_: Exception) {
                }
            }
            throw LanguageExpressionException(trimmed)
        }

        private fun parseBinaryExpression(str: String): List<Token> {
            val operators = mutableListOf<Pair<Int, Operator>>()
            for (entry in str.withIndex()) {
                if (Operator.isOperator(entry.value)) {
                    val operator = Operator(entry.value.toString())
                    operators.dropWhile { it.second < operator }
                    operators.add(Pair(entry.index, operator))
                }
            }
            return listOf(
                Expression(str.substring(0, operators.first().first)),
                operators.first().second,
                Expression(str.substring(operators.first().first + 1))
            )

        }
    }

    private val type: StatementType
    private val tokens: List<Token>

    init {
        val trimmed = str.trim()
        if (trimmed.startsWith('(') && trimmed.endsWith(')')) {
            type = StatementType.EXPR
            tokens = buildList { add(Expression(trimmed.substring(1, trimmed.length - 1))) }
        } else if (Variable.isVariable(trimmed)) {
            type = StatementType.VAR
            tokens = listOf(Variable(trimmed))
        } else if (Constant.isConstant(trimmed)) {
            type = StatementType.CONST
            tokens = listOf(Constant(trimmed))
        } else {
            type = StatementType.BINARY
            tokens = parseBinaryExpression(trimmed)
        }
    }

    override val usedVariables: Set<Variable>
        get() = getAllVariables()

    override fun toString() = when (type) {
        StatementType.VAR -> tokens.first().toString()
        StatementType.CONST -> tokens.first().toString()
        StatementType.EXPR -> "(${tokens.first()})"
        StatementType.BINARY -> tokens.joinToString(" ") { it.toString() }
    }

    override fun getAllVariables() = buildSet {
        tokens.forEach {
            addAll(it.getAllVariables())
        }
    }
}