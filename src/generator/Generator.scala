package generator

import ast.ATerm
import ast.ATerm.*
import ast.Op
import ast.Op.*
import Ins.*

type Code = List[Ins]

object Generator :
  def gen(term: ATerm): Code =
    term match {
      case Number(n) => List(Ldi(n))

      case Var(name, index) => List(Search(index))

      case BinaryExp(op, u, v) =>
        val c_u = gen(u)
        val c_v = gen(v)
        c_u ::: (Push :: c_v) ::: List(gen_op(op)) // gen u, push, gen v, op

      case Let(name, exp, body) =>
        val c_exp = gen(exp)
        val c_body = gen(body)
        Pushenv :: c_exp ::: (Extend :: c_body) ::: List(Popenv)

        // Let x = 1 in let y = x+1 in x+y
        // List(Pushenv, Ldi(1), Extend, Pushenv, Search(0), Push, Ldi(1), Add, Extend, Search(1), Push, Search(0), Add, Popenv, Popenv)

      case IfZero(cond, zExp, nzExp) =>
        val c_cond = gen(cond)
        val c_zExp = gen(zExp)
        val c_nzExp = gen(nzExp)
        c_cond ::: List(Test(c_zExp, c_nzExp))

      case Function(param, body) =>
        val c_body = gen(body)
        List(Mkclos(c_body))

      case App(fun, arg) =>
        val c_fun = gen(fun)
        val c_arg = gen(arg)
        Pushenv :: c_arg ::: (Push :: c_fun) ::: List(Apply, Popenv)

      case FixFun(name, param, exp) =>
        val c_exp = gen(exp)
        List(Mkclos(c_exp))
    }

  def gen_op(op: Op): Ins =
    op match {
      case Plus => Add
      case Minus => Sub
      case Times => Mul
      case Divide => Div
    }
