class Program(str: String) : Token {
    private val root: StatementList = StatementList(str)
    override fun toString() = root.toString()
    override fun getAllVariables() = root.getAllVariables()
    override val usedVariables: Set<Variable>
        get() = root.usedVariables
}