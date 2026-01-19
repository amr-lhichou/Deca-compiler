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

   // on etend notre cos a n'importe quel ordre 

    float _cosinePoly(float x, int order) {
        float result = 1.0f;
        int k = 1;

    // On s'assure que l'ordre est pair
        if (order % 2 != 0) {
            order = order - 1;
        }

        while (2 * k <= order) {
            float term = _pow(x, 2 * k) / _fact(2 * k);

            if (k % 2 == 1) {
                result = result - term;
            } else {
                result = result + term;
            }

            k = k + 1;
        }

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
        float eps = 0.000001f;
        int ORDER = 4;

    // 1. Périodicité : ramener dans [0 ; 2pi]
        x = _normalizeAngle(x);

    // les cas particuliers 
        if (_abs(x) < eps) {
            return 1;
        }

        if (_abs(x - PI/2) < eps) {
            return 0;
        }

        if (_abs(x - PI) < eps) {
            return -1;
        }

        if (_abs(x - (3 * PI / 2)) < eps) {
            return 0;
        }

        if (_abs(x - (2 * PI)) < eps) {
            return 1;
        }

    // 2. Symétrie : cos(-x) = cos(x)
        if (x < 0) {
            x = -x;
        }

    // 3. Réduction vers [0 ; pi]
        if (x > PI) {
            x = 2 * PI - x;
        }

    // 4. Réduction vers [0 ; pi/2] + signe
        if (x > PI / 2) {
            x = PI - x;
            sign = -sign;
        }

    // 5. Approximation polynomiale
        return sign * _cosinePoly(x, ORDER);
    }


    
}
