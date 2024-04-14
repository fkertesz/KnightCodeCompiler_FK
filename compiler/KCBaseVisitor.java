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
        System.out.println("Visit file");
        String programName = ctx.getChild(1).getText();
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

        System.out.println("Visit body");
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
        

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

		    Utilities.writeFile(b, this.programName+".class");

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
	//@Override public Object visitBody(KnightCodeParser.BodyContext ctx)
	{
        //System.out.println("Visit body");
		//mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		//mainVisitor.visitCode();
        //return super.visitBody(ctx);
	}

	/**
	 * Method evaluates an expression, number or variable, and returns the value.
	 * This method makes operation methods and senterSetVar more compact.
	 * @param ctx
	 */
	public void loadExpr(String expr)
	{
		
		//If expression is a variable
		if(expr.matches("[0-9]+"))
		{
			int value = Integer.parseInt(expr);
			mainVisitor.visitLdcInsn(value);
		}
		else
		{
			String name = expr;
			Variable var = symbolTable.get(name);
			int location = var.getMemoryLocation();
			mainVisitor.visitVarInsn(Opcodes.ILOAD, location);
		}
	}

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
			String value = ctx.expr().getText();
			mainVisitor.visitLdcInsn(value);
			mainVisitor.visitVarInsn(Opcodes.ASTORE, var.getMemoryLocation());
		}
		else if(var.getType().equals("INTEGER"))
		{
			if(ctx.expr() instanceof KnightCodeParser.NumberContext || ctx.expr() instanceof KnightCodeParser.IdContext)
			{
				loadExpr(ctx.getChild(0).getText());
			}
            else
            {
                //Load sub expressions
                String first = ctx.getChild(0).getText();
                String second = ctx.getChild(2).getText();
                loadExpr(first);
                loadExpr(second);

                //Use operator
                String operator = ctx.getChild(1).getText();
                switch(operator)
                {
                    case "+":
                        mainVisitor.visitInsn(Opcodes.IADD);
                        break;
                    case "-":
                        mainVisitor.visitInsn(Opcodes.ISUB);
                        break;
                    case "*":
                        mainVisitor.visitInsn(Opcodes.IMUL);
                        break;
                    case "/":
                        mainVisitor.visitInsn(Opcodes.IDIV);
                        break;

                }
            }
                

			mainVisitor.visitVarInsn(Opcodes.ISTORE, var.getMemoryLocation());
		}
        return super.visitSetvar(ctx);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitParenthesis(KnightCodeParser.ParenthesisContext ctx)
    {
        return super.visitParenthesis(ctx);
    }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitComparison(KnightCodeParser.ComparisonContext ctx)
    {
        return super.visitComparison(ctx);
    }

	public int[] countStats(KnightCodeParser.CompContext ctx)
	{
		int[] counts = new int [2];
		int k = 0;
		
		for(int i = 0; i < ctx.getParent().children.size(); i++)
		{
			String name = ctx.getParent().children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
			{
				counts[k] ++;
			}
			//iterate to counting else stats
			else
			{
				if(counts[k] != 0)
				{
					k=1;
				}
			}
		}
        //FIXFIXFIX
        //Iterate through children and gather the indices of the then stats and the else stats
        int[] statsCounts = new int [counts[0]];
        int[] elseCounts = new int [counts[1]];
        int j = 0;
        int n = 0;
        for(int i = 0; i < ctx.getParent().children.size(); i++)
        {
            String name = ctx.getParent().children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
			{
				//thenCounts[j] = i;
                j++;
			}
            if(j == counts[0])
            {
                n = i;
                i=ctx.getParent().children.size();
            }
        }

        j=0;
        for(int m = n; m < ctx.getParent().children.size(); m++)
        {
            String name = ctx.getParent().children.get(m).getClass().getSimpleName();
			if(name.equals("StatContext"))
			{
				//thenCounts[j] = m;
                j++;
			}
        }

		return counts;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitComp(KnightCodeParser.CompContext ctx)
	{
		/*
		String sign = ctx.getText();
	
		Label compTrue = new Label();
		Label compFalse = new Label();

		if(sign.equals("<"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPLT, compTrue);
		else if(sign.equals(">"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPGT, compTrue);
		else if(sign.equals("="))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPEQ, compTrue);
		else if(sign.equals("<>"))
			mainVisitor.visitJumpInsn(Opcodes.IF_ICMPNE, compTrue);

		int[] counts = countStats(ctx);

		//When false, load 0's for then stats and load 1's for else stats
		for(int i = 0; i < counts[0]; i++)
			mainVisitor.visitLdcInsn(0);
		for(int j = 0; j < counts[1]; j++)
			mainVisitor.visitLdcInsn(1);
		mainVisitor.visitJumpInsn(Opcodes.GOTO, compFalse);

		mainVisitor.visitLabel(compTrue);

		//When true, load 1's for then stats and load 0's for else stats
		for(int i = 0; i < counts[0]; i++)
			mainVisitor.visitLdcInsn(1);
		for(int j = 0; j < counts[1]; j++)
			mainVisitor.visitLdcInsn(0);
		mainVisitor.visitLabel(compFalse);
		//Implement 0's and 1's!!!!!!!!!!!!!!!!
		visitTerminal(null);
		*/

        return super.visitComp(ctx);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitPrint(KnightCodeParser.PrintContext ctx)
	{
		
		if(ctx.ID() != null)
		{
			//Get info about variable
			String name = ctx.ID().getText();
			Variable var = symbolTable.get(name);

			//Print value of variable
			mainVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
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
		else if(ctx.STRING() != null)
		{
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

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 * @return 
	 */
	@Override public Object visitDecision(KnightCodeParser.DecisionContext ctx)
	{
		//Load the 2 expressions to be compared and compare them
		for(int i = 0; i < 2; i++)
		{
			if(ctx.ID(i) != null)
			{
				Variable var = symbolTable.get(ctx.ID(i).getText());
				mainVisitor.visitVarInsn(Opcodes.ILOAD, var.getMemoryLocation());
			}
			else if(ctx.NUMBER(i) != null)
			{
				int value = Integer.parseInt(ctx.NUMBER(i).getText());
				mainVisitor.visitLdcInsn(value);
			}
		}

        return super.visitDecision(ctx);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public Object visitLoop(KnightCodeParser.LoopContext ctx)
	{
		System.out.println("EnterLoop");
        return super.visitLoop(ctx);
	}

}