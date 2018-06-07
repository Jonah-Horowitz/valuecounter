package net.hashjellyfish.valuecounter.vb.ops;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A unary operation on arbitrary types. Roughly equivalent to <code>java.lang.Function<T,T></code> in Java >=1.8.
 * @param <T> The input/output type of this operation.
 */
public interface Operation<T> extends Serializable {
    /**
     * Applies this <code>Operation</code> to the specified input.
     * @param target The input for this <code>Operation</code>'s application.
     * @return The output associated with the given input.
     */
    @NotNull
    T apply(@NotNull T target);

    /**
     * Clones this <code>Operation</code>.
     * @return An exact copy of this <code>Operation</code>.
     */
    @NotNull
    Operation<T> makeCopy();

    /**
     * Returns a string representation of the type of operation.
     * @return A static result, even though this is not a static method.
     */
    @NotNull
    String opType();

    /**
     * Returns the amount/quantity associated with this <code>Operation</code>.
     * @return An integer.
     */
    T opAmount();
}
