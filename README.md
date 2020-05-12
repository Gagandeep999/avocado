<h1>Avocado</h1>

<h4> What is it? </h3>

An academic project of designing a compiler.

<h4> How it works?</h4>

The process is divided into 4 parts:
* Lexer / Lexical Analysis 
* Parser / Syntactic Analysis
* Abstract Syntax Tree / Semantic Analysis
* Code Generation

<h4> > LEXER </h4>
--
A lexical analyzer that recognizes the tokens of the language and returns a list of token data structure.

<h4> > PARSER</h4>
--
The list of token from the lexer is fed to the parser which outputs an abstract syntax tree. The approach
to parsing is predictive parsing using recursive descent.
A LL(1) grammar is used to generate the AST.

<h4> > ABSTRACT SYNTAX TREE</h4>
--
This step is where, the complete code that becomes a list of token, is converted into a tree data structure.
The nodes of the tree are extended from an abstract class which allows to use the <b>visitor pattern</b> to complete
other tasks like symbol table generation and type checking easily and conveniently.
The idea is to have special nodes for some of the tokens, e.g. - funcDefNode() or classDeclNode() which when 
visited act differently than others.
Symbol table is generated in this step and all the semantic checks are performed here like checking for duplicate
variable, return type of functions etc. 

<h4> > CODE GENERATION</h4>
--
This compiler uses the tag-based approach for code generation, which is basically having a tag for each part of the code
and then using those tags to create the assembly code.
The idea is to traverse the AST once again and depending on the node that we are at, we perform some operation and write 
it's corresponding assembly code. As an example, for the addOpNode, we require three registers, one to store the result
of the operation and two to store the values of the operands. Assembly code is written in sequential manner as, loading 
both the operands first and then storing the result of the operation in the register allocated for result. Then finally 
storing the result into some memory location.

---

<h5>Testing of the compiler</h5>

All the test files are located in the src/test directory.

<h6>LEXER</h6>
Test cases are all located in src/test/LexerTest.
To run parser test cases main class - lexer.lexdriver. 
Program arguments - src/test/LexerTest/<filename>.src
* Output - 
    * filename.outlextokens
    * filename.outlexerror


<h6>PARSER</h6>
Test cases are all located in src/test/ParserTest.
To run parser test cases main class - parser.parsedriver.
Program arguments - src/test/ParserTest/<filename>.src
* Output - 
    * filename.outderivation
    * filename.outsyntaxerror
    * filename.outast


<h6>SYMBOL TABLE</h6>
Test cases are all located in src/test/SymbolTableTest.
To run symbol table test cases main class - visitor.symTabVisitorDriver.
Program arguments - src/test/SymbolTableTest/<filename>.src
* Output -
    * filename.outsymboltables
    * filename.outsemanticerrors

<h6>CODE GENERATION</h6>
Test cases are all located in src/test/CodeGen.
To run symbol table test cases main class - visitor.CodeGenVisitorDriver.
Program arguments - src/test/CodeGenTest/<filename>.src
* Output -
    * filename.m

To run the moon code, type `./moon filename.m util.m` in the terminal. The moon executable is located in the 
src/test/CodeGenTest directory along with some samples.
