package net.hashjellyfish.valuecounter.vb.ops;

/**
 * Subtracts a fixed value from the input.
 */
public class MinusN implements Operation<Integer,Integer>, Cloneable {
    private static final long serialVersionUID = 6372978879458320415L;
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
    public Integer apply(Integer target) {
        return target-n;
    }

    @Override
    public String toString() {
        return "-"+n;
    }

    @Override
    public MinusN makeCopy() {
        return (new MinusN()).setValue(n);
    }

    @Override
    public String opType() {
        return "-";
    }
}
