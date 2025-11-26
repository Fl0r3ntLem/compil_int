package evaluator

import evaluator.Evaluator.Env

enum Value {
  case IntVal(value: Int)
  case Closure(name: String, term: ast.Term, env: Env)
}


case class IceCube(name: String, term: ast.Term, env: Env)