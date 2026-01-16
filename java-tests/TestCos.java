public class TestCos {

    static float EPS = 1e-4f;

    static void test(float x, MathDeca m) {
        float ref = (float) Math.cos(x);   // référence Java
        float deca = m.cos(x);              // ton cos
        float err = Math.abs(ref - deca);

        String status = (err < EPS) ? "OK" : "!!";

        System.out.printf(
            "x = %10.6f | ref = %+1.6f | deca = %+1.6f | err = %.2e %s%n",
            x, ref, deca, err, status
        );
    }

    public static void main(String[] args) {

        MathDeca m = new MathDeca();

        System.out.println("===== Tests cos(x) – float only =====\n");

        System.out.println("[Cas particuliers]");
        test(0.0f, m);
        test((float) (Math.PI / 2), m);
        test((float) Math.PI, m);
        test((float) (3 * Math.PI / 2), m);
        test((float) (2 * Math.PI), m);

        System.out.println("\n[Valeurs classiques]");
        test(0.1f, m);
        test(0.3f, m);
        test(0.5f, m);
        test(1.0f, m);

        System.out.println("\n[Valeurs négatives]");
        test(-0.5f, m);
        test(-1.0f, m);
        test(-(float)Math.PI, m);

        System.out.println("\n[Grands angles]");
        test(10.0f, m);
        test(100.0f, m);
        test(1000.0f, m);
        test(10000.0f, m);

        System.out.println("\n[Angles proches de pi/2]");
        test((float)(Math.PI / 2 - 1e-3), m);
        test((float)(Math.PI / 2 + 1e-3), m);
    }
}
