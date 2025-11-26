package typer
import ast.Term
import ast.Term.*
import unify.TVar


object Typer :
  type Env = Map[String, Type]

  def eval(t: Term, e: Env): Type = t match

    case Number(_) => INT

    case BinaryExp(_, exp1, exp2) =>
      val t1 = eval(exp1, e)
      val t2 = eval(exp2, e)
      // could be "if (t1===t2) then t1" in a more general way but here it is designed to only accept integers
      if (t1 === INT) && (t2 === INT) then INT
      else throw TypeException(s"Mismatch type: both $exp1 ($t1) and $exp2 ($t2) must be integers")

    case IfZero(cond, zExp, nzExp) =>
      val condType = eval(cond, e)
      val zType = eval(zExp, e)
      val nzType = eval(nzExp, e)
      if (condType === INT) then
        if (zType === nzType) then zType // or nzType
        else throw TypeException(s"Mismatch type between $zExp ($zType) and $nzExp ($nzType)")
      else throw TypeException(s"The condition expression: $cond should be of type INT in an 'ifz' statement")

    case Var(name) =>
      e.getOrElse(name, throw TypeException(s"Type of variable $name not found"))

    case Let(name, exp, body) =>
      val t1 = eval(exp, e)
      val newEnv = e + (name -> t1) // when defining let x = t:A in ..., x is therefore of type A
      eval(body, newEnv)

    case Function(param, body) =>
      val paramType = TVar(param.toUpperCase())
      // function param type is not fixed yet. Also, we name it to ease reading & understanding
      val newEnv = e + (param -> paramType)
      FUNCTION(paramType, eval(body,newEnv))

    case App(fun, arg) =>
      val funType = eval(fun, e)
      val argType = eval(arg, e)
      val returnType = TVar()
      if (funType === FUNCTION(argType, returnType)) then returnType
      // returnType will be unified with the actual return type of the function
      else throw TypeException(s"Mismatch between function ($fun) type $funType and the argument ($arg): $argType")

    case Fix(name, exp) =>
      val newEnv = e + (name -> TVar())
      eval(exp, newEnv)