package evaluator

import ast.Term
import ast.Term.*
import ast.Op
import ast.Op.*
import Value.*

object Evaluator :
  type Env = Map[String, Value | IceCube]
  given int2Val: Conversion[Int, Value] = n => IntVal(n)
  given val2Int: Conversion[Value, Int] = {
    case IntVal(n) => n
    case Closure(_, _, _) => throw EvaluationException("Can't convert Closure to Int")
  }
  def eval(term: Term, e: Env): Value = term match

    case Number(value) => value

    case Var(name) =>
      val value = e.getOrElse(name, throw EvaluationException(s"Variable $name not found"))
      value match
        case IntVal(n) => IntVal(n)
        case Closure(x, t, funEnv) => Closure(x, t, funEnv)
        case IceCube(x, t, iceCubeEnv) =>
          val newEnv = iceCubeEnv + (x -> IceCube(x, t, iceCubeEnv))
          eval(t, newEnv)

    case IfZero(cond, zExp, nzExp) =>
      if eval(cond, e) == IntVal(0) then eval(zExp, e) else eval(nzExp, e)

    case BinaryExp(op, exp1, exp2) =>
      val v1 = eval(exp1, e)
      val v2 = eval(exp2, e)
      op match
        case Plus  => v1 + v2
        case Minus => v1 - v2
        case Times => v1 * v2
        case Divide => v1 / v2

    case Let(name, exp, body) =>
      val value = eval(exp, e)
      val newEnv = e + (name -> value)
      eval(body, newEnv)

    case Function(param, body) =>
      Closure(param, body, e)

    case App(fun, arg) =>
      val funValue = eval(fun, e)
      funValue match
        case Closure(param, exp, closureEnv) =>
          val argValue = eval(arg, e)
          val newEnv = closureEnv + (param -> argValue)
          eval(exp, newEnv)
        case _ => throw EvaluationException(s"Evaluation of $fun did not return a closure: $funValue")

    case Fix(name, exp) =>
      val newEnv = e + (name -> IceCube(name, exp, e))
      eval(exp, newEnv)

    

