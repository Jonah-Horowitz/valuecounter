package net.hashjellyfish.valuecounter.vb.ops;

import java.io.Serializable;

/**
 * A unary operation on arbitrary types. Roughly equivalent to <code>java.lang.Function<K,T></code> in Java >=1.8.
 * @param <K> The input type for this operation.
 * @param <T> The output type of this operation.
 */
public interface Operation<K,T> extends Serializable {
    /**
     * Applies this <code>Operation</code> to the specified input.
     * @param target The input for this <code>Operation</code>'s application.
     * @return The output associated with the given input.
     */
    T apply(K target);

    /**
     * Clones this <code>Operation</code>.
     * @return An exact copy of this <code>Operation</code>.
     */
    Operation<K,T> makeCopy();

    /**
     * Returns a string representation of the type of operation.
     * @return A static result, even though this is not a static method.
     */
    String opType();
}
