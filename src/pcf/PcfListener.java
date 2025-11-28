// Generated from /home/florent/Documents/IMT/CMPINT/TP-PCFLive/TP-PCFLive/src/pcf/Pcf.g4 by ANTLR 4.13.2
package pcf;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PcfParser}.
 */
public interface PcfListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code App}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterApp(PcfParser.AppContext ctx);
	/**
	 * Exit a parse tree produced by the {@code App}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitApp(PcfParser.AppContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Function}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterFunction(PcfParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitFunction(PcfParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParExp}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterParExp(PcfParser.ParExpContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParExp}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitParExp(PcfParser.ParExpContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterNumber(PcfParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitNumber(PcfParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Fix}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterFix(PcfParser.FixContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Fix}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitFix(PcfParser.FixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExp1}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExp1(PcfParser.BinaryExp1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExp1}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExp1(PcfParser.BinaryExp1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code Var}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterVar(PcfParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitVar(PcfParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BinaryExp2}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExp2(PcfParser.BinaryExp2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code BinaryExp2}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExp2(PcfParser.BinaryExp2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code Let}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterLet(PcfParser.LetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Let}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitLet(PcfParser.LetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfZero}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void enterIfZero(PcfParser.IfZeroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfZero}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 */
	void exitIfZero(PcfParser.IfZeroContext ctx);
}