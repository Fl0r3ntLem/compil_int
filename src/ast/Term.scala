package ast

enum Term:
  case Number(value: Int)
  case Var(name: String)
  case Let(name: String, exp:Term, body:Term) // let name = exp in body
  case IfZero(cond: Term, zExp: Term, nzExp: Term) // ifz cond then zExp else nzExp
  case BinaryExp(op: Op, exp1: Term, exp2: Term) // exp1 op exp2
  case Function(param: String, body: Term)  // fun param -> body
  case App(fun: Term, arg: Term) // fun arg
  case Fix(name: String, exp: Term) // fix name exp

  def annotate(e: List[String]) : ATerm = this match {
    case Number(value) => ATerm.Number(value)
    case Var(name) => ATerm.Var(name, e.indexOf(name)) // calculate the De Bruijn index from e
    case Let(name, exp, body) => ATerm.Let(name, exp.annotate(e), body.annotate(name::e))
    case BinaryExp(op, exp1, exp2) => ATerm.BinaryExp(op, exp1.annotate(e), exp2.annotate(e))
    case IfZero(cond, zExp, nzExp) => ATerm.IfZero(cond.annotate(e), zExp.annotate(e), nzExp.annotate(e))
    case Function(param, body) => ATerm.Function(param, body.annotate(param::"_"::e))
    // We add "_" to respect the De Bruijn indexing, even though it won't be used in this case
    case App(fun, arg) => ATerm.App(fun.annotate(e), arg.annotate(e))
    case Fix(name, Function(param, body)) => ATerm.FixFun(name, param, body.annotate(param::name::e))
    // We assume that fix is always applied to a function
  }

enum ATerm:
  case Number(value: Int)
  case Var(name: String, index:Int) // De Bruijn index
  case Let(name: String, exp:ATerm, body:ATerm) // let name = exp in body
  case IfZero(cond: ATerm, zExp: ATerm, nzExp: ATerm) // ifz cond then zExp else nzExp
  case BinaryExp(op: Op, exp1: ATerm, exp2: ATerm) // exp1 op exp2
  case Function(param: String, body: ATerm)  // fun param -> body
  case App(fun: ATerm, arg: ATerm) // fun arg
  case FixFun(name: String, param: String, exp: ATerm) // fix name exp

enum Op:
  case Plus
  case Minus
  case Times
  case Divide

object Op:  
  def parse(s: String): Op =
    s match
      case "+" => Plus
      case "-" => Minus
      case "*" => Times
      case "/" => Divide
      case _ => throw Exception(s"Unknown operator: $s")
