package ll1;

import java.util.*;

public class GrammarFactory {

    public static Grammar build() {

        Grammar g = new Grammar();
        g.startSymbol = "Program";

        g.terminals.addAll(List.of(
                "CLASS",

                "INT", "FLOAT", "DOUBLE", "STRING", "CHAR", "SHORT", "LONG", "BOOLEAN",
                "TRUE", "FALSE",

                "IF", "ELSE", "FUNCTION", "WHILE", "FOR",

                "ID",
                "NUMBER",
                "STRING_LITERAL", "CHAR_LITERAL",

                "PLUS", "MINUS", "MULTIPLY", "DIVIDE", "POW", "ROOT",

                "LT", "GT", "LE", "GE", "EQ", "NEQ",

                "ASSIGN",
                "LPAREN", "RPAREN",
                "LBRACE", "RBRACE",
                "SEMICOLON",

                "EOF"
        ));

        g.nonTerminals.addAll(List.of(
                "Program",
                "ClassDecl",

                "VarDeclList",
                "VarDecl",

                "FuncDeclList",
                "FuncDecl",

                "Block",
                "StmtList",
                "Stmt",

                "Assign",
                "AssignNoSemi",

                "IfStmt",
                "Loop",

                "Type",
                "Expr",
                "ExprPrime",
                "Term",
                "Compare"
        ));

        // Program
        g.productions.put("Program", List.of(
                p("Program", "ClassDecl", "EOF")
        ));

        // class ID { vars funcs }
        g.productions.put("ClassDecl", List.of(
                p("ClassDecl", "CLASS", "ID", "LBRACE", "VarDeclList", "FuncDeclList", "RBRACE")
        ));

        // VarDecl
        g.productions.put("VarDeclList", List.of(
                p("VarDeclList", "VarDecl", "VarDeclList"),
                p("VarDeclList")
        ));

        g.productions.put("VarDecl", List.of(
                p("VarDecl", "Type", "ID", "ASSIGN", "Expr", "SEMICOLON")
        ));

        // FuncDecl
        g.productions.put("FuncDeclList", List.of(
                p("FuncDeclList", "FuncDecl", "FuncDeclList"),
                p("FuncDeclList")
        ));

        g.productions.put("FuncDecl", List.of(
                p("FuncDecl", "FUNCTION", "ID", "Block")
        ));

        // Block → { StmtList }
        g.productions.put("Block", List.of(
                p("Block", "LBRACE", "StmtList", "RBRACE")
        ));

        // StmtList
        g.productions.put("StmtList", List.of(
                p("StmtList", "Stmt", "StmtList"),
                p("StmtList")
        ));

        // Stmt
        g.productions.put("Stmt", List.of(
                p("Stmt", "VarDecl"),
                p("Stmt", "Assign"),
                p("Stmt", "IfStmt"),
                p("Stmt", "Loop")
        ));

        // Assign → ID = Expr ;
        g.productions.put("Assign", List.of(
                p("Assign", "ID", "ASSIGN", "Expr", "SEMICOLON")
        ));

        // Assign sem ;
        g.productions.put("AssignNoSemi", List.of(
                p("AssignNoSemi", "ID", "ASSIGN", "Expr")
        ));

        // If else
        g.productions.put("IfStmt", List.of(
                p("IfStmt", "IF", "LPAREN", "Compare", "RPAREN", "Block", "ELSE", "Block")
        ));

        // Loop
        g.productions.put("Loop", List.of(
                p("Loop", "WHILE", "LPAREN", "Compare", "RPAREN", "Block"),
                p("Loop", "FOR", "LPAREN", "AssignNoSemi", "SEMICOLON", "Compare", "SEMICOLON", "AssignNoSemi", "RPAREN", "Block")
        ));

        // Types
        g.productions.put("Type", List.of(
                p("Type", "INT"),
                p("Type", "FLOAT"),
                p("Type", "DOUBLE"),
                p("Type", "STRING"),
                p("Type", "CHAR"),
                p("Type", "SHORT"),
                p("Type", "LONG"),
                p("Type", "BOOLEAN")
        ));

        // Compare → Expr | Expr op Expr
        g.productions.put("Compare", List.of(
                p("Compare", "Expr"),
                p("Compare", "Expr", "GT", "Expr"),
                p("Compare", "Expr", "LT", "Expr"),
                p("Compare", "Expr", "GE", "Expr"),
                p("Compare", "Expr", "LE", "Expr"),
                p("Compare", "Expr", "EQ", "Expr"),
                p("Compare", "Expr", "NEQ", "Expr")
        ));

        // Expr
        g.productions.put("Expr", List.of(
                p("Expr", "Term", "ExprPrime")
        ));

        // ExprPrime
        g.productions.put("ExprPrime", List.of(
                p("ExprPrime", "PLUS", "Term", "ExprPrime"),
                p("ExprPrime", "MINUS", "Term", "ExprPrime"),
                p("ExprPrime")
        ));

        // Term
        g.productions.put("Term", List.of(
                p("Term", "ID"),
                p("Term", "NUMBER"),
                p("Term", "STRING_LITERAL"),
                p("Term", "CHAR_LITERAL")
        ));

        return g;
    }

    private static Production p(String lhs, String... rhs) {
        return new Production(lhs, Arrays.asList(rhs));
    }
}
