package pcf

import java.io.{FileInputStream, InputStream}
import evaluator.Evaluator
import generator.{Code, Generator, Ins}
import ast.Term
import typer.{Type, Typer}


@main
def main(args: String*): Unit =
  val in: InputStream =
    if args.isEmpty || args(0).charAt(0) == '-' then
      System.in
    else
      FileInputStream(args(0))

  if (args.contains("-i")) println(s"==> ${interpret(in)}")
  else compile(in)

def interpret(in: InputStream): String =
    val (abstractTree, a) = analyze(in)
    val value = Evaluator.eval(abstractTree, Map())
    println("AST: " + abstractTree)
    print("==> ")
    println(s"$value: $a")
    s""

def analyze(in: InputStream): (Term, Type) =
  val term = AbstractParser.analyze(in)
  val typ = Typer.eval(term, Map())
  (term, typ)

def compile(in: InputStream): Code =
  val (term, a) = analyze(in)
  val aterm = term.annotate(List()) // calcul des indices de De Bruijn
  val code = Generator.gen(aterm)
  if check(term, code) then code
  else throw Exception("Implementation Error")

def check(term: Term, code: List[Ins]): Boolean =
  val value = Evaluator.eval(term, Map())
  println(code) // in case the execution fails
  println(s"evaluator: $value")
  val value2 = vm.VM.execute(code)
  println(s"vm: $value2")
  value2.toString == value.toString // valid only for PCF green and blue
