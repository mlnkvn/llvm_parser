class Constant(str: String) : Token {
    private val value = str.trim().toInt()
    override fun toString() = value.toString()
    override fun getAllVariables(): Set<Variable> = setOf()

    companion object {
        fun isConstant(str: String) = str.trim().toIntOrNull() != null
    }
}