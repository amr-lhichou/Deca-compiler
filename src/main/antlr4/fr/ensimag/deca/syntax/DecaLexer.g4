lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

// Symboles spéciaux

PLUS : '+';
MINUS: '-';
EOL : '\n' {skip();};
TIMES : '*' ;
OR : '||';
AND : '&&';
COMMA : ',';
EQEQ : '==';
NEQ : '!=';
LEQ : '<=';
GEQ : '>=';
LT : '<';
GT : '>';
SLASH : '/';
PERCENT : '%';
EXCLAM : '!';
DOT : '.';

// Identificateurs
DIGIT : '0' .. '9';
LETTER : 'a' .. 'z' | 'A' .. 'Z';

// Mots réservés
OBRACE : '{';
CBRACE : '}';
SEMI : ';';
EQUALS : '=';
OPARENT : '(';
CPARENT : ')';
ASM : 'asm';
CLASS : 'class';
EXTENDS : 'extends';
ELSE : 'else';
FALSE : 'false';
IF : 'if';
INSTANCEOF : 'instanceof';
NEW : 'new';
NULL  : 'null';
READINT : 'readInt';
READFLOAT : 'readFloat';
PRINTLNX : 'printlnx';
PRINTLN : 'println';
PRINTX : 'printx';
PRINT : 'print';
PROTECTED : 'protected';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';

SPACE : ' ' { skip(); };

IDENT : (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;

// Littéraux entiers
POSITIVE_DIGIT : '1' .. '9';
INT : '0' | POSITIVE_DIGIT* DIGIT;

// Littéraux flottants
NUM : DIGIT+;
SIGN : (PLUS | MINUS)? ;
EXP : ('E' | 'e') SIGN NUM;
DEC : NUM '.' NUM;
FLOATDEC : (DEC | DEC EXP) ('F' | 'f');
DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
NUMHEX : DIGITHEX+;
FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f');
FLOAT : FLOATDEC | FLOATHEX;

// Chaine de caractères
STRING_CAR : ~["\\] ;
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Inclusion de fichier

FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;


// Ignore spaces, tabs, newlines and whitespaces
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        | '//'
        ) {
              skip(); // avoid producing a token
          }
    ;

COMMENT : '//' ~[\r\n]* {
              skip();
          }
        ;

MULTI_LINE_COMMENT : '/*' .*? '*/'{
              skip();
          }
        ;

