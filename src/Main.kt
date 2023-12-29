import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw IllegalArgumentException("Incorrect number of arguments. Expected one argument: path to input file.")
    }
    val parser = LanguageParser()
    val parsedProgram = parser.parse(args[0])
    println(parsedProgram.toString())
    println("\nUnused Variables:")
    parser.printUnusedVariables(args[0])
}