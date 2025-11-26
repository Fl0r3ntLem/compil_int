package pcf

import ast.*
import ast.Term.*

import scala.jdk.CollectionConverters.*

class ASTVisitor[AST] extends PcfBaseVisitor[Term] :

  override def visitParExp(ctx: PcfParser.ParExpContext): Term = {
    val concreteExp = ctx.term() // get the child term if parentheses (it retrieves the terms within the current term)
    visit(concreteExp)
  }

  override def visitNumber(ctx: PcfParser.NumberContext): Term =
    Number(ctx.getText.toInt)

  override def visitVar(ctx: PcfParser.VarContext): Term =
    Var(ctx.getText)

  override def visitBinaryExp1(ctx: PcfParser.BinaryExp1Context): Term =
    val s = ctx.OP1().getText
    val op = Op.parse(s)
    val concreteTerms = ctx.term().asScala.toList
    // ctx.term is a Java list, it is translated in a Scala list
    val List(term1, term2) =
      for (concreteTerm <- concreteTerms) yield
        visit(concreteTerm)
    BinaryExp(op, term1, term2)

  override def visitBinaryExp2(ctx: PcfParser.BinaryExp2Context): Term =
    val s = ctx.OP2().getText
    val op = Op.parse(s)
    val concreteTerms = ctx.term().asScala.toList
    val List(term1, term2) =
      for (concreteTerm <- concreteTerms) yield
        visit(concreteTerm)
    BinaryExp(op, term1, term2)

  override def visitIfZero(ctx: PcfParser.IfZeroContext): Term =
    val concreteTerms = ctx.term().asScala.toList
    val List(cond, zExp, nzExp) =
      for (concreteTerm <- concreteTerms) yield
        visit(concreteTerm)
    IfZero(cond, zExp, nzExp)

  override def visitLet(ctx: PcfParser.LetContext): Term =
    val name = ctx.ID().getText
    val concreteTerms = ctx.term().asScala.toList
    val List(exp, body) =
      for (concreteTerm <- concreteTerms) yield
        visit(concreteTerm)
    Let(name, exp, body)

  override def visitFunction(ctx: PcfParser.FunctionContext): Term =
    val param = ctx.ID().getText
    val concreteTerm = ctx.term()
    val body = visit(concreteTerm)
    Function(param, body)

  override def visitApp(ctx: PcfParser.AppContext): Term =
    val concreteTerms = ctx.term().asScala.toList
    val List(fun, arg) =
      for (concreteTerm <- concreteTerms) yield
        visit(concreteTerm)
    App(fun, arg)

  override def visitFix(ctx: PcfParser.FixContext): Term =
    val name = ctx.ID().getText
    val concreteTerm = ctx.term()
    val term = visit(concreteTerm)
    Fix(name, term)
