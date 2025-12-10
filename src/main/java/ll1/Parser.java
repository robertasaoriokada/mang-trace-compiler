package ll1;

import lexer.Token;
import java.util.*;

public class Parser {

    private final Grammar grammar;
    private final Map<String, Map<String, Production>> parseTable;

    public Parser(Grammar grammar, Map<String, Map<String, Production>> parseTable) {
        this.grammar = grammar;
        this.parseTable = parseTable;
    }

    public void parse(List<Token> tokens) {
        Stack<String> stack = new Stack<>();
        stack.push("$");
        stack.push(grammar.startSymbol);

        int index = 0;

        while (!stack.isEmpty()) {
            String top = stack.pop();
            Token cur = tokens.get(index);

            if (grammar.terminals.contains(top) || top.equals("$")) {
                if (top.equals(cur.type.name())) {
                    index++;
                    continue;
                } else {
                    throw syntaxError("Esperado '" + top + "' mas veio '" + cur.value + "'", cur);
                }
            }

            Map<String, Production> row = parseTable.get(top);
            if (row == null) {
                throw syntaxError("Não há entrada na tabela para não-terminal: " + top, cur);
            }

            Production p = row.get(cur.type.name());
            // try fallback: maybe parser used "$" as lookahead
            if (p == null && row.containsKey("$")) p = row.get("$");

            if (p == null) {
                throw syntaxError("Nenhuma produção para '" + top + "' com lookahead '" + cur.value + "'", cur);
            }

            // empilha rhs em ordem inversa (ignora ε)
            List<String> rhs = p.rhs;
            for (int i = rhs.size() - 1; i >= 0; i--) {
                String s = rhs.get(i);
                if (!s.equals("ε")) stack.push(s);
            }
        }

        // final check: all tokens consumed except final $
        if (index != tokens.size()) {
            Token rest = tokens.get(index);
            throw syntaxError("Tokens restantes após análise: " + rest.value, rest);
        }

        System.out.println("Entrada aceita.");
    }

    private RuntimeException syntaxError(String msg, Token t) {
        return new RuntimeException("Erro sintático: " + msg + " (linha " + t.line + ")");
    }
}
