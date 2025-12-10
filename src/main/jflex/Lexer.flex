package lexer;

import lexer.Token;
import lexer.TokenType;

%%

%class Lexer
%unicode
%public
%line
%column
%type Token

%{
    // Código Java adicional se necessário
%}

DIGIT   = [0-9]
LETTER  = [a-zA-Z_]

ID      = {LETTER}({LETTER}|{DIGIT})*
//[a-zA-Z_]([a-zA-Z_]|[0-9])*

NUMBER  = {DIGIT}+("."{DIGIT}+)?
//[0-9]+("." [0-9]+)?

WHITESPACE = [ \t\r\n]+

%%

/* Palavras-chave */
"class"       { return new Token(TokenType.CLASS, yytext(), yyline + 1); }
"if"          { return new Token(TokenType.IF, yytext(), yyline + 1); }
"else"        { return new Token(TokenType.ELSE, yytext(), yyline + 1); }
"main"        { return new Token(TokenType.MAIN, yytext(), yyline + 1); }

"int"         { return new Token(TokenType.INT, yytext(), yyline + 1); }
"float"       { return new Token(TokenType.FLOAT, yytext(), yyline + 1); }
"double"      { return new Token(TokenType.DOUBLE, yytext(), yyline + 1); }
"string"      { return new Token(TokenType.STRING, yytext(), yyline + 1); }
"char"        { return new Token(TokenType.CHAR, yytext(), yyline + 1); }
"short"       { return new Token(TokenType.SHORT, yytext(), yyline + 1); }
"long"        { return new Token(TokenType.LONG, yytext(), yyline + 1); }
"boolean"     { return new Token(TokenType.BOOLEAN, yytext(), yyline + 1); }
"true"        { return new Token(TokenType.TRUE, yytext(), yyline + 1); }
"false"       { return new Token(TokenType.FALSE, yytext(), yyline + 1); }
"function"    { return new Token(TokenType.FUNCTION, yytext(), yyline + 1); }

/* Operadores aritméticos */
"+"           { return new Token(TokenType.PLUS, yytext(), yyline + 1); }
"-"           { return new Token(TokenType.MINUS, yytext(), yyline + 1); }
"*"           { return new Token(TokenType.MULTIPLY, yytext(), yyline + 1); }
"/"           { return new Token(TokenType.DIVIDE, yytext(), yyline + 1); }

/* Operadores especiais */
"^"           { return new Token(TokenType.POW, yytext(), yyline + 1); }
"root"        { return new Token(TokenType.ROOT, yytext(), yyline + 1); }

/* Operadores relacionais */
"=="          { return new Token(TokenType.EQ, yytext(), yyline + 1); }
"!="          { return new Token(TokenType.NEQ, yytext(), yyline + 1); }
">="          { return new Token(TokenType.GE, yytext(), yyline + 1); }
"<="          { return new Token(TokenType.LE, yytext(), yyline + 1); }
">"           { return new Token(TokenType.GT, yytext(), yyline + 1); }
"<"           { return new Token(TokenType.LT, yytext(), yyline + 1); }

/* Operador de atribuição */
"="           { return new Token(TokenType.ASSIGN, yytext(), yyline + 1); }

/* Símbolos */
"("           { return new Token(TokenType.LPAREN, yytext(), yyline + 1); }
")"           { return new Token(TokenType.RPAREN, yytext(), yyline + 1); }
"{"           { return new Token(TokenType.LBRACE, yytext(), yyline + 1); }
"}"           { return new Token(TokenType.RBRACE, yytext(), yyline + 1); }
";"           { return new Token(TokenType.SEMICOLON, yytext(), yyline + 1); }
"="           { return new Token(TokenType.ASSIGN, yytext(), yyline + 1); }


/* String literal: "texto" */
\"([^\"\\]|\\.)*\"
    { return new Token(TokenType.STRING_LITERAL, yytext(), yyline + 1); }

/* Char literal: 'a' ou '\n' */
\'([^\'\\]|\\.)\'
    { return new Token(TokenType.CHAR_LITERAL, yytext(), yyline + 1); }

/* Comentário estilo // */
"//".*        { /* ignorar */ }

/* Comentário estilo /* ... */ */
"/*"([^*]|(\*+[^*/]))*"*/"   { /* ignorar */ }

/* Identificadores */
{ID}          { return new Token(TokenType.ID, yytext(), yyline + 1); }

/* Números */
{NUMBER}      { return new Token(TokenType.NUMBER, yytext(), yyline + 1); }

/* Espaços em branco */
{WHITESPACE}  { /* ignorar */ }

/* Fim de arquivo */
<<EOF>>       { return new Token(TokenType.EOF, "EOF", yyline + 1); }

/* Caractere inesperado */
.             {
                 System.err.println("Unexpected character '" + yytext() + "' at line " + (yyline + 1));
                 return null;
              }
