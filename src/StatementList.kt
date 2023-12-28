class StatementList(str: String) : Token {
    private val head: StatementList?
    private val tail: Statement

    init {
        val partiallyParsed = Statement.retrieveLastStatement(str.trim())
        head = if (partiallyParsed.first.isNotEmpty()) StatementList(partiallyParsed.first) else null
        tail = partiallyParsed.second
    }

    override val usedVariables: Set<Variable>
        get() = buildSet {
            head?.usedVariables?.let { addAll(it) }
            addAll(tail.usedVariables)
        }

    override fun toString(): String {
        var res = head?.toString().orEmpty()
        if (res.isNotEmpty()) res += '\n'
        res += tail.toString()
        return res
    }

    override fun getAllVariables(): Set<Variable> = buildSet {
        head?.getAllVariables()?.let { addAll(it) }
        addAll(tail.getAllVariables())
    }
}