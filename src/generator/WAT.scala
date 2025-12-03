package generator

import Ins.*

type CodeWAT = List[WAT]

enum WAT:
  case Ins(ins: String)
  case Test(code1: CodeWAT, code2: CodeWAT)
  case Search(code: WAT*)
  case Pushenv
  case Popenv
  case Extend(code : WAT*)
  case Mkclos(code: CodeWAT)

def format(depth: Int, code: CodeWAT): String =
  code.map(ins => formatIns(depth, ins)).mkString("\n")

def formatIns(depth: Int, ins: WAT): String = ins match
  // Other WAT instructions won't appear here because emitIns only returns WAT.Ins and WAT.Test

  case WAT.Ins(s) => spaces(depth) + s
  case WAT.Test(code1, code2) =>
    val thenPart = format(depth + 2, code1)
    val elsePart = format(depth + 2, code2)
    // as WebAssembly's 'if' makes sure the condition
    // is true when the top of the stack is non-zero
    // we swap then and else parts accordingly to get a ifZero behavior
    s"""${spaces(depth)}(if (result i32)
       |${spaces(depth + 1)}(then
       |${elsePart}
       |${spaces(depth + 1)})
       |${spaces(depth + 1)}(else
       |${thenPart}
       |${spaces(depth + 1)})
       |${spaces(depth)})"""
  case WAT.Extend(code*) =>
    val body = format(depth + 1, code.toList)
    s"""${spaces(depth)};;extend
       |${body}
       |${spaces(depth)};;end extend"""
  case WAT.Search(code*) =>
    val body = format(depth + 1, code.toList)
    s"${spaces(depth)}" + body


private def spaces(depth: Int): String = (for i <- 0 until depth yield "  ").mkString

def emit(code: Code, idx: Int): CodeWAT = {
  var code_wat = List[WAT]()
  for (ins <- code) do
    code_wat = (code_wat ::: emitIns(ins, idx))
  code_wat
}

def emitIns(ins: Ins, idx: Int): CodeWAT = ins match
  case Ldi(n) => List(WAT.Ins(s"i32.const $n"))
  case Add    => List(WAT.Ins("i32.add"))
  case Sub    => List(WAT.Ins("i32.sub"))
  case Mul    => List(WAT.Ins("i32.mul"))
  case Div    => List(WAT.Ins("i32.div_s"))
  case Test(i,j) => List(WAT.Test(emit(i, idx), emit(j, idx)))
  case Search(n) => List(
    WAT.Search(
      WAT.Ins("(call $search"),
      WAT.Ins(s"(i32.const $n)"),
      WAT.Ins("(global.get $ENV)"),
      WAT.Ins(")")
    )
  )
  case Extend => List(WAT.Extend(
      WAT.Ins("global.get $ENV"),
      WAT.Ins("call $cons"),
      WAT.Ins("global.set $ENV")
  ))
  case Pushenv => List(WAT.Ins("global.get $ENV"))
  case Popenv => List(
    WAT.Ins("global.set $ACC"),
    WAT.Ins("global.set $ENV"),
    WAT.Ins("global.get $ACC")
  )
  case Mkclos(code) =>
    val idx_incr = idx + 1
    val body = emit(code, idx_incr)
    List(
//      WAT.Ins(s"(func $$function$idx (result i32)"),
//    ) ::: body ::: List(
//      WAT.Ins(")"),
      WAT.Ins(s"(call $$pair (i32.const $idx) (global.get $$ENV))"),
    )

  case Apply =>
    List(
      WAT.Ins(s"(call $$apply)")
    )
  case _ => throw Exception(s"Unsupported instruction $ins in WAT generator")