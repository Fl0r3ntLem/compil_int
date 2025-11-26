package generator

enum Ins :
  case Add, Sub, Mul, Div, Pushenv, Popenv, Extend, Apply
  case Ldi(n: Int)
  case Search(index: Int)
  case Mkclos(code: List[Ins])
  case Test(i: List[Ins], j: List[Ins])
  