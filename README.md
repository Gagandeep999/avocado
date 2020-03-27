<h1>Avocado</h1>

<h4> What is it? </h3>

An academic project of designing a compiler.

<h4> How it works?</h4>

The process is divided into 4 parts:
-->Lexer
-->Parser
-->Abstract Syntax Tree
-->Code Generation

<h4> > LEXER</h4>
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
The nodes of the tree are extended from an abstract class which allows to use the visitor pattern to complete
other tasks like symbol table generation and type checking easily and conveniently.
The idea is to have special nodes for some of the tokens, e.g. - funcDefNode() or classDeclNode() which when 
visited act differently than others.

<h4> > CODE GENERATION</h4>
--

---


<h5>Testing of the compiler</h5>

All the test files are located in the src/test directory.

<h6>Lexer</h6>
Test cases are all located in src/test/LexerTest
To run parser test cases
Main class - lexer.lexdriver
Program arguments - src/test/LexerTest/<file_name>.src
Output - two files with extension 
         filename.outlextokens
         filename.outlexerror


<h6>Parser</h6>
Test cases are all located in src/test/ParserTest
To run parser test cases
Main class - parser.parsedriver
Program arguments - src/test/ParserTest/<file_name>.src
Output - two files with extension 
         filename.outderivation
         filename.outsyntaxerror
         filename.gv


<br />
<br />
<br />
<br />

To run lexer test cases
Main class - lexer.lexdriver
Program arguments - src/test/LexerTest/<file_name>.src

To check for the abstract syntax tree / symbol table


Other details-
To be added...