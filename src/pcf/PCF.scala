package pcf

import java.io.{FileInputStream, FileWriter, InputStream}
import evaluator.Evaluator
import generator.{Code, Generator, Ins}
import ast.Term
import typer.{Type, Typer}
import parser.Analyzer

object PCF:

  def main(args: Array[String]): Unit =
    val in: InputStream =
      if args.isEmpty || args.forall(!_.contains('.')) then
        // we read input only if there is no filename given in the args
        System.in
      else
        FileInputStream(args(0))

    val verbose = args.isEmpty || (args.length > 1 && args.contains("-v"))
    val check_vm = args.length > 1 && args.contains("-vm")
    if (args.contains("-i")) then interpret(verbose, in)
    else compile(verbose, check_vm, in, Option(args.head))

  def interpret(verbose: Boolean, in: InputStream): String =
      val (abstractTree, a) = Analyzer.analyze(in, verbose)
      val value = Evaluator.eval(abstractTree, Map())
      print("Result ==> ")
      println(s"$value: $a")
      s""

  def compile(verbose: Boolean, check_vm: Boolean, is: InputStream, filename: Option[String]): Unit =
    // write code to .wat file associated to .pcf file passed as argument,
    // returning .wat file relative filename
    def write(code: String): String = {
      val wat_filename = filename.get.replaceAll("pcf", "wat")
      println("Writing .wat code to " + wat_filename)
      val out = new FileWriter(wat_filename)
      out.write(code)
      out.flush()
      out.close()
      wat_filename
    }

    val (term, _) = Analyzer.analyze(is, verbose)
    val aterm = term.annotate(List())
    if check_vm then
      val code = Generator.genAM(aterm, 0).head
      if verbose then println(s"Code: $code")
      if !check(term, code, verbose) then throw Exception("Implementation Error: mismatch value between evaluator and VM")
      else println(s"==> Result: ${vm.VM.execute(code)}")
    else
      filename match
        case Some(name) =>
          val fun_name = name.split("/").last.split("\\.").head
          val code = Generator.gen(aterm, Option(fun_name), verbose)
          if verbose then println("-> Successfully generated WAT code")
          write(code)
        case None =>
          val code = Generator.gen(aterm, None, verbose)
          println(code)

  def check(term: Term, code: List[Ins], verbose: Boolean): Boolean =
    val value = Evaluator.eval(term, Map())
    if verbose then println(s"Evaluator result: $value")
    val value2 = vm.VM.execute(code)
    if verbose then println(s"VM result: $value2")
    value2.toString == value.toString // valid only for PCF green and blue
