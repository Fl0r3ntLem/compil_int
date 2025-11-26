// Generated from /Users/tgranier/Documents/IMT/LOGIN/COMPIL/workspace/TP-PCFLive/src/pcf/Pcf.g4 by ANTLR 4.13.2
package pcf;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PcfParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PcfVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code App}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApp(PcfParser.AppContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Function}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunction(PcfParser.FunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParExp}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExp(PcfParser.ParExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(PcfParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Fix}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFix(PcfParser.FixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExp1}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExp1(PcfParser.BinaryExp1Context ctx);
	/**
	 * Visit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(PcfParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BinaryExp2}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinaryExp2(PcfParser.BinaryExp2Context ctx);
	/**
	 * Visit a parse tree produced by the {@code Let}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet(PcfParser.LetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfZero}
	 * labeled alternative in {@link PcfParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfZero(PcfParser.IfZeroContext ctx);
}