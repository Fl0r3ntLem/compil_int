package test

trait TestBlue {
  val verbose : Boolean
  def report(): Unit
  def test(verbose: Boolean, filename: String, message: String, expectation: Option[Int]): Unit
  def test(): Unit = {
    test(verbose, "test/pcf/blue0.pcf", "single let, constant", Option(22))
    test(verbose, "test/pcf/blue1.pcf", "single let, variable", Option(1))
    test(verbose, "test/pcf/blue2.pcf", "single let, arithmetic in body", Option(2))
    test(verbose, "test/pcf/blue3.pcf", "nested let in let body", Option(3))
    test(verbose, "test/pcf/blue4.pcf", "nested let in let body, same variable", Option(2))
    test(verbose, "test/pcf/blue5.pcf", "nested let in let body, overlapping variable", Option(2))
    test(verbose, "test/pcf/blue6.pcf", "nested let in definition, same variable", Option(1))
    test(verbose, "test/pcf/blue7.pcf", "nested let and arithmetic in let body, same variable", Option(3))
    test(verbose, "test/pcf/blue8.pcf", "undefined variable", None)
    test(verbose, "test/pcf/blue9.pcf", "undefined variable (requires proper environment handling)", None)
    test(verbose, "test/pcf/blue10.pcf", "priority of arithmetic wrt let", Option(2))
    report()
  }
}
