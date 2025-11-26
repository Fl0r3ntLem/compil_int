package lexer

enum Token:
  case NUMBER(value: Int)
  case LPAR
  case RPAR
  case PLUS
  case MINUS
  case MULTIPLY
  case DIVIDE
  case IFZ
  case EOF
