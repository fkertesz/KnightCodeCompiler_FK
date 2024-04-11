package compiler;

import java.io.IOException;

import org.antlr.v4.gui.Trees;
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
            //Get input, create lexer, create token stream, and create parser
            inputStream = CharStreams.fromFileName(args[0]);
            lexer = new KnightCodeLexer(inputStream);
            tokenStream = new CommonTokenStream(lexer);
            parser = new KnightCodeParser(tokenStream);
            output = args[1];

            //Create parse tree and display it
            ParseTree tree = parser.file();
            Trees.inspect(tree, parser);

            //Create baselistener and parse tree walker, parse tree walker walk the tree
            KCBaseListener baseListener = new KCBaseListener(output);
            ParseTreeWalker ptWalker = new ParseTreeWalker();
            ptWalker.walk(baseListener, tree);
        }

        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}