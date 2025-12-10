package ll1;

import java.util.*;

public class TableBuilder {

    private static final String EPS = "ε";
    private static final String DOLLAR = "$";

    // Build FIRST sets for all symbols (terminals map to themselves)
    public static Map<String, Set<String>> buildFirst(Grammar g) {
        Map<String, Set<String>> first = new HashMap<>();

        // initialize
        for (String t : g.terminals) {
            Set<String> s = new HashSet<>();
            s.add(t);
            first.put(t, s);
        }
        for (String nt : g.nonTerminals) {
            first.put(nt, new HashSet<>());
        }

        boolean changed;
        do {
            changed = false;

            for (String A : g.nonTerminals) {
                List<Production> prods = g.productions.getOrDefault(A, Collections.emptyList());
                for (Production p : prods) {
                    boolean allNullable = true;

                    for (String sym : p.rhs) {
                        Set<String> firstSym = first.get(sym);
                        if (firstSym == null) firstSym = new HashSet<>();

                        // add FIRST(sym) - {ε} to FIRST(A)
                        for (String s : firstSym) {
                            if (!s.equals(EPS)) {
                                if (first.get(A).add(s)) changed = true;
                            }
                        }

                        // if sym does not produce ε, stop
                        if (!firstSym.contains(EPS)) {
                            allNullable = false;
                            break;
                        }
                    }

                    if (allNullable) {
                        if (first.get(A).add(EPS)) changed = true;
                    }
                }
            }
        } while (changed);

        return first;
    }

    // Build FOLLOW sets for non-terminals
    public static Map<String, Set<String>> buildFollow(Grammar g, Map<String, Set<String>> first) {
        Map<String, Set<String>> follow = new HashMap<>();
        for (String nt : g.nonTerminals) follow.put(nt, new HashSet<>());

        follow.get(g.startSymbol).add(DOLLAR);

        boolean changed;
        do {
            changed = false;

            for (String B : g.nonTerminals) {
                for (Production p : g.productions.getOrDefault(B, Collections.emptyList())) {
                    List<String> rhs = p.rhs;
                    for (int i = 0; i < rhs.size(); i++) {
                        String A = rhs.get(i);
                        if (!g.nonTerminals.contains(A)) continue;

                        // compute FIRST(beta)
                        Set<String> firstBeta = new HashSet<>();
                        boolean betaNullable = true;

                        for (int j = i + 1; j < rhs.size(); j++) {
                            String Y = rhs.get(j);
                            Set<String> firstY = first.getOrDefault(Y, Collections.emptySet());
                            for (String s : firstY) {
                                if (!s.equals(EPS)) firstBeta.add(s);
                            }
                            if (!firstY.contains(EPS)) {
                                betaNullable = false;
                                break;
                            }
                        }

                        if (!firstBeta.isEmpty()) {
                            if (follow.get(A).addAll(firstBeta)) changed = true;
                        }

                        if (betaNullable) {
                            // add FOLLOW(B) to FOLLOW(A)
                            if (follow.get(A).addAll(follow.get(B))) changed = true;
                        }
                    }
                }
            }
        } while (changed);

        return follow;
    }

    // Build LL(1) parse table: Map[NonTerminal][Terminal] = Production
    public static Map<String, Map<String, Production>> buildParseTable(Grammar g,
            Map<String, Set<String>> first, Map<String, Set<String>> follow) {

        Map<String, Map<String, Production>> table = new HashMap<>();
        for (String A : g.nonTerminals) table.put(A, new HashMap<>());

        for (String A : g.nonTerminals) {
            for (Production p : g.productions.getOrDefault(A, Collections.emptyList())) {
                // FIRST(alpha)
                Set<String> firstAlpha = firstOfSequence(p.rhs, first);

                for (String a : firstAlpha) {
                    if (!a.equals(EPS)) {
                        table.get(A).put(a, p);
                    }
                }

                if (firstAlpha.contains(EPS)) {
                    for (String b : follow.get(A)) {
                        table.get(A).put(b, p);
                    }
                }
            }
        }

        return table;
    }

    // FIRST for a sequence of symbols
    private static Set<String> firstOfSequence(List<String> seq, Map<String, Set<String>> first) {
        Set<String> result = new HashSet<>();
        boolean allNullable = true;

        for (String sym : seq) {
            Set<String> f = first.getOrDefault(sym, Collections.emptySet());
            result.addAll(filterOutEps(f));
            if (!f.contains(EPS)) {
                allNullable = false;
                break;
            }
        }

        if (allNullable) result.add(EPS);
        return result;
    }

    private static Set<String> filterOutEps(Set<String> s) {
        Set<String> out = new HashSet<>(s);
        out.remove("ε");
        return out;
    }
}
