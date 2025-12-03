package generator

import ast.ATerm
import ast.ATerm.*
import ast.Op
import ast.Op.*
import generator.Ins.{Add, Apply, Div, Extend, Ldi, Mkclos, Mul, Popenv, Pushenv, Search, Sub, Test}

import scala.io.Source

type Code = List[generator.Ins]

object Generator :
  def gen(term: ATerm, name: Option[String]): String =
    genWAT(genAM(term),name)

  def genAM(term: ATerm): Code =
    term match {
      case Number(n) => List(Ldi(n))

      case Var(name, index) => List(Search(index))

      case BinaryExp(op, u, v) =>
        val c_u = genAM(u)
        val c_v = genAM(v)
        c_u ::: c_v ::: List(genAM_op(op)) // gen u, push, gen v, op

      case Let(name, exp, body) =>
        val c_exp = genAM(exp)
        val c_body = genAM(body)
        Pushenv :: c_exp ::: (Extend :: c_body) ::: List(Popenv)

        // Let x = 1 in let y = x+1 in x+y
        // List(Pushenv, Ldi(1), Extend, Pushenv, Search(0), Push, Ldi(1), Add, Extend, Search(1), Push, Search(0), Add, Popenv, Popenv)

      case IfZero(cond, zExp, nzExp) =>
        val c_cond = genAM(cond)
        val c_zExp = genAM(zExp)
        val c_nzExp = genAM(nzExp)
        c_cond ::: List(Test(c_zExp, c_nzExp))

      case Function(param, body) =>
        val c_body = genAM(body)
        List(Mkclos(c_body))

      case App(fun, arg) =>
        val c_fun = genAM(fun)
        val c_arg = genAM(arg)
        Pushenv :: c_arg ::: c_fun ::: List(Apply, Popenv)

      case FixFun(name, param, exp) =>
        val c_exp = genAM(exp)
        List(Mkclos(c_exp))
    }

  def genAM_op(op: Op): generator.Ins =
    op match {
      case Plus => Add
      case Minus => Sub
      case Times => Mul
      case Divide => Div
    }


  def genWAT(code: Code, name: Option[String]): String = {
    val postlude = "\n)\n"
    genWAT_prelude() +
    emitTable +
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
       |${format(1, emit(code, 0))}
       |  return)
       |""".stripMargin

  def genWAT_prelude(): String =
    val source = Source.fromFile("src/wat/prelude.wat")
    val contents = source.mkString
    source.close()
    contents

  private def emitTable: String =
    s""" (table funcref
       | (elem
       | )
       | )""".stripMargin
