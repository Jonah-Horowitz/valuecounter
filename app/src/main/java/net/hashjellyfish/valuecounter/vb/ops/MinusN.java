package net.hashjellyfish.valuecounter.vb.ops;

import org.jetbrains.annotations.NotNull;

/**
 * Subtracts a fixed value from the input.
 */
public class MinusN implements Operation<Integer>, Cloneable {
    private static final long serialVersionUID = 6372978879458320415L;
    public static final String OP_ID = "-";

    private int n=0;

    /**
     * Changes the value to be subtracted from inputs.
     * @param aNumber The new value to be subtracted from inputs.
     * @return This operation.
     */
    public MinusN setValue(int aNumber) {
        n = aNumber;
        return this;
    }

    @Override
    @NotNull
    public Integer apply(@NotNull Integer target) {
        return target-n;
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
