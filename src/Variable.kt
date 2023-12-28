class Variable(str: String) : Token {

    private val name = str.trim()[0]
    val assignment: Statement?
        get() = statement
    private var statement: Statement? = null
    override fun toString() = name.toString()
    override fun getAllVariables() = setOf(this)

    fun addStatement(statement: Statement) {
        this.statement = statement
    }

    fun getName() = name


    companion object {
        fun isVariable(str: String) = str.trim().let {
            it.length == 1 && it[0].code in 'a'.code..'z'.code
        }
    }
}