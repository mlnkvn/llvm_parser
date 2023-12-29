# Parser for LLVM language
The language given by the following grammar:
```
program ::= statement_list
statement_list ::= statement | statement_list statement
statement ::= variable '=' expression | 'if' expression statement_list 'end' | 'while' expression statement_list 'end'   
expression ::= variable | constant | '(' expression ')' | expression operator expression
operator ::= '+' | '-' | '*' | '/' | '<' | '>'
```

Parser also supports search of unused assignment  (an assignment to a variable is considered unused if at no further point in the program execution this variable will be read before the next assignment to it or program exit).

`Main.kt` should be executed with one argument: path to file with program code that should be parsed.
