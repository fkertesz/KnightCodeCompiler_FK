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
        //programName = ctx.ID().getText();
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
		System.out.println("Visit var");
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
        System.out.println("Visit body");
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
        return super.visitBody(ctx);
	}

	/**
	 * Method evaluates an expression, number or variable, and returns the value.
	 * This method makes operation methods and senterSetVar more compact.
	 * @param ctx
	 */
	/*
	public void loadExpr(String expr)
	{
		System.out.println("Expression: " + expr);
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
	}*/

	public void evalExpr(KnightCodeParser.ExprContext ctx)
	{
        
        //If expr number, loads value
        if (ctx instanceof KnightCodeParser.NumberContext){
            int value = Integer.parseInt(ctx.getText());
            mainVisitor.visitLdcInsn(value);
        }


        // If the expr is ID, loads value
        else if (ctx instanceof KnightCodeParser.IdContext){
            String id = ctx.getText();
            Variable var = symbolTable.get(id);
            mainVisitor.visitVarInsn(Opcodes.ILOAD, var.getMemoryLocation());
            
        }

		//If expr is an operational context, evaluate
        else if (ctx instanceof KnightCodeParser.AdditionContext)
		{
            for(KnightCodeParser.ExprContext expr : ((KnightCodeParser.AdditionContext)ctx).expr())
			{
                evalExpr(expr);
            }
        mainVisitor.visitInsn(Opcodes.IADD);
            
        }
		else if (ctx instanceof KnightCodeParser.SubtractionContext)
		{
            
            for(KnightCodeParser.ExprContext expr : ((KnightCodeParser.SubtractionContext)ctx).expr())
			{
                evalExpr(expr);
            }
        mainVisitor.visitInsn(Opcodes.ISUB);
            
        }
        else if (ctx instanceof KnightCodeParser.MultiplicationContext)
		{
            for(KnightCodeParser.ExprContext expr : ((KnightCodeParser.MultiplicationContext)ctx).expr())
			{
                evalExpr(expr);
            }
        mainVisitor.visitInsn(Opcodes.IMUL);
        }
        else if (ctx instanceof KnightCodeParser.DivisionContext)
		{
            for(KnightCodeParser.ExprContext expr : ((KnightCodeParser.DivisionContext)ctx).expr())
			{
                evalExpr(expr);
            }
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
			String valueExtra = ctx.expr().getText();
			String value = valueExtra.replace("\"", "");
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

	public int[] countStats(KnightCodeParser.DecisionContext ctx)
	{
		int[] counts = new int [2];
		counts[0] = 0;
		counts[1] = 0;
		int numChildren = ctx.children.size();
		System.out.println(numChildren);
		
		//Count how many stats are between then and else
		for(int i = 5; i < numChildren; i++)
		{
			String name = ctx.children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
				counts[0] ++;
			else
				break;
		}
		System.out.println(counts[0]);

		//Count how many stats are between else and endif
		for(int i = numChildren-2; i > -1; i--)
		{
			String name = ctx.children.get(i).getClass().getSimpleName();
			if(name.equals("StatContext"))
				counts[1]++;
			else
				break;
		}
		System.out.println(counts[1]);
        
		return counts;
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

		String sign = ctx.getChild(2).getText();
		
		Label thenLabel = new Label();
		Label endLabel = new Label();

		//Count stats
		int[] counts = countStats(ctx);
		
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