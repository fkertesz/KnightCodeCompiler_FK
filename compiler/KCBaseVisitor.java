/**KCBaseVisitor
 * This class is an extension of KnightCodeBaseVisitor with all necessary inherited methods
 * overriden and some additional methods, and is used to execute code as the tree is visited.
 * The class is used to compile KnightCode files in the kcc class.
 * @author Fanni Kertesz
 * @version 1.0
 * Assignment 5
 * CS 322 - Compiler Construction
 * Spring 2024
 */
package compiler;

import lexparse.*;
import java.util.*;
import compiler.utils.Utilities;
import org.objectweb.asm.*;

@SuppressWarnings("CheckReturnValue")
public class KCBaseVisitor extends KnightCodeBaseVisitor<Object> {

	private ClassWriter cw;//class writer for writing the output class
	private MethodVisitor mainVisitor;//method visitor for the main method
	private String programName;//name for the output program
	private HashMap<String,Variable> symbolTable;//symbol table for storing the variables
	private int memoryPtr; //memory pointer for variable memory locations

	/**
	 * Constructor with program name, the output program.
	 * @param programName
	 */
	public KCBaseVisitor(String programName)
	{
		this.programName = programName;
	}//end constructor

	/**
	 * Method writes the class and constructor for the program.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitFile(KnightCodeParser.FileContext ctx)
	{

		//Write class for program
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, programName, null, "java/lang/Object", null);

		//Creating a constructor for the class
		{
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			mv.visitInsn(Opcodes.RETURN);	
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

        return super.visitFile(ctx);
	}//end visitFile

	/**
	 * Method closes and writes the file.
	 */
	public void closeFile() 
	{
		// end visits and write class
		mainVisitor.visitInsn(Opcodes.RETURN);
		mainVisitor.visitMaxs(0, 0);
		mainVisitor.visitEnd();
		cw.visitEnd();

		byte[] b = cw.toByteArray();
		Utilities.writeFile(b, programName + ".class");

		System.out.println("Compiled " + programName + "!");
	}//end closeFile

	/**
	 * Method for visiting variable declarations.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitDeclare(KnightCodeParser.DeclareContext ctx)
	{
		//Initializing symbol table and memory pointer at 0
		symbolTable = new HashMap<>();
		memoryPtr = 0;
        return super.visitDeclare(ctx);
	}//end visitDeclare

	/**
	 * Method for visiting a variable being declared.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitVariable(KnightCodeParser.VariableContext ctx)
	{
		//Store name, type, and memory location of variable in a variable object
		String type = ctx.vartype().getText();
		String name = ctx.identifier().getText();
		Variable var = new Variable(type, name, memoryPtr++);

		//If type isn't INTEGER or STRING, print error message and exit program
		if(! type.equals("STRING") && ! type.equals("INTEGER"))
		{
			System.out.println("Variable " + name + " has unkown type. Program cannot compile.");
			System.exit(1);
		}

		//Store variable object in symbol table
		symbolTable.put(name, var);
        return super.visitVariable(ctx);
	}//end visitVariable

	/**
	 * Method for visiting the body.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitBody(KnightCodeParser.BodyContext ctx)
	{
		//Visit main method
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
        return super.visitBody(ctx);
	}//end visitBody

	/**
	 * Method for evaluating integer expressions and loading the value onto the stack.
	 * @param ctx expression context
	 */
	public void evalExpr(KnightCodeParser.ExprContext ctx)
	{   
        //If the expression is a number, load value
        if (ctx instanceof KnightCodeParser.NumberContext)
		{
            int value = Integer.parseInt(ctx.getText());
            mainVisitor.visitLdcInsn(value);
        }
        // If the expression is a variable, load value
        else if (ctx instanceof KnightCodeParser.IdContext)
		{
            String id = ctx.getText();
            Variable var = symbolTable.get(id);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, var.getMemoryLocation());
            
        }
		//If the expression is an operational context, evaluate
        else if (ctx instanceof KnightCodeParser.AdditionContext)
		{
			//Evaluate operand expressions, then add
            evalExpr(((KnightCodeParser.AdditionContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.AdditionContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.IADD);
            
        }
		else if (ctx instanceof KnightCodeParser.SubtractionContext)
		{
			//Evaluate operand expressions, then subtract
            evalExpr(((KnightCodeParser.SubtractionContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.SubtractionContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.ISUB);
            
        }
        else if (ctx instanceof KnightCodeParser.MultiplicationContext)
		{
			//Evaluate operand expressions, then multiply
            evalExpr(((KnightCodeParser.MultiplicationContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.MultiplicationContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.IMUL);
        }
        else if (ctx instanceof KnightCodeParser.DivisionContext)
		{
			//Evaluate operand expressions, then divide
            evalExpr(((KnightCodeParser.DivisionContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.DivisionContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.IDIV);   
        }

    }//end evalExpr

	/**
	 * Method for visiting integer and string variables being set.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitSetvar(KnightCodeParser.SetvarContext ctx)
	{
		//Fetch variable from symbol table
		String name = ctx.ID().getText();
		Variable var = symbolTable.get(name);

		//If the variable is null, print error and stop program.
		if(var == null)
		{
			System.err.println("Variable called " + name + " has not been declared. Program cannot compile.");
			System.exit(1);
		}
		//If the variable isn't an integer or string, print error and stop program.
		else if(!var.getType().equals("INTEGER") && !var.getType().equals("STRING"))
		{
			System.err.println("Variable called " + name + " has unrecognized type. Program cannot compile.");
			System.exit(1);
		}
		//If the variable is a string, get string value and store it
		else if(var.getType().equals("STRING"))
		{
			String valueExtra = ctx.STRING().getText();
			String value = valueExtra.replace("\"", "");
			mainVisitor.visitLdcInsn(value);
			mainVisitor.visitVarInsn(Opcodes.ASTORE, var.getMemoryLocation());
		}
		//If the variable is an integer, evaluate and store
		else if(var.getType().equals("INTEGER"))
		{
            evalExpr(ctx.expr());
			mainVisitor.visitVarInsn(Opcodes.ISTORE, var.getMemoryLocation());
		}

        return super.visitSetvar(ctx);
	}//end visitSetVar
	
	/**
	 * Method with instructions to print out a variable or string literal.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitPrint(KnightCodeParser.PrintContext ctx)
	{
		//Set up print stream
		mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

		//Printing a variable
		if(ctx.ID() != null)
		{
			//Get info about variable
			String name = ctx.ID().getText();
			Variable var = symbolTable.get(name);

			//Load variable from memory location, then print it.
			if(var.getType().equals("STRING"))
			{
        		mainVisitor.visitVarInsn(Opcodes.ALOAD, var.memoryLocation);
        		mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
			}
			else if(var.getType().equals("INTEGER"))
			{
				mainVisitor.visitVarInsn(Opcodes.ILOAD, var.memoryLocation);
        		mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
			}
		}

		//Printing a string literal
		else if(ctx.STRING() != null)
		{
			//Get string inside quotation marks, load it onto stack, print it
			String valueExtra = ctx.STRING().getText();
			String value = valueExtra.replaceAll("\"", "");
			mainVisitor.visitLdcInsn(value);
			mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		}

        return super.visitPrint(ctx);
	}//end visitPrint
	
	/**
	 * Method for reading integer and string variable values from user.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitRead(KnightCodeParser.ReadContext ctx)
	{
		//Variable to be read
		String name = ctx.ID().getText();
		Variable var = symbolTable.get(name);
		int scanLoc = memoryPtr++;

		//Create new scanner with System.in input and load it
		mainVisitor.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
		mainVisitor.visitInsn(Opcodes.DUP);
		mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");//static field from system.in
		mainVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);//Scanner constructor with system.in argument
		mainVisitor.visitVarInsn(Opcodes.ASTORE, scanLoc);
		mainVisitor.visitVarInsn(Opcodes.ALOAD, scanLoc);

		//Get input and store appropriately
		if(var.getType().equals("STRING"))
		{
			mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
			mainVisitor.visitVarInsn(Opcodes.ASTORE, var.getMemoryLocation());
		}
		else if(var.getType().equals("INTEGER"))
		{
			mainVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/util/Scanner", "nextInt", "()I", false);
			mainVisitor.visitVarInsn(Opcodes.ISTORE, var.getMemoryLocation());
		}

        return super.visitRead(ctx);
	}//end visitRead

	/**
	 * Method for counting the stat context of decisions. It counts the stats after THEN and after ELSE.
	 * @param ctx decision context
	 * @return array where the first entry is the count of the THEN stats and the second is the counts of the ELSE stats.
	 */
	public int[] countStats(KnightCodeParser.DecisionContext ctx)
	{
		//Initialize the counting array and get number of children of decision context
		int[] counts = new int [2];
		counts[0] = 0;
		counts[1] = 0;
		int numChildren = ctx.children.size();
		
		//Count how many stats are after THEN
		for(int i = 5; i < numChildren; i++)
		{
			String name = ctx.children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
				counts[0] ++;
			else
				break;
		}
		
		//If then stat nodes + decision nodes + endif node aren't all children, count the stats after ELSE
		//Otherwise, the ELSE stats remain 0
		if(6+counts[0] != numChildren)
		{
			//Count how many stats are between else and endif
			for(int i = numChildren-2; i > -1; i--)
			{
				String name = ctx.children.get(i).getClass().getSimpleName();
				if(name.equals("StatContext"))
					counts[1]++;
				else
					break;
			}
		}
        
		return counts;
	}//end countStats

	/**
	 * Method for evaluating the simple integer expressions in the methods
	 * for visiting decisions and loops.
	 * @param value String representation of the value
	 */
	public void evalSimpleExpr(String value)
	{
		//If the value is a variable, load it
		if (symbolTable.get(value) != null)
		{
			mainVisitor.visitVarInsn(Opcodes.ILOAD, symbolTable.get(value).getMemoryLocation());
		}
		else
		{
			mainVisitor.visitLdcInsn(Integer.parseInt(value));
		}
	}//end evalSimpleExpr

	/**
	 * Method for visiting decision.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitDecision(KnightCodeParser.DecisionContext ctx)
	{
		//Get the first and second integer to be compared and the comparing sign
		String first = ctx.getChild(1).getText();
		String second = ctx.getChild(3).getText();
		String sign = ctx.getChild(2).getText();
		
		//Evaluate the 2 integers to be compared
		evalSimpleExpr(first);
        evalSimpleExpr(second);

		//Create labels for jumps
		Label thenLabel = new Label();
		Label endLabel = new Label();

		//Count stats
		int[] counts = countStats(ctx);

		//if no then stats, print out error message and exit program
		if(counts[0] == 0)
		{
			System.out.println("At least 1 stat required after THEN. Program cannot compile.");
			System.exit(1);
		}
		
		// If true comparison true, jump to then statement executions
		if (sign.equals("<"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, thenLabel);
		else if (sign.equals(">"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, thenLabel);
		else if (sign.equals("="))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, thenLabel);
		else if (sign.equals("<>"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, thenLabel);

		// Visit else stats (if any) if false, then jump to end
		for (int i = 0; i < counts[1]; i++) {
			visit(ctx.getChild(6 + counts[0] + i));
		}
		mainVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);

		//Visit then label and visit then stats
		mainVisitor.visitLabel(thenLabel);
		for (int i = 0; i < counts[0]; i++) {
			visit(ctx.getChild(5 + i));
		}

		mainVisitor.visitLabel(endLabel);

		//Visit end label and return no instructions to visit children of context node
		return null;

	}//end visitDecision
	
	/**
	 * Method for visiting loop.
	 * @return visit instructions from method inherited
	 */
	@Override public Object visitLoop(KnightCodeParser.LoopContext ctx)
	{
		//If no stats in loop, print out error message and end program
		if(ctx.children.size() == 6)
		{
			System.out.println("Loop requires at least 1 stat. Program cannot compile.");
			System.exit(1);
		}

		//Get the first and second integer to be compared and the comparing sign
		String first = ctx.getChild(1).getText();
        String second = ctx.getChild(3).getText();
		String sign = ctx.getChild(2).getText();

		//Create labels for jumps
        Label endLabel = new Label();
        Label startLabel = new Label();

		//Start label visitation when loop is to be potentially executed again
        mainVisitor.visitLabel(startLabel);

		//Evaluate the integer expressions
        evalSimpleExpr(first);
        evalSimpleExpr(second);
		
		//If opposite of sign is true (so comparison false), jump to end label
		if (sign.equals(">"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLE, endLabel);
		else if (sign.equals("<"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
		else if (sign.equals("="))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, endLabel);
		else if (sign.equals("<>"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, endLabel);

		//If the comparison was true, visit stats and go jump to the start label
        for(int i = 5 ; i < ctx.children.size()-1 ; i++) {
            visit(ctx.getChild(i));
        }
        mainVisitor.visitJumpInsn(Opcodes.GOTO, startLabel);

		//Visit end label and return no instructions to visit children of context node
        mainVisitor.visitLabel(endLabel);

        mainVisitor.visitInsn(Opcodes.RETURN);

        return null;
	}//end visitLoop

}//end class