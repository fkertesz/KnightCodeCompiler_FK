// Generated from /home/kertefan/Documents/KnightCode_FK/KnightCode.g4 by ANTLR 4.13.1
package compiler;

import lexparse.*;

import java.util.*;
import compiler.utils.Utilities;
//import java.lang.*;

import org.antlr.runtime.tree.ParseTree;
//import org.antlr.v4.parse.ANTLRParser.elementOptions_return;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.objectweb.asm.*;

/**
 * This class provides an empty implementation of {@link KnightCodeListener},
 * which can be extended to create a listener which only needs to handle a subset
 * of the available methods.
 */
@SuppressWarnings("CheckReturnValue")
public class KCBaseVisitor extends KnightCodeBaseVisitor<Object> {

	private ClassWriter cw;
	private MethodVisitor mainVisitor;
	private String programName;
	private HashMap<String,Variable> symbolTable;
	private int memoryPtr; //memory pointer

	/**
	 * Constructor for this with program name
	 * @param programName
	 */
	public KCBaseVisitor(String programName)
	{
		this.programName = programName;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
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
	}

	/**
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	public void closeFile() 
	{
		    mainVisitor.visitInsn(Opcodes.RETURN);
		    mainVisitor.visitMaxs(0, 0);
		    mainVisitor.visitEnd();
		
		    cw.visitEnd();

		    byte[] b = cw.toByteArray();

		    Utilities.writeFile(b, programName+".class");

		    System.out.println("Compiled " + programName + "!");
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitDeclare(KnightCodeParser.DeclareContext ctx)
	{
		symbolTable = new HashMap<>();
		memoryPtr = 0;
        return super.visitDeclare(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitVariable(KnightCodeParser.VariableContext ctx)
	{
		String type = ctx.vartype().getText();
		String name = ctx.identifier().getText();
		Variable var = new Variable(type, name, memoryPtr++);
		symbolTable.put(name, var);
        return super.visitVariable(ctx);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitBody(KnightCodeParser.BodyContext ctx)
	{
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
        return super.visitBody(ctx);
	}

	public void evalExpr(KnightCodeParser.ExprContext ctx)
	{
		System.out.println("Eval expr");
        
        //If expr number, loads value
        if (ctx instanceof KnightCodeParser.NumberContext){
			System.out.println("Number context");
            int value = Integer.parseInt(ctx.getText());
            mainVisitor.visitLdcInsn(value);
        }
        // If the expr is ID, loads value
        else if (ctx instanceof KnightCodeParser.IdContext){
			System.out.println("Id context");
            String id = ctx.getText();
            Variable var = symbolTable.get(id);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, var.getMemoryLocation());
            
        }
		//If expr is an operational context, evaluate
        else if (ctx instanceof KnightCodeParser.AdditionContext)
		{
            evalExpr(((KnightCodeParser.AdditionContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.AdditionContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.IADD);
            
        }
		else if (ctx instanceof KnightCodeParser.SubtractionContext)
		{
            evalExpr(((KnightCodeParser.SubtractionContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.SubtractionContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.ISUB);
            
        }
        else if (ctx instanceof KnightCodeParser.MultiplicationContext)
		{
            evalExpr(((KnightCodeParser.MultiplicationContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.MultiplicationContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.IMUL);
        }
        else if (ctx instanceof KnightCodeParser.DivisionContext)
		{
            evalExpr(((KnightCodeParser.DivisionContext)ctx).expr(0));
			evalExpr(((KnightCodeParser.DivisionContext)ctx).expr(1));
        	mainVisitor.visitInsn(Opcodes.IDIV);   
        }

        
    }//end evalExpr

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitSetvar(KnightCodeParser.SetvarContext ctx)
	{
		//Fetch variable from symbol table
		String name = ctx.ID().getText();
		Variable var = symbolTable.get(name);

		if(var == null)
		{
			System.err.println("Variable called " + name + " has not been declared.");
			System.exit(1);
		}
		//If the variable is an integer, evaluate
		else if(!var.getType().equals("INTEGER") && !var.getType().equals("STRING"))
		{
			System.err.println("Variable called " + name + " has unrecognized type.");
			System.exit(1);
		}
		else if(var.getType().equals("STRING"))
		{
			String valueExtra = ctx.STRING().getText();
			String value = valueExtra.replace("\"", "");
			System.out.println(value);
			mainVisitor.visitLdcInsn(value);
			mainVisitor.visitVarInsn(Opcodes.ASTORE, var.getMemoryLocation());
		}
		else if(var.getType().equals("INTEGER"))
		{
            evalExpr(ctx.expr());
			mainVisitor.visitVarInsn(Opcodes.ISTORE, var.getMemoryLocation());
		}
        return super.visitSetvar(ctx);
	}
	
	/**
	 * Method triggered for PrintContext. Can print out a variable or string literal given in the kc program.
	 * @return Object context for next visit
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
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
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
	}

	public int[] countStats(KnightCodeParser.DecisionContext ctx)
	{
		int[] counts = new int [2];
		counts[0] = 0;
		counts[1] = 0;
		int numChildren = ctx.children.size();
		
		//Count how many stats are between then and else
		for(int i = 5; i < numChildren; i++)
		{
			String name = ctx.children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
				counts[0] ++;
			else
				break;
		}
		
		//If then stat nodes + if nodes + endif node aren't all children, count else stats
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
	}

	public int countStats(KnightCodeParser.LoopContext ctx)
	{
		int count = 0;
		int numChildren = ctx.children.size();

		//Count how many stats are between then and else
		for(int i = 5; i < numChildren; i++)
		{
			String name = ctx.children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
				count ++;
			else
				break;
		}
        
		return count;
	}

	public void evalSimpleExpr(String value)
	{
		if (symbolTable.get(value) != null)
		{
			mainVisitor.visitVarInsn(Opcodes.ILOAD, symbolTable.get(value).getMemoryLocation());
		}
		else
		{
			mainVisitor.visitLdcInsn(Integer.parseInt(value));
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 * @return 
	 */
	@Override public Object visitDecision(KnightCodeParser.DecisionContext ctx)
	{
		//Load the 2 expressions to be compared and compare them
		String first = ctx.getChild(1).getText();
		String second = ctx.getChild(3).getText();
		String sign = ctx.getChild(2).getText();
		
		evalSimpleExpr(first);
        evalSimpleExpr(second);

		Label thenLabel = new Label();
		Label endLabel = new Label();

		//Count stats
		int[] counts = countStats(ctx);

		//if no then stats, syntax error
		if(counts[0] == 0)
		{
			System.out.println("Stat(s) required after THEN.");
			return null;
		}
		else
		{
			//If true, jump to then statement executions
			if(sign.equals("<"))
				mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, thenLabel);
			else if(sign.equals(">"))
				mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, thenLabel);
			else if(sign.equals("="))
				mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, thenLabel);
			else if(sign.equals("<>"))
				mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, thenLabel);
	

			//Visit else stats, then jump to end
			for(int i = 0; i < counts[1]; i++)
			{
				visit(ctx.getChild(6+counts[0]+i));
			}
			mainVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);
			mainVisitor.visitLabel(thenLabel);

			for(int i = 0; i < counts[0]; i++)
			{
				visit(ctx.getChild(5+i));
			}

			mainVisitor.visitLabel(endLabel);

        	return null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitLoop(KnightCodeParser.LoopContext ctx)
	{
		String first = ctx.getChild(1).getText();
        String second = ctx.getChild(3).getText();
		String sign = ctx.getChild(2).getText();

        Label endLabel = new Label();
        Label startLabel = new Label();

        mainVisitor.visitLabel(startLabel);

        evalSimpleExpr(first);
        evalSimpleExpr(second);

        switch( sign ){
            case ">":
                mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLE, endLabel);
                break;
            case "<":
                mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGE, endLabel);
                break;
            case "=":
                mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, endLabel);
                break;
            case "<>":
                mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, endLabel);
                break;
        }


        for(int i = 5 ; i < ctx.children.size()-1 ; i++) {
            visit(ctx.getChild(i));
        }
		
        mainVisitor.visitJumpInsn(Opcodes.GOTO, startLabel);

        mainVisitor.visitLabel(endLabel);

        mainVisitor.visitInsn(Opcodes.RETURN);

        return null;
	}

}