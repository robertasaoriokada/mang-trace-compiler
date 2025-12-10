package lexer;

public enum TokenType {
    CLASS,

    IF, ELSE, MAIN,

    //tipagem
    INT, FLOAT, DOUBLE, STRING, CHAR, SHORT, LONG, BOOLEAN,
    TRUE, FALSE,


    FUNCTION,
    AND,
    OR, NOT, RETURN,

    ID,
    NUMBER,

    // operadores
    PLUS, MINUS, MULTIPLY, DIVIDE, POW, ROOT,

    LT,
    GT,
    LE,
    GE,
    EQ,
    NEQ,

    LPAREN, RPAREN,
    LBRACE, RBRACE,
    SEMICOLON,
    ASSIGN,
    STRING_LITERAL,
    CHAR_LITERAL,

    EOF
}
