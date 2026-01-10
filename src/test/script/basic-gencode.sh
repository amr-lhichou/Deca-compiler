#! /bin/sh

# Auteur : gl53
# Version initiale : 01/01/2026

# Encore un test simpliste. On compile un fichier (cond0.deca), on
# lance ima dessus, et on compare le résultat avec la valeur attendue.

# Ce genre d'approche est bien sûr généralisable, en conservant le
# résultat attendu dans un fichier pour chaque fichier source.
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"

# On ne teste qu'un fichier. Avec une boucle for appropriée, on
# pourrait faire bien mieux ...

test_codegen_invalide () {
    # $1 = premier argument.
    if test_codegen "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_codegen sur $1."
    else
        echo "Succes inattendu de test_codegen sur $1."
        exit 1
    fi
}    


#for cas_de_test in src/test/deca/codegen/invalid/*.deca
#do
#    test_codegen_invalide "$cas_de_test"
#done

test_codegen_valide () {
    if test_codegen "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_codegen sur $1."
        exit 1
    else
        echo "Succes attendu de test_codegen sur $1."
        
    fi
}   

for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
do
    test_codegen_valide "$cas_de_test"
done

for cas_de_test in src/test/deca/codegen/valid/*.deca
do
    test_codegen_valide "$cas_de_test"
done




#rm -f ./src/test/deca/codegen/valid/provided/cond0.ass 2>/dev/null
#decac ./src/test/deca/codegen/valid/provided/cond0.deca || exit 1
#if [ ! -f ./src/test/deca/codegen/valid/provided/cond0.ass ]; then
#    echo "Fichier cond0.ass non généré."
#    exit 1
#fi

#resultat=$(ima ./src/test/deca/codegen/valid/provided/cond0.ass) || exit 1
#rm -f ./src/test/deca/codegen/valid/provided/cond0.ass

## On code en dur la valeur attendue.
#attendu=ok

#if [ "$resultat" = "$attendu" ]; then
#   echo "Tout va bien"
#else
#    echo "Résultat inattendu de ima:"
#    echo "$resultat"
#    exit 1
#fi
