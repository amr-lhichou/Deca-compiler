public class MathDeca { 


    /* ===============================
     * Constantes mathématiques
     * =============================== */

    private final float PI = 3.14159265358979323846f;

    /* ===============================
     * Fonctions utilitaires internes
     * =============================== */

    // Valeur absolue
    float _abs(float x) {
        if (x < 0) {
            return -x;
        }
        return x;
    }

    // Puissance entière simple : x^n (n >= 0)
    float _pow(float x, int n) {
        float result = 1.0f;
        int i = 0;

        while (i < n) {
            result = result * x;
            i = i + 1;
        }
        return result;
    }

    // Factorielle simple : n!
    int _fact(int n) {
        int result = 1;
        int i = 1;

        if (n < 0) {
            n = -n;
        }

        if (n == 0) {
            return 1;
        }

        while (i <= n) {
            result = result * i;
            i = i + 1;
        }
        return result;
    }

    /* ===============================
     * Polynôme de Taylor du cosinus
     * cos(x) ≈ 1 − x²/2! + x⁴/4!
     * Valable sur [-π/4 ; π/4]
     * =============================== */

    float _cosinePoly(float x) {
        float result = 1.0f;
        result = result - (_pow(x, 2) / _fact(2));
        result = result + (_pow(x, 4) / _fact(4));
        return result;
    }

    /* ===============================
     * Réduction de domaine
     * =============================== */

    // Ramène x dans [0 ; 2π[
    float _normalizeAngle(float x) {
        float twoPi = 2.0f * PI;

        if (x >= 0) {
            x = x / twoPi;
            x = (x - (int) x) * twoPi;
        } else {
            x = x / twoPi;
            x = (x - (int) x) * twoPi;
            x = -x;
        }
        return x;
    }

    // foction cos
     

    float cos(float x) {
        int sign = 1;

        // Cas particuliers simples
        if (x == 0.0f) {
            return 1.0f;
        }

        // 1. Périodicité : ramener dans [0 ; 2π]
        x = _normalizeAngle(x);

        // 2. Symétrie : cos(-x) = cos(x)
        if (x < 0) {
            x = -x;
        }

        // 3. Réduction vers [0 ; π]
        if (x > PI) {
            x = 2.0f * PI - x;
        }

        // 4. Réduction vers [0 ; π/2] + signe
        if (x > PI / 2.0f) {
            x = PI - x;
            sign = -sign;
        }

        

        // 5. Approximation polynomiale
        return sign * _cosinePoly(x);
    }
}
