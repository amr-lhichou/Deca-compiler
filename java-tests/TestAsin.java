public class TestAsin {

    public static void main(String[] args) {

        MathDeca m = new MathDeca();

        // Valeurs de test dans [-1 ; 1]
        float[] tests = {
            -1.0f,
            -0.75f,
            -0.5f,
            -0.25f,
             0.0f,
             0.25f,
             0.5f,
             0.75f,
             1.0f
        };

        System.out.println("Test de asin(x)");
        System.out.println("-------------------------------");

        for (int i = 0; i < tests.length; i++) {
            float x = tests[i];

            float deca = m.asin(x);
            float java = (float) Math.asin(x);

            float diff = deca - java;

            System.out.println(
                "x = " + x +
                " | asin_deca = " + deca +
                " | asin_java = " + java +
                " | diff = " + diff
            );
        }

        System.out.println("-------------------------------");
        System.out.println("Fin des tests");
    }
}

