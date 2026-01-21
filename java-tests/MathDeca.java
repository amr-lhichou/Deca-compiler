public class MathDeca { 


    
    // Constantes mathématiques
    

    private final float PI = 3.14159265358979323846f;
    // Nombre de bits significatifs (mantisse + bit implicite)
    int SIGNIFICAND_WIDTH = 24;

    // Exposants réels min et max pour float normalisé
    int MIN_EXPONENT = -126;
    int MAX_EXPONENT = 127;

    // Plus petit float strictement positif (subnormal)
    float MIN_VALUE = 1.40129846e-45f;

    // Plus grand float représentable
    float MAX_VALUE = 3.40282347e38f;

    
     // Fonctions utilitaires internes
     

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
    // version de pow utilisable par ulp 
    float _pow2(int n) {
        float result = 1.0f;
        int i;
    
        if (n >= 0) {
            i = 0;
            while (i < n) {
                result = result * 2.0f;
                i = i + 1;
            }
        } else {
            i = 0;
            while (i < -n) {
                result = result / 2.0f;
                i = i + 1;
            }
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
    

    /**
     * Approximation de la racine carrée par la méthode de Newton–Raphson
     *
     * On cherche y tel que y² = x
     *
     * @param x nombre positif
     * @return approximation de sqrt(x)
     */
    public static float sqrt(float x) {

        float y;
        int i;

        // Cas particuliers
        if (x < 0.0f) {
            System.out.println("Erreur : sqrt d'un nombre négatif");
            return 0.0f;
        }

        if (x == 0.0f) {
            return 0.0f;
        }

        // Approximation initiale
        y = x;
        i = 0;

        // Méthode de Newton–Raphson
        // y_{n+1} = (y_n + x / y_n) / 2
        while (i < 10) {
            y = 0.5f * (y + x / y);
            i++;
        }

        return y;
    }

    // retourne l'exposant du float x sans passage binaire 
    int _getExponent(float x) {
        float ax = _abs(x);
        int e = 0;
    
        if (ax == 0.0f) {
            return MIN_EXPONENT - 1;
        }
    
        if (ax >= MAX_VALUE) {
            return MAX_EXPONENT;
        }
    
        // pas de _pow
        while (ax >= 2.0f) {
            ax = ax / 2.0f;
            e = e + 1;
        }
    
        while (ax < 1.0f) {
            ax = ax * 2.0f;
            e = e - 1;
        }
    
        return e;
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
    
    // développement du sinus Taylor en fct de l'ordre

    float _sinePoly(float x, int order) {

        float result = x;
        int i = 3;
        int sign = -1;

        while (i <= order) {
            result = result + sign * (_pow(x, i) / _fact(i));
            sign = -sign;
            i = i + 2;
        }

        return result;
    }
    // Puis la fonction avec restriction et cas et antisymétrie 

    float sin(float x) {

        int sign = 1;
        float eps = 0.000001f;
    
        // 0. Cas particuliers immédiats
        if (_abs(x) < eps) {
            return 0;
        }
    
        // 1. Gestion du signe (fonction impaire)
        if (x < 0) {
            x = -x;
            sign = -1;
        }
    
        // 2. Périodicité : ramener dans [0 ; 2π]
        x = _normalizeAngle(x);
    
        // Cas remarquables après normalisation
        if (_abs(x - (PI / 2)) < eps) {
            return sign * 1;
        }
        if (_abs(x - PI) < eps) {
            return 0;
        }
        if (_abs(x - (3 * PI / 2)) < eps) {
            return sign * (-1);
        }
        if (_abs(x - (2 * PI)) < eps) {
            return 0;
        }
    
        // 3. Réduction vers [0 ; π]
        if (x > PI) {
            x = 2 * PI - x;
            sign = -sign;
        }
    
        // 4. Réduction vers [0 ; π/2]
        if (x > PI / 2) {
            x = PI - x;
        }
    
        // 5. Réduction finale vers [0 ; π/4]
        if (x > PI / 4) {
            // sin(x) = cos(π/2 - x)
            return sign * cos((PI / 2) - x);
        }
    
        // 6. Approximation polynomiale (Taylor)
        return sign * _sinePoly(x, 7);
    }



    static float asinPoly(float x) {
        float x2 = x * x;

        float r = x;
        r += (x * x2) / 6.0f;
        r += (3.0f * x * x2 * x2) / 40.0f;
        r += (5.0f * x * x2 * x2 * x2) / 112.0f;

        return r;
    }


    public  float asin(float x) {

        float eps = 1e-6f;
        int sign = 1;

        if (x > 1.0f || x < -1.0f) {
            throw new IllegalArgumentException("asin hors domaine");
        }

        if (_abs(x) < eps) return 0.0f;
        if (_abs(x - 1.0f) < eps) return PI / 2;
        if (_abs(x + 1.0f) < eps) return -PI / 2;

        if (x < 0) {
            x = -x;
            sign = -1;
        }

        if (x > 0.5f) {
            float y = sqrt(1.0f - x * x);
            return sign * (PI / 2 - asinPoly(y));
        }

        return sign * asinPoly(x);
    }


    float atanPoly(float x) {
        float r = x;
        r -= _pow(x, 3) / 3f;
        r += _pow(x, 5) / 5f;
        r -= _pow(x, 7) / 7f;
        return r;
    }



    float atan(float x) {

        float eps = 1e-6f;
        int sign = 1;

        if (_abs(x) < eps) {
            return 0f;
        }

        if (x < 0) {
            x = -x;
            sign = -1;
        }

        if (x > 1) {
            return sign * (PI / 2f - atanPoly(1f / x));
        }

        return sign * atanPoly(x);
    }

    // ULP 
    float ulp(float x) {
        int e;
    
        if (x == 0.0f) {
            return MIN_VALUE;
        }
    
        e = _getExponent(x);
    
        return _pow2(e - 23);
    }
    



    
}
