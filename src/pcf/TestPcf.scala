package pcf

import java.io.{FileInputStream, FileWriter, InputStream}
import evaluator.Evaluator
import generator.{Code, Generator, Ins}
import ast.Term
import typer.{Type, Typer}


@main
def main(args: String*): Unit =
  val in: InputStream =
    if args.isEmpty || args.contains("-i") then
      System.in
    else
      FileInputStream(args(0))

  val verbose = args.isEmpty || (args.length > 1 && args.contains("-v"))
  val check_vm = args.length > 1 && args.contains("-vm")
  if (args.contains("-i")) println(s"==> ${interpret(in)}")
  else compile(verbose, check_vm, in, Option(args.head))

def interpret(in: InputStream): String =
    val (abstractTree, a) = analyze(in,true)
    val value = Evaluator.eval(abstractTree, Map())
    println("AST: " + abstractTree)
    print("==> ")
    println(s"$value: $a")
    s""

def analyze(in: InputStream,verbose: Boolean): (Term, Type) =
  val term = AbstractParser.analyze(in)
  val typ = Typer.eval(term, Map())
  val result = (term, typ)
  if (verbose)
    println(result)
  result

def compile(verbose: Boolean, check_vm: Boolean, is: InputStream, filename: Option[String]): Unit =
  // write code to .wat file associated to .pcf file passed as argument,
  // returning .wat file relative filename
  def write(code: String): String = {
    val wat_filename = filename.get.replaceAll("pcf", "wat")
    println("writing .wat code to " + wat_filename)
    val out = new FileWriter(wat_filename)
    out.write(code)
    out.flush()
    out.close()
    wat_filename
  }
  
  val (term, _) = analyze(is,verbose)
  val aterm = term.annotate(List())
  if check_vm then
    val code = Generator.genAM(aterm, 0).head
    if verbose then println(s"Code: $code")
      if !check(term, code) then throw Exception("Implementation Error")
  else
    filename match
        case Some(name) =>
          val fun_name = name.split("/").last.split("\\.").head
          val code = Generator.gen(aterm, Option(fun_name))
          write(code)
        case None =>
          val code = Generator.gen(aterm, None)
          println(code)

def check(term: Term, code: List[Ins]): Boolean =
  val value = Evaluator.eval(term, Map())
//  println(code) // in case the execution fails
  println(s"evaluator: $value")
  val value2 = vm.VM.execute(code)
  println(s"vm: $value2")
  value2.toString == value.toString // valid only for PCF green and blue