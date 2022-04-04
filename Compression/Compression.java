import java.util.*;

public class Compression implements CompressionInterface {
    int var = 0;
    List<String> original = new ArrayList<>();
    List<String> compressed = new ArrayList<>();
    List<String> words = new ArrayList<>();
    List<Integer> repeats = new ArrayList<>();
    Map<String, String> encoding = new HashMap<>();

    @Override
    public void addWord(String word) {
        original.add(word);
    }

    @Override
    public void compress() {
        sortWords(checkRepeats());
        var = isProfitable();
        if(var > 0)
            makeCompression(var);
    }

    private Map<String, Integer> checkRepeats() {
        Map<String, Integer> repeats = new HashMap<>();
        for(String word : original) {
            if(repeats.containsKey(word))
                repeats.replace(word, repeats.get(word)+1);
            repeats.putIfAbsent(word, 1);
        }
        return repeats;
    }

    private void sortWords(Map<String, Integer> map) {
        words.clear();
        repeats.clear();
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            words.add(entry.getKey());
            repeats.add(entry.getValue());
        }
        int temp1;
        String temp2;
        for(int i=0; i<repeats.size(); i++)
            for(int j=i; j<repeats.size(); j++) {
                if(repeats.get(i) < repeats.get(j)) {
                    temp1 = repeats.get(i);
                    repeats.set(i, repeats.get(j));
                    repeats.set(j, temp1);
                    temp2 = words.get(i);
                    words.set(i, words.get(j));
                    words.set(j, temp2);
                }
            }
    }

    private int isProfitable() {
        int length = words.get(0).length();
        int profit, loss;
        int bits;
        int max=0, idx=-1;
        for(int i=1; i <= words.size(); i++) {
            profit = 0;
            loss = 0;
            bits = (int)Math.ceil(Math.log10(i)/Math.log10(2)) + 1;
            for(int j=0; j < i; j++) {
                profit += repeats.get(j)*(length - bits);
                loss += bits + length;
            }
            for(int k=i; k < words.size(); k++)
                loss += repeats.get(k);
            if(profit-loss > max) {
                max = profit - loss;
                idx = i;
            }
        }
        return idx;
    }

    private void makeCompression(int words) {
        List<String> codes = new ArrayList<>();
        codes.add("0");
        if (words > 1)
            codes = generateCode(words);
        int i = 0;
        for (String word : original) {
            if (!encoding.containsKey(word) && toCompress(word, words)) {
                encoding.put(word, codes.get(i));
                this.compressed.add(codes.get(i));
                i++;
            }
            else if (toCompress(word, words)) {
                for (Map.Entry<String, String> entry : encoding.entrySet())
                    if (entry.getKey().equals(word))
                        this.compressed.add(entry.getValue());
            }
            else
                this.compressed.add("1"+word);
        }
    }

    private boolean toCompress(String word, int words) {
        for(int i=0; i<words; i++)
            if(word.equals(this.words.get(i)))
                return true;
        return false;
    }

    private List<String> generateCode(int words) {
        List<String> result = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(0);
        int bits = (int)Math.ceil(Math.log10(words)/Math.log10(2)) + 1;
        String res = list.get(0).toString();
        for(int i=1; i<bits; i++)
            res = "0".concat(res);
        result.add(res);
        int j;
        boolean flag;
        for(int k=0; k<words-1; k++) {
            flag = false;
            j = 0;
            do {
                try {
                    list.get(j);
                }
                catch (IndexOutOfBoundsException e) {
                    list.add(0);
                }
                if(list.get(j) == 0) {
                    list.set(j, 1);
                    flag = true;
                }
                else
                    list.set(j, 0);
                j++;
            } while(!flag);
            res = "";
            for(int i= list.size()-1; i > -1;i--)
                res = res.concat(list.get(i).toString());
            for(int i=res.length(); i<bits; i++)
                res = "0".concat(res);
            result.add(res);
        }
        return result;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> result = new HashMap<>();
        if(var == 0)
            return result;
        for(Map.Entry<String, String> entry : encoding.entrySet()) {
            result.put(entry.getValue(), entry.getKey());
        }
        return result;
    }

    @Override
    public String getWord() {
        return compressed.remove(0);
    }
}
