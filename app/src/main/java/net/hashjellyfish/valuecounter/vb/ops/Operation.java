package net.hashjellyfish.valuecounter.vb.ops;

public interface Operation<K,T> {
    public T apply(K target);

    public Operation<K,T> makeCopy();
}
