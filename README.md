# KnightCode_FK

Overview:

This repository is a compiler built for the KnightCode language using ANTLR. KnightCode supports INTEGER and STRING variables, printing variables and string literals onto the screen, basic arithmetic (addition, subtraction, multiplication, division), comparisons of integers, getting inputs from user, if/else conditionals, and while loops.

Required libraries:

- ANTLR: antlr-4.13.1-complete.jar
- ASM: asm-9.6.jar

Set-up Instructions:
- Clone this repository onto your machine. To do this, run "git clone https://github.com/fkertesz/KnightCode_FK.git" in your desired directory in the command line. Change into this directory (KnightCode_FK).
- Run the commands "ant build-grammar", then "ant compile-grammar", then "ant compile". These commands will create a lexparse directory with the appropriate java files built by ANTLR and compile these java files and the compiler java files into class files.

Using the Compiler:

- To compile kc files, run the command "java compiler.kcc path/programIP.kc output/programOP" where "path" is the path to the kc file to be compiled, "programIP.kc" is the kc file to be compiled, and "programOP" is the name of the class file this will compile (usually, "programIP" and "programOP" will be the same). The compiled class file will be in the output directory.
- To run the compiled file, run the command "java output/programOP" where "programOP" is the name of the class file which was compiled in the previous step. Your kc program is now running!

Author: Fanni Kertesz
This is a project for the Compiler Construction class at Bellarmine University.
