/**kcc
 * This class contains the main method that kicks off the KnightCode compiler.
 * It takes 2 arguments: 1. The name of the kc file (with path if necessary).
 * 2. The name of the output class file (with path if necessary).
 * @author Fanni Kertesz
 * @version 1.0
 * Assignment 5
 * CS 322 - Compiler Construction
 * Spring 2024
 */
package compiler;

import java.io.IOException;
import org.antlr.v4.runtime.tree.*;
import lexparse.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.*;

public class kcc
{
    public static void main(String[] args)
    {
        //If arguments are wrong, end program with error message
        if(args.length != 2) {
            System.out.println("Enter 2 arguments: 1. kc file 2. output file.");
            return;
        }
        else if(! args[0].endsWith(".kc")) {
            System.out.println("First argument must be a kc file.");
            return;
        }

        //Setting up lexer, parser, character stream (input), tokens (from input), and the output
        KnightCodeLexer lexer;
        KnightCodeParser parser;
        CharStream inputStream;
        CommonTokenStream tokenStream;
        String output;

        try{
            //Get input, and create lexer, token stream, parser, and tree
            inputStream = CharStreams.fromFileName(args[0]);
            lexer = new KnightCodeLexer(inputStream);
            tokenStream = new CommonTokenStream(lexer);
            parser = new KnightCodeParser(tokenStream);
            output = args[1];
            ParseTree tree = parser.file();

            //Create basevisitor and visit the tree, then close file when done
            KCBaseVisitor visitor = new KCBaseVisitor(output);
            visitor.visit(tree);
            visitor.closeFile();
        }

        //Catch input output exception and print error message
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }

    }//end main method
}//end class
