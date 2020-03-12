<h1>Avocado</h1>

<h4> What is it? </h3>

An academic project of designing a compiler.

<h4> How it works?</h4>

The process is divided into 4 parts:
--Lexer
--Parser
--Abstract Syntax Tree
--Code Generation

<h4>LEXER</h4>
--
<h4>PARSER</h4>
--
<h4>ABSTRACT SYNTAX TREE</h4>
--
This step is where, the complete code that becomes a list of token, is converted into a tree data structure.
The nodes of the tree are extended from an abstract class which allows to use the visitor pattern to complete
other tasks like symbol table generation and type checking easily and conveniently.
The idea is to have special nodes for some of the tokens, e.g. - funcDefNode() or classDeclNode() which when 
visited act differently than others.

<h4>CODE GENERATION</h4>
--

---


<h5>Testing of the compiler</h5>

Directory structure is as follows-
Test cases are all located in src/test/LexerTest and src/test/ParserTest

To run parser test cases
Main class - parser.parsedriver
Program arguments - src/test/ParserTest/<file_name>.src

To run lexer test cases
Main class - lexer.lexdriver
Program arguments - src/test/LexerTest/<file_name>.src

To check for the abstract syntax tree / symbol table


Other details-
To be added...