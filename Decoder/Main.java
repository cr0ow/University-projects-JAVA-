public class Main {

    public static void main(String[] args) {
        DecoderInterface obj = new Decode();

        String input="1101111000000001101111110011011";
        for(int i=0;i<input.length();i++) {
            obj.input(input.charAt(i)-'0');
        }
        System.out.println(obj.output());
    }
}
