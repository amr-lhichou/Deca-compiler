public class TestSqrt {

    public static void main(String[] args) {

        float[] tests = {
            0.0f,
            0.25f,
            1.0f,
            2.0f,
            4.0f,
            10.0f,
            100.0f
        };

        for (float x : tests) {
            float approx = MathDeca.sqrt(x);
            float ref = (float) Math.sqrt(x);

            System.out.println("x = " + x);
            System.out.println("sqrt (Newton) = " + approx);
            System.out.println("sqrt (Java)   = " + ref);
            System.out.println("erreur = " + (approx - ref));
            System.out.println("----------------------------");
        }
    }
}
