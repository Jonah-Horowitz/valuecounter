package net.hashjellyfish.valuecounter.vb.ops;

import org.jetbrains.annotations.NotNull;

/**
 * Adds a fixed value to the input.
 */
public class PlusN implements Operation<Integer> {
    private static final long serialVersionUID = 839334479128530865L;
    public static final String OP_ID = "+";
    private int n=0;

    /**
     * Changes the value which is to be added to the input.
     * @param aNumber The new value to be added to the input.
     * @return This operation.
     */
    public PlusN setValue(int aNumber) {
        n = aNumber;
        return this;
    }

    @Override
    @NotNull
    public Integer apply(@NotNull Integer integer) {
        return integer+n;
    }

    @Override
    public String toString() {
        return OP_ID+n;
    }

    @Override
    @NotNull
    public String opType() {
        return OP_ID;
    }
}
