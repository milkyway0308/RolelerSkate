package skywolf46.rolelerskate.util;

public class SimplePair<K, V> {
    private K k;
    private V v;

    public SimplePair(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }
}
