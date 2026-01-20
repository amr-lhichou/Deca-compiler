public class TestUlp {

    public static void main(String[] args) {

        MathDeca m = new MathDeca();

        float[] values = {
            0.0f,
            1.0f,
            2.0f,
            0.5f,
            10.0f,
            1000.0f,
            1e-6f,
            1e-20f,
            1e20f
        };

        System.out.println("x\t\tulp_maison\t\tMath.ulp");

        for (int i = 0; i < values.length; i++) {
            float x = values[i];

            float ulpDeca = m.ulp(x);
            float ulpJava = Math.ulp(x);

            System.out.println(
                x + "\t\t" +
                ulpDeca + "\t\t" +
                ulpJava
            );
        }
    }
}
