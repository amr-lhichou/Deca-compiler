#! /bin/sh


# Script de tests syntaxiques pour le compilateur Deca
# ----------------------------------------------------
# - Vérifie que les fichiers INVALIDES échouent bien à l'analyse syntaxique
# - Vérifie que les fichiers VALIDES passent sans erreur
# - Le test_synt doit produire une erreur localisée (fichier:ligne:)
#   pour être considéré comme un échec attendu

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# Test syntaxique : cas INVALIDES
# Le test est un succès si test_synt détecte une erreur

test_synt_invalide () {
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_synt sur $1."
    else
        echo "Succes inattendu de test_synt sur $1."
        exit 1
    fi
}    

for cas_de_test in src/test/deca/syntax/invalid/provided/*.deca
do
    test_synt_invalide "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/invalid/*.deca
do
    test_synt_invalide "$cas_de_test"
done

# Test syntaxique : cas VALIDES
# Le test est un succès si AUCUNE erreur n'est détectée

test_synt_valide () {
    if test_synt "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec inattendu pour test_synt sur $1."
        exit 1
    else
        echo "Succes attendu de test_synt sur $1."
        
    fi
}   

for cas_de_test in src/test/deca/syntax/valid/provided/*.deca
do
    test_synt_valide "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/*.deca
do
    test_synt_valide "$cas_de_test"
done