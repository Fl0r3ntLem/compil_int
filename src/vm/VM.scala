package vm

import generator.Ins
import Ins.*
import Value.*

import scala.annotation.tailrec

enum Value:
  case IntVal(n: Int)
  case Closure(code: List[Ins], env: Env)

type Env = List[Value]
case class VMState(a: Value, s:List[Value|Env], e: Env, c: List[Ins])

object VM:
  def execute(c: List[Ins]): Value =
    execute(IntVal(0), List(), List(), c)

  @tailrec
  def execute(a: Value, s:List[Value|Env], e: Env, c: List[Ins]): Value = (a, s, e, c) match
    case (_, _, _, List()) => a
    case (_, _, _, Push::c) => execute(a, a::s, e, c)
    case (_, _, _, Ldi(n)::c) => execute(IntVal(n), s, e, c)
    case (IntVal(n), IntVal(m)::s, _, Add::c) => execute(IntVal(m+n), s, e, c)
    case (IntVal(n), IntVal(m)::s, _, Sub::c) => execute(IntVal(m-n), s, e, c)
    case (IntVal(n), IntVal(m)::s, _, Mul::c) => execute(IntVal(m*n), s, e, c)
    case (IntVal(n), IntVal(m)::s, _, Div::c) => execute(IntVal(m/n), s, e, c)
    case (IntVal(0), _, _, Test(i, _)::c) => execute(a, s, e, i:::c)
    case (_, _, _, Test(_, j)::c) => execute(a, s, e, j:::c)
    case (_,_,_,Search(p)::c) => execute(e(p), s, e, c)
    case (_,_,_,Pushenv::c) => execute(a, e::s, e, c)
    case (a, env::s, e, Popenv::c) => execute(a, s, env.asInstanceOf[Env], c)
    case (a, s, e, Extend::c) => execute(a, s, a::e, c)
    case (a, s, e, Mkclos(i)::c) => execute(Closure(i,e), s, e, c)
    case (Closure(code, env), arg::s, e, Apply::c) =>
      execute(arg.asInstanceOf[Value], s, arg.asInstanceOf[Value]::Closure(code,env)::env, code:::c)
    case state => throw Exception(s"unexpected VM state $state")

@main
def test(): Unit =
  println(VM.execute(List(Ldi(1), Push, Ldi(2), Add, Test(List(Ldi(1)),List(Ldi(2))))))



