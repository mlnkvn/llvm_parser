fun main(args: Array<String>) {
    val parser = LanguageParser()
    val parsedProgram = parser.parse(args[0])
    println(parsedProgram.toString())
    println("\nUnused Variables:")
    parser.printUnusedVariables(args[0])
}