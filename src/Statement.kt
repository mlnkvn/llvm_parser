class LanguageStatementException(private val str: String) : RuntimeException() {
    override val message: String
        get() = super.message +
                System.lineSeparator() +
                "'" + str + "' is not an Expression"
}

class Statement(str: String) : Token {

    companion object {
        enum class StatementType { ASSIGNING, IF, WHILE }

        fun retrieveLastStatement(str: String): Pair<String, Statement> {
            val trimmed = str.trim()
            if (trimmed.endsWith("end")) {
                val startPosition = trimmed.findLastAnyOf(listOf("if", "while")) ?: throw RuntimeException()
                val statement = Statement(trimmed.substring(startPosition.first))
                return Pair(trimmed.substring(0, startPosition.first), statement)
            }
            val eqPosition = trimmed.findLastAnyOf(listOf("=")) ?: throw RuntimeException()
            var start = -1
            var end = -1
            for (pos in eqPosition.first - 1 downTo 0) {
                if (!trimmed[pos].isWhitespace() && end == -1) end = pos + 1
                if (trimmed[pos].isWhitespace() && end != -1) {
                    start = pos + 1
                    break
                }
            }
            if (start == -1) start = 0
            return Pair(trimmed.substring(0, start), Statement(trimmed.substring(start)))
        }
    }

    private val tokens: List<Token>
    private val type: StatementType

    init {
        val trimmed = str.trim()
        val eqPosition = trimmed.indexOf('=')
        val startsIf = trimmed.startsWith("if")
        val startsWhile = trimmed.startsWith("while")
        if ((startsIf || startsWhile) && trimmed.endsWith("end")) {
            type = if (startsIf) StatementType.IF else StatementType.WHILE
            tokens = buildList {
                val inside = trimmed.split("\\s+".toRegex()).dropLast(1).drop(1).joinToString(" ")
                val partiallyParsed = Expression.retrieveFirstExpression(inside)
                add(partiallyParsed.first)
                add(StatementList(partiallyParsed.second))
            }
        } else if (eqPosition != -1) {
            type = StatementType.ASSIGNING
            tokens = buildList {
                add(Variable(trimmed.substring(0, eqPosition)))
                add(Expression(trimmed.substring(eqPosition + 1)))
            }
            (tokens[0] as Variable).addStatement(this)
        } else {
            throw LanguageStatementException(trimmed)
        }
    }

    override val usedVariables: Set<Variable>
        get() = buildSet {
            tokens.forEach {
                if (it is Expression || it is StatementList) addAll(it.usedVariables)
            }
        }

    override fun toString() =
        when (type) {
            StatementType.ASSIGNING -> tokens.joinToString(" = ") { it.toString() }
            StatementType.IF -> "if ${tokens[0]}\n" +
                    "${tokens[1]}\n" +
                    "end"

            StatementType.WHILE -> "while ${tokens[0]}\n" +
                    "${tokens[1]}\n" +
                    "end"
        }

    override fun getAllVariables() = buildSet {
        tokens.forEach {
            addAll(it.getAllVariables())
        }
    }


}