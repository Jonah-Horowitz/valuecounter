package net.hashjellyfish.valuecounter.vb.ops;

/**
 * Multiplies inputs by a fixed number.
 */
public class TimesN implements Operation<Integer,Integer> {
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
    public Integer apply(Integer integer) {
        return integer*n;
    }

    @Override
    public String toString() {
        return OP_ID+n;
    }

    @Override
    public TimesN makeCopy() {
        return (new TimesN()).setValue(n);
    }

    @Override
    public String opType() {
        return OP_ID;
    }
}
