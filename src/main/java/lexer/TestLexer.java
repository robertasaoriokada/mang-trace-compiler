package lexer;

import java.io.FileReader;

public class TestLexer {
    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer(new FileReader("test.txt"));

        Token t;
        while ((t = lexer.yylex()) != null && t.type != TokenType.EOF) {
            System.out.println(
                    "Token: " + t.type +
                            ", Valor: " + t.value +
                            ", Linha: " + t.line
            );
        }
    }
}
