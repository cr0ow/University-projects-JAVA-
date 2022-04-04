import java.util.*;

public class ParallelSearcher implements ParallelSearcherInterface {
    private final Object latch = new Object();
    private double prevValue = 0;
    private HidingPlaceSupplier hidingPlaceSupplier;

    private class SearcherThread extends Thread {
        public void run() {
            HidingPlaceSupplier.HidingPlace hidingPlace = hidingPlaceSupplier.get();
            while(hidingPlace != null) {
                if(hidingPlace.isPresent())
                    synchronized (latch) {
                        prevValue += hidingPlace.openAndGetValue();
                    }
                hidingPlace = hidingPlaceSupplier.get();
            }
        }
    }

    public void set(HidingPlaceSupplierSupplier supplier) {
        hidingPlaceSupplier = supplier.get(prevValue);
        List<SearcherThread> threads = new ArrayList<>();
        while(hidingPlaceSupplier != null) {
            prevValue = 0;
            threads.clear();
            for(int i=0; i<hidingPlaceSupplier.threads(); i++) {
                threads.add(new SearcherThread());
                threads.get(i).start();
            }
            for(Thread thread : threads) {
                try {
                    thread.join(0);
                } catch (InterruptedException exception) {
                    //interruption
                }
            }
            hidingPlaceSupplier = supplier.get(prevValue);
        }
    }
}