package net.hashjellyfish.valuecounter.vb.ops;

import org.jetbrains.annotations.NotNull;

/**
 * Outputs a fixed value.
 */
public class EqualsN implements Operation<Integer> {
    private static final long serialVersionUID = -9175502930396860316L;
    public static final String OP_ID = "=";

    private int n=0;

    /**
     * Sets the fixed output value to the given number.
     * @param aNumber The new output value.
     * @return This operation.
     */
    public EqualsN setValue(int aNumber) {
        n = aNumber;
        return this;
    }

    @Override
    @NotNull
    public Integer apply(@NotNull Integer target) {
        return n;
    }

    @Override
    public String toString() {
        return OP_ID+n;
    }

    @Override
    @NotNull
    public EqualsN makeCopy() {
        return (new EqualsN()).setValue(n);
    }

    @Override
    @NotNull
    public String opType() {
        return OP_ID;
    }

    @Override
    public Integer opAmount() {
        return n;
    }
}
