# KnightCodeCompiler_FK

## Overview

This repository is a compiler built for the KnightCode programming language. This compiler translates KnightCode source files into Java bytecode using ANTLR for lexing and parsing, and ASM for bytecode manipulation.

## Features

The compiler supports the following language features of KnightCode:
- Variables of type INTEGER and STRING
- Printing variables and string literals onto the screen
- Reading values for variables from the user
- Basic arithmetic operations (addition, subtraction, multiplication, division)
- Control structures including IF/ELSE and WHILE loops

## Required libraries

- ANTLR: antlr-4.13.1-complete.jar
- ASM: asm-9.6.jar

## Set-up Instructions

- Clone this repository onto your machine. To do this, run "git clone https://github.com/fkertesz/KnightCodeCompiler_FK.git" in your desired directory in the command line. Change into this directory (KnightCodeCompiler_FK).
- Run the commands "ant build-grammar", then "ant compile-grammar", then "ant compile". These commands will create a lexparse directory with the appropriate java files built by ANTLR and compile these java files and the compiler java files into class files.

## Using the Compiler

- To compile kc files, run the command "java compiler.kcc path/programIP.kc output/programOP" where "path" is the path to the kc file to be compiled, "programIP.kc" is the kc file to be compiled, and "programOP" is the name of the class file this will compile (usually, "programIP" and "programOP" will be the same). The compiled class file will be in the output directory.
- To run the compiled file, run the command "java output/programOP" where "programOP" is the name of the class file which was compiled in the previous step. Your kc program is now running!

## Example Usage

- To compile "program1.kc" in the tests directory and name the compiled class file "program1" as well while in the KnightCodeCompiler_FK directory, run "java compiler.kcc tests/program1.kc output/program1" to compile and then "java output/program1" to run the program1.

##### Author: Fanni Kertesz
##### This is a project for the Compiler Construction class at Bellarmine University.
