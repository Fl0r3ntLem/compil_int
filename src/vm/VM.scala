package vm

import generator.Ins
import Ins.*
import Value.*

import scala.annotation.tailrec

enum Value:
  case IntVal(n: Int)
  case Closure(code: List[Ins], env: Env)

type Env = List[Value]
case class VMState(s:List[Value|Env], e: Env, c: List[Ins])

object VM:
  def execute(c: List[Ins]): Value =
    execute(List(), List(), c)

  @tailrec
  def execute(s:List[Value|Env], e: Env, c: List[Ins]): Value = (s, e, c) match
    case (head::s, _, List()) => head.asInstanceOf[Value]
    case (_, _, Ldi(n)::c) => execute(IntVal(n)::s, e, c)
    case (IntVal(n)::IntVal(m)::s, _, Add::c) => execute(IntVal(m+n)::s, e, c)
    case (IntVal(n)::IntVal(m)::s, _, Sub::c) => execute(IntVal(m-n)::s, e, c)
    case (IntVal(n)::IntVal(m)::s, _, Mul::c) => execute(IntVal(m*n)::s, e, c)
    case (IntVal(n)::IntVal(m)::s, _, Div::c) => execute(IntVal(m/n)::s, e, c)
    case (IntVal(0)::s, _, Test(i, _)::c) => execute(s, e, i:::c)
    case (head::s, _, Test(_, j)::c) => execute(s, e, j:::c)
    case (_,_,Search(p)::c) => execute(e(p)::s, e, c)
    case (_,_,Pushenv::c) => execute(e::s, e, c)
    case (v::env::s, e, Popenv::c) => execute(v :: s, env.asInstanceOf[Env], c)
    case (env::s, e, Popenv::c) => execute(s, env.asInstanceOf[Env], c)
    case (head::s, e, Extend::c) => execute(s, head.asInstanceOf[Value]::e, c)
    case (s, e, Mkclos(i)::c) => execute(Closure(i,e)::s, e, c)
    case (Closure(code, env)::arg::s, e, Apply::c) =>
      execute(
        s,
        arg.asInstanceOf[Value]::Closure(code,env)::env,
        code:::c)
    case state => throw Exception(s"unexpected VM state $state")

@main
def test(): Unit =
  println(VM.execute(List(Ldi(1), Ldi(2), Add, Test(List(Ldi(1)),List(Ldi(2))))))



