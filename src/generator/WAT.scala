package generator

import Ins.*

type CodeWAT = List[WAT]

enum WAT:
  case Ins(ins: String)
  case Test(code1: CodeWAT, code2: CodeWAT)
  case Search(code: WAT*)
  case Pushenv
  case Popenv(code: WAT*)
  case Extend(code : WAT*)
  case Mkclos(fun_name: WAT, body: CodeWAT, fun_end: WAT, call: WAT)

def format(depth: Int, code: CodeWAT): String =
  code.map(ins => formatIns(depth, ins)).mkString("\n")

def formatIns(depth: Int, ins: WAT): String = ins match
  // Other WAT instructions won't appear here because emitIns only returns WAT.Ins and WAT.Test

  case WAT.Ins(s) => spaces(depth) + s
  case WAT.Test(code1, code2) =>
    val thenPart = format(depth + 1, code1)
    val elsePart = format(depth + 1, code2)
    // as WebAssembly's 'if' makes sure the condition
    // is true when the top of the stack is non-zero
    // we swap then and else parts accordingly to get a ifZero behavior
    s"""
       |${spaces(depth)}(if (result i32)
       |${spaces(depth + 1)}(then
       |${elsePart}
       |${spaces(depth + 1)})
       |${spaces(depth + 1)}(else
       |${thenPart}
       |${spaces(depth + 1)})
       |${spaces(depth)})""".stripMargin
  case WAT.Extend(code*) =>
    val body = format(depth + 1, code.toList)
    s"""
       |${spaces(depth)};;extend
       |${body}
       |${spaces(depth)};;end extend""".stripMargin
  case WAT.Search(code*) =>
    val depths = List(depth,depth+1,depth+1,depth)
    val instructions = code.toList
    instructions.zip(depths).map {
      case (ins, d) =>
      formatIns(d, ins)}.mkString("\n")
  case WAT.Popenv(code*) =>
    val body = format(depth + 1, code.toList)
    s"""
       |${spaces(depth)};;popenv
       |${body}
       |${spaces(depth)};;end popenv""".stripMargin
  case WAT.Mkclos(fun_name, body, fun_end, call) =>
    s"""
        |${spaces(depth)};;mkclos
        |${formatIns(depth, fun_name)}
        |${format(depth + 1, body)}
        |${formatIns(depth, fun_end)}
        |${formatIns(depth, call)}
        |${spaces(depth)};;end mkclos""".stripMargin


private def spaces(depth: Int): String = (for i <- 0 until depth yield "  ").mkString

def emit(code: Code): CodeWAT = {
  var code_wat = List[WAT]()
  for (ins <- code) do
    code_wat = (code_wat :+ emitIns(ins))
  code_wat
}

def emitIns(ins: Ins): WAT = ins match
  case Ldi(n) => WAT.Ins(s"i32.const $n")
  case Add    => WAT.Ins("i32.add")
  case Sub    => WAT.Ins("i32.sub")
  case Mul    => WAT.Ins("i32.mul")
  case Div    => WAT.Ins("i32.div_s")
  case Test(i,j) => WAT.Test(emit(i), emit(j))
  case Search(n) =>
    WAT.Search(
      WAT.Ins("(call $search"),
      WAT.Ins(s"(i32.const $n)"),
      WAT.Ins("(global.get $ENV)"),
      WAT.Ins(")")
    )
  case Extend => WAT.Extend(
      WAT.Ins("global.get $ENV"),
      WAT.Ins("call $cons"),
      WAT.Ins("global.set $ENV")
  )
  case Pushenv => WAT.Ins("global.get $ENV ;; pushenv")
  case Popenv => WAT.Popenv(
    WAT.Ins("global.set $ACC"),
    WAT.Ins("global.set $ENV"),
    WAT.Ins("global.get $ACC")
  )
  case Mkclos(idx, code) =>
      WAT.Ins(s"(call $$pair (i32.const $idx) (global.get $$ENV))")
  case Apply =>
      WAT.Ins(s"(call $$apply)")
  case _ => throw Exception(s"Unsupported instruction $ins in WAT generator")


def emitFunctions(bodies: List[Code]): String = {
  bodies.zipWithIndex.map((body, index) =>
    s"(func $$closure${index} (result i32)\n" +
    format(1,emit(body)) +
    "\n)"
  ).mkString("\n") + "\n"
}
