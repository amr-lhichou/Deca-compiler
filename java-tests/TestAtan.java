public class TestAtan {

    private static float abs(float x) {
        return (x < 0) ? -x : x;
    }

    public static void main(String[] args) {

        MathDeca m = new MathDeca();

        float[] values = {
            0.0f,
            0.1f,
            0.5f,
            1.0f,
            -0.1f,
            -0.5f,
            -1.0f,
            10.0f,
            -10.0f,
            100.0f,
            -100.0f,
            1000.0f,
            -1000.0f
        };

        System.out.println("===== TESTS ATAN (float) =====");

        for (int i = 0; i < values.length; i++) {
            float x = values[i];

            float atanDeca = m.atan(x);
            float atanJava = (float) Math.atan(x);
            float error = abs(atanDeca - atanJava);

            System.out.printf(
                "x = %+10.6f | atanDeca = %+10.6f | atanJava = %+10.6f | err = %.3e",
                x, atanDeca, atanJava, error
            );

            if (error < 1e-4f) {
                System.out.println("  OK");
            } else {
                System.out.println("  !!");
            }
        }

        System.out.println("===== FIN DES TESTS =====");
    }
}
