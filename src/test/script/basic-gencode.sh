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


test_codegen_valide () {
    src="$1"

    dir=$(dirname "$src")
    base=$(basename "$src" .deca)

    ass="$dir/$base.ass"
    expected="$dir/$base.expected"

    rm -f "$ass" 2>/dev/null

    decac "$src" || exit 1

    if [ ! -f "$ass" ]; then
        echo "Fichier $ass non généré."
        exit 1
    fi

    resultat=$(ima "$ass") || exit 1
    attendu=$(cat "$expected")

    rm -f "$ass"

    if [ "$resultat" = "$attendu" ]; then
        echo "Succes attendu pour $src"
    else
        echo "Résultat inattendu pour $src"
        echo "Attendu :"
        echo "$attendu"
        echo "Obtenu :"
        echo "$resultat"
        exit 1
    fi
}


for src in src/test/deca/codegen/valid/provided/*.deca
do
    test_codegen_valide "$src"
done

for src in src/test/deca/codegen/valid/*.deca
do
    test_codegen_valide "$src"
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
#    echo "Tout va bien"
#else
#    echo "Résultat inattendu de ima:"
#    echo "$resultat"
#    exit 1
#fi






