interface Token {
    val usedVariables: Set<Variable>
        get() = setOf()

    override fun toString(): String
    fun getAllVariables(): Set<Variable>
}