public class TestSin {

    // EPS utilisé pour comparer les flottants
    static float EPS = 1e-5f;

    public static void main(String[] args) {

        MathDeca m = new MathDeca();

        System.out.println("===== TESTS SINUS (float) =====");

        // 1. Cas particuliers exacts
        testValue(m, 0.0f);
        testValue(m, (float)Math.PI / 2);
        testValue(m, (float)Math.PI);
        testValue(m, 3 * (float)Math.PI / 2);
        testValue(m, 2 * (float)Math.PI);

        // 2. Autour de points sensibles
        testValue(m, (float)Math.PI / 2 + 1e-4f);
        testValue(m, (float)Math.PI / 2 - 1e-4f);
        testValue(m, (float)Math.PI + 1e-4f);
        testValue(m, (float)Math.PI - 1e-4f);

        // 3. Valeurs usuelles
        testValue(m, (float)Math.PI / 6);   // 30°
        testValue(m, (float)Math.PI / 4);   // 45°
        testValue(m, (float)Math.PI / 3);   // 60°

        // 4. Valeurs négatives
        testValue(m, -(float)Math.PI / 6);
        testValue(m, -(float)Math.PI / 2);

        // 5. Grandes valeurs
        testValue(m, 10.0f);
        testValue(m, 100.0f);
        testValue(m, 1000.0f);
        testValue(m, -1000.0f);

        System.out.println("===== FIN DES TESTS =====");
    }

    private static void testValue(MathDeca m, float x) {
        float sinDeca = m.sin(x);
        float sinJava = (float)Math.sin(x);
        float error = Math.abs(sinDeca - sinJava);

        System.out.printf(
            "x = %-12.6f | sinDeca = %-12.7f | sinJava = %-12.7f | error = %.7e %s%n",
            x,
            sinDeca,
            sinJava,
            error,
            (error < EPS ? "OK" : "!!")
        );
    }
}
