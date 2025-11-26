package test

import pcf.main

import evaluator.Evaluator.eval
import evaluator.Value
import evaluator.Value.{Closure, IntVal}
import pcf.analyze
import ast.Term

import java.io.InputStream
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

//object Test extends App {
//  // A simple mechanism to check a condition.
//  // Throws an exception if 'condition' is false.
//  //  private def assert(value : String, expected_result : Value, message: String = "Assertion failed"): Unit = {
//  //    val inputStream: InputStream = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8))
//  //    val (abstractTree, a) = analyze(inputStream)
//  //    val evaluation = eval(abstractTree, Map())
//  //    if (!evaluation.equals(expected_result)) {
//  //      throw new AssertionError(message)
//  //    }
//  //  }
//}

def test(file: String): Unit =
  val args = Array("test/" + file + ".pcf")
  println(s"************* $file")
  try
    main(args*)
  catch
    case e: Exception => println(s"Error: ${e.getMessage}")

@main
def test(): Unit = {
//  test_green()
//  test_blue()
//  test_red()
  test_black()
}

def test_green() : Unit = {
  test("green0")
  test("green1")
  test("green2")
  test("green3")
  test("green4")
  test("green5")
  test("green6")
  test("green7")
  test("green8")
  test("green9")
}

def test_blue() : Unit = {
  test("blue0")
  test("blue1")
  test("blue2")
  test("blue3")
  test("blue4")
  test("blue5")
  test("blue6")
  test("blue7")
  test("blue8") // should fail
  test("blue9") // should fail
  test("blue10")
}

def test_red() : Unit = {
  test("red0") // fail because of different closure representation
  test("red1")
  test("red2")
  test("red3")
  test("red4")
  test("red5")
  test("red6")
  test("red7")
  test("red11") // should fail -> type error
  test("red13")
  test("red14")
  test("red15")
  test("red16")
  test("red17")
  test("red18") // should fail -> type error
  test("red19") // fail because of different closure representation
  test("red20") // fail because of different closure representation
  test("red40")
  test("red41")
  test("red42")
  test("red43")
}

def test_black() : Unit = {
  test("black0")
  test("black1")
  test("black2")
  test("black3")
  test("black4") // should fail -> case not handled in annotate method
  test("black5")
}


/**
  def main(): Unit = {
    // Test 1 : 1 + 1 = 2
    assert(
      "1+1",
      IntVal(2),
      "Expected 2 got something else..."
    )
    // Test 2
    assert(
      "let cond = ifz 4 - 2 then 3 - 2 * 2 else 1\nin 2 / cond",
      IntVal(2)
    )
    // Test 3
    assert(
      "let cond = ifz 4 - 2 then 3 - 2 * 2 else 1\nin (2 / cond)",
      IntVal(2)
    )
    // Test 4
    assert(
      "let x = 1 in let y = 2 in x + y",
      IntVal(3)
    )
    // Test 5
    assert(
      "let x = 1 in let x = 2 in x",
      IntVal(2)
    )
    // Test 6
    assert(
      "let x = 1 in let x = x + 1 in x",
      IntVal(2)
    )
    // Test 7
    assert(
      "fun x -> 0",
      Closure("x",Term.Number(0),Map())
    )
    // Test 8
    assert(
      "(fun x -> 0 ) 1",
      IntVal(0)
    )
    // Test 9
    assert(
      "(fun x -> x +1) 1",
      IntVal(2)
    )
    // Test 10
    assert(
      "let zero = fun x -> 0 in zero 1",
      IntVal(0)
    )
    // Test 11
    assert(
      "(fun y -> let x = 1 in x + 1) 41",
      IntVal(2)
    )
    // Test 12
    assert(
      "(fun x -> fun y -> x + y) 2 41",
      IntVal(43)
    )
    // Test 13
    assert(
      "let minus = fun x -> fun y -> x - y in\n\t    " +
        "let g = minus\n    " +
        "68 in g 2",
      IntVal(66)
    )
    // Test 14
    assert(
      "let id = fun x -> x in\n\t"+
        "let inc = fun x -> x +1 in\n" +
        "id (inc 76)",
      IntVal(77)
    )
    // Test 15
    assert(
      "fix x 1",
      IntVal(1)
    )
    // Test 16
    assert(
      "let count = fix f fun n -> ifz n " +
        "then 0\n" +
        "else f(n - 1) in count 2",
      IntVal(0)
    )
    // Test 17
    assert(
      "let fact = fix f fun n -> ifz n\n" +
        "then 1\n" +
        "else n * f(n - 1) in fact\n" +
        "3",
      IntVal(6)
    )
    // Test 18
    assert(
      "let multiply = fix m fun a -> fun b -> ifz a\n" +
        "then 0\n" +
        "else b + m(a - 1) b in\n" +
        "multiply 3 4",
      IntVal(12)
    )
  }
  **/
//  main()
//  println("All tests passed !!")


