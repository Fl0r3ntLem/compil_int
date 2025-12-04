package generator

import ast.ATerm
import ast.ATerm.*
import ast.Op
import ast.Op.*
import generator.Ins.{Add, Apply, Div, Extend, Ldi, Mkclos, Mul, Popenv, Pushenv, Search, Sub, Test}

import scala.io.Source

type Code = List[generator.Ins]

object Generator :
  def gen(term: ATerm, name: Option[String]): String = {
    val (code, count) = genAM(term, 0)
    val bodies = collectBodies(code, List())
    genWAT(code, bodies, name)
  }

  def genAM(term: ATerm, idx: Int): (Code, Int) =
    term match {
      case Number(n) => (List(Ldi(n)), idx)

      case Var(name, index) => (List(Search(index)), idx)

      case BinaryExp(op, u, v) =>
        val (c_u, i1) = genAM(u, idx)
        val (c_v, i2) = genAM(v, i1)
        (c_u ::: c_v ::: List(genAM_op(op)), i2) // gen u, push, gen v, op

      case Let(name, exp, body) =>
        val (c_exp, i1) = genAM(exp, idx)
        val (c_body, i2) = genAM(body, i1)
        (Pushenv :: c_exp ::: (Extend :: c_body) ::: List(Popenv), i2)

        // Let x = 1 in let y = x+1 in x+y
        // List(Pushenv, Ldi(1), Extend, Pushenv, Search(0), Push, Ldi(1), Add, Extend, Search(1), Push, Search(0), Add, Popenv, Popenv)

      case IfZero(cond, zExp, nzExp) =>
        val (c_cond, i1) = genAM(cond, idx)
        val (c_zExp, i2) = genAM(zExp, i1)
        val (c_nzExp, i3) = genAM(nzExp, i2)
        (c_cond ::: List(Test(c_zExp, c_nzExp)), i3)

      case Function(param, body) =>
        val (c_body, i1) = genAM(body, idx)
        (List(Mkclos(i1, c_body)), i1+1)

      case App(fun, arg) =>
        val (c_fun, i1) = genAM(fun, idx)
        val (c_arg, i2) = genAM(arg, i1)
        (Pushenv :: c_arg ::: c_fun ::: List(Apply, Popenv), i2)

      case FixFun(name, param, exp) =>
        val (c_exp, i1) = genAM(exp, idx)
        (List(Mkclos(i1, c_exp)), i1+1)
    }

  def genAM_op(op: Op): generator.Ins =
    op match {
      case Plus => Add
      case Minus => Sub
      case Times => Mul
      case Divide => Div
    }


  def genWAT(code: Code, bodies: List[Code], name: Option[String]): String = {
    val postlude = "\n)\n"
    genWAT_prelude() +
    emitTable(bodies.size) +
    genWAT_main(code, name) +
    postlude
  }

  def genWAT_main(code: Code, name: Option[String]) =
    val fun_name = name match {
      case Some(n) => n
      case None => "main"
    }
    s"""
       |(func (export \"$fun_name\") (result i32)
       |${format(1, emit(code))}
       |  return)
       |""".stripMargin

  def genWAT_prelude(): String =
    val source = Source.fromFile("src/wat/prelude.wat")
    val contents = source.mkString
    source.close()
    contents

  def functionName(i: Int): String = "$closure" + i

  private def emitTable(size: Int): String = {
    val closures = (0 until size).map(i => s"     ${functionName(i)}").mkString("\n")
    s""" (table funcref
    |   (elem
    |${closures}
    |   )
    |)""".stripMargin
  }

  def collectBodies(code: Code, bodiesSoFar: List[Code]): List[Code] =
    code match {
      case Nil => bodiesSoFar
      case Mkclos(idx, body) :: rest =>
        collectBodies(rest, bodiesSoFar :+ body)
      case _ :: rest =>
        collectBodies(rest, bodiesSoFar)
    }
