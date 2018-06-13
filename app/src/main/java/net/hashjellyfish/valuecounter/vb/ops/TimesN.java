package net.hashjellyfish.valuecounter.vb.ops;

import org.jetbrains.annotations.NotNull;

/**
 * Multiplies inputs by a fixed number.
 */
public class TimesN implements Operation<Integer> {
    private static final long serialVersionUID = 8261435150568043500L;
    public static final String OP_ID = "x";

    private int n=1;

    /**
     * Changes the value to be multiplied by inputs.
     * @param aNumber The new value to be multiplied by inputs.
     * @return This operation.
     */
    public TimesN setValue(int aNumber) {
        n = aNumber;
        return this;
    }

    @Override
    @NotNull
    public Integer apply(@NotNull Integer integer) {
        return integer*n;
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
