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
//EOL : '\n' {skip();};
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
fragment DIGIT : '0' .. '9';
fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';

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
PUBLIC : 'public';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';

//SPACE : ' ' { skip(); };
// les types
TYPE_INT : 'int';
TYPE_FLOAT : 'float';
//TYPE_STRING : 'string';
TYPE_BOOLEAN : 'boolean';
VOID : 'void';
IDENT : (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;
// Littéraux entiers
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | POSITIVE_DIGIT DIGIT*;

// j ai ajouter trop de fragment pour empécher les jetons intermédiaire (ex : DEC ... )
// que le Parser ne connait pas . seul float est exposé
// car sinon il affiche erreur , il reconnait par exemple 1.8 comme un DEC pas comme un FLOAT
// Littéraux flottants
fragment NUM : DIGIT+;
fragment SIGN : (PLUS | MINUS) ;
fragment EXP : ('E' | 'e') SIGN? NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP | NUM EXP) ('F' | 'f')?;
fragment DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') ('+'|'-')? NUM ('F' | 'f')?;
FLOAT : FLOATDEC | FLOATHEX;

// Chaine de caractères
fragment STRING_CAR : ~["\\\r\n] ;
MULTI_LINE_STRING : '"' (STRING_CAR | '\\"' | '\\\\' | '\r'? '\n')* '"' ;
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
// Inclusion de fichier
// j vais gerer ici les include (#include)
// j vais utilisé le doInclude qui existe deja dans AbstratDecaLexer
// il permet de chercher le fichier .decah et de remplacer par son contenue , dinc l inclusion
INCLUDE : '#include' ' '* STRING { doInclude(getText()); };

//FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
//  j ai commenté token FILENAME , car il y a un '.' dedans , le lexer croyait que "this.numero"
//c etait un seul mots (noms de fichier ), du coup le parser ne capte pas le token DOT
// en servirar pas de tt façcon car les STRING font largement le taf pour les noms des fichier

// Ignore spaces, tabs, newlines and whitespaces
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
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


