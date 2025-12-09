package test

trait TestGreen {
  val verbose : Boolean
  def report(): Unit
  def test(verbose: Boolean, filename: String, message: String, expectation: Option[Int]): Unit
  def test(): Unit = {
    test(verbose, "test/pcf/green0.pcf", "number", Option(0))
    test(verbose, "test/pcf/green1.pcf", "number", Option(42))
    test(verbose, "test/pcf/green2.pcf", "arithmetic (-)", Option(-1))
    test(verbose, "test/pcf/green3.pcf", "simple ifz", Option(1))
    test(verbose, "test/pcf/green4.pcf", "simple ifz", Option(2))
    test(verbose, "test/pcf/green5.pcf", "arithmetic (-)", Option(-4))
    test(verbose, "test/pcf/green6.pcf", "arithmetic (+, *)", Option(7))
    test(verbose, "test/pcf/green7.pcf", "simple parentheses", Option(1))
    test(verbose, "test/pcf/green8.pcf", "parentheses & arithmetic (+, *)", Option(9))
    test(verbose, "test/pcf/green9.pcf", "arithmetic (/)", Option(2))
    report()
  }
}
