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
public class KCBaseListener extends KnightCodeBaseListener {

	private ClassWriter cw;
	private MethodVisitor mainVisitor;
	private String programName;
	private HashMap<String,Variable> symbolTable;
	private int memoryPtr; //memory pointer

	/**
	 * Constructor for this with program name
	 * @param programName
	 */
	public KCBaseListener(String programName)
	{
		this.programName = programName;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFile(KnightCodeParser.FileContext ctx)
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
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitFile(KnightCodeParser.FileContext ctx) 
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
	@Override public void enterDeclare(KnightCodeParser.DeclareContext ctx)
	{
		symbolTable = new HashMap<>();
		memoryPtr = 0;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDeclare(KnightCodeParser.DeclareContext ctx)
	{

	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVariable(KnightCodeParser.VariableContext ctx)
	{
		String type = ctx.vartype().getText();
		String name = ctx.identifier().getText();
		Variable var = new Variable(type, name, memoryPtr++);
		symbolTable.put(name, var);
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVariable(KnightCodeParser.VariableContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterIdentifier(KnightCodeParser.IdentifierContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitIdentifier(KnightCodeParser.IdentifierContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVartype(KnightCodeParser.VartypeContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVartype(KnightCodeParser.VartypeContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBody(KnightCodeParser.BodyContext ctx)
	{
		mainVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mainVisitor.visitCode();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBody(KnightCodeParser.BodyContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterStat(KnightCodeParser.StatContext ctx)
	{
		//if(ctx.getParent().getClass().getSimpleName().equals("DecisionContext"))
		//{
		//	if(ctx.print() != null)
		//	{
		//		//ctx.
		//	}
		//}

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitStat(KnightCodeParser.StatContext ctx) { }

	/**
	 * Method evaluates an expression, number or variable, and returns the value.
	 * This method makes operation methods and senterSetVar more compact.
	 * @param ctx
	 */
	public void loadExpr(KnightCodeParser.ExprContext ctx)
	{
		
		//If expression is a variable
		if(ctx instanceof KnightCodeParser.NumberContext)
		{
			int value = Integer.parseInt(ctx.getText());
			mainVisitor.visitLdcInsn(value);
		}
		else if(ctx instanceof KnightCodeParser.IdContext)
		{
			String name = ctx.getText();
			Variable var = symbolTable.get(name);
			int location = var.getMemoryLocation();
			mainVisitor.visitVarInsn(Opcodes.ILOAD, location);
		}
		System.out.println("Loaded " + ctx.getText());
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSetvar(KnightCodeParser.SetvarContext ctx) 
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
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSetvar(KnightCodeParser.SetvarContext ctx)
	{
		//Fetch variable from symbol table
		String name = ctx.ID().getText();
		Variable var = symbolTable.get(name);

		if(var.getType().equals("STRING"))
		{
			String value = ctx.expr().getText();
			mainVisitor.visitLdcInsn(value);
			mainVisitor.visitVarInsn(Opcodes.ASTORE, var.getMemoryLocation());
		}
		else if(var.getType().equals("INTEGER"))
		{
			if(ctx.expr() instanceof KnightCodeParser.NumberContext || ctx.expr() instanceof KnightCodeParser.IdContext)
			{
				loadExpr(ctx.expr());
			}
			mainVisitor.visitVarInsn(Opcodes.ISTORE, var.getMemoryLocation());
		}
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParenthesis(KnightCodeParser.ParenthesisContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParenthesis(KnightCodeParser.ParenthesisContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMultiplication(KnightCodeParser.MultiplicationContext ctx)
	{
		//Load the 2 numbers to be multiplied and multiplies them
		for(int i = 0; i < 2; i++)
		{
			loadExpr(ctx.expr(i));
		}
		mainVisitor.visitInsn(Opcodes.IMUL);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMultiplication(KnightCodeParser.MultiplicationContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterAddition(KnightCodeParser.AdditionContext ctx)
	{
		//Load the 2 numbers to be added and add them
		for(int i = 0; i < 2; i++)
		{
			loadExpr(ctx.expr(i));
		}
		mainVisitor.visitInsn(Opcodes.IADD);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitAddition(KnightCodeParser.AdditionContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSubtraction(KnightCodeParser.SubtractionContext ctx)
	{
		//Load the 2 numbers to be subtracted and subtract them
		for(int i = 0; i < 2; i++)
		{
			loadExpr(ctx.expr(i));
		}
		mainVisitor.visitInsn(Opcodes.ISUB);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSubtraction(KnightCodeParser.SubtractionContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterNumber(KnightCodeParser.NumberContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitNumber(KnightCodeParser.NumberContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterComparison(KnightCodeParser.ComparisonContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitComparison(KnightCodeParser.ComparisonContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDivision(KnightCodeParser.DivisionContext ctx)
	{
		//Load the 2 numbers to be divided and divide them
		for(int i = 0; i < 2; i++)
		{
			loadExpr(ctx.expr(i));
		}
		mainVisitor.visitInsn(Opcodes.IDIV);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDivision(KnightCodeParser.DivisionContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterId(KnightCodeParser.IdContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitId(KnightCodeParser.IdContext ctx) { }

	/**
	 * Method to count how many stats a decision has for then and else
	 * @return
	 */
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
					k++;
				}
			}
		}
		return counts;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterComp(KnightCodeParser.CompContext ctx)
	{
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
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitComp(KnightCodeParser.CompContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterPrint(KnightCodeParser.PrintContext ctx)
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
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitPrint(KnightCodeParser.PrintContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterRead(KnightCodeParser.ReadContext ctx)
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
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitRead(KnightCodeParser.ReadContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterDecision(KnightCodeParser.DecisionContext ctx)
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
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitDecision(KnightCodeParser.DecisionContext ctx)
	{

	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLoop(KnightCodeParser.LoopContext ctx)
	{
		System.out.println("EnterLoop");
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLoop(KnightCodeParser.LoopContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitEveryRule(ParserRuleContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitTerminal(TerminalNode node) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void visitErrorNode(ErrorNode node) { }
}