// Generated from /home/kertefan/Documents/KnightCode_FK/KnightCode.g4 by ANTLR 4.13.1
package lexparse; 
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class KnightCodeLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, ID=19, ESC=20, STRING=21, ASSIGN=22, LETTER=23, NUMBER=24, MUL=25, 
		DIV=26, ADD=27, SUB=28, GT=29, LT=30, EQ=31, NEQ=32, LINE_COMMENT=33, 
		WS=34;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "ID", "ESC", "STRING", "ASSIGN", "LETTER", "NUMBER", "MUL", 
			"DIV", "ADD", "SUB", "GT", "LT", "EQ", "NEQ", "LINE_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'PROGRAM'", "'DECLARE'", "'INTEGER'", "'STRING'", "'BEGIN'", "'END'", 
			"'SET'", "'('", "')'", "'PRINT'", "'READ'", "'IF'", "'THEN'", "'ELSE'", 
			"'ENDIF'", "'WHILE'", "'DO'", "'ENDWHILE'", null, null, null, "':='", 
			null, null, "'*'", "'/'", "'+'", "'-'", "'>'", "'<'", "'='", "'<>'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "ID", "ESC", "STRING", "ASSIGN", 
			"LETTER", "NUMBER", "MUL", "DIV", "ADD", "SUB", "GT", "LT", "EQ", "NEQ", 
			"LINE_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public KnightCodeLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "KnightCode.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\"\u00ee\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002"+
		"\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002"+
		"\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002"+
		"\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002"+
		"\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007"+
		"!\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f"+
		"\u0001\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001"+
		"\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u00aa\b\u0012\n\u0012\f\u0012"+
		"\u00ad\t\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0003\u0013"+
		"\u00b3\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014\u00b8\b"+
		"\u0014\n\u0014\f\u0014\u00bb\t\u0014\u0001\u0014\u0001\u0014\u0001\u0015"+
		"\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0004\u0017"+
		"\u00c5\b\u0017\u000b\u0017\f\u0017\u00c6\u0001\u0018\u0001\u0018\u0001"+
		"\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0001\u001c\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0005 \u00dc\b \n \f \u00df"+
		"\t \u0001 \u0003 \u00e2\b \u0001 \u0001 \u0001 \u0001 \u0001!\u0004!\u00e9"+
		"\b!\u000b!\f!\u00ea\u0001!\u0001!\u0002\u00b9\u00dd\u0000\"\u0001\u0001"+
		"\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f"+
		"\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f"+
		"\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017/\u0018"+
		"1\u00193\u001a5\u001b7\u001c9\u001d;\u001e=\u001f? A!C\"\u0001\u0000\u0003"+
		"\u0001\u000009\u0002\u0000AZaz\u0003\u0000\t\n\r\r  \u00f6\u0000\u0001"+
		"\u0001\u0000\u0000\u0000\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005"+
		"\u0001\u0000\u0000\u0000\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001"+
		"\u0000\u0000\u0000\u0000\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000"+
		"\u0000\u0000\u0000\u000f\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000"+
		"\u0000\u0000\u0000\u0013\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000"+
		"\u0000\u0000\u0000\u0017\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000"+
		"\u0000\u0000\u0000\u001b\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000"+
		"\u0000\u0000\u0000\u001f\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000"+
		"\u0000\u0000#\u0001\u0000\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000"+
		"\'\u0001\u0000\u0000\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001"+
		"\u0000\u0000\u0000\u0000-\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000"+
		"\u0000\u00001\u0001\u0000\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u0000"+
		"5\u0001\u0000\u0000\u0000\u00007\u0001\u0000\u0000\u0000\u00009\u0001"+
		"\u0000\u0000\u0000\u0000;\u0001\u0000\u0000\u0000\u0000=\u0001\u0000\u0000"+
		"\u0000\u0000?\u0001\u0000\u0000\u0000\u0000A\u0001\u0000\u0000\u0000\u0000"+
		"C\u0001\u0000\u0000\u0000\u0001E\u0001\u0000\u0000\u0000\u0003M\u0001"+
		"\u0000\u0000\u0000\u0005U\u0001\u0000\u0000\u0000\u0007]\u0001\u0000\u0000"+
		"\u0000\td\u0001\u0000\u0000\u0000\u000bj\u0001\u0000\u0000\u0000\rn\u0001"+
		"\u0000\u0000\u0000\u000fr\u0001\u0000\u0000\u0000\u0011t\u0001\u0000\u0000"+
		"\u0000\u0013v\u0001\u0000\u0000\u0000\u0015|\u0001\u0000\u0000\u0000\u0017"+
		"\u0081\u0001\u0000\u0000\u0000\u0019\u0084\u0001\u0000\u0000\u0000\u001b"+
		"\u0089\u0001\u0000\u0000\u0000\u001d\u008e\u0001\u0000\u0000\u0000\u001f"+
		"\u0094\u0001\u0000\u0000\u0000!\u009a\u0001\u0000\u0000\u0000#\u009d\u0001"+
		"\u0000\u0000\u0000%\u00a6\u0001\u0000\u0000\u0000\'\u00b2\u0001\u0000"+
		"\u0000\u0000)\u00b4\u0001\u0000\u0000\u0000+\u00be\u0001\u0000\u0000\u0000"+
		"-\u00c1\u0001\u0000\u0000\u0000/\u00c4\u0001\u0000\u0000\u00001\u00c8"+
		"\u0001\u0000\u0000\u00003\u00ca\u0001\u0000\u0000\u00005\u00cc\u0001\u0000"+
		"\u0000\u00007\u00ce\u0001\u0000\u0000\u00009\u00d0\u0001\u0000\u0000\u0000"+
		";\u00d2\u0001\u0000\u0000\u0000=\u00d4\u0001\u0000\u0000\u0000?\u00d6"+
		"\u0001\u0000\u0000\u0000A\u00d9\u0001\u0000\u0000\u0000C\u00e8\u0001\u0000"+
		"\u0000\u0000EF\u0005P\u0000\u0000FG\u0005R\u0000\u0000GH\u0005O\u0000"+
		"\u0000HI\u0005G\u0000\u0000IJ\u0005R\u0000\u0000JK\u0005A\u0000\u0000"+
		"KL\u0005M\u0000\u0000L\u0002\u0001\u0000\u0000\u0000MN\u0005D\u0000\u0000"+
		"NO\u0005E\u0000\u0000OP\u0005C\u0000\u0000PQ\u0005L\u0000\u0000QR\u0005"+
		"A\u0000\u0000RS\u0005R\u0000\u0000ST\u0005E\u0000\u0000T\u0004\u0001\u0000"+
		"\u0000\u0000UV\u0005I\u0000\u0000VW\u0005N\u0000\u0000WX\u0005T\u0000"+
		"\u0000XY\u0005E\u0000\u0000YZ\u0005G\u0000\u0000Z[\u0005E\u0000\u0000"+
		"[\\\u0005R\u0000\u0000\\\u0006\u0001\u0000\u0000\u0000]^\u0005S\u0000"+
		"\u0000^_\u0005T\u0000\u0000_`\u0005R\u0000\u0000`a\u0005I\u0000\u0000"+
		"ab\u0005N\u0000\u0000bc\u0005G\u0000\u0000c\b\u0001\u0000\u0000\u0000"+
		"de\u0005B\u0000\u0000ef\u0005E\u0000\u0000fg\u0005G\u0000\u0000gh\u0005"+
		"I\u0000\u0000hi\u0005N\u0000\u0000i\n\u0001\u0000\u0000\u0000jk\u0005"+
		"E\u0000\u0000kl\u0005N\u0000\u0000lm\u0005D\u0000\u0000m\f\u0001\u0000"+
		"\u0000\u0000no\u0005S\u0000\u0000op\u0005E\u0000\u0000pq\u0005T\u0000"+
		"\u0000q\u000e\u0001\u0000\u0000\u0000rs\u0005(\u0000\u0000s\u0010\u0001"+
		"\u0000\u0000\u0000tu\u0005)\u0000\u0000u\u0012\u0001\u0000\u0000\u0000"+
		"vw\u0005P\u0000\u0000wx\u0005R\u0000\u0000xy\u0005I\u0000\u0000yz\u0005"+
		"N\u0000\u0000z{\u0005T\u0000\u0000{\u0014\u0001\u0000\u0000\u0000|}\u0005"+
		"R\u0000\u0000}~\u0005E\u0000\u0000~\u007f\u0005A\u0000\u0000\u007f\u0080"+
		"\u0005D\u0000\u0000\u0080\u0016\u0001\u0000\u0000\u0000\u0081\u0082\u0005"+
		"I\u0000\u0000\u0082\u0083\u0005F\u0000\u0000\u0083\u0018\u0001\u0000\u0000"+
		"\u0000\u0084\u0085\u0005T\u0000\u0000\u0085\u0086\u0005H\u0000\u0000\u0086"+
		"\u0087\u0005E\u0000\u0000\u0087\u0088\u0005N\u0000\u0000\u0088\u001a\u0001"+
		"\u0000\u0000\u0000\u0089\u008a\u0005E\u0000\u0000\u008a\u008b\u0005L\u0000"+
		"\u0000\u008b\u008c\u0005S\u0000\u0000\u008c\u008d\u0005E\u0000\u0000\u008d"+
		"\u001c\u0001\u0000\u0000\u0000\u008e\u008f\u0005E\u0000\u0000\u008f\u0090"+
		"\u0005N\u0000\u0000\u0090\u0091\u0005D\u0000\u0000\u0091\u0092\u0005I"+
		"\u0000\u0000\u0092\u0093\u0005F\u0000\u0000\u0093\u001e\u0001\u0000\u0000"+
		"\u0000\u0094\u0095\u0005W\u0000\u0000\u0095\u0096\u0005H\u0000\u0000\u0096"+
		"\u0097\u0005I\u0000\u0000\u0097\u0098\u0005L\u0000\u0000\u0098\u0099\u0005"+
		"E\u0000\u0000\u0099 \u0001\u0000\u0000\u0000\u009a\u009b\u0005D\u0000"+
		"\u0000\u009b\u009c\u0005O\u0000\u0000\u009c\"\u0001\u0000\u0000\u0000"+
		"\u009d\u009e\u0005E\u0000\u0000\u009e\u009f\u0005N\u0000\u0000\u009f\u00a0"+
		"\u0005D\u0000\u0000\u00a0\u00a1\u0005W\u0000\u0000\u00a1\u00a2\u0005H"+
		"\u0000\u0000\u00a2\u00a3\u0005I\u0000\u0000\u00a3\u00a4\u0005L\u0000\u0000"+
		"\u00a4\u00a5\u0005E\u0000\u0000\u00a5$\u0001\u0000\u0000\u0000\u00a6\u00ab"+
		"\u0003-\u0016\u0000\u00a7\u00aa\u0003-\u0016\u0000\u00a8\u00aa\u0007\u0000"+
		"\u0000\u0000\u00a9\u00a7\u0001\u0000\u0000\u0000\u00a9\u00a8\u0001\u0000"+
		"\u0000\u0000\u00aa\u00ad\u0001\u0000\u0000\u0000\u00ab\u00a9\u0001\u0000"+
		"\u0000\u0000\u00ab\u00ac\u0001\u0000\u0000\u0000\u00ac&\u0001\u0000\u0000"+
		"\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000\u00ae\u00af\u0005\\\u0000\u0000"+
		"\u00af\u00b3\u0005\"\u0000\u0000\u00b0\u00b1\u0005\\\u0000\u0000\u00b1"+
		"\u00b3\u0005\\\u0000\u0000\u00b2\u00ae\u0001\u0000\u0000\u0000\u00b2\u00b0"+
		"\u0001\u0000\u0000\u0000\u00b3(\u0001\u0000\u0000\u0000\u00b4\u00b9\u0005"+
		"\"\u0000\u0000\u00b5\u00b8\u0003\'\u0013\u0000\u00b6\u00b8\t\u0000\u0000"+
		"\u0000\u00b7\u00b5\u0001\u0000\u0000\u0000\u00b7\u00b6\u0001\u0000\u0000"+
		"\u0000\u00b8\u00bb\u0001\u0000\u0000\u0000\u00b9\u00ba\u0001\u0000\u0000"+
		"\u0000\u00b9\u00b7\u0001\u0000\u0000\u0000\u00ba\u00bc\u0001\u0000\u0000"+
		"\u0000\u00bb\u00b9\u0001\u0000\u0000\u0000\u00bc\u00bd\u0005\"\u0000\u0000"+
		"\u00bd*\u0001\u0000\u0000\u0000\u00be\u00bf\u0005:\u0000\u0000\u00bf\u00c0"+
		"\u0005=\u0000\u0000\u00c0,\u0001\u0000\u0000\u0000\u00c1\u00c2\u0007\u0001"+
		"\u0000\u0000\u00c2.\u0001\u0000\u0000\u0000\u00c3\u00c5\u0007\u0000\u0000"+
		"\u0000\u00c4\u00c3\u0001\u0000\u0000\u0000\u00c5\u00c6\u0001\u0000\u0000"+
		"\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000"+
		"\u0000\u00c70\u0001\u0000\u0000\u0000\u00c8\u00c9\u0005*\u0000\u0000\u00c9"+
		"2\u0001\u0000\u0000\u0000\u00ca\u00cb\u0005/\u0000\u0000\u00cb4\u0001"+
		"\u0000\u0000\u0000\u00cc\u00cd\u0005+\u0000\u0000\u00cd6\u0001\u0000\u0000"+
		"\u0000\u00ce\u00cf\u0005-\u0000\u0000\u00cf8\u0001\u0000\u0000\u0000\u00d0"+
		"\u00d1\u0005>\u0000\u0000\u00d1:\u0001\u0000\u0000\u0000\u00d2\u00d3\u0005"+
		"<\u0000\u0000\u00d3<\u0001\u0000\u0000\u0000\u00d4\u00d5\u0005=\u0000"+
		"\u0000\u00d5>\u0001\u0000\u0000\u0000\u00d6\u00d7\u0005<\u0000\u0000\u00d7"+
		"\u00d8\u0005>\u0000\u0000\u00d8@\u0001\u0000\u0000\u0000\u00d9\u00dd\u0005"+
		"#\u0000\u0000\u00da\u00dc\t\u0000\u0000\u0000\u00db\u00da\u0001\u0000"+
		"\u0000\u0000\u00dc\u00df\u0001\u0000\u0000\u0000\u00dd\u00de\u0001\u0000"+
		"\u0000\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00de\u00e1\u0001\u0000"+
		"\u0000\u0000\u00df\u00dd\u0001\u0000\u0000\u0000\u00e0\u00e2\u0005\r\u0000"+
		"\u0000\u00e1\u00e0\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000"+
		"\u0000\u00e2\u00e3\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005\n\u0000\u0000"+
		"\u00e4\u00e5\u0001\u0000\u0000\u0000\u00e5\u00e6\u0006 \u0000\u0000\u00e6"+
		"B\u0001\u0000\u0000\u0000\u00e7\u00e9\u0007\u0002\u0000\u0000\u00e8\u00e7"+
		"\u0001\u0000\u0000\u0000\u00e9\u00ea\u0001\u0000\u0000\u0000\u00ea\u00e8"+
		"\u0001\u0000\u0000\u0000\u00ea\u00eb\u0001\u0000\u0000\u0000\u00eb\u00ec"+
		"\u0001\u0000\u0000\u0000\u00ec\u00ed\u0006!\u0000\u0000\u00edD\u0001\u0000"+
		"\u0000\u0000\n\u0000\u00a9\u00ab\u00b2\u00b7\u00b9\u00c6\u00dd\u00e1\u00ea"+
		"\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}