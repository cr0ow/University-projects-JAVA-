import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Decrypter implements DecrypterInterface {
    String encrypted = "";
    String name = "Wydział Fizyki, Astronomii i Informatyki Stosowanej";
    String[] split = encrypted.split("\\s+");
    String[] pattern = name.split("\\s+");

    public void setInputText(String encryptedDocument) {
        if(encryptedDocument != null)
            encrypted = encryptedDocument;
        split = encrypted.split("\\s+");
    }

    public Map<Character, Character> getCode() {
        int start = isDecodable();
        System.out.println("a");
        if(start == -1)
            return new HashMap<>();
        System.out.println("a");
        return createMap(start);
    }

    public Map<Character, Character> getDecode() {
        int start = isDecodable();
        Map<Character, Character> result = new HashMap<>();
        if(start == -1)
            return result;
        result = createMap(start);
        Map<Character, Character> flipped = new HashMap<>();
        for(Map.Entry<Character, Character> entry : result.entrySet()) {
            flipped.put(entry.getValue(), entry.getKey());
        }
        return flipped;
    }

    private Map<Character, Character> createMap(int start) {
        Map<Character, Character> result = new HashMap<>();
        int j;
        for(int i=start; i<start+6; i++) {
            j = 0;
            while(j < split[i].length()) {
                result.putIfAbsent(pattern[i - start].charAt(j), split[i].charAt(j));
                j++;
            }
        }
        result.remove(',');
        return result;
    }

    private int isDecodable() {
        int i=0, j=0, start=0;
        while(i < split.length) {
            j = 0;
            if(split[i].length() == pattern[0].length()) {
                start = i;
                while(j<6 && split[i+j].length() == pattern[j].length()) {
                    if(j != 1 && split[i+j].charAt(split[i+j].length()-1) == ',')
                        break;
                    j++;
                }
                if(j == 6 && !validMap(createMap(start)))
                    j = 0;
                if(split[start+2].charAt(split[start+2].length()-1) != split[start+3].charAt(0))
                    j = 0;
            }
            if(j == 6)
                break;
            i++;
        }
        if(j < 6)
            return -1;
        return start;
    }

    private boolean validMap(Map<Character, Character> map) {
        List<Character> values = new ArrayList<>();
        for(Map.Entry<Character, Character> entry : map.entrySet()) {
            if(values.contains(entry.getValue()))
                return false;
            values.add(entry.getValue());
        }
        return true;
    }
}
