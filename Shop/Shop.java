import java.util.*;

public class Shop implements ShopInterface {
    private final List<Product> shop = new ArrayList<>();

    private static class Product {
        String name;
        int quantity;
        final Object blockade = new Object();

        public Product(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }

    @Override
    public void delivery(Map<String, Integer> goods) {
        for (Map.Entry<String, Integer> entry : goods.entrySet()) {
            synchronized (this) {
                boolean contain = false;
                for (Product product : shop)
                    if (product.name.equals(entry.getKey())) {
                        product.quantity += entry.getValue();
                        contain = true;
                        break;
                    }
                if (!contain)
                    shop.add(new Product(entry.getKey(), entry.getValue()));
                for (Product product : shop) {
                    if (product.name.equals(entry.getKey())) {
                        synchronized (product.blockade) {
                            product.blockade.notifyAll();
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean purchase(String productName, int quantity) {
        int idx = -1;
        for (int i = 0; i < shop.size(); i++) {
            if (shop.get(i).name.equals(productName)) {
                idx = i;
                break;
            }
        }
        if (idx < 0) {
            Product p = new Product(productName, 0);
            shop.add(p);
            idx = shop.indexOf(p);
        }
        synchronized (shop.get(idx).blockade) {
            if (shop.get(idx).quantity >= quantity) {
                shop.get(idx).quantity -= quantity;
                return true;
            }
            try {
                shop.get(idx).blockade.wait();
            } catch (InterruptedException ex) {
                //catching
            }
            if (shop.get(idx).quantity >= quantity) {
                shop.get(idx).quantity -= quantity;
                return true;
            }
        }
        return false;
    }

    public synchronized Map<String, Integer> stock() {
        Map<String, Integer> result = new HashMap<>();
        for (Product product : shop)
            result.put(product.name, product.quantity);
        return result;
    }
}
