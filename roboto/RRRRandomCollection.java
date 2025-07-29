package dre.elfocrash.roboto;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Elfocrash
 * @param <E> 
 *
 */
public class RRRRandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RRRRandomCollection() {
        this(new Random());
    }

    public RRRRandomCollection(Random random) {
        this.random = random;
    }

    public RRRRandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
