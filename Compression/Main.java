import java.util.*;

public class Main {

    public static void main(String[] args) {
        Compression c = new Compression();

        c.addWord("000");
        c.addWord("001");
        c.addWord("000");
        c.addWord("001");
        c.addWord("000");
        c.addWord("001");
        c.addWord("000");
        c.addWord("001");
        c.addWord("011");
        c.addWord("001");
        c.addWord("000");
        c.addWord("110");
        c.addWord("001");
        c.addWord("000");
        c.addWord("111");
        c.addWord("001");
        c.addWord("001");
        c.addWord("000");
        c.addWord("000");
        c.addWord("000");
        c.addWord("001");
        c.compress();
        Map<String, String> map = c.getHeader();

        System.out.println("original: ");
        for(String s : c.original)
            System.out.print(s+" ");
        System.out.println();
        if(map.size() > 0) {
            System.out.println("compressed: ");
            System.out.print("[");
            for(Map.Entry<String, String> entry : map.entrySet())
                System.out.print(entry.getKey()+"->"+entry.getValue()+" ");
            System.out.println("] ");
            String s = "";
            while(s != null) {
                try {
                    s = c.getWord();
                    System.out.print(s+" ");
                }
                catch(IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
    }
}
