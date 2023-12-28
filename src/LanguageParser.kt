import java.io.File

class LanguageParser {
    fun parse(inputFilePath: String): Program {
        val inputFile = File(inputFilePath)
        val fullProgramCode = inputFile.readLines().joinToString(" ") { it.split("\\s+".toRegex()).joinToString(" ") }
        return Program(fullProgramCode)
    }

    fun getUnusedAssignments(inputFilePath: String): List<Statement> {
        val inputFile = File(inputFilePath)
        val fullProgramCode = inputFile.readLines().joinToString(" ") { it.split("\\s+".toRegex()).joinToString(" ") }
        val program = Program(fullProgramCode)
        val usedVariableNames = program.usedVariables.map { it.getName() }.toSet()
        val unusedVariables = program.getAllVariables().filter { it.getName() !in usedVariableNames }
        return buildList {
            unusedVariables.forEach {
                it.assignment?.let { it1 -> add(it1) }
            }
        }
    }

    fun printUnusedVariables(inputFilePath: String) {
        val unusedAssignments = getUnusedAssignments(inputFilePath)
        unusedAssignments.forEach {
            println(it.toString())
        }
    }
}