package ll1;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Grammar {
    Set<String> terminals;
    Set<String> nonTerminals;
    Map<String, List<Production>> productions;
    String startSymbol;

    public Grammar() {
        this.terminals = new java.util.HashSet<>();
        this.nonTerminals = new java.util.HashSet<>();
        this.productions = new java.util.HashMap<>();
    }
}
