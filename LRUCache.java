import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final DoublyLinkedList<K, V> list;

    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.list = new DoublyLinkedList<>();
    }

    public synchronized V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }

        Node<K, V> node = map.get(key);
        list.moveToFront(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        if (map.containsKey(key)) {
            // Update existing entry
            Node<K, V> node = map.get(key);
            node.value = value;
            list.moveToFront(node);
        } else {
            // Add new entry: evict LRU item if capacity is reached
            if (map.size() >= capacity) {
                Node<K, V> lru = list.removeLast();
                if (lru != null) {
                    map.remove(lru.key);
                }
            }

            Node<K, V> newNode = new Node<>(key, value);
            list.addFirst(newNode);
            map.put(key, newNode);
        }
    }

    public synchronized V remove(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K, V> node = map.remove(key);
        list.remove(node);
        return node.value;
    }

    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public synchronized int size() {
        return map.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized void clear() {
        map.clear();
        list.clear();
    }

    public synchronized void display() {
        System.out.println("Cache [MRU -> LRU]: " + list);
    }

    @Override
    public synchronized String toString() {
        return list.toString();
    }

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("  LRU Cache Demonstration (Capacity = 3)  ");
        System.out.println("==========================================\n");

        LRUCache<Integer, String> cache = new LRUCache<>(3);

        System.out.println("Step 1: Adding 3 entries -> (1, 'One'), (2, 'Two'), (3, 'Three')");
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        cache.display();
        System.out.println();

        System.out.println("Step 2: Accessing key 1 (get(1))");
        System.out.println("Result: " + cache.get(1));
        System.out.println("Notice: Key 1 was moved to the front (MRU):");
        cache.display();
        System.out.println();

        System.out.println("Step 3: Inserting key 4 -> (4, 'Four') with capacity 3");
        System.out.println("Eviction: Least recently used item (key 2) should be evicted.");
        cache.put(4, "Four");
        cache.display();
        System.out.println("Checking evicted key 2 (get(2)): " + cache.get(2) + " (null means evicted)");
        System.out.println();

        System.out.println("Step 4: Updating value of existing key 3 -> 'Three-Updated'");
        cache.put(3, "Three-Updated");
        cache.display();
        System.out.println();

        System.out.println("Step 5: Removing key 1 explicitly (remove(1))");
        System.out.println("Removed value: " + cache.remove(1));
        cache.display();
        System.out.println();

        System.out.println("Step 6: Cache stats");
        System.out.println("Current size: " + cache.size());
        System.out.println("Capacity: " + cache.getCapacity());
        System.out.println("Contains key 4? " + cache.containsKey(4));
        System.out.println("Contains key 1? " + cache.containsKey(1));
        System.out.println("\nAll operations executed successfully!");
    }
}
