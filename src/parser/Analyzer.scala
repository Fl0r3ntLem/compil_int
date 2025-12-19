package parser

import ast.Term
import pcf.AbstractParser
import typer.{Type, Typer}

import java.io.InputStream

object Analyzer {

  def analyze(in: InputStream, verbose: Boolean): (Term, Type) =
    val term = AbstractParser.analyze(in)
    val typ = Typer.eval(term, Map())
    val result = (term, typ)
    if (verbose) {
      println(s"Analyzer (AST): $term")
      println(s"Typer: $typ")
    }
    result
}
