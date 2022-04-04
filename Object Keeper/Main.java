public class Main {

    static class klasaA {
        int a;
        float b;
        String c;

        public klasaA(int as, float bs, String cs) {
            a = as;
            b = bs;
            c = cs;
        }
    }

    static class klasaB {
        klasaA asd;
        float b;
        String c;

        public klasaB(klasaA as, float bs, String cs) {
            asd = as;
            b = bs;
            c = cs;
        }
    }

    public static void main(String[] args) {
        PrzechowywaczObiektow p = new PrzechowywaczObiektow();
        p.setConnection(p.connection);
        klasaA a = new klasaA(1, 5.5f, "abcd");
        klasaB b = new klasaB(new klasaA(5, 7f, "efgh"), 0.0005f, "qwerty");

        p.save(1, a);
        p.save(3, b);

    }
}
