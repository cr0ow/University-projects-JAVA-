import java.util.ArrayList;
import java.util.List;

class Loops implements GeneralLoops {
    public int nesting;
    public List<Integer> minLimits;
    public List<Integer> maxLimits;

    public Loops() {
        minLimits = new ArrayList<>();
        maxLimits = new ArrayList<>();
        minLimits.add(0);
        maxLimits.add(0);
    }

    public void setLowerLimits(List<Integer> limits) {
        minLimits.clear();
        minLimits.addAll(limits);
    }

    public void setUpperLimits(List<Integer> limits) {
        maxLimits.clear();
        maxLimits.addAll(limits);
    }

    public List<List<Integer>> getResult() {
        List<List<Integer>> result = new ArrayList<>();
        if(maxLimits.size() > minLimits.size()) equalLimits(true);
        else if(maxLimits.size() < minLimits.size()) equalLimits(false);
        List<Integer> list = new ArrayList<>(minLimits);
        nesting = maxLimits.size();
        int iterations = 1;
        for(int i = 0; i < nesting; i++) {
            iterations *= (maxLimits.get(i) - minLimits.get(i) + 1);
        }
        result.add(list);
        for(int i = 1; i < iterations; i++) {
            list = Loop(list);
            result.add(list);
        }
        return result;
    }

    private List<Integer> Loop(List<Integer> list) {
        List<Integer> temporaryList = new ArrayList<>(list);
        boolean valid = false;
        int position = 1;
        int temp;
        while(!valid) {
            valid = true;
            temp = temporaryList.get(nesting - position) + 1;
            if(temp > maxLimits.get(nesting - position)) {
                temp = minLimits.get(nesting - position);
                valid = false;
            }
            temporaryList.remove(nesting - position);
            temporaryList.add(nesting - position, temp);
            position++;
        }
        return temporaryList;
    }

    private void equalLimits(boolean loop) {
        if(loop) {
            minLimits.clear();
            for(int i : maxLimits) minLimits.add(0);
        }
        else {
            maxLimits.clear();
            for(int i : minLimits)
                maxLimits.add(Math.max(i, 0));
        }
    }
}
