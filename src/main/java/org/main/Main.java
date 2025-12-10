package org.main;

import lexer.Lexer;
import lexer.Token;
import ll1.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        // arquivo de entrada
        // String file = "exemplo.txt";
        // Reader reader = new FileReader(file);

        // cria e usa o lexer gerado pelo JFlex
        Lexer lexer = new Lexer(new FileReader("test.txt"));

        List<Token> tokens = new ArrayList<>();
        Token t;
        while ((t = lexer.yylex()) != null) {
            tokens.add(t);
            // ajuste o nome do enum aqui conforme TokenType (por exemplo EOF)
            if (t.type.name().equals("EOF") || t.type.name().equals("$")) {
                break;
            }
        }

        System.out.println("TOKENS LIDOS:");
        for (Token tok : tokens) {
            System.out.printf("  type=%s lexeme=\"%s\" line=%d%n",
                    tok.type.name(), tok.value == null ? "" : tok.value, tok.line);
        }

        // monta gramática e as tabelas
        Grammar g = GrammarFactory.build();
        Map<String, Set<String>> first = TableBuilder.buildFirst(g);
        Map<String, Set<String>> follow = TableBuilder.buildFollow(g, first);
        Map<String, Map<String, Production>> table = TableBuilder.buildParseTable(g, first, follow);

        // (Opcional) imprimir FIRST/FOLLOW
        System.out.println("FIRST:");
        first.forEach((k,v) -> System.out.println(k + " -> " + v));
        System.out.println("FOLLOW:");
        follow.forEach((k,v) -> System.out.println(k + " -> " + v));
        System.out.println("TABLE:");
        table.forEach((nt, row) -> {
            row.forEach((term, prod) -> System.out.println(nt + "," + term + " => " + prod));
        });

        // parse
        Parser parser = new Parser(g, table);
        parser.parse(tokens);
    }
}
